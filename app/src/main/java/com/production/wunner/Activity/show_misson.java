package com.production.wunner.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.production.wunner.Model.Station;
import com.production.wunner.R;
import com.production.wunner.Tab_Layout_Adapter;
import com.production.wunner.fragment_rated;

public class show_misson extends AppCompatActivity {
    TabLayout mTabs;
    View mIndicator;
    ViewPager mViewPager;
    Station station;
    private int indicatorWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_misson);
        Intent intent =getIntent();
        station= (Station) intent.getSerializableExtra("StationData");
        mTabs = findViewById(R.id.tabLayout);
        mIndicator = findViewById(R.id.indicator);
        mViewPager = findViewById(R.id.viewPager);
        getSupportActionBar().hide();
        //Set up the view pager and fragments
       Tab_Layout_Adapter adapter = new Tab_Layout_Adapter(getSupportFragmentManager());
        adapter.addFragment(Misson.newInstance(this,station), "Mission");
        adapter.addFragment(fragment_rated.newInstance(this), "Rated");
        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);
        mTabs.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = mTabs.getWidth() / mTabs.getTabCount();

                //Assign new width
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                mIndicator.setLayoutParams(indicatorParams);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mIndicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset =  (positionOffset+position) * indicatorWidth ;
                params.leftMargin = (int) translationOffset;
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
