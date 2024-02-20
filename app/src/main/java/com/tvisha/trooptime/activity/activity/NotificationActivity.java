package com.tvisha.trooptime.activity.activity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.ImageView;

import com.tvisha.trooptime.R;
import com.tvisha.trooptime.activity.activity.helper.Navigation;

/**
 * Created by tvisha on 25/2/17.
 */
public class NotificationActivity extends Activity implements View.OnClickListener {

    // lock controles
    PowerManager pm;
    PowerManager.WakeLock wl;
    KeyguardManager km;
    KeyguardManager.KeyguardLock kl;

    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notification_activty);
        logo = (ImageView) findViewById(R.id.logo);
        aboveLockScreen();
    }

    //invoke lock
    public void aboveLockScreen() {
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("INFO");
        wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "INFO");
        wl.acquire(); //wake up the screen
        //kl.disableKeyguard();// dismiss the keyguard
    }

    @Override
    public void onClick(View view) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        switch (view.getId()) {
            case R.id.leaveRequest:
                Navigation.getInstance().leaveRequest(this);

                break;
            case R.id.permissionRequest:
                Navigation.getInstance().permissionRequest(this);

                break;
            case R.id.directClientVistRequest:
                Navigation.getInstance().directClientLocation(this);

                break;
            case R.id.callManagerRequest:

                Navigation.getInstance().openCallLog(this);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
