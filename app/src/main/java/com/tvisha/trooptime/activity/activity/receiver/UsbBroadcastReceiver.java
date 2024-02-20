package com.tvisha.trooptime.activity.activity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbManager;

import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.handlers.HandlerHolder;

/**
 * Created by Shiva prasad on 31/08/2017.
 */

public class UsbBroadcastReceiver extends BroadcastReceiver {

    private static String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {

            /*Intent i = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);*/

        } else if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
            if (HandlerHolder.usbHandler != null) {
                HandlerHolder.usbHandler.obtainMessage(Constants.UsbPermission).sendToTarget();
            }
        }
    }
}