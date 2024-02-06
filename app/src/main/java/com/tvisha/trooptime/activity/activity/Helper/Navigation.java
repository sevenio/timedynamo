package com.tvisha.trooptime.activity.activity.Helper;

import android.content.Context;
import android.content.Intent;
import android.provider.CallLog;

import com.tvisha.trooptime.activity.activity.AttendanceActivity;
import com.tvisha.trooptime.activity.activity.AttendanceFilterActivity;
import com.tvisha.trooptime.activity.activity.DirectClientVistActivity;
import com.tvisha.trooptime.activity.activity.ForgotPasswordActivity;
import com.tvisha.trooptime.activity.activity.HomeActivity;
import com.tvisha.trooptime.activity.activity.LeaveRequestActivity;
import com.tvisha.trooptime.activity.activity.LoginActivity;
import com.tvisha.trooptime.activity.activity.NotificationActivityNew;
import com.tvisha.trooptime.activity.activity.NotificationsActivity;
import com.tvisha.trooptime.activity.activity.OtpVerifyActivity;
import com.tvisha.trooptime.activity.activity.PermissionRequestActivity;
import com.tvisha.trooptime.activity.activity.ProfileActivity;
import com.tvisha.trooptime.activity.activity.RequestActivity;
import com.tvisha.trooptime.activity.activity.RequestCompoffActivity;
import com.tvisha.trooptime.activity.activity.RequestFilterActivity;
import com.tvisha.trooptime.activity.activity.SwapActivity;
import com.tvisha.trooptime.activity.activity.UpdateActivity;

/**
 * Created by koti on 22/5/17.
 */

public class Navigation {


    private static Navigation ourInstance = new Navigation();

    public static Navigation getInstance() {
        if (ourInstance == null) {
            ourInstance = new Navigation();
        }
        return ourInstance;
    }

    public void login(Context context) {
        Intent myIntent = new Intent(context, LoginActivity.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);


    }

    public void home(Context context) {
        Intent intent = new Intent(context, AttendanceActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type", "2");
        intent.putExtra("filterType", false);
        context.startActivity(intent);
    }

    public void notifications(Context context) {
        Intent intent = new Intent(context, NotificationsActivity.class);
        context.startActivity(intent);
    }

    public void directClientLocation(Context context) {
        Intent intent = new Intent(context, DirectClientVistActivity.class);
        context.startActivity(intent);
    }

    public void leaveRequest(Context context) {
        Intent intent = new Intent(context, LeaveRequestActivity.class);
        intent.putExtra("date", " ");
        context.startActivity(intent);
    }

    public void permissionRequest(Context context) {
        Intent intent = new Intent(context, PermissionRequestActivity.class);
        context.startActivity(intent);
    }

    public void openCallLog(Context context) {
        try {
            Intent showCallLog = new Intent();
            showCallLog.setAction(Intent.ACTION_VIEW);
            showCallLog.setType(CallLog.Calls.CONTENT_TYPE);
            context.startActivity(showCallLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openFilterPage(Context context, int type) {

        //1 for self
        //2 for team
        //3 for all
        try {
            Intent filter_intent = new Intent(context, AttendanceFilterActivity.class);
            filter_intent.putExtra("filter_type", type);
            context.startActivity(filter_intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openRequestFilterPage(Context context, int type) {

        //1 for self
        //2 for team
        //3 for all
        try {
            Intent filter_intent = new Intent(context, RequestFilterActivity.class);
            filter_intent.putExtra("filter_type", type);
            context.startActivity(filter_intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void compoffRequest(Context context) {
        try {
            Intent intent = new Intent(context, RequestCompoffActivity.class);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void swapRequest(Context context) {
        try {
            Intent intent = new Intent(context, SwapActivity.class);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openProfilePage(Context context) {
        try {
            Intent intent = new Intent(context, ProfileActivity.class);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void openAttendance(Context context, String type) {
        try {
            Intent intent = new Intent(context, AttendanceActivity.class);
            intent.putExtra("type", type);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openRequest(Context context, String type) {
        try {
            Intent intent = new Intent(context, RequestActivity.class);
            intent.putExtra("type", type);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void openLoginPage(Context context) {
        try {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openForgotPasswordPage(Context context) {
        try {
            Intent intent = new Intent(context, ForgotPasswordActivity.class);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openHomePage(Context context) {
        try {
            Intent intent = new Intent(context, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openUpdatePage(Context context) {
        try {
            Intent intent = new Intent(context, UpdateActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openHomePageWithBack(Context context) {
        try {
            Intent intent = new Intent(context, HomeActivity.class);
            context.startActivity(intent);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openOtpVerify(Context context, String userId, String number) {
        try {
            Intent intent = new Intent(context, OtpVerifyActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("userId", userId);
            intent.putExtra("number", number);
            context.startActivity(intent);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void openNotification(Context context) {
        try {
            Intent intent = new Intent(context, NotificationActivityNew.class);
            context.startActivity(intent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
