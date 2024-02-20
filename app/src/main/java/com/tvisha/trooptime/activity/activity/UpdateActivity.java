package com.tvisha.trooptime.activity.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tvisha.trooptime.activity.activity.helper.Navigation;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.R;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    CountDownTimer cTimer = null;
    SharedPreferences sharedPreferences;
    String userId, email, name, user_avatar;
    Button updateButton;
    boolean isLogin;

    @Override
    public void onBackPressed() {

        // super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        try {
            updateButton = findViewById(R.id.updateButton);
            updateButton.setOnClickListener(this);

            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            isLogin = sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false);
            if (isLogin) {
                shareprefernceData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //start timer function
    private void startTimer() {
        cTimer = new CountDownTimer(3000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                if (isLogin) {

                    Navigation.getInstance().openHomePage(UpdateActivity.this);

                } else {

                    Navigation.getInstance().openLoginPage(UpdateActivity.this);


                }

            }
        };
        cTimer.start();
    }


    //cancel timer
    private void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    private void shareprefernceData() {
        try {
            userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
            name = sharedPreferences.getString(SharePreferenceKeys.USER_NAME, "");
            user_avatar = sharedPreferences.getString(SharePreferenceKeys.USER_AVATAR, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void openTd() {
        try {
            String tmPackageName = "com.tvisha.trooptime";
            //https://play.google.com/store/apps/details?id=com.tvisha.trooptime
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + tmPackageName)));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void openTroopMessenger() {
        String tmPackageName = "com.tvisha.troopmessenger";
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(tmPackageName);
        if (launchIntent != null) {
            startActivity(launchIntent);

        } else {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + tmPackageName)));

        }

    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.updateButton:
                    openTd();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
