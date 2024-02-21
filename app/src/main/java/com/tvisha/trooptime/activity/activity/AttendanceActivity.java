package com.tvisha.trooptime.activity.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.tvisha.trooptime.R;
import com.tvisha.trooptime.activity.activity.adapter.TroopTimeAdapter;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.activity.activity.apiPostModels.AllAttendenceApiResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.Attendance;
import com.tvisha.trooptime.activity.activity.apiPostModels.FcmUpdateResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.FilterAllAttendenceApiResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.FilterAttendenceApiResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.SelfAttendenceApiResponce;
import com.tvisha.trooptime.activity.activity.app.MyApplication;
import com.tvisha.trooptime.activity.activity.dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.dialog.RequestDialog;
import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.helper.Navigation;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.model.CalenderModel;
import com.tvisha.trooptime.activity.activity.model.SelfAttenedModel;

import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AttendanceActivity extends Activity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, DatePickerDialog.OnDateSetListener,
        TabLayout.OnTabSelectedListener {
    public final static int PERMISSION_READ_STATE = 1;
    public static final String permissions[] = {"android.permission.CALL_PHONE",};
    public static Handler handler;
    public static String start_date, end_date;
    public static int dialog_month, dialog_year;
    TextView text_self, text_team, text_all, logger_name, month_name, name_of_month,
            total_emp_leaves, emp_lossofpay, available_leaves, last_month_leaves, notification_count, filterDate;
    RecyclerView emp_list_recyclerview, month_calendar;
    ApiInterface apiService;
    boolean teamtext_selected = false;
    List<SelfAttenedModel> self_data_arrayList;
    View underline_view;
    List<Attendance> attendence_arrayList = new ArrayList<>();
    List<Attendance> attendence_arrayList1 = new ArrayList<>();
    int selected_position, month_position, notification_num, tab_selected_position = 1;
    String userId, email, name, user_avatar, start_mon, end_dat, clicked_date, month_found, select_day, select_year;
    Calendar _calendar;
    int month, year, current_year, next_year, current_month, current_day;
    GridCellAdapter adapter;
    TroopTimeAdapter timeDataAdapter;
    LinearLayoutManager linearLayoutManager, manager;
    DrawerLayout drawer;
    RelativeLayout nav_drawer, search_view;
    LinearLayout logout_todo, todo_task, self_leave_layout, dashboard, monthCalenderLayout, dateLayout, filterDateLayout;
    EditText et_search;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    List<CalenderModel> calenderModels;
    SwipeRefreshLayout swipe_refresh;
    DatePickerDialog pic_dialog;
    RelativeLayout notification;
    ImageView filterImage, closeImage, profile_pic, closearch, text_nodata;
    ;
    SharedPreferences sharedPreferences;
    String dateFormat3 = "dd MMM yyyy", dateFormat = "yyyy-MM-dd hh:mm:ss", dateFormat1 = "dd MMM yyyy", dateFormat2 = "dd MMM yy";
    SimpleDateFormat sdf3 = new SimpleDateFormat(dateFormat3, Locale.US);
    SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat, Locale.US);
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat1, Locale.US);
    SimpleDateFormat sdf2 = new SimpleDateFormat(dateFormat2, Locale.US);
    boolean self = false, team = false, all = false, refresh = false, selfRefresh = false, filter_self = false, filter_team = false, filter_all = false,
            filterType = false, filterClicked = false, teamLead = false, is_full_access = false,attendance_all_tab=false,attendance_team_tab=false;
    String refreshedToken, type = "", apiKey = "";
    Intent intent;
    String selected_date = "", work_time, employee_name, fromDate = "", toDate = "", attendance_filter = "", filte_working_time = "", employee_userid = "";
    CustomProgressBar customProgressBar;
    TabLayout navigation;
    boolean isScroll = false;
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

    public void openProgress() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(AttendanceActivity.this).isFinishing()) {
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(AttendanceActivity.this).isFinishing()) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        processActivity();


    }

    @SuppressLint("HandlerLeak")
    private void processActivity() {

        try {


            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            if (sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false)) {
                shareprefernceData();
                refreshedToken = "";//FirebaseInstanceId.getInstance().getToken();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(SharePreferenceKeys.FCM_TOKEN, refreshedToken).apply();
                if (getIntent().getExtras() != null) {
                    if (getIntent().getExtras().getString("notification") != null && getIntent().getExtras().getString("notification").equals("notification")) {
                        RequestDialog requestDialog = new RequestDialog(this);
                        requestDialog.show();
                    }
                }
                intent = getIntent();

                filterType = intent.getBooleanExtra("filterType", false);
                filterClicked = filterType;
                if (filterType) {
                    getIntentData();
                } else {
                    type = intent.getStringExtra("type");
                }

                initializeWidgets();
                initBottomMenuBar();
                eventHandlings();


                Calendar c = Calendar.getInstance();
                String currentDate = sdf.format(c.getTime());

                name_of_month.setText(currentDate.substring(3, 6) + ", " + currentDate.substring(6));

                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case Constants.Static.CHECK_IN:
                                JSONObject object = (JSONObject) msg.obj;

                                if (object.optInt("month") > current_month && object.optInt("year") >= object.optInt("current_year")) {
                                    //  emp_list_recyclerview.setVisibility(View.GONE);
                                    //  text_nodata.setVisibility(View.VISIBLE);
                                    // adapter.scrollPosition();
                                    Toast.makeText(AttendanceActivity.this, getResources().getString(R.string.no_future_data_available), Toast.LENGTH_LONG).show();
                                } else {

                                    month_position = object.optInt("month_position");
                                    selfisOnedCalenderViewFilter(object.optInt("year"));
                                    dateLayout.setVisibility(View.GONE);
                                    filterDateLayout.setVisibility(View.VISIBLE);
                                    filterDate.setText(object.optInt("year") + "");

                                    getTheTroopTimeData(object.optString("start_date"), object.optString("end_date"));
                                }
                                break;
                        }
                    }
                };
                toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                drawer.closeDrawer(GravityCompat.START);
                drawer.addDrawerListener(toggle);
                toggle.syncState();

                Calendar calendar;


                if (!filterType) {
                    calendar = Calendar.getInstance();
                    String day = new SimpleDateFormat("dd").format(calendar.getTime());
                    current_day = Integer.parseInt(day);
                    selected_position = Integer.parseInt(day) - 1;
                    String months = new SimpleDateFormat("MM").format(calendar.getTime());
                    current_month = Integer.parseInt(months);
                    month_position = Integer.parseInt(months) - 1;
                    String current_year = new SimpleDateFormat("yyyy").format(calendar.getTime());
                    year = Integer.parseInt(current_year);
                    this.current_year = Integer.parseInt(current_year);
                    next_year = Integer.parseInt(current_year + 1);
                    selfisOnedCalenderView(year);
                } else {

                    calendar = Calendar.getInstance();
                    String day = new SimpleDateFormat("dd").format(calendar.getTime());
                    current_day = Integer.parseInt(day);
                    String months = new SimpleDateFormat("MM").format(calendar.getTime());
                    current_month = Integer.parseInt(months);
                    String current_year = new SimpleDateFormat("yyyy").format(calendar.getTime());
                    this.current_year = Integer.parseInt(current_year);
                    next_year = Integer.parseInt(current_year + 1);


                    if (!self) {
                        Date d = null;
                        try {
                            d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(selected_date);
                            calendar = Calendar.getInstance();
                            calendar.setTime(d);
                            String day1 = new SimpleDateFormat("dd").format(calendar.getTime());
                            selected_position = Integer.parseInt(day1) - 1;
                            String months1 = new SimpleDateFormat("MM").format(calendar.getTime());
                            month_position = Integer.parseInt(months1) - 1;
                            String current_year1 = new SimpleDateFormat("yyyy").format(calendar.getTime());
                            year = Integer.parseInt(current_year1);
                            selfisOnedCalenderView(year);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                }


            } else {
                Navigation.getInstance().login(this);

                finish();
            }

            if (!filterType) {
                if (type.equals("2")) {
                    selfData();

                } else {
                    teamData();
                }
            } else {
                if (self) {
                    month_calendar.setVisibility(View.GONE);
                    monthCalenderLayout.setVisibility(View.GONE);
                    selfData();
                    String y, m, d;

                    if (filterType) {
                        y = toDate.substring(0, 4);
                        m = toDate.substring(5, 7);

                        d = toDate.substring(8);
                        month = Integer.parseInt(m);

                        month_position = Integer.parseInt(m) - 1;
                        year = Integer.parseInt(y);
                        selfisOnedCalenderView(Integer.parseInt(y));

                        if (start_date != null && !start_date.trim().isEmpty() && start_date.length() > 3 && end_date != null && !end_date.trim().isEmpty() && end_date.length() > 3) {
                            filterDateLayout.setVisibility(View.VISIBLE);
                            dateLayout.setVisibility(View.GONE);
                            //month_calendar.setFocusableInTouchMode(false);
                            month_calendar.setClickable(false);
                            try {

                                //01 May 19

                                String fDt = getRequiredFilterDate(start_date);
                                String tDt = getRequiredFilterDate(end_date);

                                filterDate.setText(fDt.substring(0, 6) + "'" + fDt.substring(6) + " - " + tDt.substring(0, 6) + "'" + tDt.substring(6));
                                //filterDate.setText(getRequiredFilterDate(start_date)+" - "+getRequiredFilterDate(end_date));


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            dateLayout.setVisibility(View.VISIBLE);
                            filterDateLayout.setVisibility(View.GONE);
                        }

                    }

                } else if (team) {
                    month_calendar.setVisibility(View.GONE);
                    monthCalenderLayout.setVisibility(View.GONE);
                    updateTeamButton();
                    callToTeam();
                    String y, m, d;
                    if (filterType) {


                        try {


                            y = selected_date.substring(0, 4);

                            m = selected_date.substring(5, 7);

                            d = selected_date.substring(8);
                            d = selected_date.substring(8);

                            month = Integer.parseInt(m);
                            setGridCellAdapterToDate(Integer.parseInt(m), Integer.parseInt(y));
                            getSelfRequiredDate1(selected_date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }


                } else {
                    month_calendar.setVisibility(View.GONE);
                    monthCalenderLayout.setVisibility(View.GONE);
                    updateAllButton();
                    String y, m, d;
                    if (filterType) {
                        try {
                            y = selected_date.substring(0, 4);

                            m = selected_date.substring(5, 7);
                            d = selected_date.substring(8);
                            month = Integer.parseInt(m);
                            setGridCellAdapterToDate(Integer.parseInt(m), Integer.parseInt(y));
                            getSelfRequiredDate1(selected_date);


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                    callToAll();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void filter(String text) {

        try {

            List<Attendance> attendence_arrayList1 = new ArrayList<>();


            if (attendence_arrayList.size() > 0) {
                if (text != null && text.trim().length() > 0) {
                    for (int i = 0; i < attendence_arrayList.size(); i++) {
                        String s = attendence_arrayList.get(i).getName();

                        if (s.toLowerCase().contains(text.toLowerCase())) {
                            //adding the element to filtered list
                            attendence_arrayList1.add(attendence_arrayList.get(i));
                        }

                    }

                    if (team) {
                        sortFilterList("1", attendence_arrayList1);
                    } else if (all) {
                        sortFilterList("2", attendence_arrayList1);
                    }
                } else {
                    if (team) {
                        sortList("1");
                    } else if (all) {
                        sortList("2");
                    }
                    attendence_arrayList1.addAll(attendence_arrayList);
                }

            }


            timeDataAdapter.setarrayList(attendence_arrayList1);
            timeDataAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void resetForm() {

        try {

            currentDay();
            dateLayout.setVisibility(View.VISIBLE);
            filterDateLayout.setVisibility(View.GONE);
            start_date = "";
            end_date = "";
            fromDate = "";
            toDate = "";
            selected_date = "";
            employee_userid = "";
            filte_working_time="";
            attendance_filter="";
            filterType = false;
            start_mon = "";
            end_dat = "";
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getIntentData() {

        try {


            employee_userid = intent.getStringExtra("employee_userid");
            selected_date = intent.getStringExtra("selected_date");
            work_time = intent.getStringExtra("work_time");
            employee_name = intent.getStringExtra("employee_name");
            attendance_filter = intent.getStringExtra("attendance_filter");
            filte_working_time = intent.getStringExtra("filte_working_time");
            fromDate = intent.getStringExtra("fromDate");
            toDate = intent.getStringExtra("toDate");


            if (!self) {
                clicked_date = selected_date;

            } else {
                clicked_date = fromDate;
                start_date = fromDate;
                end_date = toDate;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void updateTheFcmTokeninServer() {
        try {
            if (sharedPreferences != null && sharedPreferences.getString(SharePreferenceKeys.FCM_TOKEN, "") != null && !sharedPreferences.getString(SharePreferenceKeys.FCM_TOKEN, "").trim().isEmpty() && !sharedPreferences.getString(SharePreferenceKeys.FCM_TOKEN, "").equals("null")) {
                refreshedToken = sharedPreferences.getString(SharePreferenceKeys.FCM_TOKEN, "");
            }
            /*} else if (FirebaseInstanceId.getInstance().getToken() != null) {
                refreshedToken = FirebaseInstanceId.getInstance().getToken();
            } else {
                refreshedToken = FirebaseIDService.getRefreshToken();
            }*/
            if (Utilities.getConnectivityStatus(this) > 0 && refreshedToken != null && !refreshedToken.trim().isEmpty() && !refreshedToken.equals("null")) {
                updateFcmTokenServercall(refreshedToken);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateFcmTokenServercall(String refreshedToken) {

        try {

            Call<FcmUpdateResponce> call = apiService.getupdate(sharedPreferences.getString(SharePreferenceKeys.USER_ID, ""),
                    sharedPreferences.getString(SharePreferenceKeys.API_KEY, ""), refreshedToken, "1");
            call.enqueue(new Callback<FcmUpdateResponce>() {
                @Override
                public void onResponse(Call<FcmUpdateResponce> call, Response<FcmUpdateResponce> response) {
                    FcmUpdateResponce apiResponse = response.body();

                    if (apiResponse != null) {
                        if (apiResponse.getSuccess()) {
                        }

                    }


                }

                @Override
                public void onFailure(Call<FcmUpdateResponce> call, Throwable t) {


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(permissions, PERMISSION_READ_STATE);
        } else {
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        try {
            switch (requestCode) {
                case PERMISSION_READ_STATE:
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    } else {
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selfisOnedCalenderView(int year) {
        //2018-09-01
        try {

            _calendar = Calendar.getInstance(Locale.getDefault());
            month = _calendar.get(Calendar.MONTH) + 1;
            try {
                name_of_month.setText(year + "");
            } catch (Exception e) {
                e.printStackTrace();
            }


            underline_view.setVisibility(View.VISIBLE);
            year = _calendar.get(Calendar.YEAR);

            String[] months = new DateFormatSymbols().getMonths();
            calenderModels = new ArrayList<>();
            int count = 0;
            int month_no = 1;
            for (int i = 0; i < months.length; i++) {
                String month = months[i];
                String month_name = month.substring(0, 3);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, count, 1);
                int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                String start_mon = "" + year + "-" + month_no + "-01";
                String end_mon = "" + year + "-" + month_no + "-" + numDays;

                count++;
                month_no++;
                CalenderModel model = new CalenderModel();
                model.setMonthStartDate(start_mon);
                model.setMonthEndDate(end_mon);
                model.setMonth_Name(month_name);
                model.setYears(year);
                model.setSelected(false);
                calenderModels.add(model);
            }
            adapter = new GridCellAdapter(AttendanceActivity.this, month, this.year, "self");
            adapter.notifyDataSetChanged();
            manager = new LinearLayoutManager(AttendanceActivity.this, LinearLayoutManager.HORIZONTAL, false);
            manager.setSmoothScrollbarEnabled(false);
            month_calendar.setLayoutManager(manager);
            month_calendar.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selfisOnedCalenderViewFilter(int year) {

        try {
            //2018-09-01

            _calendar = Calendar.getInstance(Locale.getDefault());
            month = _calendar.get(Calendar.MONTH) + 1;
            try {
                name_of_month.setText(year + "");
            } catch (Exception e) {
                e.printStackTrace();
            }


            underline_view.setVisibility(View.VISIBLE);
            //year = _calendar.get(Calendar.YEAR);

            String[] months = new DateFormatSymbols().getMonths();
            calenderModels = new ArrayList<>();
            int count = 0;
            int month_no = 1;
            for (int i = 0; i < months.length; i++) {
                String month = months[i];
                String month_name = month.substring(0, 3);
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, count, 1);
                int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                String start_mon = "" + year + "-" + month_no + "-01";
                String end_mon = "" + year + "-" + month_no + "-" + numDays;

                count++;
                month_no++;
                CalenderModel model = new CalenderModel();
                model.setMonthStartDate(start_mon);
                model.setMonthEndDate(end_mon);
                model.setMonth_Name(month_name);
                model.setYears(year);
                model.setSelected(false);
                calenderModels.add(model);
            }
            adapter = new GridCellAdapter(AttendanceActivity.this, month, this.year, "self");
            adapter.notifyDataSetChanged();
            manager = new LinearLayoutManager(AttendanceActivity.this, LinearLayoutManager.HORIZONTAL, false);
            manager.setSmoothScrollbarEnabled(false);
            month_calendar.setLayoutManager(manager);
            month_calendar.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setGridCellAdapterToDate(int month, int year) {

        try {

            adapter = new GridCellAdapter(AttendanceActivity.this, month, year, "team");

            _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));

            if ((selected_position - 4) > 1 && (selected_position - 4) < 28) {
                month_calendar.getLayoutManager().scrollToPosition(selected_position - 4);
            } else {
                month_calendar.getLayoutManager().scrollToPosition(selected_position);
            }
            adapter.notifyDataSetChanged();
            month_calendar.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void shareprefernceData() {
        try {
            userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
            name = sharedPreferences.getString(SharePreferenceKeys.USER_NAME, "");
            user_avatar = sharedPreferences.getString(SharePreferenceKeys.USER_AVATAR, "");
            notification_num = sharedPreferences.getInt(SharePreferenceKeys.NOTIFICATION_COUNT, 0);
            teamLead = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false);
            is_full_access = sharedPreferences.getBoolean(SharePreferenceKeys.FULL_ACCESS, false);
            attendance_all_tab = sharedPreferences.getBoolean(SharePreferenceKeys.attendance_all_tab, false);
            attendance_team_tab = sharedPreferences.getBoolean(SharePreferenceKeys.attendance_team_tab, false);

            self = sharedPreferences.getBoolean(SharePreferenceKeys.SELF_CLICKED, false);
            team = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_CLICKED, false);
            all = sharedPreferences.getBoolean(SharePreferenceKeys.ADMIN_CLICKED, false);
            filter_self = sharedPreferences.getBoolean(SharePreferenceKeys.FILTER_SELF_CLICKED, false);
            filter_team = sharedPreferences.getBoolean(SharePreferenceKeys.FILTER_TEAM_CLICKED, false);
            filter_all = sharedPreferences.getBoolean(SharePreferenceKeys.FILTER_ADMIN_CLICKED, false);
            apiKey = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void getTheTroopTimeData(String f_date, String e_date) {
        try {
            start_mon = f_date;
            end_dat = e_date;
            //month_calendar.setFocusableInTouchMode(false);
            //swipe_refresh.setRefreshing(true);
            //swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_purple, android.R.color.holo_orange_light, android.R.color.holo_red_light);
            self_data_arrayList = new ArrayList<>();
            if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                attendence_arrayList.clear();
            } else {
                attendence_arrayList = new ArrayList<>();
            }
            callToselfAttendence(f_date, e_date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callToselfAttendence(String f_date, String e_date) {

        try {


            if (filterType) {
                f_date = fromDate;
                e_date = toDate;
            } else {
                adapter.scrollPosition();

            }


            self = true;
            team = false;
            all = false;


            //String token = "";

            if (!filterType) {
                if (MyApplication.selfAttendenceApiResponce == null) {
                    callSelfAttendanceApi(f_date, e_date);
                } else {

                    callSelfAttendanceApi(f_date, e_date);

             /*
                SelfAttendenceApiResponce apiResponse=MyApplication.selfAttendenceApiResponce;
                if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                attendence_arrayList.clear();
                timeDataAdapter.setarrayList(attendence_arrayList);
                timeDataAdapter.setAdapterType("self");
                timeDataAdapter.notifyDataSetChanged();

            }
                if (apiResponse!=null && apiResponse.getAttendance() != null && apiResponse.getAttendance().size() > 0) {
                    emp_list_recyclerview.setVisibility(View.VISIBLE);
                    attendence_arrayList.addAll(apiResponse.getAttendance());
                }
                else
                {
                    emp_list_recyclerview.setVisibility(View.INVISIBLE);
                    text_nodata.setVisibility(View.VISIBLE);
                }


                addTheDataToAdapter("self");*/

                }
            } else {
                callSelfAttendanceApi(f_date, e_date);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void callSelfAttendanceApi(String f_date, String e_date) {
        try {
            openProgress();
            String userID = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            String token = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
            Call<SelfAttendenceApiResponce> call = apiService.getSelefAttendence(
                    userID,
                    token,
                    f_date,
                    e_date,
                    attendance_filter,
                    filte_working_time);


            call.enqueue(new Callback<SelfAttendenceApiResponce>() {
                @Override
                public void onResponse(Call<SelfAttendenceApiResponce> call, Response<SelfAttendenceApiResponce> response) {
                    SelfAttendenceApiResponce apiResponse = response.body();
                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.getSuccess()) {

                            if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                                attendence_arrayList.clear();
                                timeDataAdapter.setarrayList(attendence_arrayList);
                                timeDataAdapter.setAdapterType("self");
                                timeDataAdapter.notifyDataSetChanged();

                            }
                            if (apiResponse.getAttendance() != null && apiResponse.getAttendance().size() > 0) {
                                emp_list_recyclerview.setVisibility(View.VISIBLE);
                                attendence_arrayList.addAll(apiResponse.getAttendance());

                            } else {
                                emp_list_recyclerview.setVisibility(View.INVISIBLE);
                                text_nodata.setVisibility(View.VISIBLE);
                            }


                            addTheDataToAdapter("self");
                        } else {

                        }
                    } else {

                        closeProgress();
                        Utilities.ShowSnackbar(AttendanceActivity.this, emp_list_recyclerview, getResources().getString(R.string.somthing_went_wrong));
                    }

                }

                @Override
                public void onFailure(Call<SelfAttendenceApiResponce> call, Throwable t) {


                    closeProgress();


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void eventHandlings() {

        //    nav_drawer.setOnClickListener(this);
        try {
            logout_todo.setOnClickListener(this);
            dashboard.setOnClickListener(this);
            swipe_refresh.setOnRefreshListener(this);
            todo_task.setOnClickListener(this);
            text_self.setOnClickListener(this);
            text_team.setOnClickListener(this);
            text_all.setOnClickListener(this);
            name_of_month.setOnClickListener(this);
            notification.setOnClickListener(this);
            filterImage.setOnClickListener(this);
            closeImage.setOnClickListener(this);
            filterDateLayout.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void currentDay() {
        try {
            Calendar calendar = Calendar.getInstance();
            String day = new SimpleDateFormat("dd").format(calendar.getTime());
            current_day = Integer.parseInt(day);
            selected_position = Integer.parseInt(day) - 1;
            String months = new SimpleDateFormat("MM").format(calendar.getTime());
            current_month = Integer.parseInt(months);
            month_position = Integer.parseInt(months) - 1;
            String current_year = new SimpleDateFormat("yyyy").format(calendar.getTime());
            year = Integer.parseInt(current_year);
            this.current_year = Integer.parseInt(current_year);
            next_year = Integer.parseInt(current_year + 1);
            selfisOnedCalenderView(year);
            String cuurentDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            clicked_date = cuurentDate;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void currentDay1() {
        Calendar calendar = Calendar.getInstance();
        String day = new SimpleDateFormat("dd").format(calendar.getTime());
        current_day = Integer.parseInt(day);
        selected_position = Integer.parseInt(day) - 1;
        String months = new SimpleDateFormat("MM").format(calendar.getTime());
        current_month = Integer.parseInt(months);
        month_position = Integer.parseInt(months) - 1;
        String current_year = new SimpleDateFormat("yyyy").format(calendar.getTime());
        year = Integer.parseInt(current_year);
        this.current_year = Integer.parseInt(current_year);
        next_year = Integer.parseInt(current_year + 1);
        //selfisOnedCalenderView(year);
    }

    private void initializeWidgets() {

        try {

            apiService = ApiClient.getInstance();
            customProgressBar = new CustomProgressBar(AttendanceActivity.this);


            text_self = (TextView) findViewById(R.id.text_self);
            text_team = (TextView) findViewById(R.id.text_team);
            text_all = (TextView) findViewById(R.id.text_all);
            dateLayout = findViewById(R.id.dateLayout);
            filterDateLayout = findViewById(R.id.filterDateLayout);
            filterDate = findViewById(R.id.filterDate);
            closeImage = findViewById(R.id.closeImage);
            navigation = findViewById(R.id.navigation);


            closearch = (ImageView) findViewById(R.id.closearch);
            et_search = (EditText) findViewById(R.id.et_search);
            search_view = (RelativeLayout) findViewById(R.id.search_view);

            monthCalenderLayout = (LinearLayout) findViewById(R.id.monthCalenderLayout);
            emp_list_recyclerview = (RecyclerView) findViewById(R.id.emp_list_recyclerview);
            text_nodata = findViewById(R.id.text_nodata);
            month_calendar = (RecyclerView) findViewById(R.id.month_caledar);
            name_of_month = (TextView) findViewById(R.id.name_of_month);
            // title_text = (TextView) findViewById(R.id.title_text);
            nav_drawer = (RelativeLayout) findViewById(R.id.nav_drawer);
            navigationView = (NavigationView) findViewById(R.id.nav_view_3);
            logger_name = (TextView) findViewById(R.id.logger_name);
            //logger_email = (TextView) findViewById(R.id.logger_email);
            swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
            swipe_refresh.setRefreshing(false);
            drawer = (DrawerLayout) findViewById(R.id.drawer_layout_3);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            logout_todo = (LinearLayout) findViewById(R.id.logout);
            dashboard = (LinearLayout) findViewById(R.id.dashBoardLayout);
            month_name = (TextView) findViewById(R.id.month_name);

            todo_task = (LinearLayout) findViewById(R.id.todo_task);
            self_leave_layout = (LinearLayout) findViewById(R.id.self_leave_layout);
            total_emp_leaves = (TextView) findViewById(R.id.total_emp_leaves);
            last_month_leaves = (TextView) findViewById(R.id.last_month_leaves);
            available_leaves = (TextView) findViewById(R.id.available_leaves);
            emp_lossofpay = (TextView) findViewById(R.id.emp_lossofpay);
            underline_view = findViewById(R.id.underline_view);
            notification = (RelativeLayout) findViewById(R.id.notification);
            notification_count = (TextView) findViewById(R.id.notification_count);
            notification_count.setText("" + notification_num);
            filterImage = findViewById(R.id.filterImage);
            filterImage.setVisibility(View.VISIBLE);

            linearLayoutManager = new LinearLayoutManager(AttendanceActivity.this);
            linearLayoutManager.setSmoothScrollbarEnabled(true);
            emp_list_recyclerview.setLayoutManager(linearLayoutManager);
            timeDataAdapter = new TroopTimeAdapter(AttendanceActivity.this, attendence_arrayList, "self", "mainactivity");
            emp_list_recyclerview.setAdapter(timeDataAdapter);
//   if (sharedPreferences.getInt(SharePreferenceKeys.USER_ROLE, 0) == Constants.UserRole.ADMIN || is_full_access) {
            if ((sharedPreferences.getInt(SharePreferenceKeys.USER_ROLE, 0) == Constants.UserRole.ADMIN && attendance_all_tab)|| (is_full_access && attendance_all_tab) || attendance_all_tab) {
                text_all.setVisibility(View.VISIBLE);
                if (sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false)) {
                    text_team.setVisibility(View.VISIBLE);
                } else {
                    text_team.setVisibility(View.GONE);
                }
            } else if ((sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false) && attendance_team_tab) || attendance_team_tab) {
                text_all.setVisibility(View.GONE);
                text_team.setVisibility(View.VISIBLE);
            } else {
                text_all.setVisibility(View.GONE);
                text_team.setVisibility(View.GONE);
                text_self.setVisibility(View.GONE);
            }
            profile_pic = (ImageView) findViewById(R.id.profile_pic);
            if (user_avatar != null && !user_avatar.isEmpty()) {
                RequestOptions options = new RequestOptions()
                        .circleCropTransform()
                        .error(R.drawable.avatar_placeholder_light)
                        .priority(Priority.HIGH);
                Glide.with(AttendanceActivity.this).load(MyApplication.AWS_BASE_URL + user_avatar)
                        .apply(options).into(profile_pic);
            } else {
                Glide.with(AttendanceActivity.this).load(R.drawable.avatar_placeholder_light).into(profile_pic);
            }

            logger_name.setText(name);
            // logger_email.setText(email);
            et_search.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            et_search.setCursorVisible(true);
                        }
                    });

            et_search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().trim().length() > 0) {
                   /* if (timeDataAdapter != null) {
                        timeDataAdapter.search(s.toString());
                    }*/
                        filter(s.toString());
                    } else {
                      /*  if (timeDataAdapter != null) {
                            timeDataAdapter.setarrayList(attendence_arrayList);
                            timeDataAdapter.notifyDataSetChanged();
                        }*/
                        filter("");


                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
     /*   if(navigation!=null)
        {
            TabLayout.Tab tab = navigation.getTabAt(1);
            tab.select();
        }*/
    }

    @Override
    public void onClick(View view) {

        try {
            switch (view.getId()) {
                case R.id.notification:
                    if (sharedPreferences.getInt(SharePreferenceKeys.NOTIFICATION_COUNT, 0) != 0) {
                        notification_num = 0;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("notification_count", notification_num);
                        editor.apply();
                        notification_count.setText("" + notification_num);
                        Navigation.getInstance().notifications(this);


                    }

                    break;

                case R.id.name_of_month:

                    getFilterDate();
                    break;
                case R.id.text_self:
                    filterDateLayout.setVisibility(View.GONE);
                    dateLayout.setVisibility(View.VISIBLE);
                    resetForm(); //update 1/11/2019
                    if (filterType) {
                        resetForm();
                        month_calendar.setVisibility(View.VISIBLE);
                        monthCalenderLayout.setVisibility(View.VISIBLE);
                    }
                    setTheData(Constants.Tabclick.SELF);
                    storeSelf();

                    break;
                case R.id.text_team:

                    filterDateLayout.setVisibility(View.GONE);
                    dateLayout.setVisibility(View.VISIBLE);
                    emp_list_recyclerview.setVisibility(View.VISIBLE);
                    text_nodata.setVisibility(View.GONE);
                    if (filterType) {
                        resetForm();
                        month_calendar.setVisibility(View.VISIBLE);
                        monthCalenderLayout.setVisibility(View.VISIBLE);
                    }
                    setTheData(Constants.Tabclick.TEAM);

                    storeTeam();
                    break;
                case R.id.text_all:
                    filterDateLayout.setVisibility(View.GONE);
                    dateLayout.setVisibility(View.VISIBLE);
                    emp_list_recyclerview.setVisibility(View.VISIBLE);
                    text_nodata.setVisibility(View.GONE);
                    if (filterType) {
                        resetForm();
                        month_calendar.setVisibility(View.VISIBLE);
                        monthCalenderLayout.setVisibility(View.VISIBLE);
                    }
                    storeAll();
                    setTheData(Constants.Tabclick.ALL);
                    break;
                case R.id.todo_task:
                    drawer.closeDrawer(GravityCompat.START);
                    break;

                case R.id.filterImage:

                    filter_self = sharedPreferences.getBoolean(SharePreferenceKeys.FILTER_SELF_CLICKED, false);
                    filter_team = sharedPreferences.getBoolean(SharePreferenceKeys.FILTER_TEAM_CLICKED, false);
                    filter_all = sharedPreferences.getBoolean(SharePreferenceKeys.FILTER_ADMIN_CLICKED, false);

                    if (filterDateLayout != null && filterDateLayout.getVisibility() == View.VISIBLE && filterType) {
                        onBackPressed();

                    } else {
                        Intent filter_intent = new Intent(AttendanceActivity.this, AttendanceFilterActivity.class);
                        filter_intent.putExtra("filter_type", tab_selected_position);
                        startActivity(filter_intent);

                    }


                    break;

                case R.id.nav_drawer:
                    drawer.openDrawer(GravityCompat.START);
                    break;
                case R.id.dashBoardLayout:
                    drawer.closeDrawer(GravityCompat.START);
                    Navigation.getInstance().openHomePageWithBack(AttendanceActivity.this);


                    break;
                case R.id.logout:
                    sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
                    sharedPreferences.edit().putBoolean(SharePreferenceKeys.SP_LOGOUT_STATUS, true).apply();
                    sharedPreferences.edit().putBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false).apply();
                    sharedPreferences.edit().clear().apply();
                    Navigation.getInstance().openLoginPage(AttendanceActivity.this);



                    break;
                case R.id.nav_request:
                    drawer.closeDrawer(GravityCompat.START);
                    callRequest();


                    break;
                case R.id.previous_month:
                    previousMonth();
                    break;
                case R.id.next_month:
                    nextMonth();
                    break;
                case R.id.closeImage:
                    resetForm();
                    if (self) {
                        month_calendar.setVisibility(View.VISIBLE);
                        monthCalenderLayout.setVisibility(View.VISIBLE);
                        selfData();
                    } else if (team) {
                        month_calendar.setVisibility(View.VISIBLE);
                        monthCalenderLayout.setVisibility(View.VISIBLE);
                        teamData();
                    } else {
                        month_calendar.setVisibility(View.VISIBLE);
                        monthCalenderLayout.setVisibility(View.VISIBLE);
                        allData();
                    }
                    break;

                case R.id.filterDateLayout:
                    resetForm();
                    if (self) {
                        month_calendar.setVisibility(View.VISIBLE);
                        monthCalenderLayout.setVisibility(View.VISIBLE);
                        selfData();
                    } else if (team) {
                        month_calendar.setVisibility(View.VISIBLE);
                        monthCalenderLayout.setVisibility(View.VISIBLE);
                        teamData();
                    } else {
                        month_calendar.setVisibility(View.VISIBLE);
                        monthCalenderLayout.setVisibility(View.VISIBLE);
                        allData();
                    }
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void storeAll() {

        try {

            self = false;
            team = false;
            all = true;

            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SELF_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.ADMIN_CLICKED, true).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.TEAM_CLICKED, false).apply();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void storeTeam() {
        try {
            self = false;
            team = true;
            all = false;

            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SELF_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.ADMIN_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.TEAM_CLICKED, true).apply();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void storeSelf() {
        try {
            self = true;
            team = false;
            all = false;

            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SELF_CLICKED, true).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.TEAM_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.ADMIN_CLICKED, false).apply();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void nextMonth() {
        try {
            if (Utilities.getConnectivityStatus(AttendanceActivity.this) > 0) {
                if (teamtext_selected) {
                    calenderModels.removeAll(calenderModels);
                    if (month > 11) {
                        month = 1;
                        year++;
                    } else {
                        month++;
                    }
                    setToNameOfMonth(month, year);

                    setGridCellAdapterToDate(month, year);
                    Calendar calendar = Calendar.getInstance();
                    String days = new SimpleDateFormat("dd").format(calendar.getTime());
                    String cur_date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                    String day = "";
                    if (clicked_date != null) {
                        String[] split_date = clicked_date.split("-");
                        day = split_date[2];
                        if (month < 10) {
                            String mon = "0" + "" + month;
                            clicked_date = year + "-" + mon + "-" + day;
                        } else {
                            clicked_date = year + "-" + month + "-" + day;
                        }
                        selected_position = Integer.parseInt(day) - 1;
                    } else {
                        clicked_date = cur_date;
                        selected_position = Integer.parseInt(days) - 1;
                    }
                    if (tab_selected_position == Constants.Tabclick.TEAM) {
                        getTheTroopTeamData(clicked_date);
                    } else if (tab_selected_position == Constants.Tabclick.ALL) {
                        getTheTroopAllData(clicked_date);
                    }
                } else {
                    Utilities.ShowSnackbar(AttendanceActivity.this, emp_list_recyclerview, getResources().getString(R.string.network_check));
                }

            } else {
                year++;

                try {
                    name_of_month.setText(year);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                selfisOnedCalenderView(year);

                Calendar calendar = Calendar.getInstance();
                int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                String mont = new SimpleDateFormat("MM").format(calendar.getTime());

                String mon = "", day = "", s_date = "", e_date = "", e_mon = "", e_day = "";
                if (start_mon != null && end_dat != null) {
                    String[] split_date = start_mon.split("-");
                    String[] split_edate = end_dat.split("-");
                    for (int i = 0; i < split_date.length; i++) {
                        mon = split_date[1];
                        day = split_date[2];
                        e_mon = split_edate[1];
                        e_day = split_edate[2];
                        s_date = year + "-" + mon + "-" + day;
                        e_date = year + "-" + e_mon + "-" + e_day;
                        month_position = Integer.parseInt(e_mon) - 1;
                    }
                } else {
                    s_date = year + "-" + mont + "-" + "01";
                    e_date = year + "-" + mont + "-" + days;
                    month_position = Integer.parseInt(mont) - 1;
                }
                if (Utilities.getConnectivityStatus(AttendanceActivity.this) > 0) {
                    getTheTroopTimeData(s_date, e_date);
                } else {
                    Utilities.ShowSnackbar(AttendanceActivity.this, emp_list_recyclerview, getResources().getString(R.string.network_check));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void previousMonth() {
        try {
            if (Utilities.getConnectivityStatus(AttendanceActivity.this) > 0) {
                if (teamtext_selected) {
                    calenderModels.removeAll(calenderModels);
                    if (month <= 1) {
                        month = 12;
                        year--;
                    } else {
                        month--;
                    }
                    setToNameOfMonth(month, year);
                    setGridCellAdapterToDate(month, year);
                    Calendar calendar = Calendar.getInstance();
                    String days = new SimpleDateFormat("dd").format(calendar.getTime());
                    String cur_date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                    String day = "";
                    if (clicked_date != null) {
                        String[] split_date = clicked_date.split("-");
                        day = split_date[2];
                        if (month < 10) {
                            String mon = "0" + "" + month;
                            clicked_date = year + "-" + mon + "-" + day;
                        } else {
                            clicked_date = year + "-" + month + "-" + day;
                        }
                    } else {
                        clicked_date = cur_date;
                        selected_position = Integer.parseInt(days) - 1;
                    }

                    if (Utilities.getConnectivityStatus(AttendanceActivity.this) > 0) {
                        if (tab_selected_position == Constants.Tabclick.TEAM) {
                            getTheTroopTeamData(clicked_date);
                        } else if (tab_selected_position == Constants.Tabclick.ALL) {
                            getTheTroopAllData(clicked_date);
                        }
                    } else {
                        Utilities.ShowSnackbar(AttendanceActivity.this, emp_list_recyclerview, getResources().getString(R.string.network_check));
                    }
                } else {
                    year--;

                    month_name.setText(" " + year + " ");
                    selfisOnedCalenderView(year);
                    Calendar calendar = Calendar.getInstance();
                    int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    String mont = new SimpleDateFormat("MM").format(calendar.getTime());

                    String mon = "", day = "", s_date = "", e_date = "", e_mon = "", e_day = "";

                    if (start_mon != null && end_dat != null) {
                        String[] split_date = start_mon.split("-");
                        String[] split_edate = end_dat.split("-");
                        for (int i = 0; i < split_date.length; i++) {
                            mon = split_date[1];
                            day = split_date[2];
                            e_mon = split_edate[1];
                            e_day = split_edate[2];
                            s_date = year + "-" + mon + "-" + day;
                            e_date = year + "-" + e_mon + "-" + e_day;
                            month_position = Integer.parseInt(e_mon) - 1;
                        }
                    } else {
                        s_date = year + "-" + mont + "-" + "01";
                        e_date = year + "-" + mont + "-" + days;
                        month_position = Integer.parseInt(mont) - 1;
                    }
                    month_found = e_date;
                    getTheTroopTimeData(s_date, e_date);
                }
            } else {
                Utilities.ShowSnackbar(AttendanceActivity.this, emp_list_recyclerview, getResources().getString(R.string.network_check));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callRequest() {
        try {
            Intent intent = new Intent(AttendanceActivity.this, RequestActivity.class);
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

    private void getFilterDate() {
        try {
            final Calendar pic_calendar = Calendar.getInstance();
            final int pic_year = pic_calendar.get(Calendar.YEAR);
            final int pic_month = pic_calendar.get(Calendar.MONTH);
            int pic_day = pic_calendar.get(Calendar.DAY_OF_MONTH);

            if (teamtext_selected) {

                pic_dialog = new DatePickerDialog(AttendanceActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int pi_year, int day_month, int dayOfMonth) {

                        if (view.isShown()) {


                            if ((day_month + 1) < 10) {
                                String mon = "0" + "" + (day_month + 1);
                                if (dayOfMonth < 10) {
                                    String day = "0" + "" + dayOfMonth;
                                    select_day = "" + String.valueOf(pi_year) + "-" + mon + "-" + day;
                                } else {
                                    select_day = "" + String.valueOf(pi_year) + "-" + mon + "-" + dayOfMonth;
                                }
                            } else {
                                if (dayOfMonth < 10) {
                                    String day = "0" + "" + dayOfMonth;
                                    select_day = "" + String.valueOf(pi_year) + "-" + String.valueOf(day_month + 1) + "-" + day;
                                } else {
                                    select_day = "" + String.valueOf(pi_year) + "-" + String.valueOf(day_month + 1) + "-" + dayOfMonth;
                                }
                            }
                            month = day_month + 1;
                            year = pi_year;
                            select_year = "" + String.valueOf(year);
                            selected_position = (dayOfMonth - 1);
                            setGridCellAdapterToDate(month, year);
                            setToNameOfMonth(month, year);
                            if (et_search != null) {
                                et_search.getText().clear();
                            }
                            if (Utilities.getConnectivityStatus(AttendanceActivity.this) > 0) {
                                if (tab_selected_position == Constants.Tabclick.TEAM) {

                                    getTheTroopTeamData(select_day);
                                } else {

                                    getTheTroopAllData(select_day);
                                }
                            } else {

                                Utilities.ShowSnackbar(AttendanceActivity.this, emp_list_recyclerview, getResources().getString(R.string.network_check));
                            }
                        }

                    }
                }, pic_year, pic_month, pic_day);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    pic_dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                } else {
                    pic_dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                }
                pic_dialog.show();

            } else {
                try {
                    MonthYearDialog pd = new MonthYearDialog();
                    pd.setListener(this);
                    pd.show(getFragmentManager(), "MonthYearPickerDialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setTheData(int tab_click) {
        try {
            switch (tab_click) {
                case Constants.Tabclick.SELF:
                    selfData();
                    break;
                case Constants.Tabclick.TEAM:
                    teamData();
                    break;
                case Constants.Tabclick.ALL:
                    allData();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void allData() {

        try {


            if (et_search != null) {
                et_search.getText().clear();
            }
            if (timeDataAdapter != null) {
                timeDataAdapter.setTheTabPosition(tab_selected_position);
            }
            updateAllButton();

            getAllAttendanceData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateAllButton() {
        try {
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SELF_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.TEAM_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.ADMIN_CLICKED, true).apply();
            search_view.setVisibility(View.VISIBLE);
            tab_selected_position = Constants.Tabclick.ALL;
            underline_view.setVisibility(View.VISIBLE);
            teamtext_selected = true;

            text_self.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_notselected));
            text_self.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_notselected));

            if (text_team != null && text_team.getVisibility() == View.VISIBLE) {
                text_team.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_notselected));
                text_team.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_notselected));
            }
            if (text_all != null && text_all.getVisibility() == View.VISIBLE) {
                text_all.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_selected));
                text_all.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_sleceted));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selfData() {
        try {
            if (et_search != null) {
                et_search.getText().clear();
            }
            if (timeDataAdapter != null) {
                timeDataAdapter.setTheTabPosition(tab_selected_position);
            }
            updateSelfButton();
            getSelefAttendenceData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateSelfButton() {
        try {
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SELF_CLICKED, true).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.TEAM_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.ADMIN_CLICKED, false).apply();
            search_view.setVisibility(View.GONE);
            tab_selected_position = Constants.Tabclick.SELF;
            teamtext_selected = false;
            // addRequest.setVisibility(View.GONE);
            underline_view.setVisibility(View.VISIBLE);

            try {
                name_of_month.setText(String.valueOf(year));
            } catch (Exception e) {
                e.printStackTrace();
            }


            text_self.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_selected));
            text_self.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_sleceted));
            if (text_team != null && text_team.getVisibility() == View.VISIBLE) {
                text_team.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_notselected));
                text_team.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_notselected));
            }
            if (text_all != null && text_all.getVisibility() == View.VISIBLE) {
                text_all.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_notselected));
                text_all.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_notselected));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void teamData() {
        try {


            if (et_search != null) {
                et_search.getText().clear();
            }
            if (timeDataAdapter != null) {
                timeDataAdapter.setTheTabPosition(tab_selected_position);
            }
            updateTeamButton();

            getTeamAttendanceData();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateTeamButton() {

        try {

            sharedPreferences.edit().putBoolean(SharePreferenceKeys.SELF_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.TEAM_CLICKED, true).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.ADMIN_CLICKED, false).apply();
            search_view.setVisibility(View.VISIBLE);
            tab_selected_position = Constants.Tabclick.TEAM;

            underline_view.setVisibility(View.VISIBLE);
            teamtext_selected = true;

            text_self.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_notselected));
            text_self.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_notselected));

            if (text_team != null && text_team.getVisibility() == View.VISIBLE) {
                text_team.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_selected));
                text_team.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_sleceted));
            }
            if (text_all != null && text_all.getVisibility() == View.VISIBLE) {
                text_all.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_notselected));
                text_all.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_notselected));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void getAllAttendanceData() {

        try {

            setToNameOfMonth(month, year);

            setGridCellAdapterToDate(month, year);
            Calendar calendar = Calendar.getInstance();
            String days = new SimpleDateFormat("dd").format(calendar.getTime());
            String cur_date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            String day = "";

            String[] split_date = cur_date.split("-");
            String year_d = split_date[0];
            int month_d = Integer.parseInt(split_date[1]);
            int day_d = Integer.parseInt(split_date[2]);
            if (clicked_date == null || clicked_date.trim().isEmpty() || clicked_date.trim().equals("null")) {
                if (month_d < 10) {
                    String mon = "0" + month_d;
                    if (day_d < 10) {
                        String day_a = "0" + day_d;
                        clicked_date = year_d + "-" + mon + "-" + day_a;
                        selected_position = day_d - 1;
                    } else {
                        clicked_date = year_d + "-" + mon + "-" + String.valueOf(day_d);
                        selected_position = day_d - 1;
                    }
                } else {
                    if (day_d < 10) {
                        String day_a = "0" + day_d;
                        clicked_date = year_d + "-" + String.valueOf(month_d) + "-" + day_a;
                        selected_position = day_d - 1;
                    } else {
                        clicked_date = year_d + "-" + String.valueOf(month_d) + "-" + String.valueOf(day_d);
                        selected_position = day_d - 1;
                    }
                }
            }
            if (Utilities.getConnectivityStatus(AttendanceActivity.this) > 0) {
                getTheTroopAllData(clicked_date);
            } else {
                Utilities.ShowSnackbar(AttendanceActivity.this, emp_list_recyclerview, getResources().getString(R.string.network_check));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getTeamAttendanceData() {
        try {

            setToNameOfMonth(month, year);

            setGridCellAdapterToDate(month, year);
            Calendar calendar = Calendar.getInstance();
            String days = new SimpleDateFormat("dd").format(calendar.getTime());
            String cur_date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            String day = "";

            String[] split_date = cur_date.split("-");
            String year_d = split_date[0];
            int month_d = Integer.parseInt(split_date[1]);
            int day_d = Integer.parseInt(split_date[2]);
            if (clicked_date == null || clicked_date.trim().isEmpty() || clicked_date.trim().equals("null")) {
                if (month_d < 10) {
                    String mon = "0" + month_d;
                    if (day_d < 10) {
                        String day_a = "0" + day_d;
                        clicked_date = year_d + "-" + mon + "-" + day_a;
                        selected_position = day_d - 1;
                    } else {
                        clicked_date = year_d + "-" + mon + "-" + String.valueOf(day_d);
                        selected_position = day_d - 1;
                    }
                } else {
                    if (day_d < 10) {
                        String day_a = "0" + day_d;
                        clicked_date = year_d + "-" + String.valueOf(month_d) + "-" + day_a;
                        selected_position = day_d - 1;
                    } else {
                        clicked_date = year_d + "-" + String.valueOf(month_d) + "-" + String.valueOf(day_d);
                        selected_position = day_d - 1;
                    }
                }
            }
            if (Utilities.getConnectivityStatus(AttendanceActivity.this) > 0) {
                getTheTroopTeamData(clicked_date);
            } else {
                Utilities.ShowSnackbar(AttendanceActivity.this, emp_list_recyclerview, getResources().getString(R.string.network_check));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setToNameOfMonth(int month, int year) {
        try {
            if (month != 0 && month <= 12) {
                name_of_month.setText(Constants.MONTHS[month - 1] + ", " + year);
                try {
                    month_name.setText(year + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getSelefAttendenceData() {

        try {
            if (name_of_month != null) {
                name_of_month.setText(year + "");
            }


            selfisOnedCalenderView(year);

            String mon = "", day = "", s_date = "", e_date = "", e_mon = "", e_day = "";

            if (start_mon == null || start_mon.trim().isEmpty() || end_dat == null || end_dat.trim().isEmpty()) {
                Calendar calendar = Calendar.getInstance();
                int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                String mont = new SimpleDateFormat("MM").format(calendar.getTime());
                String c_year = new SimpleDateFormat("yyyy").format(calendar.getTime());

                int month_s = Integer.parseInt(mont);

                if (month_s < 10) {
                    String months_s = "0" + month_s;
                    s_date = c_year + "-" + months_s + "-" + "01";
                    e_date = c_year + "-" + months_s + "-" + days;
                    month_position = month_s - 1;
                } else {
                    s_date = c_year + "-" + mont + "-" + "01";
                    e_date = c_year + "-" + mont + "-" + days;
                    month_position = Integer.parseInt(mont) - 1;
                }
            }
            if (Utilities.getConnectivityStatus(AttendanceActivity.this) > 0) {
                if (start_mon != null && !start_mon.trim().isEmpty() && end_dat != null && !end_dat.trim().isEmpty()) {
                    getTheTroopTimeData(start_mon, end_dat);
                } else {
                    getTheTroopTimeData(s_date, e_date);
                }

            } else {
                Utilities.ShowSnackbar(AttendanceActivity.this, emp_list_recyclerview, getResources().getString(R.string.network_check));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void getTheTroopTeamData(String data) {
        try {

            clicked_date = data;
            // swipe_refresh.setRefreshing(true);
            //month_calendar.setFocusableInTouchMode(false);

            //  swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_purple, android.R.color.holo_orange_light, android.R.color.holo_red_light);
            if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                attendence_arrayList.removeAll(attendence_arrayList);
                if (timeDataAdapter != null) {
                    timeDataAdapter.notifyDataSetChanged();
                }
            }
            attendence_arrayList = new ArrayList<>();

            callToserverToGetThedata(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTheTroopAllData(String data) {
        try {

            //month_calendar.setFocusableInTouchMode(false);
            //  swipe_refresh.setRefreshing(true);
            //    swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_purple, android.R.color.holo_orange_light, android.R.color.holo_red_light);
            if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                attendence_arrayList.removeAll(attendence_arrayList);
                if (timeDataAdapter != null) {
                    timeDataAdapter.notifyDataSetChanged();
                }
            }
            attendence_arrayList = new ArrayList<>();

            callToserverToGetTheAlldata(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callToserverToGetTheAlldata(String date) {
        try {

            openProgress();
            self = false;
            team = false;
            all = true;
            try {
                retrofit2.Call<AllAttendenceApiResponce> call = ApiClient.getInstance().getAllAttendence(userId, sharedPreferences.getString(SharePreferenceKeys.API_KEY, ""), date);
                call.enqueue(new retrofit2.Callback<AllAttendenceApiResponce>() {
                    @Override
                    public void onResponse(@NonNull Call<AllAttendenceApiResponce> call, @NonNull Response<AllAttendenceApiResponce> response) {
                        if (response.code() == Constants.RESPONCE_SUCCESSFUL) {
                            closeProgress();
                            AllAttendenceApiResponce attendenceApiResponce = response.body();
                            if (attendenceApiResponce != null) {
                                if (attendenceApiResponce.getSuccess()) {
                                    if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                                        attendence_arrayList.clear();
                                        timeDataAdapter.setarrayList(attendence_arrayList);
                                        timeDataAdapter.setAdapterType("team");
                                        timeDataAdapter.notifyDataSetChanged();
                                    }


                                    if (attendenceApiResponce.getAttendance() != null && attendenceApiResponce.getAttendance().size() > 0) {
                                        emp_list_recyclerview.setVisibility(View.VISIBLE);
                                        attendence_arrayList.addAll(attendenceApiResponce.getAttendance());
                                        sortList("2");
                                        addTheDataToAdapter("team");
                                    } else {
                                        emp_list_recyclerview.setVisibility(View.INVISIBLE);
                                        text_nodata.setVisibility(View.VISIBLE);
                                    }


                                }

                            }
                        } else {
                            closeProgress();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AllAttendenceApiResponce> call, @NonNull Throwable t) {


                        closeProgress();


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callToserverToGetThedata(String date) {
        try {
            openProgress();
            self = false;
            team = true;
            all = false;
            //team attendance api

            try {
                retrofit2.Call<FilterAttendenceApiResponce> call =  ApiClient.getInstance()
                        .getFilterAttendence(userId, date, sharedPreferences.getString(SharePreferenceKeys.API_KEY, ""),
                                employee_userid.trim(), attendance_filter, filte_working_time);


                call.enqueue(new retrofit2.Callback<FilterAttendenceApiResponce>() {
                    @Override
                    public void onResponse(@NonNull Call<FilterAttendenceApiResponce> call, @NonNull Response<FilterAttendenceApiResponce> response) {


                        FilterAttendenceApiResponce attendenceApiResponce = response.body();
                        closeProgress();
                        if (attendenceApiResponce != null) {
                            if (attendenceApiResponce.getSuccess()) {


                                if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                                    attendence_arrayList.clear();
                                    timeDataAdapter.setarrayList(attendence_arrayList);
                                    timeDataAdapter.setAdapterType("team");
                                    timeDataAdapter.notifyDataSetChanged();
                                }

                                if (attendenceApiResponce.getAttendance() != null && attendenceApiResponce.getAttendance().size() > 0) {
                                    emp_list_recyclerview.setVisibility(View.VISIBLE);
                                    attendence_arrayList.addAll(attendenceApiResponce.getAttendance());
                                    sortList("1");
                                    addTheDataToAdapter("team");
                                } else {
                                    emp_list_recyclerview.setVisibility(View.INVISIBLE);
                                    text_nodata.setVisibility(View.VISIBLE);
                                }


                            }

                        }


                    }

                    @Override
                    public void onFailure(@NonNull Call<FilterAttendenceApiResponce> call, @NonNull Throwable t) {
                        closeProgress();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callToAll() {

        try {

            if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                attendence_arrayList.removeAll(attendence_arrayList);
                if (timeDataAdapter != null) {
                    timeDataAdapter.notifyDataSetChanged();
                }
            }
            attendence_arrayList = new ArrayList<>();
            self = false;
            team = false;
            all = true;
            //all attendance api
            openProgress();


            retrofit2.Call<FilterAllAttendenceApiResponce> call = ApiClient.getInstance()
                    .getFilterAllAttendence(userId, selected_date, sharedPreferences.getString(SharePreferenceKeys.API_KEY, ""),
                            employee_userid.trim(), Integer.parseInt(attendance_filter), Integer.parseInt(filte_working_time));


            call.enqueue(new retrofit2.Callback<FilterAllAttendenceApiResponce>() {
                @Override
                public void onResponse(@NonNull Call<FilterAllAttendenceApiResponce> call, @NonNull Response<FilterAllAttendenceApiResponce> response) {
                    if (response.code() == Constants.RESPONCE_SUCCESSFUL) {
                        closeProgress();
                        FilterAllAttendenceApiResponce attendenceApiResponce = response.body();

                        if (attendenceApiResponce != null) {
                            if (attendenceApiResponce.getSuccess()) {
                                if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                                    attendence_arrayList.clear();
                                    timeDataAdapter.setarrayList(attendence_arrayList);
                                    timeDataAdapter.setAdapterType("team");
                                    timeDataAdapter.notifyDataSetChanged();
                                }

                                if (attendenceApiResponce.getAttendance() != null && attendenceApiResponce.getAttendance().size() > 0) {
                                    emp_list_recyclerview.setVisibility(View.VISIBLE);
                                    attendence_arrayList.addAll(attendenceApiResponce.getAttendance());
                                    sortList("2");
                                    addTheDataToAdapter("team");
                                } else {
                                    emp_list_recyclerview.setVisibility(View.INVISIBLE);
                                    text_nodata.setVisibility(View.VISIBLE);
                                }


                            }
                        }
                    } else {
                        closeProgress();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FilterAllAttendenceApiResponce> call, @NonNull Throwable t) {


                    closeProgress();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callToTeam() {

        try {

            if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                attendence_arrayList.removeAll(attendence_arrayList);
                if (timeDataAdapter != null) {
                    timeDataAdapter.notifyDataSetChanged();
                }
            }
            attendence_arrayList = new ArrayList<>();
            self = false;
            team = true;
            all = false;
            //team attendance api

            openProgress();


            retrofit2.Call<FilterAttendenceApiResponce> call =  ApiClient.getInstance()
                    .getFilterAttendence(userId, selected_date, sharedPreferences.getString(SharePreferenceKeys.API_KEY, ""),
                            employee_userid.trim(), attendance_filter + "", filte_working_time + "");


            call.enqueue(new retrofit2.Callback<FilterAttendenceApiResponce>() {
                @Override
                public void onResponse(@NonNull Call<FilterAttendenceApiResponce> call, @NonNull Response<FilterAttendenceApiResponce> response) {
                    if (response.code() == Constants.RESPONCE_SUCCESSFUL) {
                        closeProgress();
                        FilterAttendenceApiResponce attendenceApiResponce = response.body();
                        /*Log.e("response", attendenceApiResponce.toString());*/
                        if (attendenceApiResponce != null) {
                            if (attendenceApiResponce.getSuccess()) {
                                if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                                    attendence_arrayList.clear();
                                    timeDataAdapter.setarrayList(attendence_arrayList);
                                    timeDataAdapter.setAdapterType("team");
                                    timeDataAdapter.notifyDataSetChanged();
                                }


                                if (attendenceApiResponce.getAttendance() != null && attendenceApiResponce.getAttendance().size() > 0) {
                                    emp_list_recyclerview.setVisibility(View.VISIBLE);
                                    attendence_arrayList.addAll(attendenceApiResponce.getAttendance());
                                    sortList("1");
                                    addTheDataToAdapter("team");
                                } else {
                                    emp_list_recyclerview.setVisibility(View.INVISIBLE);
                                    text_nodata.setVisibility(View.VISIBLE);
                                }


                            } else {
                                emp_list_recyclerview.setVisibility(View.INVISIBLE);
                                text_nodata.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        closeProgress();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FilterAttendenceApiResponce> call, @NonNull Throwable t) {

                    closeProgress();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

            closeProgress();
        }
    }


    private void addTheDataToAdapter(String category) {

        try {


            int day = 0;
            //month_calendar.setFocusableInTouchMode(true);
            //   swipe_refresh.setRefreshing(false);
            if (clicked_date != null && !clicked_date.isEmpty()) {
                String[] split = clicked_date.split("-");
                String date = split[2];
                day = Integer.parseInt(date);
            }


            if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                if (category.equals("team")) {


                    search_view.setVisibility(View.VISIBLE);

                    if (!filterType) {
                        if (day <= current_day || month < current_month || year < this.current_year) {
                            text_nodata.setVisibility(View.GONE);
                            emp_list_recyclerview.setVisibility(View.VISIBLE);
                        } else {
                            text_nodata.setVisibility(View.VISIBLE);
                            emp_list_recyclerview.setVisibility(View.GONE);
                        }
                    } else {
                        text_nodata.setVisibility(View.GONE);
                        emp_list_recyclerview.setVisibility(View.VISIBLE);
                    }


                    underline_view.setVisibility(View.VISIBLE);
                    teamtext_selected = true;
                    if (tab_selected_position == Constants.Tabclick.TEAM) {
                        if (text_team != null && text_team.getVisibility() == View.VISIBLE) {
                            text_team.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_selected));
                            text_team.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_sleceted));
                        }
                        if (text_all != null && text_all.getVisibility() == View.VISIBLE) {
                            text_all.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_notselected));
                            text_all.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_notselected));
                        }
                    } else if (tab_selected_position == Constants.Tabclick.ALL) {
                        if (text_all != null && text_all.getVisibility() == View.VISIBLE) {
                            text_all.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_selected));
                            text_all.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_sleceted));
                        }
                        if (text_team != null && text_team.getVisibility() == View.VISIBLE) {
                            text_team.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_notselected));
                            text_team.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_notselected));
                        }
                    }
                    text_self.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_notselected));
                    text_self.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_notselected));

                } else if (category.equals("self")) {
                    text_nodata.setVisibility(View.GONE);
                    emp_list_recyclerview.setVisibility(View.VISIBLE);
                    teamtext_selected = false;
                    underline_view.setVisibility(View.VISIBLE);

                    try {
                        name_of_month.setText(year + "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    text_self.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_selected));
                    text_self.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_sleceted));
                    if (text_team != null && text_team.getVisibility() == View.VISIBLE) {
                        text_team.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_notselected));
                        text_team.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_notselected));
                    }
                    if (text_all != null && text_all.getVisibility() == View.VISIBLE) {
                        text_all.setBackgroundColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_team_notselected));
                        text_all.setTextColor(ContextCompat.getColor(AttendanceActivity.this, R.color.text_notselected));
                    }
                }

                if (timeDataAdapter != null) {
                    timeDataAdapter.setTheTabPosition(tab_selected_position);

                    if (!filterType) {
                        timeDataAdapter.setarrayList(attendence_arrayList);

                    } else {
                        if (self) {
                            timeDataAdapter.setarrayList(attendence_arrayList);
                        } else {
                            timeDataAdapter.setarrayList(attendence_arrayList);
                            if (refresh) {
                                refresh = false;
                            }
                        }
                    }
                    timeDataAdapter.setAdapterType(category);
                    emp_list_recyclerview.setVisibility(View.VISIBLE);
                    text_nodata.setVisibility(View.GONE);
                    timeDataAdapter.notifyDataSetChanged();
                }

            } else {

                search_view.setVisibility(View.GONE);
                // addRequest.setVisibility(View.GONE);
                text_nodata.setVisibility(View.VISIBLE);
                emp_list_recyclerview.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortList(String pinType) {

        try {
            List<Attendance> top_arrayList = new ArrayList<>();
            List<Attendance> bottom_arrayList = new ArrayList<>();
            List<Attendance> middle_arrayList = new ArrayList<>();

            if (attendence_arrayList != null && !attendence_arrayList.isEmpty() && attendence_arrayList.size() > 0) {
                int size = attendence_arrayList.size();
                String pinStatus = "", pinnedDate = "", pin_type = "";
                for (int i = 0; i < size; i++) {
                    if (attendence_arrayList.get(i).getIsPinned() != null && !attendence_arrayList.get(i).getIsPinned().trim().isEmpty()) {
                        pinStatus = attendence_arrayList.get(i).getIsPinned();
                        pinnedDate = attendence_arrayList.get(i).getPinnedDate();
                        pin_type = attendence_arrayList.get(i).getPinType();


                        if (pinStatus.equals("1") && pin_type.equals(pinType)) {
                            top_arrayList.add(attendence_arrayList.get(i));
                        } else if (pinStatus.equals("2") && pin_type.equals(pinType)) {

                            bottom_arrayList.add(attendence_arrayList.get(i));
                        } else {
                            middle_arrayList.add(attendence_arrayList.get(i));
                        }


                    }

                }
                if (top_arrayList != null && !top_arrayList.isEmpty() && top_arrayList.size() > 0) {

                    Collections.sort(top_arrayList, new Comparator<Attendance>() {
                        public int compare(Attendance o1, Attendance o2) {
                            try {
                                return getRequiredDate2(o2.getPinnedDate()).compareTo(getRequiredDate2(o1.getPinnedDate()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });

                }

                if (bottom_arrayList != null && !bottom_arrayList.isEmpty() && bottom_arrayList.size() > 0) {

                    Collections.sort(bottom_arrayList, new Comparator<Attendance>() {
                        public int compare(Attendance o1, Attendance o2) {
                            try {
                                return getRequiredDate2(o1.getPinnedDate()).compareTo(getRequiredDate2(o2.getPinnedDate()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });

                }


                if (top_arrayList.size() > 0 || bottom_arrayList.size() > 0 || middle_arrayList.size() > 0) {
                    attendence_arrayList.clear();
                    //adapter.notifyDataSetChanged();
                }
                if (top_arrayList.size() > 0) {
                    for (int i = 0; i < top_arrayList.size(); i++) {
                        attendence_arrayList.add(top_arrayList.get(i));

                    }

                }
                if (middle_arrayList.size() > 0) {
                    for (int i = 0; i < middle_arrayList.size(); i++) {
                        attendence_arrayList.add(middle_arrayList.get(i));

                    }
                }
                if (bottom_arrayList.size() > 0) {
                    for (int i = 0; i < bottom_arrayList.size(); i++) {
                        attendence_arrayList.add(bottom_arrayList.get(i));

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sortFilterList(String pinType, List<Attendance> attendence_arrayList) {

        try {

            List<Attendance> top_arrayList = new ArrayList<>();
            List<Attendance> bottom_arrayList = new ArrayList<>();
            List<Attendance> middle_arrayList = new ArrayList<>();

            if (attendence_arrayList != null && !attendence_arrayList.isEmpty() && attendence_arrayList.size() > 0) {
                int size = attendence_arrayList.size();
                String pinStatus = "", pinnedDate = "", pin_type = "";
                for (int i = 0; i < size; i++) {
                    if (attendence_arrayList.get(i).getIsPinned() != null && !attendence_arrayList.get(i).getIsPinned().trim().isEmpty()) {
                        pinStatus = attendence_arrayList.get(i).getIsPinned();
                        pinnedDate = attendence_arrayList.get(i).getPinnedDate();
                        pin_type = attendence_arrayList.get(i).getPinType();


                        if (pinStatus.equals("1") && pin_type.equals(pinType)) {
                            top_arrayList.add(attendence_arrayList.get(i));
                        } else if (pinStatus.equals("2") && pin_type.equals(pinType)) {

                            bottom_arrayList.add(attendence_arrayList.get(i));
                        } else {
                            middle_arrayList.add(attendence_arrayList.get(i));
                        }


                    }

                }
                if (top_arrayList != null && !top_arrayList.isEmpty() && top_arrayList.size() > 0) {

                    Collections.sort(top_arrayList, new Comparator<Attendance>() {
                        public int compare(Attendance o1, Attendance o2) {
                            try {
                                return getRequiredDate2(o2.getPinnedDate()).compareTo(getRequiredDate2(o1.getPinnedDate()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });

                }

                if (bottom_arrayList != null && !bottom_arrayList.isEmpty() && bottom_arrayList.size() > 0) {

                    Collections.sort(bottom_arrayList, new Comparator<Attendance>() {
                        public int compare(Attendance o1, Attendance o2) {
                            try {
                                return getRequiredDate2(o1.getPinnedDate()).compareTo(getRequiredDate2(o2.getPinnedDate()));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return 0;
                        }
                    });

                }


                if (top_arrayList.size() > 0 || bottom_arrayList.size() > 0 || middle_arrayList.size() > 0) {
                    attendence_arrayList.clear();
                    //adapter.notifyDataSetChanged();
                }

                if (top_arrayList.size() > 0) {
                    for (int i = 0; i < top_arrayList.size(); i++) {
                        attendence_arrayList.add(top_arrayList.get(i));

                    }

                }
                if (middle_arrayList.size() > 0) {
                    for (int i = 0; i < middle_arrayList.size(); i++) {
                        attendence_arrayList.add(middle_arrayList.get(i));

                    }
                }
                if (bottom_arrayList.size() > 0) {
                    for (int i = 0; i < bottom_arrayList.size(); i++) {
                        attendence_arrayList.add(bottom_arrayList.get(i));

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRefresh() {
        try {
            if (et_search != null) {
                et_search.getText().clear();
            }
            swipe_refresh.setRefreshing(false);
            swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            if (Utilities.getConnectivityStatus(AttendanceActivity.this) > 0) {
                if (teamtext_selected) {
                    Calendar calendar = Calendar.getInstance();
                    String days = new SimpleDateFormat("dd").format(calendar.getTime());
                    String cur_date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                    String day = "";

                    if (select_day != null && !select_day.isEmpty()) {
                        clicked_date = select_day;
                        select_day = "";
                        start_date = "";
                        end_date = "";
                    } else if (clicked_date != null && !clicked_date.isEmpty()) {
                        String[] split_date = clicked_date.split("-");
                        day = split_date[2];
                        if (month < 10) {
                            String mo = "0" + "" + month;
                            clicked_date = year + "-" + mo + "-" + day;
                        } else {

                            clicked_date = year + "-" + month + "-" + day;
                        }
                        selected_position = Integer.parseInt(day) - 1;

                    } else {
                        clicked_date = cur_date;
                        selected_position = Integer.parseInt(days) - 1;
                    }
                    try {
                        String[] spliting_date = clicked_date.split("-");
                        int day1 = Integer.parseInt(spliting_date[2]);
                        int month1 = Integer.parseInt(spliting_date[1]);
                        int year1 = Integer.parseInt(spliting_date[0]);
                        Calendar calendar_s = Calendar.getInstance();
                        //calendar_s.set(year1, (month1 - 1), day1);
                        String serverDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar_s.getTime());
                        Date server_date = dateFormat.parse(clicked_date);
                        Date cuurent_date = dateFormat.parse(serverDate);

                        if (server_date.after(cuurent_date)) {
                            //month_calendar.setFocusableInTouchMode(true);
                            //   swipe_refresh.setRefreshing(false);
                            text_nodata.setVisibility(View.VISIBLE);
                            emp_list_recyclerview.setVisibility(View.GONE);


                        } else {
                            if (team) {
                                swipe_refresh.setRefreshing(false);

                                if (filterType) {

                                    refresh = true;

                                    callToTeam();
                                } else {

                                    getTheTroopTeamData(clicked_date);
                                }
                            } else if (all) {
                                swipe_refresh.setRefreshing(false);

                                if (filterType) {

                                    refresh = true;
                                    callToAll();
                                } else {

                                    getTheTroopAllData(clicked_date);
                                }
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                    //month_name.setText(" " + year + " ");
                    selfRefresh = true;
                    adapter.scrollPosition();
                    Calendar calendar = Calendar.getInstance();
                    int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    String mont = new SimpleDateFormat("MM").format(calendar.getTime());

                    String mon = "", day = "", s_date = "", e_date = "", e_mon = "", e_day = "";
                    if (start_date != null && end_date != null) {

                        start_mon = start_date;
                        end_dat = end_date;
                        //select_day = "";
                        //  start_date = "";
                        //end_date = "";
                    } else {


                        Calendar calendar1 = Calendar.getInstance();
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar1.getTime());
                        int no_of_days = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH);
                        String[] split_date = date.split("-");
                        int month = Integer.parseInt(split_date[1]);
                        int year = Integer.parseInt(split_date[0]);
                        if (month < 10) {
                            String current_mon = "0" + "" + month;
                            start_mon = year + "-" + current_mon + "-" + "01";
                            end_dat = year + "-" + current_mon + "-" + no_of_days;
                        } else {
                            start_mon = year + "-" + month + "-" + "01";
                            end_dat = year + "-" + month + "-" + no_of_days;
                        }
                    }
                    if ((start_mon == null || start_mon.trim().isEmpty()) || (end_dat == null || end_dat.trim().isEmpty())) {
                        Calendar calendar1 = Calendar.getInstance();
                        String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar1.getTime());
                        int no_of_days = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH);
                        String[] split_date = date.split("-");
                        int month = Integer.parseInt(split_date[1]);
                        int year = Integer.parseInt(split_date[0]);
                        if (month < 10) {
                            String current_mon = "0" + "" + month;
                            start_mon = year + "-" + current_mon + "-" + "01";
                            end_dat = year + "-" + current_mon + "-" + no_of_days;
                        } else {
                            start_mon = year + "-" + month + "-" + "01";
                            end_dat = year + "-" + month + "-" + no_of_days;
                        }
                    }
                    try {

                        Calendar to_day = Calendar.getInstance();
                        String today_date = new SimpleDateFormat("yyyy-MM-dd").format(to_day.getTime());
                        Date todays_date = dateFormat.parse(today_date);
                        Date sending_date = dateFormat.parse(start_mon);
                        String[] splitting = start_mon.split("-");
                        int year = Integer.parseInt(splitting[0]);
                        selfisOnedCalenderView(year);
                        if (sending_date.after(todays_date)) {
                            //month_calendar.setFocusableInTouchMode(true);
                            //    swipe_refresh.setRefreshing(false);
                            text_nodata.setVisibility(View.VISIBLE);
                            emp_list_recyclerview.setVisibility(View.GONE);


                        } else {
                            swipe_refresh.setRefreshing(false);
                            getTheTroopTimeData(start_mon, end_dat);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //month_calendar.setFocusableInTouchMode(true);
                //   swipe_refresh.setRefreshing(false);
                Utilities.ShowSnackbar(AttendanceActivity.this, emp_list_recyclerview, getResources().getString(R.string.network_check));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int yr, int mnth, int dayOfMonth) {


    }

    public int getTheTabPosition() {
        return tab_selected_position;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

      /*  if(tab.getPosition()==1)
        {
            View tabView = tab.getCustomView();
            TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
            ImageView tab_icon = (ImageView) tabView.findViewById(R.id.nav_icon);
            tab_label.setTextColor(getResources().getColor(R.color.tab_active_color));
            tab_icon.setImageResource(navIconsActive[tab.getPosition()]);
        }*/
        setFragment(tab);

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        setFragment(tab);
    }

    public Date getRequiredDate2(String date) throws ParseException {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void getSelfRequiredDate1(String date) throws ParseException {

        try {

            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);

            Calendar c = Calendar.getInstance();
            c.setTime(d);

            String dt = sdf3.format(c.getTime());
            filterDateLayout.setVisibility(View.VISIBLE);
            dateLayout.setVisibility(View.GONE);
            filterDate.setText(null);

            if (!self) {
                // filterDate.setText(dt.substring(3, 6) + " , " + dt.substring(6));
                filterDate.setText(dt.substring(0, 6) + ", " + dt.substring(6));
            } else {
                filterDate.setText(dt.substring(6));
            }

            //month_calendar.setFocusableInTouchMode(false);
            month_calendar.setClickable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void disableButtons() {
        text_self.setClickable(false);
        text_team.setClickable(false);
        text_all.setClickable(false);
    }

    private void enableButtons() {
        text_self.setClickable(true);
        text_team.setClickable(true);
        text_all.setClickable(true);
    }

    public String getRequiredFilterDate(String date) throws ParseException {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return sdf2.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void setFragment(TabLayout.Tab tab) {
        try {
            if (tab != null) {
                switch (tab.getPosition()) {

                    case 0:
                        callHomePage();
                        break;

                    case 1:
                        Intent intent = new Intent(AttendanceActivity.this, NewAttendanceActivity.class);
                        intent.putExtra("type", "0");
                        startActivity(intent);

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

    private void callHomePage() {
        Navigation.getInstance().openHomePageWithBack(AttendanceActivity.this);

    }

    private void request() {

        try {

            Intent intent = new Intent(AttendanceActivity.this, RequestActivity.class);
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

    private void initBottomMenuBar() {

        try {

            for (int i = 0; i < navLabels.length; i++) {

                LinearLayout tab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.nav_tab, null);
                TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);
                ImageView tab_icon = (ImageView) tab.findViewById(R.id.nav_icon);

                tab_label.setText(navLabels[i]);

                if (i == 1) {


                    tab_label.setTextColor(getResources().getColor(R.color.tab_inactive_color));
                    tab_icon.setImageResource(navIcons[i]);

                } else {
                    tab_label.setTextColor(getResources().getColor(R.color.tab_inactive_color));
                    tab_icon.setImageResource(navIcons[i]);
                }

                navigation.addTab(navigation.newTab().setCustomView(tab));
            }


            navigation.addOnTabSelectedListener(this);
            navigation.addOnTabSelectedListener(AttendanceActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ValidFragment")
    public static class MonthYearDialog extends DialogFragment {
        private static final int MAX_YEAR = 2099;
        AttendanceActivity mainActivity;
        private DatePickerDialog.OnDateSetListener listener;

        public MonthYearDialog() {

        }

        public void setListener(DatePickerDialog.OnDateSetListener listener) {
            this.listener = listener;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            final Calendar cal = Calendar.getInstance();

            View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
            final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
            final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

            monthPicker.setMinValue(1);
            monthPicker.setMaxValue(12);
            monthPicker.setValue(cal.get(Calendar.MONTH) + 1);


            final int year = 2016;
            final int current_year = cal.get(Calendar.YEAR);
            yearPicker.setMinValue(year);
            yearPicker.setMaxValue(current_year);
            yearPicker.setValue(current_year);

            builder.setView(dialog)
                    // Add action buttons
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {


                            listener.onDateSet(null, yearPicker.getValue(), monthPicker.getValue(), 0);
                            dialog_year = yearPicker.getValue();
                            dialog_month = monthPicker.getValue();

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(dialog_year, dialog_month - 1, 01);

                            int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);


                            if (dialog_month < 10) {
                                String mon = 0 + "" + dialog_month;
                                start_date = "" + String.valueOf(dialog_year) + "-" + mon + "-" + "01";
                            } else {
                                start_date = "" + String.valueOf(dialog_year) + "-" + String.valueOf(dialog_month) + "-" + "01";
                            }
                            if (dialog_month < 10) {
                                String mon = 0 + "" + dialog_month;
                                end_date = "" + String.valueOf(dialog_year) + "-" + mon + "-" + days;
                            } else {
                                end_date = "" + String.valueOf(dialog_year) + "-" + String.valueOf(dialog_month) + "-" + days;
                            }
                            if (handler != null) {
                                try {
                                    JSONObject object = new JSONObject();
                                    object.put("month_position", dialog_month - 1);
                                    object.put("year", dialog_year);
                                    object.put("month", dialog_month);
                                    object.put("current_year", current_year);
                                    object.put("start_date", start_date);
                                    object.put("end_date", end_date);

                                    handler.obtainMessage(Constants.Static.CHECK_IN, object).sendToTarget();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            MonthYearDialog.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }

    public class GridCellAdapter extends RecyclerView.Adapter<GridCellAdapter.MyHolder> implements View.OnClickListener {

        private static final String tag = "GridCellAdapter";
        private static final int DAY_OFFSET = 1;
        private final Context _context;
        private final List<String> list;
        private final HashMap<String, Integer> eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
        String which_one;
        private int daysInMonth;
        private int currentDayOfMonth;
        private int currentWeekDay;
        private TextView num_events_per_day;

        // Days in Current Month

        public GridCellAdapter(Context applicationContext, int month, int year, String team) {

            this._context = applicationContext;
            this.list = new ArrayList<String>();
            which_one = team;

            _calendar = Calendar.getInstance();
            setCurrentDayOfMonth(_calendar.get(Calendar.DAY_OF_MONTH));
            setCurrentWeekDay(_calendar.get(Calendar.DAY_OF_WEEK));


            // Print Month
            if (which_one.equals("team")) {
                printMonth(month, year);
            }
            // Find Number of Events
            eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
        }

        private String getMonthAsString(int i) {
            return Constants.months[i];
        }

        private String getWeekDayAsString(int i) {
            return Constants.weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return Constants.daysOfMonth[i];
        }


        /**
         * Prints Month
         *
         * @param mm
         * @param yy
         */
        private void printMonth(int mm, int yy) {
            try {
                int trailingSpaces = 0;
                int daysInPrevMonth = 0;
                int prevMonth = 0;
                int prevYear = 0;
                int nextMonth = 0;
                int nextYear = 0;
                int pos = 0;
                int currentMonth = mm - 1;
                String currentMonthName = getMonthAsString(currentMonth);
                daysInMonth = getNumberOfDaysOfMonth(currentMonth);

                Calendar cal1 = Calendar.getInstance();
                cal1.set(yy, mm, 1);

                SimpleDateFormat df = new SimpleDateFormat("EEE yyyy MMM dd");
                calenderModels = new ArrayList<>();
                for (int i = 1; i <= daysInMonth; i++) {
                    cal1.set(yy, currentMonth, i);
                    String date = df.format(cal1.getTime());
                    String[] split = date.split(" ");
                    CalenderModel model = new CalenderModel();
                    String day_name = split[0];
                    String year = split[1];
                    String month = split[2];
                    String day = split[3];
                    String dates = df.format(cal1.getTime());
                    model.setDayName(day_name);
                    model.setYear(year);
                    model.setMonth(month);
                    model.setDay(day);
                    model.setDate(dates);
                    model.setSelected(false);
                    calenderModels.add(model);
                }

                GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);

                if (currentMonth == 11) {
                    prevMonth = currentMonth - 1;
                    daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                    nextMonth = 0;
                    prevYear = yy;
                    nextYear = yy + 1;
                } else if (currentMonth == 0) {
                    prevMonth = 11;
                    prevYear = yy - 1;
                    nextYear = yy;
                    daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                    nextMonth = 1;
                } else {
                    prevMonth = currentMonth - 1;
                    nextMonth = currentMonth + 1;
                    nextYear = yy;
                    prevYear = yy;
                    daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
                }

                int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
                trailingSpaces = currentWeekDay;

                if (cal.isLeapYear(cal.get(Calendar.YEAR)))
                    if (mm == 2)
                        ++daysInMonth;
                    else if (mm == 3)
                        ++daysInPrevMonth;

                // Trailing Month days
                for (int i = 0; i < trailingSpaces; i++) {
                    list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-GREY" + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
                }

                // Current Month Days
                for (int i = 1; i <= daysInMonth; i++) {
                    if (i == getCurrentDayOfMonth()) {
                        list.add(String.valueOf(i) + "-BLUE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    } else {
                        list.add(String.valueOf(i) + "-WHITE" + "-" + getMonthAsString(currentMonth) + "-" + yy);
                    }
                }

                // Leading Month days
                for (int i = 0; i < list.size() % 7; i++) {
                    list.add(String.valueOf(i + 1) + "-GREY" + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year, int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();

            return map;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.month_view, parent, false);
            return new MyHolder(row);
        }

        @Override
        public void onBindViewHolder(final MyHolder holder, final int position) {
            try {
                final CalenderModel model = calenderModels.get(position);
                if (which_one.equals("team")) {
                    String dayname = model.getDayName();


                    String dayDate = model.getDay();
                    holder.day_date.setText(dayDate);
                    holder.day_name.setText(dayname);


                    if (selected_position == position) {
                        holder.under_line_month.setVisibility(View.VISIBLE);
                        month_calendar.getLayoutManager().scrollToPosition(selected_position + 4);
                        if (dayname.equals("Sat")) {
                            holder.day_date.setTextColor(ContextCompat.getColor(_context, R.color.text_color_team));

                        } else if (dayname.equals("Sun")) {

                            holder.day_date.setTextColor(ContextCompat.getColor(_context, R.color.text_color_team));

                        } else {
                            holder.day_date.setTextColor(ContextCompat.getColor(_context, R.color.colorAccent));
                            holder.day_name.setTextColor(ContextCompat.getColor(_context, R.color.select_text));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            holder.day_date.setText(Html.fromHtml(model.getDay(), Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            holder.day_date.setText(Html.fromHtml(model.getDay()));
                        }
                    } else {
                        if (dayname.equals("Sat")) {

                            holder.day_date.setTextColor(ContextCompat.getColor(_context, R.color.text_color_team));
                        } else if (dayname.equals("Sun")) {
                            holder.day_date.setTextColor(ContextCompat.getColor(_context, R.color.text_color_team));
                        } else {
                            holder.under_line_month.setVisibility(View.GONE);
                            holder.day_name.setTextColor(ContextCompat.getColor(_context, R.color.text_color_team));
                            holder.day_date.setTextColor(ContextCompat.getColor(_context, R.color.text_color_team));
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            holder.day_date.setText(Html.fromHtml(model.getDay(), Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            holder.day_date.setText(Html.fromHtml(model.getDay()));
                        }
                    }


                    int day = Integer.parseInt(dayDate);


                    holder.month_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            try {
                                notifyItemChanged(selected_position);
                                selected_position = position;
                                notifyItemChanged(selected_position);

                                //month_calendar.getLayoutManager().scrollToPosition(selected_position + 4);

                                CalenderModel model1 = calenderModels.get(position);
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                String select_date = model1.getDate();

                                Date ddddd = new Date(select_date);
                                String dd = simpleDateFormat.format(ddddd);
                                Date date = simpleDateFormat.parse(dd);
                                Calendar calendar = Calendar.getInstance();
                                String cur_date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                                Date curent_date = simpleDateFormat.parse(cur_date);
                                clicked_date = dd;
                                if (date.after(curent_date)) {

                                    emp_list_recyclerview.setVisibility(View.GONE);
                                    text_nodata.setVisibility(View.VISIBLE);

                                } else {
                                    if (Utilities.getConnectivityStatus(AttendanceActivity.this) > 0) {
                                        if (tab_selected_position == Constants.Tabclick.TEAM) {
                                            if (et_search != null) {
                                                et_search.getText().clear();
                                            }
                                            getTheTroopTeamData(clicked_date);
                                        } else {
                                            if (et_search != null) {
                                                et_search.getText().clear();
                                            }
                                            getTheTroopAllData(clicked_date);
                                        }
                                    } else {
                                        Utilities.ShowSnackbar(AttendanceActivity.this, emp_list_recyclerview, getResources().getString(R.string.network_check));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else if (which_one.equals("self")) {


                    final String month_name = model.getMonth_Name();


                    holder.day_name.setText(month_name);
                    holder.day_name.setVisibility(View.VISIBLE);
                    holder.day_date.setVisibility(View.GONE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        month_calendar.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                            @Override
                            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                isScroll = true;
                            }
                        });
                    }


                    if (month_position == position && !calenderModels.get(position).isSelected()) {

                        calenderModels.get(position).setSelected(true);
                        scrollPosition();
                        holder.day_name.setTextColor(ContextCompat.getColor(_context, R.color.colorAccent));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            holder.day_name.setText(Html.fromHtml(model.getMonth_Name(), Html.FROM_HTML_MODE_LEGACY));
                            holder.under_line_month.setVisibility(View.VISIBLE);
                        } else {
                            holder.under_line_month.setVisibility(View.VISIBLE);
                            holder.day_name.setText(Html.fromHtml(model.getMonth_Name()));
                        }
                    } else if (month_position == position && calenderModels.get(position).isSelected()) {
                        scrollPosition();
                        holder.day_name.setTextColor(ContextCompat.getColor(_context, R.color.colorAccent));
                        calenderModels.get(position).setSelected(true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            holder.day_name.setText(Html.fromHtml(model.getMonth_Name(), Html.FROM_HTML_MODE_LEGACY));
                            holder.under_line_month.setVisibility(View.VISIBLE);
                        } else {
                            holder.under_line_month.setVisibility(View.VISIBLE);
                            holder.day_name.setText(Html.fromHtml(model.getMonth_Name()));
                        }
                    } else {
                        //change
                        scrollPosition();
                        calenderModels.get(position).setSelected(false);
                        holder.under_line_month.setVisibility(View.GONE);
                        holder.day_name.setTextColor(ContextCompat.getColor(_context, R.color.text_color_team));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            holder.day_name.setText(Html.fromHtml(model.getMonth_Name(), Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            holder.day_name.setText(Html.fromHtml(model.getMonth_Name()));
                        }
                    }


                    holder.month_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            isScroll = false;

                            notifyItemChanged(month_position);
                            month_position = position;
                            month_calendar.scrollToPosition(month_position);
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.set(current_year, month_position + 1, 01);

                            int days = calendar1.getActualMaximum(Calendar.DAY_OF_MONTH);


                            if (month_position + 1 < 10) {

                                String mon = 0 + "" + String.valueOf(month_position + 1);
                                start_date = "" + String.valueOf(current_year) + "-" + mon + "-" + "01";
                            } else {
                                start_date = "" + String.valueOf(current_year) + "-" + String.valueOf(month_position + 1) + "-" + "01";
                            }
                            if (month_position + 1 < 10) {

                                String mon = 0 + "" + String.valueOf(month_position + 1);
                                end_date = "" + String.valueOf(current_year) + "-" + mon + "-" + days;
                            } else {
                                end_date = "" + String.valueOf(current_year) + "-" + String.valueOf(month_position + 1) + "-" + days;
                            }


                            notifyItemChanged(month_position);
                            CalenderModel model1 = calenderModels.get(position);
                            scrollPosition();

                            for (int i = 0; i < calenderModels.size(); i++) {
                                if (calenderModels.get(i).isSelected()) {
                                    if (month_position != i) {
                                        calenderModels.get(i).setSelected(false);
                                        notifyItemChanged(i);
                                    } else {
                                        calenderModels.get(i).setSelected(true);

                                    }

                                    break;
                                }
                            }
                            month_found = model1.getMonthStartDate();


                            start_mon = model1.getMonthStartDate();
                            end_dat = model1.getMonthEndDate();
                            Calendar calendar = Calendar.getInstance();
                            String cur_date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                Date actual_date = dateFormat.parse(cur_date);
                                Date date = dateFormat.parse(start_mon);
                                if (date.after(actual_date)) {
                                    emp_list_recyclerview.setVisibility(View.GONE);
                                    text_nodata.setVisibility(View.VISIBLE);

                                } else if (date.before(actual_date)) {
                                    if (Utilities.getConnectivityStatus(AttendanceActivity.this) > 0) {
                                        getTheTroopTimeData(start_mon, end_dat);
                                    } else {
                                        Utilities.ShowSnackbar(AttendanceActivity.this, emp_list_recyclerview, getResources().getString(R.string.network_check));
                                    }
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void scrollPosition() {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (isScroll) {
                            return;
                        }

                        if (month_position == 0) {
                            month_calendar.getLayoutManager().scrollToPosition(month_position);
                        } else if (month_position == 1) {
                            month_calendar.getLayoutManager().scrollToPosition(month_position);
                        } else if (month_position == 2) {
                            month_calendar.getLayoutManager().scrollToPosition(month_position);
                        } else if (month_position == 3) {
                            month_calendar.getLayoutManager().scrollToPosition(month_position);
                        } else if (month_position == 4) {
                            month_calendar.getLayoutManager().scrollToPosition(month_position);
                        } else if (month_position == 5) {
                            month_calendar.getLayoutManager().scrollToPosition(month_position);
                        } else if (month_position == 8) {
                            month_calendar.getLayoutManager().scrollToPosition(month_position + 3);
                        } else if (month_position == 9) {

                            month_calendar.getLayoutManager().smoothScrollToPosition(month_calendar, null, month_position + 2);
                        } else if (month_position == 10) {
                            month_calendar.getLayoutManager().smoothScrollToPosition(month_calendar, null, month_position + 1);

                        } else if (month_position == 11) {
                            month_calendar.getLayoutManager().smoothScrollToPosition(month_calendar, null, month_position);

                        } else {
                            month_calendar.getLayoutManager().scrollToPosition(month_position + 4);
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return calenderModels.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onClick(View view) {
            String date_month_year = (String) view.getTag();

            try {
                Date parsedDate = dateFormatter.parse(date_month_year);


            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public int getCurrentDayOfMonth() {
            return currentDayOfMonth;
        }

        private void setCurrentDayOfMonth(int currentDayOfMonth) {
            this.currentDayOfMonth = currentDayOfMonth;
        }

        public int getCurrentWeekDay() {
            return currentWeekDay;
        }

        public void setCurrentWeekDay(int currentWeekDay) {
            this.currentWeekDay = currentWeekDay;
        }

        public void setTheCalenderList(List<CalenderModel> calendermodels) {
            if (calenderModels != null && calenderModels.size() > 0) {
                calendermodels.clear();
            }
            if (calenderModels == null) {
                calenderModels = new ArrayList<>();
            }
            calenderModels.addAll(calendermodels);
        }


        public class MyHolder extends RecyclerView.ViewHolder {
            TextView day_name, day_date, month_name;
            View under_line_month;
            LinearLayout month_layout;

            public MyHolder(View itemView) {
                super(itemView);

                day_date = (TextView) itemView.findViewById(R.id.day_date);
                day_name = (TextView) itemView.findViewById(R.id.day_name);
                under_line_month = itemView.findViewById(R.id.under_line_month);
                month_layout = (LinearLayout) itemView.findViewById(R.id.month_layout);

            }
        }

    }


}