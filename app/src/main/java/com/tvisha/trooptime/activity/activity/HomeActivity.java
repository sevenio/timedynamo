package com.tvisha.trooptime.activity.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.tvisha.trooptime.activity.activity.adapter.BreaksAdapter;
import com.tvisha.trooptime.activity.activity.adapter.EventsAdaper;
import com.tvisha.trooptime.activity.activity.adapter.HolidaysAdapter;
import com.tvisha.trooptime.activity.activity.adapter.SelfRequestDataAdapter;
import com.tvisha.trooptime.activity.activity.apiPostModels.Break;
import com.tvisha.trooptime.activity.activity.apiPostModels.Breaks_list;
import com.tvisha.trooptime.activity.activity.apiPostModels.CheckUpdateResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.CommonResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.HomePageResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.LogoutApi;
import com.tvisha.trooptime.activity.activity.apiPostModels.SelfRequest;
import com.tvisha.trooptime.activity.activity.apiPostModels.UpcomingEvent;
import com.tvisha.trooptime.activity.activity.apiPostModels.UpcomingHolidy;
import com.tvisha.trooptime.activity.activity.dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.helper.Navigation;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.UpdateHelper;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.helper.Values;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.activity.activity.app.MyApplication;
import com.tvisha.trooptime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener, View.OnTouchListener, UpdateHelper.OnUpdateCheckListener ,InstallStateUpdatedListener {

    public static Handler ha = null;
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
    String userId, email, name, user_avatar, device_Id = "", apiKey = "";
    int notification_num, servicecount;
    ImageView profile_pic, settingsImage,notificationImage, drawerImage;
    TextView employeeName, time, dynamo,tv_privacy_policy ;
    TabLayout navigation;
    Typeface poppins_bold, poppins_regular;
    LinearLayout logout, attendance, request, dashboard, settingsLayout;
    DrawerLayout drawer;
    Toolbar toolbar;
    TabLayout.Tab currentTab = null, previousTab = null;
    View view;
    int selectedPosition = 0;
    boolean isLogin;
    String deviceId, token;
    RelativeLayout selfAttendanceLayout, teamAttendanceLayout, selfRequestLayout, teamRequestLayout;
    TextView checkinTime, totalHours, totalMinutes, checkInStatus, totalBreakTime, mins, hrs, totalSalesTime, totalTransitTime, totalOthersTime,
            breaksTitle, requestsTitle, upcomingEventsTitle, upcomingHolidaysTitle;
    TextView moreRequests, isHolidayStatus;
    String loginTime = "", currentTime = "", logoutTime = "";
    LinearLayout breaksLayout, requestsLayout, totalBreakTimeLayout, teamLayout, salesLayout, transitLayout, othersLayout;
    View selfLayoutLine;
    int breaktime = 0, salesTime = 0, transitTime, othersTime = 0;
    SwipeRefreshLayout swipe_refresh;
    RecyclerView.LayoutManager breaksLayoutManager;
    BreaksAdapter breaksAdapter, salesAdapter, transitAdapter, othersAdapter;
    List<Break> breaksLists = new ArrayList<>();
    List<Break> salesList = new ArrayList<>();
    List<Break> transitList = new ArrayList<>();
    List<Break> othersList = new ArrayList<>();
    List<Break> tempBreaksLists = new ArrayList<>();
    List<Break> tempSalesList = new ArrayList<>();
    List<Break> tempTransitList = new ArrayList<>();
    List<Break> tempOthersList = new ArrayList<>();
    RecyclerView.LayoutManager eventsLayoutManager;
    EventsAdaper eventsAdapter;
    List<UpcomingEvent> upcomingEvents = new ArrayList<>();
    RecyclerView.LayoutManager todayEventsLayoutManager;
    EventsAdaper todayEventsAdapter;
    List<UpcomingEvent> todayEvents = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    SelfRequestDataAdapter selfRequestDataAdapter;
    List<SelfRequest> selfRequestArrayList = new ArrayList<>();
    List<SelfRequest> tempSelfRequestArrayList = new ArrayList<>();
    RecyclerView.LayoutManager holidaysLayoutManager;
    HolidaysAdapter holidaysAdapter;
    List<UpcomingHolidy> upcomingHolidies = new ArrayList<>();
    RecyclerView.LayoutManager todayHolidaysLayoutManager;
    HolidaysAdapter todayHolidaysAdapter;
    List<UpcomingHolidy> todayHolidies = new ArrayList<>();
    boolean teamLead, isRefresh = false, isFirstTime = true;
    LinearLayout checkinLayout, todayLayout, holidaysLayout, eventsLayout, todayEventsLayout, todayHolidaysLayout;
    View line;
    CustomProgressBar customProgressBar;
    private RecyclerView breaksRecycleView, sales_recycler_view, transit_recycler_view, others_recycler_view;
    private RecyclerView eventsRecycleView;
    private RecyclerView todayEventsRecyclerView;
    private RecyclerView requestListRecycleView;
    private RecyclerView holidaysRecycleView;
    private RecyclerView todayHolidaysRecyclerView;
    public static int eventsItemSize=0,holdaysItemSize=0;
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
    public void onPause() {
        if (ha != null) {
            ha.removeCallbacksAndMessages(null);
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        isRefresh = true;
        callHomePageApi();
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        activity = HomeActivity.this;
        context = HomeActivity.this;


      /*  MyApplication application=(MyApplication) getApplication();
        application.remoteConfig();
        UpdateHelper.with(this)
                .onUpdateCheck(this)
        .check();*/

        try {

            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            if (sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false)) {
                shareprefernceData();
            } else {

                Navigation.getInstance().openLoginPage(HomeActivity.this);


            }

            device_Id = Settings.System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            sharedPreferences.edit().putString(SharePreferenceKeys.DEVICE_ID, device_Id).apply();
            String refreshedToken = "";//FirebaseInstanceId.getInstance().getToken();
            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            isLogin = sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false);
            userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            deviceId = sharedPreferences.getString(SharePreferenceKeys.DEVICE_ID, "");
            token = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
            apiService = ApiClient.getClient().create(ApiInterface.class);
            if (isLogin) {

                sendRegistrationToServer(refreshedToken);
            }

            initViews();
            initViews1();
            initListeners();
            processActivity();
           // checkVersion();
            checkAppUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {

            if ((appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                try {
                    appUpdateManager.unregisterListener(HomeActivity.this);
                    appUpdateManager.registerListener(HomeActivity.this);
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            HomeActivity.this,
                            Values.MyActionsRequestCode.APP_UPDATE);


                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    AppUpdateManager appUpdateManager;
    @Override
    public void onStateUpdate(InstallState state) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            appUpdateManager
                    .getAppUpdateInfo()
                    .addOnSuccessListener(appUpdateInfo -> {
                        if (appUpdateInfo.updateAvailability()
                                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
//If an in-app update is already running, resume the update.
                            try {
                                appUpdateManager.startUpdateFlowForResult(
                                        appUpdateInfo,
                                        AppUpdateType.IMMEDIATE,
                                        this,
                                        Values.MyActionsRequestCode.APP_UPDATE);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        }
    }

    @Override
    public void onUpdateCheckListener(String urlApp) {
 /*       new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Update is available")
                .setMessage("Do you want to update new version?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {



                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        openTd();

                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }

                })
                .show();*/
    }

    private void openTd() {
        String tmPackageName = "com.tvisha.trooptime";
        //https://play.google.com/store/apps/details?id=com.tvisha.trooptime
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + tmPackageName)));


    }

    @Override
    public void onBackPressed() {

        try {
            ActivityManager mngr = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

            List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

            if (taskList.get(0).numActivities == 1 && taskList.get(0).topActivity.getClassName().equals(this.getClass().getName())) {
                System.out.println("onBackPressed" + "This is last activity in the stack");

                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("Are you sure?")
                        .setMessage("Do you really want to close the app?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                finish();

                            }

                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onRestart();

                            }

                        })
                        .show();
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //  super.onBackPressed();
    }

    private void processActivity() {

        try {

            updateProfile();

            initBottomMenuBar();
            // callHomeFragment();
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

                if (i == 0) {
                    tab_label.setTextColor(getResources().getColor(R.color.tab_active_color));
                    tab_icon.setImageResource(navIconsActive[i]);
                } else {
                    tab_label.setTextColor(getResources().getColor(R.color.tab_inactive_color));
                    tab_icon.setImageResource(navIcons[i]);
                }

                navigation.addTab(navigation.newTab().setCustomView(tab));
            }


            navigation.addOnTabSelectedListener(this);
            navigation.addOnTabSelectedListener(HomeActivity.this);

            TabLayout.Tab tab = navigation.getTabAt(0);
            tab.select();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProfile() {
        try {
            if (user_avatar != null && !user_avatar.isEmpty()) {
                Glide.with(HomeActivity.this).load(MyApplication.AWS_BASE_URL + user_avatar)
                        .apply(RequestOptions.circleCropTransform())
                        .into(profile_pic);
            } else {
                Glide.with(HomeActivity.this).load(R.drawable.avatar_placeholder_light).
                        into(profile_pic);
            }
            employeeName.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        try {
            if (currentTab == null && previousTab == null) {
                currentTab = tab;
                previousTab = tab;
            } else {
                previousTab = currentTab;
                currentTab = tab;
            }

            if (tab.getPosition() == 0) {
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

        /*View tabView = tab.getCustomView();
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
                selectedPosition = tab.getPosition();
                switch (tab.getPosition()) {

                    case 0:

                        //callHomeFragment();
                        break;

                    case 1:

                        attendance();
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

    private void setItem(TabLayout.Tab tab) {
        if (tab != null) {
            View tabView = tab.getCustomView();
            TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
            tab_label.setTextColor(getResources().getColor(R.color.tab_active_color));
            tab_icon.setImageResource(navIconsActive[tab.getPosition()]);
        }
    }

    private void resetItem(TabLayout.Tab tab) {
        if (tab != null) {
            View tabView = tab.getCustomView();
            TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
            tab_label.setTextColor(getResources().getColor(R.color.tab_inactive_color));
            tab_icon.setImageResource(navIcons[tab.getPosition()]);
        }

    }

    private void initViews() {

        try {

            drawer = findViewById(R.id.drawer_layout);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
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
            settingsImage.setOnClickListener(this);

            drawerImage = findViewById(R.id.drawerImage);
            drawerImage.setOnClickListener(this);
            notificationImage = findViewById(R.id.notificationImage);
            notificationImage.setOnClickListener(this);
            settingsLayout = findViewById(R.id.settingsLayout);

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

            //settingsImage.setOnClickListener(this);
            //settingsLayout.setOnClickListener(this);
            // settingsImage.setOnTouchListener(this);

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

            setupPrivacyPolicy();

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
//                    callHomeFragment();
                }
            });

            selfRequestLayout.setOnClickListener(this);
            selfAttendanceLayout.setOnClickListener(this);
            teamRequestLayout.setOnClickListener(this);
            teamAttendanceLayout.setOnClickListener(this);
            moreRequests.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
      /*  attendance.setOnClickListener(this);
        request.setOnClickListener(this);
        logout.setOnClickListener(this);
        dashboard.setOnClickListener(this);*/
        //  view.findViewById(R.id.menubarLayout).setOnClickListener(this);
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
                    callHomeFragment();
                    break;
                case R.id.menubarLayout:
                    drawer.openDrawer(GravityCompat.START);
                    break;

                case R.id.settingsImage:

                    Navigation.getInstance().openProfilePage(HomeActivity.this);

                    break;

                case R.id.drawerImage:
                    drawer.openDrawer(GravityCompat.START);

                    break;
                case R.id.notificationImage:

                    Navigation.getInstance().openNotification(HomeActivity.this);

                    break;

                case R.id.selfRequestLayout:
                    callSelfRequestFragment();
                    break;
                case R.id.selfAttendanceLayout:
                    callSelfAttendanceFragment();
                    break;
                case R.id.teamRequestLayout:
                    teamRequestFragment();
                    break;
                case R.id.teamAttendanceLayout:
                    teamAttendanceFragment();
                    break;
                case R.id.moreRequests:
                    moreRequests();
                    break;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            switch (v.getId()) {
                case R.id.settingsImage:
                    Navigation.getInstance().openProfilePage(HomeActivity.this);
                    break;
                case R.id.notificationImage:
                    Navigation.getInstance().openNotification(HomeActivity.this);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void request() {

        try {

            Intent intent = new Intent(HomeActivity.this, RequestActivity.class);
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

        //old

     /*   Intent intent=new Intent(getApplicationContext(),AttendanceActivity.class);
        intent.putExtra("type","2");
        startActivity(intent);*/

        //new
        try {
            Intent intent = new Intent(HomeActivity.this, NewAttendanceActivity.class);
            intent.putExtra("type", "0");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void logout() {

        try {
            if (MyApplication.homePageResponse != null) {
                MyApplication.homePageResponse = null;
            }
            if (MyApplication.UserRequestListResponse != null) {
                MyApplication.UserRequestListResponse = null;
            }
            if (MyApplication.selfAttendenceApiResponce != null) {
                MyApplication.selfAttendenceApiResponce = null;
            }
            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SP_LOGOUT_STATUS, true).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false).apply();
            sharedPreferences.edit().clear().apply();
            Navigation.getInstance().openLoginPage(HomeActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callHomeFragment() {

        try {

            fragment = new HomeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("emailId", emailId);
            bundle.putString("password", password);
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
            userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            apiKey = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
            email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
            name = sharedPreferences.getString(SharePreferenceKeys.USER_NAME, "");
            user_avatar = sharedPreferences.getString(SharePreferenceKeys.USER_AVATAR, "");
            notification_num = sharedPreferences.getInt(SharePreferenceKeys.NOTIFICATION_COUNT, 0);
            servicecount = sharedPreferences.getInt("countforservice", 0);
            teamLead = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void callToLogoutSrever() {
        try {
            retrofit2.Call<LogoutApi.LogoutApiResponce> call = LogoutApi.getApiService().getLogoutDetails(sharedPreferences.getString(SharePreferenceKeys.USER_ID, ""), sharedPreferences.getString(SharePreferenceKeys.API_KEY, ""), sharedPreferences.getString(SharePreferenceKeys.FCM_TOKEN, ""));
            call.enqueue(new retrofit2.Callback<LogoutApi.LogoutApiResponce>() {
                @Override
                public void onResponse(@NonNull Call<LogoutApi.LogoutApiResponce> call, @NonNull Response<LogoutApi.LogoutApiResponce> response) {
                    if (response.code() == Constants.RESPONCE_SUCCESSFUL) {
                        LogoutApi.LogoutApiResponce apiResponce = response.body();
                        if (apiResponce != null) {
                            if (apiResponce.getSuccess()) {
                                sharedPreferences.edit().clear().apply();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean(SharePreferenceKeys.SP_LOGOUT_STATUS, true).apply();
                                editor.putBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false).apply();

                            } else {
                                Toast.makeText(HomeActivity.this, apiResponce.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LogoutApi.LogoutApiResponce> call, @NonNull Throwable t) {

                }
            });

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

    public void sendRegistrationToServer(String refreshedToken) {


        if (Utilities.isNetworkAvailable(HomeActivity.this)) {

            callSaveFcmApi(refreshedToken);
        }

    }

    public void callSaveFcmApi(String refreshedToken) {

        try {

            Call<CommonResponse> call = apiService.saveFcmToken(token, userId, refreshedToken, "1", deviceId);
            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    CommonResponse apiResponse = response.body();
                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //home

    private void initViews1() {
        try {
            customProgressBar = new CustomProgressBar(activity);
            selfRequestLayout = findViewById(R.id.selfRequestLayout);
            totalBreakTimeLayout = findViewById(R.id.totalBreakTimeLayout);
            selfAttendanceLayout = findViewById(R.id.selfAttendanceLayout);
            teamAttendanceLayout = findViewById(R.id.teamAttendanceLayout);
            teamRequestLayout = findViewById(R.id.teamRequestLayout);
            apiService = ApiClient.getClient().create(ApiInterface.class);
            checkinTime = findViewById(R.id.checkinTime);
            totalHours = findViewById(R.id.totalHours);
            isHolidayStatus = findViewById(R.id.isHolidayStatus);
            totalMinutes = findViewById(R.id.totalMinutes);
            checkInStatus = findViewById(R.id.checkinStatus);
            breaksLayout = findViewById(R.id.breaksLayout);
            salesLayout = findViewById(R.id.salesLayout);
            transitLayout = findViewById(R.id.transitLayout);
            othersLayout = findViewById(R.id.othersLayout);

            requestsLayout = findViewById(R.id.requestsLayout);
            moreRequests = findViewById(R.id.moreRequests);
            totalBreakTime = findViewById(R.id.totalBreakTime);
            totalSalesTime = findViewById(R.id.totalSalesTime);
            totalTransitTime = findViewById(R.id.totalTransitTime);
            totalOthersTime = findViewById(R.id.totalOthersTime);
            checkinLayout = findViewById(R.id.checkinLayout);
            breaksTitle = findViewById(R.id.breaks);
            requestsTitle = findViewById(R.id.requests);
            upcomingEventsTitle = findViewById(R.id.upcomingEvents);
            upcomingHolidaysTitle = findViewById(R.id.upcomingHolidays);
            holidaysLayout = findViewById(R.id.holidaysLayout);
            eventsLayout = findViewById(R.id.eventsLayout);
            todayHolidaysLayout = findViewById(R.id.todayHolidaysLayout);
            todayEventsLayout = findViewById(R.id.todayEventsLayout);


            breaksRecycleView = findViewById(R.id.breaks_recycler_view);
            sales_recycler_view = findViewById(R.id.sales_recycler_view);
            transit_recycler_view = findViewById(R.id.transit_recycler_view);
            others_recycler_view = findViewById(R.id.others_recycler_view);
            eventsRecycleView = findViewById(R.id.upcoming_events_recycler_view);
            requestListRecycleView = findViewById(R.id.requests_recycler_view);
            holidaysRecycleView = findViewById(R.id.upcoming_holidays_recycler_view);
            todayEventsRecyclerView = findViewById(R.id.today_events_recycler_view);
            todayHolidaysRecyclerView = findViewById(R.id.today_holidays_recycler_view);


            eventsRecycleView.setNestedScrollingEnabled(false);
            requestListRecycleView.setNestedScrollingEnabled(false);
            holidaysRecycleView.setNestedScrollingEnabled(false);
            breaksRecycleView.setNestedScrollingEnabled(false);
            todayEventsRecyclerView.setNestedScrollingEnabled(false);
            todayHolidaysRecyclerView.setNestedScrollingEnabled(false);


            mins = findViewById(R.id.mins);
            hrs = findViewById(R.id.hrs);
            todayLayout = findViewById(R.id.todayLayout);
            line = findViewById(R.id.line);
            selfLayoutLine = findViewById(R.id.selfLayoutLine);
            teamLayout = findViewById(R.id.teamLayout);

            breaksRecycleView.setHasFixedSize(true);
            breaksLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            breaksRecycleView.setLayoutManager(breaksLayoutManager);
            breaksAdapter = new BreaksAdapter(context, breaksLists);
            breaksRecycleView.setAdapter(breaksAdapter);
            breaksRecycleView.setNestedScrollingEnabled(false);


            sales_recycler_view.setHasFixedSize(true);
            breaksLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            sales_recycler_view.setLayoutManager(breaksLayoutManager);
            salesAdapter = new BreaksAdapter(context, salesList);
            sales_recycler_view.setAdapter(salesAdapter);
            sales_recycler_view.setNestedScrollingEnabled(false);


            transit_recycler_view.setHasFixedSize(true);
            breaksLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            transit_recycler_view.setLayoutManager(breaksLayoutManager);
            transitAdapter = new BreaksAdapter(context, transitList);
            transit_recycler_view.setAdapter(transitAdapter);
            transit_recycler_view.setNestedScrollingEnabled(false);

            others_recycler_view.setHasFixedSize(true);
            breaksLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            others_recycler_view.setLayoutManager(breaksLayoutManager);
            othersAdapter = new BreaksAdapter(context, othersList);
            others_recycler_view.setAdapter(othersAdapter);
            others_recycler_view.setNestedScrollingEnabled(false);


            eventsRecycleView.setHasFixedSize(true);
            eventsLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            eventsRecycleView.setLayoutManager(eventsLayoutManager);


            eventsAdapter = new EventsAdaper(context, upcomingEvents);
            eventsRecycleView.setAdapter(eventsAdapter);
            eventsRecycleView.setNestedScrollingEnabled(false);

            todayEventsRecyclerView.setHasFixedSize(true);
            todayEventsLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            todayEventsRecyclerView.setLayoutManager(todayEventsLayoutManager);
            todayEventsAdapter = new EventsAdaper(context, todayEvents);
            todayEventsRecyclerView.setAdapter(todayEventsAdapter);
            todayEventsRecyclerView.setNestedScrollingEnabled(false);


            requestListRecycleView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            requestListRecycleView.setLayoutManager(layoutManager);
            selfRequestDataAdapter = new SelfRequestDataAdapter(context, selfRequestArrayList, true, "", "");
            requestListRecycleView.setAdapter(selfRequestDataAdapter);
            requestListRecycleView.setNestedScrollingEnabled(false);


            holidaysRecycleView.setHasFixedSize(true);
            holidaysLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            holidaysRecycleView.setLayoutManager(holidaysLayoutManager);
            holidaysAdapter = new HolidaysAdapter(context, upcomingHolidies);

            holidaysRecycleView.setAdapter(holidaysAdapter);
            holidaysRecycleView.setNestedScrollingEnabled(false);

            todayHolidaysRecyclerView.setHasFixedSize(true);
            todayHolidaysLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            todayHolidaysRecyclerView.setLayoutManager(todayHolidaysLayoutManager);
            todayHolidaysAdapter = new HolidaysAdapter(context, todayHolidies);
            todayHolidaysRecyclerView.setAdapter(todayHolidaysAdapter);
            todayHolidaysRecyclerView.setNestedScrollingEnabled(false);


            breaksLayout.setVisibility(View.GONE);
            checkinLayout.setVisibility(View.GONE);
            todayLayout.setVisibility(View.GONE);
            line.setVisibility(View.GONE);

            swipe_refresh = findViewById(R.id.swipe_refresh);


            if (!teamLead) {
                selfLayoutLine.setVisibility(View.GONE);
                teamLayout.setVisibility(View.GONE);
                teamAttendanceLayout.setVisibility(View.GONE);
                teamRequestLayout.setVisibility(View.GONE);

            }

            swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (Utilities.isNetworkAvailable(context)) {
                        swipe_refresh.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_bright, android.R.color.holo_orange_light, android.R.color.holo_green_light);
                        swipe_refresh.setRefreshing(true);
                        isRefresh = true;
                        callHomePageApi();

                    } else {
                        swipe_refresh.setRefreshing(false);
                        Toast.makeText(context, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callHomePageApi() {

        try {
            breaktime = 0;
            salesTime = 0;
            othersTime = 0;
            transitTime = 0;

            if (Utilities.isNetworkAvailable(context)) {

                callHomeDetails();

            } else {
                swipe_refresh.setRefreshing(false);
                Toast.makeText(context, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStatus() {
        try {
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SELF_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.TEAM_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.ADMIN_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_TEAM_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_ADMIN_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_SELF_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_CLICKED, false).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void callHomeDetails() {

        try {

            if (!isRefresh || isFirstTime) {
                isFirstTime = false;
                openProgress();
            } else {
                isRefresh = false;
            }

            Call<HomePageResponse> call = apiService.getHomeDetails(apiKey, userId);
            call.enqueue(new Callback<HomePageResponse>() {
                @Override
                public void onResponse(Call<HomePageResponse> call, Response<HomePageResponse> response) {
                    HomePageResponse apiResponse = response.body();
                    swipe_refresh.setRefreshing(false);
                    if (apiResponse != null) {
                        closeProgress();

                        if (apiResponse.isSuccess()) {
                            updateViews(apiResponse);
                        } else {
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                            // Utilities.ShowSnackbar(context,eventsRecycleView,apiResponse.getMessage());
                        }

                    } else {
                        closeProgress();
                        Toast.makeText(context, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();
                        //  Utilities.ShowSnackbar(context,eventsRecycleView,getResources().getString(R.string.somthing_went_wrong));
                    }

                }

                @Override
                public void onFailure(Call<HomePageResponse> call, Throwable t) {
                    closeProgress();
                    swipe_refresh.setRefreshing(false);


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateViews(HomePageResponse apiResponse) {

        try {

            breaktime = 0;
            salesTime = 0;
            othersTime = 0;
            transitTime = 0;



            try {

                MyApplication.homePageResponse = apiResponse;


                if (apiResponse.getToday() != null) {
                    //checkin
                    if (apiResponse.getToday().getCheck_in() != null && !apiResponse.getToday().getCheck_in().equals("0000-00-00 00:00:00")) {



                        checkinLayout.setVisibility(View.VISIBLE);
                        todayLayout.setVisibility(View.VISIBLE);
                        line.setVisibility(View.VISIBLE);

                        String checkIn = apiResponse.getToday().getCheck_in();
                        String checkOut = "";
                        String time1 = "";
                        if (!apiResponse.getToday().getCheck_out().equals("0000-00-00 00:00:00") && !apiResponse.getToday().getCheck_out().isEmpty()) {
                            checkOut = apiResponse.getToday().getCheck_out();
                            time1 = checkOut.substring(11);
                        }
                        String time = checkIn.substring(11);



                        loginTime = time;
                        logoutTime = time1;


                        int t = Integer.parseInt(checkIn.substring(11, 13));
                        if (t > 12) {
                            t = t - 12;
                            if (t < 10) {
                                checkinTime.setText("0" + t + checkIn.substring(13, 16) + " PM");
                            } else {
                                checkinTime.setText(t + checkIn.substring(13, 16) + " PM");
                            }

                        } else {
                            checkinTime.setText(checkIn.substring(11, 16) + " AM");
                        }

                        if (apiResponse.getToday().getShift_start_time() != null && !apiResponse.getToday().getShift_start_time().equals("00:00:00") && !apiResponse.getToday().getShift_start_time().isEmpty()) {
                            updateCheckInStatus(apiResponse.getToday().getShift_start_time());
                        }


                        int breakTime = apiResponse.getToday().getBreak_time();

                        if (breakTime == 0) {
                            totalBreakTimeLayout.setVisibility(View.GONE);
                        } else {
                            totalBreakTimeLayout.setVisibility(View.VISIBLE);

                        }

                        //breaks
                        if (apiResponse.getToday().getBreaks_list() != null) {

                            Breaks_list breaks_list = apiResponse.getToday().getBreaks_list();

                            if (breaks_list.getBreaks() != null) {
                                if (breaksLists != null && breaksLists.size() > 0) {
                                    breaksLists.clear();
                                }


                                tempBreaksLists = breaks_list.getBreaks();


                                for (int i = 0; i < tempBreaksLists.size(); i++) {

                                    boolean status = timeDifferenceOfBreaks(tempBreaksLists.get(i).getStart_time(), tempBreaksLists.get(i).getEnd_time());

                                    if (status) {
                                        breaksLists.add(tempBreaksLists.get(i));

                                    }


                                }
                            }
                            if (breaks_list.getSales() != null && breaks_list.getSales().size() > 0) {
                                if (salesList != null && salesList.size() > 0) {
                                    salesList.clear();
                                }
                                tempSalesList = breaks_list.getSales();



                                for (int i = 0; i < tempSalesList.size(); i++) {

                                    boolean status = timeDifferenceOfBreaks(tempSalesList.get(i).getStart_time(), tempSalesList.get(i).getEnd_time());
                                    if (status) {
                                        salesList.add(tempSalesList.get(i));
                                    }
                                }
                            }
                            if (breaks_list.getTransit() != null && breaks_list.getTransit().size() > 0) {

                                if (transitList != null && transitList.size() > 0) {
                                    transitList.clear();
                                }
                                tempTransitList = breaks_list.getTransit();


                                for (int i = 0; i < tempTransitList.size(); i++) {

                                    boolean status = timeDifferenceOfBreaks(tempTransitList.get(i).getStart_time(), tempTransitList.get(i).getEnd_time());
                                    if (status) {
                                        transitList.add(tempTransitList.get(i));
                                    }
                                }
                            }
                            if (breaks_list.getOthers() != null && breaks_list.getOthers().size() > 0) {
                                if (othersList != null && othersList.size() > 0) {
                                    othersList.clear();
                                }
                                tempOthersList = breaks_list.getOthers();
                                for (int i = 0; i < tempOthersList.size(); i++) {

                                    boolean status = timeDifferenceOfBreaks(tempOthersList.get(i).getStart_time(), tempOthersList.get(i).getEnd_time());
                                    if (status) {
                                        othersList.add(tempOthersList.get(i));
                                    }
                                }

                            }


                        }


                        if (breaksLists != null && breaksLists.size() > 0) {
                            breaksLayout.setVisibility(View.VISIBLE);
                            Collections.reverse(breaksLists);
                            updateBreaksAdapter();
                            for (int i = 0; i < breaksLists.size(); i++) {
                                timeDifference(breaksLists.get(i).getStart_time(), breaksLists.get(i).getEnd_time(), Constants.Breaks.breaks);
                            }
                            totalBreakTime.setVisibility(View.VISIBLE);
                            totalBreakTime.setText(breaktime + "");

                            if (breaktime <= 60) {
                                totalBreakTime.setText(breaktime + "");
                            } else {
                                int temp, hrs = 0, mins = 0;
                                temp = breaktime;
                                hrs = breaktime / 60;
                                mins = breaktime % 60;
                                totalBreakTime.setText(hrs + " hr " + mins);
                            }
                        } else {
                            breaksLayout.setVisibility(View.GONE);
                            totalBreakTime.setVisibility(View.GONE);
                        }


                        if (salesList != null && salesList.size() > 0) {
                            salesLayout.setVisibility(View.VISIBLE);
                            Collections.reverse(salesList);
                            salesAdapter.notifyDataSetChanged();
                            for (int i = 0; i < salesList.size(); i++) {
                                timeDifference(salesList.get(i).getStart_time(), salesList.get(i).getEnd_time(), Constants.Breaks.sales);
                            }


                            if (salesTime <= 60) {
                                totalSalesTime.setText(salesTime + "");
                            } else {
                                int temp, hrs = 0, mins = 0;
                                temp = salesTime;
                                hrs = salesTime / 60;
                                mins = salesTime % 60;
                                totalSalesTime.setText(hrs + " hr " + mins);
                            }


                        } else {
                            salesLayout.setVisibility(View.GONE);

                        }

                        if (transitList != null && transitList.size() > 0) {
                            transitLayout.setVisibility(View.VISIBLE);
                            Collections.reverse(transitList);

                            transitAdapter.notifyDataSetChanged();
                            for (int i = 0; i < transitList.size(); i++) {
                                timeDifference(transitList.get(i).getStart_time(), transitList.get(i).getEnd_time(), Constants.Breaks.transit);
                            }

                            totalTransitTime.setText(transitTime + "");

                            if (transitTime <= 60) {
                                totalTransitTime.setText(transitTime + "");
                            } else {
                                int temp, hrs = 0, mins = 0;
                                temp = transitTime;
                                hrs = transitTime / 60;
                                mins = transitTime % 60;
                                totalTransitTime.setText(hrs + " hr " + mins);
                            }


                        } else {
                            transitLayout.setVisibility(View.GONE);

                        }

                        if (othersList != null && othersList.size() > 0) {
                            othersLayout.setVisibility(View.VISIBLE);
                            Collections.reverse(othersList);
                            othersAdapter.notifyDataSetChanged();
                            for (int i = 0; i < othersList.size(); i++) {
                                timeDifference(othersList.get(i).getStart_time(), othersList.get(i).getEnd_time(), Constants.Breaks.others);
                            }
                            totalOthersTime.setText(othersTime + "");
                            if (othersTime <= 60) {
                                totalOthersTime.setText(othersTime + "");
                            } else {
                                int temp, hrs = 0, mins = 0;
                                temp = othersTime;
                                hrs = othersTime / 60;
                                mins = othersTime % 60;
                                totalOthersTime.setText(hrs + " hr " + mins);
                            }
                        } else {
                            othersLayout.setVisibility(View.GONE);

                        }

                        findWorkingTime();
                    } else {


                        sharedPreferences.edit().putBoolean(SharePreferenceKeys.IS_CHECKED, true).apply();

                        breaksLayout.setVisibility(View.GONE);
                        checkinLayout.setVisibility(View.VISIBLE);
                        todayLayout.setVisibility(View.VISIBLE);
                        line.setVisibility(View.VISIBLE);

                        updateHolidayStatus(apiResponse);


                    }

                } else {

                    breaksLayout.setVisibility(View.GONE);
                    checkinLayout.setVisibility(View.VISIBLE);
                    todayLayout.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);


                }


                if (apiResponse.getTodayEvents() != null && apiResponse.getTodayEvents().size() > 0) {
                    todayEvents = apiResponse.getTodayEvents();
                }
                //events
                if (apiResponse.getUpcomingEvents() != null && apiResponse.getUpcomingEvents().size() > 0) {
                    upcomingEvents = apiResponse.getUpcomingEvents();
                }

                if (todayEvents != null && todayEvents.size() > 0) {
                    for (int i = 0; i < todayEvents.size(); i++) {
                        upcomingEvents.add(todayEvents.get(i));
                    }
                }


                if (upcomingEvents != null && upcomingEvents.size() > 0) {
                    Collections.sort(upcomingEvents, new Comparator<UpcomingEvent>() {
                        public int compare(UpcomingEvent o1, UpcomingEvent o2) {
                            try {

                                return getDate(o1.getTmp_date()).compareTo(getDate(o2.getTmp_date()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });

                    // Collections.reverse(upcomingEvents);


                    eventsLayout.setVisibility(View.VISIBLE);
                    updateEventsAdapter();
                    //holidays

                }


           /* if(todayEvents!=null && todayEvents.size()>0)
            {

                todayEventsLayout.setVisibility(View.VISIBLE);
                if (todayEventsAdapter!=null){
                    todayEventsAdapter.setList(todayEvents);
                }
                todayEventsAdapter.notifyDataSetChanged();
            }*/

                if (apiResponse.getTodayHolidays() != null && apiResponse.getTodayHolidays().size() > 0) {
                    todayHolidies = apiResponse.getTodayHolidays();

                }


                if (apiResponse.getUpcomingHolidys() != null && apiResponse.getUpcomingHolidys().size() > 0) {
                    upcomingHolidies = apiResponse.getUpcomingHolidys();

                }

                if (todayHolidies != null && todayHolidies.size() > 0) {
                    for (int i = 0; i < todayHolidies.size(); i++) {
                        upcomingHolidies.add(todayHolidies.get(i));
                    }
                }


                if (upcomingHolidies != null && upcomingHolidies.size() > 0) {
                    //Collections.reverse(upcomingHolidies);
                    holidaysLayout.setVisibility(View.VISIBLE);
                    updateHolidaysAdapter();
                }







        /*    if(todayHolidies!=null && todayHolidies.size()>0)
            {
                todayHolidaysLayout.setVisibility(View.VISIBLE);
                if (todayHolidaysAdapter!=null){
                    todayHolidaysAdapter.setList(todayHolidies);
                }
                todayHolidaysAdapter.notifyDataSetChanged();
            }
*/


                //requests
                if (apiResponse.getRequest_self() != null && apiResponse.getRequest_self().size() > 0) {
                    if (tempSelfRequestArrayList != null && tempSelfRequestArrayList.size() > 0) {
                        tempSelfRequestArrayList.clear();
                    }

                    if (selfRequestArrayList != null && selfRequestArrayList.size() > 0) {
                        selfRequestArrayList.clear();
                    }


                    selfRequestArrayList.addAll(apiResponse.getRequest_self());


                }

                if (selfRequestArrayList != null && selfRequestArrayList.size() > 0) {
                    requestsLayout.setVisibility(View.VISIBLE);
                    updateRequestAdapter();
                } else {
                    requestsLayout.setVisibility(View.GONE);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(upcomingEvents!=null && upcomingEvents.size()>0){
            setEventListHeight();


        }

        if(upcomingHolidies!=null && upcomingHolidies.size()>0){
            setHolidaysListHeight();
        }



    }
    View trialView=null;
    private void setHolidaysListHeight() {
        if (true){
            return;
        }
        holidaysRecycleView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        if (upcomingHolidies != null && upcomingHolidies.size() > 4) {
                            recyclerView.getParent().requestDisallowInterceptTouchEvent(true);
                        }

                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

        /*float h = getResources().getDimension(R.dimen.homeListSize);

        if(holdaysItemSize!=0)
        {
            h=holdaysItemSize*3;
        }

        if (upcomingHolidies.size() > 4) {
            holidaysRecycleView.getLayoutParams().height = (int) h;
        } else {
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            holidaysRecycleView.setLayoutParams(lparams);
        }

        holidaysAdapter.notifyDataSetChanged();*/
    }

    private void setEventListHeight() {
        if (true){
            return;
        }
        eventsRecycleView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        if (upcomingEvents != null && upcomingEvents.size() > 4) {
                            recyclerView.getParent().requestDisallowInterceptTouchEvent(true);
                        }

                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView recyclerView, @NonNull MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean b) {

            }
        });

        /*float h = getResources().getDimension(R.dimen.homeListSize);

        if(eventsItemSize!=0)
        {
            h=eventsItemSize*3;
        }


        if (upcomingEvents.size() > 4) {
            eventsRecycleView.getLayoutParams().height = (int) h;
        } else {
            LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            eventsRecycleView.setLayoutParams(lparams);
        }

        eventsAdapter.notifyDataSetChanged();*/
    }

    private void updateHolidayStatus(HomePageResponse apiResponse) {
        try {
            if (apiResponse.getToday().getIs_holiday() != null && !apiResponse.getToday().getIs_holiday().isEmpty()) {
                String isHoliday = apiResponse.getToday().getIs_holiday();

                isHolidayStatus.setVisibility(View.VISIBLE);
                checkinLayout.setVisibility(View.GONE);
                todayLayout.setVisibility(View.GONE);
                line.setVisibility(View.GONE);

                if (isHoliday.equals(Constants.HOLIDAY_TYPE_NONE)) {
                    //isHolidayStatus.setText("ABSENT");
                    checkinLayout.setVisibility(View.VISIBLE);
                    todayLayout.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    isHolidayStatus.setText("");
                } else if (isHoliday.equals(Constants.HOLIDAY_TYPE_WEEKOFF)) {

                    isHolidayStatus.setText("WEEK OFF");
                } else if (isHoliday.equals(Constants.HOLIDAY_TYPE_CONFIRM)) {

                    isHolidayStatus.setText("HOLIDAY");
                } else if (isHoliday.equals(Constants.HOLIDAY_TYPE_OPTIONAL)) {

                    isHolidayStatus.setText("OPTIONAL HOLIDAY");
                } else if (isHoliday.equals(Constants.HOLIDAY_TYPE_LEAVE)) {

                    isHolidayStatus.setText("LEAVE");
                } else if (isHoliday.equals(Constants.HOLIDAY_TYPE_COMPOFF)) {

                    isHolidayStatus.setText("COMP OFF");
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCheckInStatus(String shifTime) {
        try {
            double shftTime = 0, logintime = 0;
            shftTime = getSecs(shifTime);
            logintime = getSecs(loginTime);
            if (logintime > shftTime) {
                double secs = logintime - shftTime;
                if (secs > 59) {
                    double mins = 0;
                    double hours = 0;
                    double totalmins = secs / 60;
                    hours = totalmins / 60;
                    mins = totalmins % 60;

                    checkInStatus.setVisibility(View.VISIBLE);
                    if (!String.valueOf((int) hours).equals("0")) {
                        if (String.valueOf((int) hours).equals("1")) {
                            checkInStatus.setText("Late Login ( " + String.valueOf((int) hours) + " hr " + String.valueOf((int) mins) + " mins )");
                        } else {
                            checkInStatus.setText("Late Login ( " + String.valueOf((int) hours) + " hrs " + String.valueOf((int) mins) + " mins )");
                        }

                    } else {
                        checkInStatus.setText("Late Login ( " + String.valueOf((int) mins) + " mins )");
                    }

                    // totalMinutes.setText(String.valueOf((int)mins));

                }
            } else {
                checkInStatus.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();


        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;


    }

    private void findWorkingTime() {

        try {

            Date currentTime = Calendar.getInstance().getTime();
            String curTime = (String.valueOf(currentTime)).substring(11, 19);
            double presebtTime = 0, logintime = 0;

            if (logoutTime.equals("")) {
                presebtTime = getSecs(curTime);
            } else {
                presebtTime = getSecs(logoutTime);
            }
            logintime = getSecs(loginTime);
            double secs = presebtTime - logintime;
            if (secs > 0) {
                double mins = 0;
                double hours = 0;
                double totalmins = secs / 60;
                hours = totalmins / 60;
                mins = totalmins % 60;
                totalHours.setText(String.valueOf((int) hours));
                totalMinutes.setText(String.valueOf((int) mins));
            }

            if (logoutTime != null && logoutTime.equals("")) {
                updateWotkingHours();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void updateWotkingHours() {
        try {
            ha = new Handler();
            ha.postDelayed(new Runnable() {

                @Override
                public void run() {
                    //call function
                    Date currentTime = Calendar.getInstance().getTime();
                    String curTime = (String.valueOf(currentTime)).substring(11, 19);
                    double presebtTime = 0, logintime = 0;
                    presebtTime = getSecs(curTime);
                    logintime = getSecs(loginTime);
                    double secs = presebtTime - logintime;
                    if (secs > 0) {
                        double mins = 0;
                        double hours = 0;
                        double totalmins = secs / 60;
                        hours = totalmins / 60;
                        mins = totalmins % 60;
                        if (totalHours != null) {
                            totalHours.setText(String.valueOf((int) hours));
                        }

                        if (totalMinutes != null) {
                            totalMinutes.setText(String.valueOf((int) mins));
                        }


                    }
                    ha.postDelayed(this, 10000);
                }
            }, 10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getSecs(String time) {
        try {
            double total = 0;
            if (time != null && time.length() > 0) {

                String hours, mins, secs;
                hours = time.substring(0, 2);
                mins = time.substring(3, 5);
                secs = time.substring(6);

                total = total + Double.parseDouble(hours) * 60 * 60;
                total = total + Double.parseDouble(mins) * 60;
                total = total + Double.parseDouble(secs);
            }
            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private void updateBreaksAdapter() {
        try {
            if (breaksLists != null && breaksLists.size() > 0) {
                breaksAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateEventsAdapter() {
        try {
            if (upcomingEvents != null && upcomingEvents.size() > 0) {

                if (eventsAdapter != null) {
                    eventsAdapter.setList(upcomingEvents);
                }
                eventsAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void updateHolidaysAdapter() {
        try {
            if (upcomingHolidies != null && upcomingHolidies.size() > 0) {

                if (holidaysAdapter != null) {
                    holidaysAdapter.setList(upcomingHolidies);
                }
                holidaysAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void updateRequestAdapter() {
        try {
            if (selfRequestDataAdapter != null) {
                //  selfRequestDataAdapter.setList(selfRequestArrayList);
                selfRequestDataAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private Date getDate(String date) throws ParseException {
        try {
            Date d = new SimpleDateFormat("MM-dd", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void timeDifference(String startTime, String endTime, int type) {

        try {

            Date startDate, endDate;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            try {

                startDate = simpleDateFormat.parse(startTime);
                endDate = simpleDateFormat.parse(endTime);
                //milliseconds
                long different = endDate.getTime() - startDate.getTime();


                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;


                long elapsedDays = different / daysInMilli;
                different = different % daysInMilli;

                long elapsedHours = different / hoursInMilli;
                different = different % hoursInMilli;

                long elapsedMinutes = different / minutesInMilli;
                different = different % minutesInMilli;

                long elapsedSeconds = different / secondsInMilli;


                switch (type) {
                    case Constants.Breaks.breaks:
                        if ((int) elapsedSeconds > 30) {
                            breaktime = breaktime + ((int) elapsedHours * 60) + (int) elapsedMinutes + 1;
                        } else {
                            breaktime = breaktime + ((int) elapsedHours * 60) + (int) elapsedMinutes;
                        }
                        break;
                    case Constants.Breaks.sales:
                        if ((int) elapsedSeconds > 30) {
                            salesTime = salesTime + ((int) elapsedHours * 60) + (int) elapsedMinutes + 1;
                        } else {
                            salesTime = salesTime + ((int) elapsedHours * 60) + (int) elapsedMinutes;
                        }
                        break;
                    case Constants.Breaks.transit:
                        if ((int) elapsedSeconds > 30) {
                            transitTime = transitTime + ((int) elapsedHours * 60) + (int) elapsedMinutes + 1;
                        } else {
                            transitTime = transitTime + ((int) elapsedHours * 60) + (int) elapsedMinutes;
                        }
                        break;
                    case Constants.Breaks.others:
                        if ((int) elapsedSeconds > 30) {
                            othersTime = othersTime + ((int) elapsedHours * 60) + (int) elapsedMinutes + 1;
                        } else {
                            othersTime = othersTime + ((int) elapsedHours * 60) + (int) elapsedMinutes;
                        }
                        break;

                }


            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void filterSelfRequests(List<SelfRequest> tempSelfRequestArrayList) {
        if (tempSelfRequestArrayList != null && tempSelfRequestArrayList.size() > 0) {
            for (int i = 0; i < tempSelfRequestArrayList.size(); i++) {
                boolean isAdd = false;
                String requestType = tempSelfRequestArrayList.get(i).getRequest_type().trim();

                if (Integer.parseInt(requestType) == Constants.REQUEST_LEAVE) {
                    isAdd = true;
                } else if (Integer.parseInt(requestType) == Constants.REQUEST_PERMISSION) {
                    isAdd = true;
                } else if (Integer.parseInt(requestType) == Constants.REQUEST_COMPOFF) {
                    isAdd = true;
                } else if (Integer.parseInt(requestType) == Constants.REQUEST_SWAP) {
                    isAdd = true;
                } else {
                    isAdd = false;
                }

                if (isAdd) {
                    selfRequestArrayList.add(tempSelfRequestArrayList.get(i));
                }
            }

        }
    }


    private void checkVersionApi(String version) {


        try {

            Call<CheckUpdateResponse> call = apiService.checkUpdateAvailable(apiKey, userId, "1", version);
            call.enqueue(new Callback<CheckUpdateResponse>() {
                @Override
                public void onResponse(Call<CheckUpdateResponse> call, Response<CheckUpdateResponse> response) {
                    CheckUpdateResponse apiResponse = response.body();

                    if (apiResponse != null) {

                        if (apiResponse.isSuccess()) {


                            if (apiResponse.isUpdate()) {
                                Navigation.getInstance().openUpdatePage(context);
                                activity.finish();
                           /*
                            Intent intent=new Intent(context,UpdateActivity.class);
                            startActivity(intent);*/

                            }


                        } else {
                            if (apiResponse.isUpdate()) {
                                Navigation.getInstance().openUpdatePage(context);
                                activity.finish();

                            }
                            // Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(context, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();

                    }

                }

                @Override
                public void onFailure(Call<CheckUpdateResponse> call, Throwable t) {


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean timeDifferenceOfBreaks(String startTime, String endTime) {


        boolean status = false;

        Date startDate, endDate;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            startDate = simpleDateFormat.parse(startTime);
            endDate = simpleDateFormat.parse(endTime);
            //milliseconds
            long different = endDate.getTime() - startDate.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;




           /* long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;*/


            long seconds = 0, mins = 0;

            seconds = different / secondsInMilli;
            mins = different / minutesInMilli;


            if (mins > 0 || (mins == 0 && seconds > 30)) {
                status = true;
                return true;
            } else {
                status = false;
                return false;
            }


        } catch (ParseException e) {
            e.printStackTrace();


        }

        return status;
    }

    public void openProgress() {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if ((activity != null)) {
                            if (customProgressBar != null && !customProgressBar.isShowing()) {
                                customProgressBar.show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void closeProgress() {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if ((activity != null)) {
                            if (customProgressBar != null && customProgressBar.isShowing()) {
                                customProgressBar.dismiss();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void moreRequests() {
        try {
            Intent intent = new Intent(context, RequestActivity.class);
            intent.putExtra("type", "1");
            intent.putExtra("requestType", "");
            intent.putExtra("fromDate", "");
            intent.putExtra("toDate", "");
            intent.putExtra("users", "");
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void teamAttendanceFragment() {
        try {
            updateStatus();
            Intent intent = new Intent(context, AttendanceActivity.class);
            intent.putExtra("type", "4");
            intent.putExtra("filterType", false);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void callSelfAttendanceFragment() {
        try {
            updateStatus();
            Intent intent = new Intent(context, AttendanceActivity.class);
            intent.putExtra("type", "2");
            intent.putExtra("filterType", false);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callSelfRequestFragment() {
        try {
            updateStatus();
            Intent intent = new Intent(context, RequestActivity.class);
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

    private void teamRequestFragment() {
        try {
            updateStatus();
            Intent intent = new Intent(context, RequestActivity.class);
            intent.putExtra("type", "3");
            intent.putExtra("requestType", " ");
            intent.putExtra("fromDate", " ");
            intent.putExtra("toDate", " ");
            intent.putExtra("users", " ");
            intent.putExtra("filter", false);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void checkVersion() {
        try {
            PackageInfo pInfo = null;
            String version = null;
            try {
                pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                version = pInfo.versionName;
                if (Utilities.isNetworkAvailable(context)) {

                    checkVersionApi(version);

                } else {
                    Toast.makeText(context, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                    //  Utilities.ShowSnackbar(context,eventsRecycleView,getResources().getString(R.string.no_internet_connection));
                }

            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openLink(String url){
        Uri uri = Uri.parse(url);
        CustomTabsIntent.Builder intentBuilder =
                new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = intentBuilder.build();
        customTabsIntent.launchUrl(this, uri);

    }

    private void setupPrivacyPolicy(){
        tv_privacy_policy = (TextView) findViewById(R.id.tv_privacy_policy);
        SpannableString ss = new SpannableString("Please read our Privacy policy");
//        ClickableSpan clickableSpanTerms = new ClickableSpan() {
//            @Override
//            public void onClick(View textView) {
//                drawableClose();
//                openLink("https://www.timedynamo.com/terms");
//            }
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
//            }
//        };
        ClickableSpan clickableSpanPrivacy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                drawableClose();
                openLink("https://www.timedynamo.com/privacy");
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
//        ss.setSpan(clickableSpanTerms, 16, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpanPrivacy, 16, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_privacy_policy.setText(ss);
        tv_privacy_policy.setMovementMethod(LinkMovementMethod.getInstance());
        tv_privacy_policy.setHighlightColor(Color.TRANSPARENT);

    }

    public void setEventItemSize(int height) {
        eventsItemSize=height;
        setEventListHeight();
    }

    public void setHolidaysItemSize(int height) {
        holdaysItemSize=height;
        setHolidaysListHeight();
    }
}
