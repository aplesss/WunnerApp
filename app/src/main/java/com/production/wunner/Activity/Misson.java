package com.production.wunner.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.production.wunner.Model.Station;
import com.production.wunner.R;
import com.production.wunner.TimeCounterService;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class Misson extends Fragment {
    private Context context;
    ProgressBar progressBarView;
    CountDownTimer countDownTimer;
    int endTime = 25;
    TextView txt_timer,txt_mission;
    Button btn_submit;
    Station station;
    public static Misson newInstance(Context context, Station station) {
        return new Misson(context,station);
    }
    public Misson(Context context, Station station) {
        this.context = context;
        this.station=station;
    }

    @Override
    public View onCreateView(  LayoutInflater inflater,  ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_misson, container, false);
        //setContentView(R.layout.activity_misson);
        // Toolbar toolbar = findViewById(R.id.toolbar);
        txt_timer =view.findViewById(R.id.txt_timer);
        txt_mission=view.findViewById(R.id.txt_mission);
        btn_submit= view.findViewById(R.id.btn_submit);
        endTime= (int) station.getStationTime();
        // setSupportActionBar(toolbar);

        progressBarView = (ProgressBar) view.findViewById(R.id.view_progress_bar);
        txt_mission.setText(station.getStationDescription());
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(0);
        Intent intent =new  Intent(context, TimeCounterService.class);
        intent.putExtra("count_timer",(long) endTime);
        context.startService(intent);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStage(1);
                context.unregisterReceiver(receiver);
                Toast.makeText(context,"Congratulation. Please wait to check ",Toast.LENGTH_SHORT).show();
            }
        });
        return view;


    }

    private void updateStage(int numstage) {

            SharedPreferences.Editor editor =context.getSharedPreferences("Stage",Context.MODE_PRIVATE).edit();
            editor.putInt("Num_Stage",numstage);
            editor.apply();
    }

    private BroadcastReceiver receiver= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            UpdateTimerView(intent);
        }
    };

    private void UpdateTimerView(Intent intent) {
        long timer =intent.getExtras().getLong("responetimer");
        setProgress((int)timer,endTime);
        txt_timer.setText(new SimpleDateFormat("mm:ss").format(new Date(timer)));
        if(timer<=0)
        {
            Toast.makeText(context,"Time out. Fighting", Toast.LENGTH_SHORT).show();

        }
    }

    private void setProgress(int myProgress, long time) {
        progressBarView.setMax((int) time);
        progressBarView.setSecondaryProgress( (int) time);
        progressBarView.setProgress(myProgress/1000);
        txt_timer.setText(new SimpleDateFormat("mm:ss").format(new Date(time-myProgress)));
        if(myProgress<=0)
        {
            context.stopService(new Intent(context, TimeCounterService.class));
            updateStage(1);
        }

    }
    @Override
    public void onResume() {
        context.registerReceiver(receiver, new IntentFilter(TimeCounterService.COUNTTIMER_BR));
        super.onResume();
    }

    @Override
    public void onPause() {
            try {
                context.unregisterReceiver(receiver);
            }
            catch (Exception ex ) {

            }
        super.onPause();
    }
    @Override
    public void onStop() {
        try {
            context.unregisterReceiver(receiver);
        }
        catch (Exception ex )
        {

        }
        super.onStop();
    }

    @Override
    public void onDestroy() {
        context.stopService(new Intent(context, TimeCounterService.class));
        super.onDestroy();
    }
}
