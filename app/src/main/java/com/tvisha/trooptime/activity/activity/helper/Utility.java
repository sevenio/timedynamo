package com.tvisha.trooptime.activity.activity.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Window;
import android.view.WindowManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by koti on 3/10/16.
 */

public class Utility {

    public static void setFullScreen(Activity context) {
        context.requestWindowFeature(Window.FEATURE_NO_TITLE);
        context.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static Typeface getTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "font/poppins/Poppins-Light.ttf");
    }

    public static Typeface getTypefaceMedium(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "font/poppins/Poppins-Medium.ttf");
    }

    public static Typeface getTypefaceRegular(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "font/poppins/Poppins-Regular.ttf");
    }

    public static Typeface getTypefaceBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "font/poppins/Poppins-Bold.ttf");
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean validEmail(String email) {
        if (email.length() != 0) {
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            Pattern p = Pattern.compile(ePattern);
            Matcher m = p.matcher(email);
            return m.matches();
        } else {
            return false;
        }
    }

    public static boolean isValidName(String name) {
        if (name != null) {
            if (!Utility.isNumeric(name)) {
                //CharSequence inputStr = name;
                Pattern pattern = Pattern.compile(new String("^[a-zA-Z\\s]*$"));
                Matcher matcher = pattern.matcher(name);
                return matcher.matches();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String isNullCheck(String val, String x) {
        //return  (val != null ? val :"");
        return (val != null ? val : x);
    }

    public static int isNullCheck(int val) {
        return 0 != val ? val : 0;
    }

    public static String getCurrent_time() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }
}
