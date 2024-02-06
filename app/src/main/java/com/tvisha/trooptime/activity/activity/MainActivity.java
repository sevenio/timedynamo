package com.tvisha.trooptime.activity.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.tvisha.trooptime.activity.activity.Helper.Navigation;
import com.tvisha.trooptime.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout selfAttendanceLayout, teamAttendanceLayout, selfRequestLayout, teamRequestLayout;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();


    }

    private void initViews() {
        try {
            selfRequestLayout = findViewById(R.id.selfRequestLayout);
            selfAttendanceLayout = findViewById(R.id.selfAttendanceLayout);
            teamAttendanceLayout = findViewById(R.id.teamAttendanceLayout);
            teamRequestLayout = findViewById(R.id.teamRequestLayout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        try {
            selfRequestLayout.setOnClickListener(this);
            selfAttendanceLayout.setOnClickListener(this);
            teamAttendanceLayout.setOnClickListener(this);
            teamRequestLayout.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.selfRequestLayout:
                    Navigation.getInstance().openRequest(MainActivity.this, "1");
                    break;
                case R.id.selfAttendanceLayout:
                    Navigation.getInstance().openRequest(MainActivity.this, "2");
                    break;
                case R.id.teamRequestLayout:
                    Navigation.getInstance().openRequest(MainActivity.this, "3");
                    break;
                case R.id.teamAttendanceLayout:
                    Navigation.getInstance().openRequest(MainActivity.this, "4");
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
