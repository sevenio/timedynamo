package com.tvisha.trooptime.activity.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.tvisha.trooptime.activity.activity.Helper.Navigation;
import com.tvisha.trooptime.activity.activity.Helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.activity.activity.app.MyApplication;
import com.tvisha.trooptime.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class NewAttendanceActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {


    Context context;
    Activity activity;


    Fragment fragment = null;
    Intent intent;
    FragmentManager fragmentManager;
    Calendar myCalendar;
    String dateFormat = "dd-MM-yyyy";
    String dateFormat1 = "yyyy-MM-dd";
    DatePickerDialog.OnDateSetListener date, date1;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);
    SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat, Locale.GERMAN);
    String CONTENT_TYPE = "application/x-www-form-urlencoded";
    ApiInterface apiService;
    String emailId, password;
    SharedPreferences sharedPreferences;
    String userId, email, name, user_avatar;
    int notification_num, servicecount;
    ImageView profile_pic, settingsImage,notificationImage;
    TextView employeeName, time, dynamo;
    TabLayout navigation;

    Typeface poppins_bold, poppins_regular;
    LinearLayout logout, attendance, request, dashboard, settingsLayout;
    DrawerLayout drawer;
    Toolbar toolbar;
    TabLayout.Tab currentTab = null, previousTab = null;
    View view;
    Bundle bundle;
    private int[] navIcons = {
            R.drawable.home_inactive,
            R.drawable.attendance_inactive,
            R.drawable.requests_inactive,
            R.drawable.tm_logo
    };

    private String[] navLabels = {
            "HOME",
            "ATTENDANCE",
            "REQUESTS",
            "TROOP M"
    };

    private int[] navIconsActive = {
            R.drawable.home_active,
            R.drawable.attendance_active,
            R.drawable.requests_active,
            R.drawable.tm_logo

    };

    public static void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction("Action", null);
        snackbar.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_attendance);
        shareprefernceData();
        initViews();
        initListeners();
        processActivity();


        /*Thread t = new Thread(){
            public void run(){
                your_stuff();
            }
        };
        t.start();*/


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onRestart() {
        if (navigation != null) {
      /*      TabLayout.Tab tab = navigation.getTabAt(1);
            tab.select();*/
        }
        super.onRestart();
    }

    private void processActivity() {

        try {

            updateProfile();
            initBottomMenuBar();
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowCustomEnabled(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initBottomMenuBar() {

        try {

            for (int i = 0; i < navLabels.length; i++) {

                LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.nav_tab, null);
                TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
                ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);

                tab_label.setText(navLabels[i]);

                if (i == 1) {
                    tab_label.setTextColor(getResources().getColor(R.color.tab_active_color));
                    tab_icon.setImageResource(navIconsActive[i]);
                } else {
                    tab_label.setTextColor(getResources().getColor(R.color.tab_inactive_color));
                    tab_icon.setImageResource(navIcons[i]);
                }

                navigation.addTab(navigation.newTab().setCustomView(tab));
            }


            navigation.addOnTabSelectedListener(this);
            navigation.addOnTabSelectedListener(NewAttendanceActivity.this);

            TabLayout.Tab tab = navigation.getTabAt(1);
            tab.select();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateProfile() {
        try {
            if (user_avatar != null && !user_avatar.isEmpty()) {
                RequestOptions options = new RequestOptions()
                        .circleCropTransform()
                        .error(R.drawable.user)
                        .priority(Priority.HIGH);
                Glide.with(NewAttendanceActivity.this).load(MyApplication.AWS_BASE_URL + user_avatar)
                        .apply(options)
                        .into(profile_pic);
            } else {
                Glide.with(NewAttendanceActivity.this).load(R.drawable.user).into(profile_pic);
            }
            employeeName.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        try {
            if (tab.getPosition() == 1) {
                View tabView = tab.getCustomView();
                TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
                tab_label.setTextColor(getResources().getColor(R.color.tab_active_color));
                tab_icon.setImageResource(navIconsActive[tab.getPosition()]);
            }
            setFragment(tab);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
/*
        View tabView = tab.getCustomView();
        TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
        ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
        tab_label.setTextColor(getResources().getColor(R.color.tab_inactive_color));
        tab_icon.setImageResource(navIcons[tab.getPosition()]);*/

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        setFragment(tab);
    }

    private void setFragment(TabLayout.Tab tab) {
        try {
            if (tab != null) {
                switch (tab.getPosition()) {

                    case 0:
                        callHomePage();
                        break;

                    case 1:
                        callAttendanceFragment();
                        break;
                    case 2:
                        request();
                        break;

                    case 3:
                        openTroopMessenger();
                        break;

                    default:
                        break;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openTroopMessenger() {
        try {
            String tmPackageName = "com.tvisha.troopmessenger";
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage(tmPackageName);
            if (launchIntent != null) {
                startActivity(launchIntent);


            } else {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + tmPackageName)));


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initViews() {


        try {
            drawer = findViewById(R.id.drawer_layout);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            toolbar = findViewById(R.id.toolbar);
            view = toolbar.findViewById(R.id.menubarLayout);
            dashboard = findViewById(R.id.dashBoardLayout);


            request = findViewById(R.id.requestLayout);
            logout = findViewById(R.id.logoutLayout);
            attendance = findViewById(R.id.attendanceLayout);
            navigation = findViewById(R.id.navigation);
            apiService = ApiClient.getClient().create(ApiInterface.class);
            time = findViewById(R.id.time);
            dynamo = findViewById(R.id.dynamo);
            profile_pic = findViewById(R.id.profileImage);
            employeeName = findViewById(R.id.employeeName);
            settingsImage = findViewById(R.id.settingsImage);
            notificationImage = findViewById(R.id.notificationImage);
            //settingsLayout = findViewById(R.id.settingsLayout);

            poppins_bold = Typeface.createFromAsset(getAssets(), "font/poppins/Poppins-Bold.ttf");
            poppins_regular = Typeface.createFromAsset(getAssets(), "font/poppins/Poppins-Regular.ttf");

            employeeName.setTypeface(poppins_regular);
            time.setTypeface(poppins_regular);
            dynamo.setTypeface(poppins_regular);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        try {

            attendance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawableClose();
                    attendance();
                }
            });

            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawableClose();
                    request();
                }
            });

            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });

            dashboard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawableClose();
                    callHomePage();
                }
            });
            notificationImage.setOnClickListener(this);
            settingsImage.setOnClickListener(this);
            //  settingsImage.setOnClickListener(this);
            //settingsLayout.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
      /*  attendance.setOnClickListener(this);
        request.setOnClickListener(this);
        logout.setOnClickListener(this);
        dashboard.setOnClickListener(this);*/
        // view.findViewById(R.id.menubarLayout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.attendance:
                    drawableClose();
                    attendance();
                    break;
                case R.id.logout:
                    logout();
                    break;
                case R.id.request:
                    drawableClose();
                    request();
                    break;
                case R.id.dashBoard:
                    drawableClose();
                    callHomePage();
                    break;
                case R.id.menubarLayout:
                    drawer.openDrawer(GravityCompat.START);
                    break;
                case R.id.settingsImage:
                    Navigation.getInstance().openProfilePage(NewAttendanceActivity.this);
                    break;
                case R.id.notificationImage:
                    Navigation.getInstance().openNotification(NewAttendanceActivity.this);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void request() {

        try {

            Intent intent = new Intent(NewAttendanceActivity.this, RequestActivity.class);
            intent.putExtra("type", "1");
            intent.putExtra("requestType", "");
            intent.putExtra("fromDate", "");
            intent.putExtra("toDate", "");
            intent.putExtra("users", "");
            intent.putExtra("filter", false);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void attendance() {

        Navigation.getInstance().openRequest(NewAttendanceActivity.this, "2");


    }

    private void logout() {

        try {

            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SP_LOGOUT_STATUS, true).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false).apply();
            sharedPreferences.edit().clear().apply();
            Navigation.getInstance().openLoginPage(NewAttendanceActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callHomePage() {

      /*  fragment=new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("emailId", emailId);
        bundle.putString("password", password);
        fragment.setArguments(bundle);
        if (fragment != null) {
            fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
        }*/


        Navigation.getInstance().openHomePageWithBack(NewAttendanceActivity.this);



    }

    private void callAttendanceFragment() {

        try {

            String userId = "", name = "", user_avatar = "";

            bundle = getIntent().getExtras();
            String type = "";
            if (bundle != null) {

                type = bundle.getString("type");
                if (type.equals("1")) {
                    userId = bundle.getString("employee_user_id");
                    name = bundle.getString("employee_user_name");
                    user_avatar = bundle.getString("employee_image_path");
                } else if (type.equals("0")) {
                    userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
                    name = sharedPreferences.getString(SharePreferenceKeys.USER_NAME, "");
                    user_avatar = sharedPreferences.getString(SharePreferenceKeys.USER_AVATAR, "");
                }

            }


            fragment = new AttendanceFragment();
            Bundle bundle = new Bundle();
            bundle.putString("employee_user_id", userId);
            bundle.putString("employee_user_name", name);
            bundle.putString("employee_image_path", user_avatar);
            bundle.putString("type", type);

            fragment.setArguments(bundle);
            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawableClose() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void shareprefernceData() {

        try {

            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            name = sharedPreferences.getString(SharePreferenceKeys.USER_NAME, "");
            user_avatar = sharedPreferences.getString(SharePreferenceKeys.USER_AVATAR, "");
            email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
            notification_num = sharedPreferences.getInt(SharePreferenceKeys.NOTIFICATION_COUNT, 0);
            servicecount = sharedPreferences.getInt("countforservice", 0);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public Context getContext() {
        if (context != null) {
            return context;
        } else {
            context = this;
            return context;
        }

    }

    public Activity getActivity() {
        if (activity != null) {
            return activity;
        } else {
            activity = this;
            return activity;
        }

    }


}
