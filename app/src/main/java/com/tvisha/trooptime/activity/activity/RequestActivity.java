package com.tvisha.trooptime.activity.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.tvisha.trooptime.activity.activity.apiPostModels.LogoutApi;
import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.helper.Navigation;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.ToastUtil;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.activity.activity.app.MyApplication;
import com.tvisha.trooptime.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;

;


public class RequestActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener, View.OnClickListener {

    Context context;
    Activity activity;

    Fragment fragment = null;
    Intent intent;

    FragmentManager fragmentManager;

    SharedPreferences sharedPreferences;
    Calendar myCalendar;
    DrawerLayout drawer;

    String dateFormat = "dd-MM-yyyy";
    String dateFormat1 = "yyyy-MM-dd";
    DatePickerDialog.OnDateSetListener date, date1;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.GERMAN);
    SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat, Locale.GERMAN);
    String CONTENT_TYPE = "application/x-www-form-urlencoded";
    ApiInterface apiService;

    boolean self = false;
    boolean team = false;
    boolean all = false;
    boolean cc = false;

    boolean filter_self = false;
    boolean filter_team = false;
    boolean filter_all = false;
    boolean filter_cc = false;

    String userId = "", email, name, user_avatar;
    int notification_num, servicecount;
    ImageView profile_pic, filterImage, drawerImage;

    TextView employeeName, employeeEmail, time, dynamo, tv_privacy_policy;
    String requestType = "", fromDate = "", toDate = "", users = "", type;

    boolean filter = false;
    boolean filterClicked = false;

    TabLayout navigation;


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
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onRestart() {
       /* if(navigation!=null)
        {
            TabLayout.Tab tab = navigation.getTabAt(2);
            tab.select();
        }*/
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        initViews();
        initListeners();
        initBottomMenuBar();
        shareprefernceData();
        handleIntent();

        processActivity();


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
        ss.setSpan(clickableSpanPrivacy, 16, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_privacy_policy.setText(ss);
        tv_privacy_policy.setMovementMethod(LinkMovementMethod.getInstance());
        tv_privacy_policy.setHighlightColor(Color.TRANSPARENT);

    }

    private void processActivity() {
        if (type.equals("1")) {

            requestType = intent.getStringExtra("requestType");
            fromDate = intent.getStringExtra("fromDate");
            toDate = intent.getStringExtra("toDate");
            users = intent.getStringExtra("users");
            filter = intent.getBooleanExtra("filter", false);
            filterClicked = filter;

            fragment = new SelfRequestFragment();
            Bundle bundle = new Bundle();
            bundle.putString("type", "1");
            bundle.putString("requestType", requestType);
            bundle.putString("fromDate", fromDate);
            bundle.putString("toDate", toDate);
            bundle.putString("users", "");
            bundle.putBoolean("filter", filter);

            fragment.setArguments(bundle);
            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
            }

        } else if (type.equals("2")) {

            // callSelfAttendanceFragment();
            Navigation.getInstance().openAttendance(RequestActivity.this, "2");


        } else if (type.equals("3")) {


            requestType = intent.getStringExtra("requestType");
            fromDate = intent.getStringExtra("fromDate");
            toDate = intent.getStringExtra("toDate");
            users = intent.getStringExtra("users");
            fragment = new SelfRequestFragment();
            filter = intent.getBooleanExtra("filter", false);

            Bundle bundle = new Bundle();
            bundle.putString("type", "2");
            bundle.putString("requestType", requestType);
            bundle.putString("fromDate", fromDate);
            bundle.putString("toDate", toDate);
            bundle.putString("users", users);
            bundle.putBoolean("filter", filter);
            filterClicked = filter;


            fragment.setArguments(bundle);
            if (fragment != null) {
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
            }

        } else if (type.equals("4")) {

            Navigation.getInstance().openAttendance(RequestActivity.this, "4");


        }


        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        Toolbar toolbar = findViewById(R.id.toolbar);
        View view = toolbar.findViewById(R.id.menubarLayout);

      /*  view.findViewById(R.id.menuLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    drawer.openDrawer(GravityCompat.START);
            }
        });*/

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowCustomEnabled(true);
        }


        updateProfile();


        LinearLayout attendanceLayout = findViewById(R.id.attendanceLayout);
        attendanceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawableClose();
//                Navigation.getInstance().openAttendance(RequestActivity.this, "2");
                try {
                    Intent intent = new Intent(RequestActivity.this, NewAttendanceActivity.class);
                    intent.putExtra("type", "0");
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        LinearLayout logout = findViewById(R.id.logoutLayout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                Navigation.getInstance().openLoginPage(RequestActivity.this);


            }
        });
        LinearLayout dashboard = findViewById(R.id.dashBoardLayout);
        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawableClose();
                callHomePage();
            }
        });


        LinearLayout request = findViewById(R.id.requestLayout);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                drawableClose();

//                requestType = intent.getStringExtra("requestType");
//                fromDate = intent.getStringExtra("fromDate");
//                toDate = intent.getStringExtra("toDate");
//                users = intent.getStringExtra("users");
//
//                fragment = new SelfRequestFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("type", "1");
//                bundle.putString("requestType", requestType);
//                bundle.putString("fromDate", fromDate);
//                bundle.putString("toDate", toDate);
//                bundle.putString("users", "");
//                bundle.putBoolean("filter", false);
//
//                fragment.setArguments(bundle);
//                if (fragment != null) {
//                    fragmentManager = getSupportFragmentManager();
//                    fragmentManager.beginTransaction().replace(R.id.content_main, fragment).commit();
//                }

            }
        });
    }

    private void updateProfile() {
        try {
            if (user_avatar != null && !user_avatar.isEmpty()) {
                Glide.with(RequestActivity.this).load(MyApplication.AWS_BASE_URL + user_avatar)
                        .apply(RequestOptions.circleCropTransform())
                        .into(profile_pic);
            } else {
                Glide.with(RequestActivity.this).load(R.drawable.avatar_placeholder_light).into(profile_pic);
            }

            employeeName.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFilterData() {
        try {
            self = sharedPreferences.getBoolean(SharePreferenceKeys.SELF_CLICKED, false);
            team = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_CLICKED, false);
            all = sharedPreferences.getBoolean(SharePreferenceKeys.ADMIN_CLICKED, false);
            cc = sharedPreferences.getBoolean(SharePreferenceKeys.CC_CLICKED, false);


            filter_self = sharedPreferences.getBoolean(SharePreferenceKeys.FILTER_SELF_CLICKED, false);
            filter_team = sharedPreferences.getBoolean(SharePreferenceKeys.FILTER_TEAM_CLICKED, false);
            filter_all = sharedPreferences.getBoolean(SharePreferenceKeys.FILTER_ADMIN_CLICKED, false);
            filter_cc = sharedPreferences.getBoolean(SharePreferenceKeys.FILTER_CC_CLICKED, false);


            if (self) {
                if (SelfRequestFragment.filterDateLayout != null && SelfRequestFragment.filterDateLayout.getVisibility() == View.VISIBLE && filter) {
                    onBackPressed();

                } else {
                    Intent filter_intent = new Intent(RequestActivity.this, RequestFilterActivity.class);
                    filter_intent.putExtra("filter_type", 1);
                    startActivity(filter_intent);

                }

            } else if (team) {

                if (SelfRequestFragment.filterDateLayout != null && SelfRequestFragment.filterDateLayout.getVisibility() == View.VISIBLE && filter) {
                    onBackPressed();

                } else {
                    Intent filter_intent = new Intent(RequestActivity.this, RequestFilterActivity.class);
                    filter_intent.putExtra("filter_type", 2);
                    startActivity(filter_intent);

                }

            } else if (all) {
                if (SelfRequestFragment.filterDateLayout != null && SelfRequestFragment.filterDateLayout.getVisibility() == View.VISIBLE && filter) {
                    onBackPressed();

                } else {
                    Intent filter_intent = new Intent(RequestActivity.this, RequestFilterActivity.class);
                    filter_intent.putExtra("filter_type", 3);
                    startActivity(filter_intent);

                }
            } else {
                if (SelfRequestFragment.filterDateLayout != null && SelfRequestFragment.filterDateLayout.getVisibility() == View.VISIBLE && filter) {
                    onBackPressed();

                } else {
                    Intent filter_intent = new Intent(RequestActivity.this, RequestFilterActivity.class);
                    filter_intent.putExtra("filter_type", 4);
                    startActivity(filter_intent);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleIntent() {
        try {
            intent=getIntent();
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                type = bundle.getString("type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initViews() {
        try {
            drawer = findViewById(R.id.drawer_layout);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            apiService = ApiClient.getClient().create(ApiInterface.class);
            time = findViewById(R.id.time);
            dynamo = findViewById(R.id.dynamo);
            navigation = findViewById(R.id.navigation);
            filterImage = findViewById(R.id.filterImage);
            drawerImage = findViewById(R.id.drawerImage);

            profile_pic = findViewById(R.id.profileImage);
            employeeName = findViewById(R.id.employeeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        setupPrivacyPolicy();
        filterImage.setOnClickListener(this);
        drawerImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.filterImage:
                    setFilterData();
                    break;
                case R.id.drawerImage:
                    drawer.openDrawer(GravityCompat.START);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void drawableClose() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void showRequestDialog() {


        final androidx.appcompat.app.AlertDialog alertDialog;
        LayoutInflater factory = LayoutInflater.from(RequestActivity.this);
        final View DialogView = factory.inflate(R.layout.dialog_request, null);
        androidx.appcompat.app.AlertDialog.Builder Dialog = new androidx.appcompat.app.AlertDialog.Builder(new ContextThemeWrapper(RequestActivity.this, R.style.myDialog));
        Dialog.setView(DialogView);
        alertDialog = Dialog.create();
        ImageButton requestClose;
        TextView request;
        requestClose = DialogView.findViewById(R.id.requestClose);
        request = DialogView.findViewById(R.id.request);

        alertDialog.show();
        requestClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkedID = ((RadioGroup) DialogView.findViewById(R.id.requestGroup)).getCheckedRadioButtonId();
                if (checkedID == R.id.leaveRequest) {
                    Navigation.getInstance().leaveRequest(RequestActivity.this);
                    alertDialog.dismiss();
                } else if (checkedID == R.id.permissionRequest) {
                    Navigation.getInstance().permissionRequest(RequestActivity.this);
                    alertDialog.dismiss();
                } else if (checkedID == R.id.directClientVistRequest) {
                    Navigation.getInstance().directClientLocation(RequestActivity.this);
                    alertDialog.dismiss();
                } else if (checkedID == R.id.compoffrequest) {
                    //ToastUtil.showToast(getContext(), "Comming Soon..!");
                    //Helper.getInstance().callConfirmation(getContext(),"9441255989");
                    Navigation.getInstance().compoffRequest(RequestActivity.this);
                    alertDialog.dismiss();
                } else {
                    ToastUtil.showToast(RequestActivity.this, "Select request type to continue");
                }
            }
        });
    }

    private void shareprefernceData() {
        try {
            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
            name = sharedPreferences.getString(SharePreferenceKeys.USER_NAME, "");
            user_avatar = sharedPreferences.getString(SharePreferenceKeys.USER_AVATAR, "");
            notification_num = sharedPreferences.getInt(SharePreferenceKeys.NOTIFICATION_COUNT, 0);
            servicecount = sharedPreferences.getInt("countforservice", 0);
            self = sharedPreferences.getBoolean(SharePreferenceKeys.SELF_CLICKED, false);
            team = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_CLICKED, false);
            all = sharedPreferences.getBoolean(SharePreferenceKeys.ADMIN_CLICKED, false);

            filter_self = sharedPreferences.getBoolean(SharePreferenceKeys.FILTER_SELF_CLICKED, false);
            filter_team = sharedPreferences.getBoolean(SharePreferenceKeys.FILTER_TEAM_CLICKED, false);
            filter_all = sharedPreferences.getBoolean(SharePreferenceKeys.FILTER_ADMIN_CLICKED, false);
            filter_cc = sharedPreferences.getBoolean(SharePreferenceKeys.FILTER_CC_CLICKED, false);
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

                if (i == 2) {
                    tab_label.setTextColor(getResources().getColor(R.color.tab_active_color));
                    tab_icon.setImageResource(navIconsActive[i]);
                } else {
                    tab_label.setTextColor(getResources().getColor(R.color.tab_inactive_color));
                    tab_icon.setImageResource(navIcons[i]);
                }

                navigation.addTab(navigation.newTab().setCustomView(tab));
            }


            navigation.addOnTabSelectedListener(this);
            navigation.addOnTabSelectedListener(RequestActivity.this);

            TabLayout.Tab tab = navigation.getTabAt(2);
            tab.select();
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

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        try {
            if (tab.getPosition() == 2) {
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
                        attendance();
                        break;
                    case 2:

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


    private void callHomePage() {
        try {
            Navigation.getInstance().openHomePageWithBack(RequestActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void attendance() {
        try {
            Intent intent = new Intent(RequestActivity.this, NewAttendanceActivity.class);
            intent.putExtra("type", "0");
            startActivity(intent);

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
