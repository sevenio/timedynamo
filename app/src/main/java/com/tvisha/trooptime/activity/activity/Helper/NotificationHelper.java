package com.tvisha.trooptime.activity.activity.Helper;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;

import com.tvisha.trooptime.activity.activity.HomeActivity;
import com.tvisha.trooptime.R;

import me.leolin.shortcutbadger.ShortcutBadger;

public class NotificationHelper {
    public static int count = 0;
    public static NotificationHelper notificationHelper = new NotificationHelper();
    SharedPreferences sharedPreferences;

    public static NotificationHelper getInstatance() {
        return notificationHelper;
    }

    public void sendNotification(Context context, String messageTitle, String messageBody) {
        sharedPreferences = context.getSharedPreferences(SharePreferenceKeys.SP_NAME, Context.MODE_PRIVATE);
        int c = sharedPreferences.getInt("notificationCount", 0);
        c++;
        sharedPreferences.edit().putInt("notificationCount", c).apply();


        Intent notificationIntent = new Intent(context, HomeActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder notification = new Notification.Builder(context);
        notification.setSmallIcon(getNotificationIcon());
        notification.setContentTitle(messageTitle);
        notification.setContentText(messageBody);
        notification.setAutoCancel(true);
        notification.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.firebase_notification))
                .setSound(defaultSoundUri)
                .setContentIntent(contentIntent);

     /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.setColor(context.getResources().getColor( R.color.colorPrimaryDark));
        }
*/

      /*  NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(count,notification.build());*/
        count++;
        int badgeCount = 1;
        ShortcutBadger.applyCount(context, badgeCount);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_app_icon_48_x_48 : R.drawable.firebase_notification;
    }


}


