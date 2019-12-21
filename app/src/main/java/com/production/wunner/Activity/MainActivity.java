package com.production.wunner.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.production.wunner.Api.GetWunnerDataService;
import com.production.wunner.Api.RetrofitInstance;
import com.production.wunner.AsyncLoadLatLng;
import com.production.wunner.Interface.GetCoordinates;
import com.production.wunner.Model.Location;
import com.production.wunner.Model.Station;
import com.production.wunner.R;
import com.production.wunner.TimeCounterService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback   {

    private GoogleMap ggmap;
    private MapView mapView;
    private Boolean RemoveService=false;

    private ArrayList<Location> data = new ArrayList<>();
    private static final String MAP_VIEW_BUNDEL_KEY = "MapViewBundleKey";
    ArrayList<String> name=new ArrayList<>();
    TextView txt_name_location,txt_timer,txt_distance,txt_postion_location;
    CountDownTimer countDownTimer;
    private int timedown=1000;
    private Station station;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        Intent intent =getIntent();
        station = (Station) intent.getSerializableExtra("StationDataRun");
        station.setCheckin(false);
        Bundle mapviewBundle = null;
        if (savedInstanceState != null) {
            mapviewBundle = (Bundle) savedInstanceState.get(MAP_VIEW_BUNDEL_KEY);
        }
        mapView = findViewById(R.id.map_view);
        txt_name_location=findViewById(R.id.txt_name_location);
        txt_timer=findViewById(R.id.txt_count_timer);
        txt_distance=findViewById(R.id.txt_distance);
        txt_postion_location=findViewById(R.id.txt_postion_location);

        UpdateStage(2);
        mapView.onCreate(mapviewBundle);
        mapView.getMapAsync(this);
    }

    private void UpdateStage(int num) {
        SharedPreferences.Editor editor =getSharedPreferences("Stage",MODE_PRIVATE).edit();
        editor.putInt("Num_Stage",num);
        editor.apply();

    }

    private BroadcastReceiver receiver =new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            UpdateTextTime(intent);
        }
    };

    private void UpdateTextTime(Intent intent) {
        long timer =intent.getExtras().getLong("responetimer");
        txt_timer.setText(new SimpleDateFormat("mm:ss").format(new Date(timer)));
        if(timer<=0)
        {
            Toast.makeText(MainActivity.this,"Time out. Fighting", Toast.LENGTH_SHORT).show();
            station.setCheckin(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDEL_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDEL_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        registerReceiver(receiver, new IntentFilter(TimeCounterService.COUNTTIMER_BR));
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        try {
            unregisterReceiver(receiver);
        }
        catch (Exception ex)
        {

        }
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        try {
            unregisterReceiver(receiver);
        }
        catch (Exception ex )
        {

        }
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!RemoveService)
        {
            this.stopService(new Intent(MainActivity.this,TimeCounterService.class));
        }
        mapView.onDestroy();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        ggmap = googleMap;
        ggmap.setMinZoomPreference(12);
        ggmap.setMaxZoomPreference(30);
        fetchData();
        ggmap.setMyLocationEnabled(true);
        new AsyncLoadLatLng(this, name, new GetCoordinates() {
            @Override
            public void UpdatLatLng(final ArrayList<LatLng> list) {
               /* for(int i =0; i<list.size();i++)
                {
                    data.add(new Location(name.get(i),list.get(i)));
                    LatLng temp= list.get(i);
                    Circle circle = ggmap.addCircle(new CircleOptions()
                            .center(temp)
                            .radius(100)
                            .strokeColor(Color.RED)
                            .fillColor(Color.argb(50,100,0,0)));
                    Marker marker =ggmap.addMarker(new MarkerOptions().position(temp));
                    data.get(0).setMyMarker(marker);
                    data.get(0).getMyMarker().setTitle(name.get(i));
                    ggmap.moveCamera(CameraUpdateFactory.newLatLng(temp));
                    data.get(i).setTimeDest(1);
                }*/
                    LatLng temp= new LatLng(Double.parseDouble(station.getStationLatitude()),Double.parseDouble(station.getStationLongtitude()));
                    station.setLatLng(temp);
                    Circle circle = ggmap.addCircle(new CircleOptions()
                            .center(temp)
                            .radius(100)
                            .strokeColor(Color.RED)
                            .fillColor(Color.argb(50,100,0,0)));
                    Marker marker =ggmap.addMarker(new MarkerOptions().position(temp));
                    station.setMarker(marker);
                    station.getMarker().setTitle(station.getStationName());
                    ggmap.moveCamera(CameraUpdateFactory.newLatLng(temp));
                    Intent intent = new Intent(MainActivity.this, TimeCounterService.class);
                    //intent.putExtra("count_timer", 100000000);

                    intent.putExtra("count_timer", (long) station.getStationTime());
                    startService(intent);
                if(ggmap!=null) {
                    ggmap.setOnMarkerClickListener(MainActivity.this);
                    ggmap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                        @Override
                        public void onMyLocationChange(android.location.Location location) {

                            android.location.Location loc2 = new android.location.Location("");
                            loc2.setLatitude(station.getLatLng().latitude);
                            loc2.setLongitude(station.getLatLng().longitude);
                            int distanceInMeters = (int) location.distanceTo(loc2)/1000;
                            txt_distance.setText("" + distanceInMeters);
                            float[] distance = new float[2];
                            if (location != null & loc2 != null) {
                                android.location.Location.distanceBetween(location.getLatitude(), location.getLongitude(), loc2.getLatitude(), loc2.getLongitude(), distance);
                                if (distance[0] < 100) {
                                    station.setCheckin(true);
                                    Toast.makeText(MainActivity.this, "Congratulation!!! New Mission......Please clicking marker", Toast.LENGTH_SHORT).show();
                                    //CheckIn();


                                }
                            }
                        }
                    });
                }
                txt_name_location.setText(station.getStationName());
                txt_postion_location.setText("1st Station");



            }
        }).execute();
    }

    private void CheckIn() {
        stopService(new Intent(MainActivity.this, TimeCounterService.class));
        RemoveService=true;
        FetchData(station.getStationID());


    }

    private void FetchData(String stationID) {
        GetWunnerDataService service = RetrofitInstance.getRetrofitInstance().create(GetWunnerDataService.class);
        Call<Station> call =service.GetStation(stationID);
        call.enqueue(new Callback<Station>() {
            @Override
            public void onResponse(Call<Station> call, Response<Station> response) {
                if(response.isSuccessful())
                {
                    Intent intent =new Intent(getApplicationContext(),show_misson.class);
                    intent.putExtra("StationData",response.body());
                    UpdateStage(3);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                }
            }

            @Override
            public void onFailure(Call<Station> call, Throwable t) {

            }
        });
    }

    private void StartCountTimer(int timeDest) {
        countDownTimer =new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txt_timer.setText(new SimpleDateFormat("mm:ss").format(new Date(millisUntilFinished)));
                timedown= (int) millisUntilFinished;
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this,"Time out. Fighting. Please clicking marker to get mission", Toast.LENGTH_SHORT).show();
               station.setCheckin(true);
                CheckIn();

            }
        }.start();
    }

    private void fetchData() {
        //name.add( "Cong vien 30/4");
        //name.add("Ho con rua");
        //name.add("Bao tang Phu nu Nam Bo");
        name.add("Truong dai hoc khoa hoc tu nhien");
        name.add("Hồ Con Rùa");

    }


    @Override
    public boolean onMarkerClick(Marker marker) {

       if (marker.equals(station.getMarker()))
       {
           if(station.getCheckin())
           {
              CheckIn();
           }
       }
       return false;
    }
}
