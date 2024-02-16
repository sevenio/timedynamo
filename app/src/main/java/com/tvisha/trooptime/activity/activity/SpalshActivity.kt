package com.tvisha.trooptime.activity.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;

import com.tvisha.trooptime.activity.activity.Helper.Navigation;
import com.tvisha.trooptime.activity.activity.Helper.SharePreferenceKeys;
import com.tvisha.trooptime.R;

public class SpalshActivity extends AppCompatActivity {

    CountDownTimer cTimer = null;
    SharedPreferences sharedPreferences;
    String userId, email, name, user_avatar;
    boolean isLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);

        sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
        isLogin = sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false);
       /* if(isLogin) {
            shareprefernceData();
        }*/
        startTimer();
    }

    //start timer function
    private void startTimer() {
        try {
            cTimer = new CountDownTimer(1000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    if (isLogin) {

                        Navigation.getInstance().openHomePage(SpalshActivity.this);

                        finish();
                    } else {

                        Navigation.getInstance().openLoginPage(SpalshActivity.this);

                        finish();

                    }

                }
            };
            cTimer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //cancel timer
    private void cancelTimer() {
        if (cTimer != null)
            cTimer.cancel();
    }

    private void shareprefernceData() {
        userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
        email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
        name = sharedPreferences.getString(SharePreferenceKeys.USER_NAME, "");
        user_avatar = sharedPreferences.getString(SharePreferenceKeys.USER_AVATAR, "");


    }
}
