package com.tvisha.trooptime.activity.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.tvisha.trooptime.activity.activity.adapter.AttendanceAdapter;
import com.tvisha.trooptime.activity.activity.adapter.AttendanceSummaryAdapter;
import com.tvisha.trooptime.activity.activity.adapter.CommentsAdapter;
import com.tvisha.trooptime.activity.activity.adapter.CompOffAdapter;
import com.tvisha.trooptime.activity.activity.adapter.LopsAdapter;
import com.tvisha.trooptime.activity.activity.adapter.PermissionSummaryAdapter;
import com.tvisha.trooptime.activity.activity.apiPostModels.Attendance;
import com.tvisha.trooptime.activity.activity.apiPostModels.AttendanceReport;
import com.tvisha.trooptime.activity.activity.apiPostModels.AttendanceReportResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.AutoCheckDetails;
import com.tvisha.trooptime.activity.activity.apiPostModels.AutoCheckDetailsResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.Comment;
import com.tvisha.trooptime.activity.activity.apiPostModels.CommonResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.CompOffDetails;
import com.tvisha.trooptime.activity.activity.apiPostModels.EmployeeData;
import com.tvisha.trooptime.activity.activity.apiPostModels.EmployeePermissionSummaryResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.EmployeeSummaryResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.ExceptionDetails;
import com.tvisha.trooptime.activity.activity.apiPostModels.ExceptionDetailsResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.ExceptionStatusDetails;
import com.tvisha.trooptime.activity.activity.apiPostModels.ExceptionStatusResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.LeaveReport;
import com.tvisha.trooptime.activity.activity.apiPostModels.LeaveReportResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.PermissionData;
import com.tvisha.trooptime.activity.activity.apiPostModels.RequestCommentsResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.SendCommentResponse;
import com.tvisha.trooptime.activity.activity.dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.helper.Helper;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.activity.activity.api.response.SelfAttendenceApiResponce;
import com.tvisha.trooptime.activity.activity.app.MyApplication;
import com.tvisha.trooptime.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceFragment extends Fragment implements TabLayout.OnTabSelectedListener, View.OnClickListener {
    public static Handler handler;
    public static String start_date, end_date;
    public static int dialog_month, dialog_year;
    Context context;
    Activity activity;
    View rootView;
    TabLayout navigation, attendanceTablayout, leaveTablayout;
    SharedPreferences sharedPreferences;
    String userId, email, name, user_avatar, token, loginUserId;
    String fromDate = "", toDate = "", attendance_filter = "", filte_working_time = "", startDate = "", endDate = "", type = "";
    int permissions = 0, earlyLogouts = 0, lateLogins = 0, lessThanWorkingHours = 0, exception = 0, workingDays = 0;
    int notification_num, servicecount;
    Double sick = 0.0, casual = 0.0, lops = 0.0, optional = 0.0, leaves = 0.0, autoCheckOut = 0.0;
    ImageView profileImage, closeImage, closeFilterImage, noDataImage;
    TextView employee_name_tv, name_of_month_tv, working_days_tv, permissions_tv, leaves_tv, lops_tv, early_logouts_tv,
            late_logins_tv, permissions_count_tv, lessathan_working_hours, sick_tv, casual_tv, optional_tv,
            lops_tv1, permissions_count_tv1, lessathan_working_hours1, exception_tv, auto_checkout_tv, comp_off_title, comp_off_title1, filterDate;
    LinearLayout listLayout, attendanceLayout, leaveLayout, dateLayout, filterDateLayout;
    LinearLayout earlyLogoutsLayout, lateLoginsLayout, permissionsLayout, lessThanWorkingHoursLayout,
            permissionsLayout1, lessThanWorkingHoursLayout1, exceptionsLayout, autoCheckOutLayout,casualLayout,optionalLeaveLayout,lopsLayout;
    SwipeRefreshLayout swipe_refresh;
    ImageView refreshImage;
    boolean isRefresh = false;
    RecyclerView.LayoutManager attendanceLayoutManager;
    AttendanceAdapter attendanceAdapter;
    List<Attendance> attendanceList = new ArrayList<>();
    RecyclerView.LayoutManager compoffLayoutManager;
    CompOffAdapter compoffAdapter;
    List<CompOffDetails> compOffDetailsList = new ArrayList<>();
    RecyclerView.LayoutManager compoffLayoutManager1;
    CompOffAdapter compoffAdapter1;
    List<CompOffDetails> compOffDetailsList1 = new ArrayList<>();

    ImageView cancelImage;
    TextView title_tv, emp_name_tv;
    Dialog dialog = null;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    AttendanceSummaryAdapter adapter;
    LopsAdapter lopsAdapter;
    List<EmployeeData> employeeDataList = new ArrayList<>();

    RecyclerView permissionRecyclerView;
    RecyclerView.LayoutManager permissionLayoutManager;
    PermissionSummaryAdapter permissionAdapter;
    List<PermissionData> permissionDataList = new ArrayList<>();
    CustomProgressBar customProgressBar;
    ApiInterface apiService;

    String dateFormat1 = "dd MMM yyyy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat1, Locale.US);
    EditText commentEditText;
    ImageView send;
    LinearLayout approveRejectLayout, timeLayout, UpdatedTimeLayout,sickLayout;
    FrameLayout approveLayout, rejectLayout;
    TextView reporting_manager_names_tv, hr_names_tv, time_tv, name_tv, updated_time_tv, action_by_tv, action_by_name_tv, reason_tv, comment_tv;
    String changedTime = "";
    RecyclerView comments_recycler_view;
    RecyclerView.LayoutManager manager;
    CommentsAdapter commentsAdapter;
    ArrayList<Comment> comments = new ArrayList<>();
    String AM_PM = "";
    boolean isAutoCheckOutDialogOpen = false, isExceptionDialogOpen = false, isLeavesDialogOpen = false, isPermssionsDialogOpen = false;
    WebView webView;
    TextView noData;
    String chartUrl = "";
    int stackChartWidth = 0, stackChartHeight = 0;
    JSONArray workArray = new JSONArray();
    JSONArray overTimeArray = new JSONArray();
    JSONArray lateArray = new JSONArray();
    JSONArray breakArray = new JSONArray();
    JSONArray dateArray = new JSONArray();
    JSONArray timeArray = new JSONArray();
    int month, year, current_year, next_year, current_month, current_day;
    boolean isTabchanged = false, isListClicked = false, isAttendanceClicked = false, isLeaveReportsClicked = false,
            isListCalled = false, isAttendanceCalled = false, isLeaveReportsCalled = false,
            isAttendanceThisMonthClicked = false, isAttendanceThisYearClicked = false,
            isLeaveThisMonthClicked = false, isLeaveThisYearClicked = false;
    private RecyclerView attendanceRecycleview;
    private RecyclerView compoffRecycleview;
    private RecyclerView compoffRecycleview1;
    private String[] categoryLables = {
            "LIST",
            "ATTENDANCE",
            "LEAVE"
    };
    private String[] durationNavLabels = {
            "This month",
            "This year"

    };
    private BarChart stackedBarChart;

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
            isRefresh = false;
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

    @Override
    public void onResume() {
        try {
            if (isPermssionsDialogOpen) {
                if (permissions != 0) {

                    if (Utilities.isNetworkAvailable(context)) {
                        callEmployeePermissionSummary(Constants.PERMISSION_REPORT, dialog);
                    } else {
                        Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();

                    }
                }
            }
            if (isLeavesDialogOpen) {
                if (autoCheckOut != 0) {
                    if (Utilities.isNetworkAvailable(context)) {
                        callEmployeePermissionSummary(Constants.LEAVE_REPORT, dialog);
                    } else {
                        Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_attendance, container, false);
        initViews();
        initListeners();
        processFragment();
        return rootView;
    }

    @SuppressLint("HandlerLeak")
    private void processFragment() {

        try {

            shareprefernceData();
            updateProfile();
            initNavigationMenu();
            initAttendanceTabLayout();
            initLeaveTabLayout();


            thisMonth();
            callSelfAttendance();

            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case Constants.Static.CHECK_IN:
                            JSONObject object = (JSONObject) msg.obj;
                            //month_position = object.optInt("month_position");
                            // selfisOnedCalenderView(object.optInt("year"));
                            if (object.optInt("month") > current_month && object.optInt("year") >= object.optInt("current_year")) {
                            /*emp_list_recyclerview.setVisibility(View.GONE);
                            text_nodata.setVisibility(View.VISIBLE);
                            adapter.scrollPosition();*/
                                Toast.makeText(context, getResources().getString(R.string.no_future_data_available), Toast.LENGTH_LONG).show();
                            } else {
                                callToselfAttendence(object.optString("start_date"), object.optString("end_date"));
                                filterDateLayout.setVisibility(View.VISIBLE);
                                //swipe_refresh.setEnabled(false);
                                refreshImage.setVisibility(View.INVISIBLE);
                                dateLayout.setVisibility(View.GONE);

                                try {
                                    String dt = null;
                                    dt = getRequiredFilterDate(object.optString("start_date"));
                                    filterDate.setText(" " + dt.substring(3, 6) + "," + dt.substring(6) + " ");
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                    }
                }
            };


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callSelfAttendance() {

        try {

            if (MyApplication.selfAttendenceApiResponce == null) {
                if (Utilities.isNetworkAvailable(context)) {
                    isListCalled = true;
                    isListClicked = true;
                    isAttendanceClicked = false;
                    isLeaveReportsClicked = false;
                    callToselfAttendence(fromDate, toDate);
                } else {
                    Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                    // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                }
            } else {

                if (Utilities.isNetworkAvailable(context)) {
                    isListCalled = true;
                    isListClicked = true;
                    isAttendanceClicked = false;
                    isLeaveReportsClicked = false;
                    callToselfAttendence(fromDate, toDate);
                } else {
                    Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                    // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                }

        /*    if(loginUserId.equals(userId))
            {
                isListCalled=true;
                isListClicked =true;
                isAttendanceClicked =false;
                isLeaveReportsClicked =false;
                SelfAttendenceApiResponce apiResponse=MyApplication.selfAttendenceApiResponce;
                if (apiResponse!=null  && apiResponse.getAttendance() != null && apiResponse.getAttendance().size() > 0) {
                    noDataImage.setVisibility(View.GONE);
                    if(attendanceList!=null && attendanceList.size()>0)
                    {
                        attendanceList.clear();
                    }
                    attendanceRecycleview.setVisibility(View.VISIBLE);
                    attendanceList.addAll(apiResponse.getAttendance());
                    attendanceAdapter.notifyDataSetChanged();
                }
                else
                {
                    attendanceRecycleview.setVisibility(View.GONE);
                    noDataImage.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                if(Utilities.isNetworkAvailable(context))
                {
                    isListCalled=true;
                    isListClicked =true;
                    isAttendanceClicked =false;
                    isLeaveReportsClicked =false;
                    callToselfAttendence(fromDate,toDate);
                }
                else{
                   Toast.makeText(context,"Please check internet connection",Toast.LENGTH_LONG).show();
                  //  Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                }
            }*/

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void initLeaveTabLayout() {

        try {

            for (int i = 0; i < durationNavLabels.length; i++) {

                LinearLayout tabView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.small_nav_tab, null);
                TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);


                tab_label.setText(durationNavLabels[i]);

                if (i == 0) {
                    tab_label.setTextColor(getResources().getColor(R.color.small_tab_active_text_color));
                    tabView.setBackgroundColor(getResources().getColor(R.color.small_tab_active_background_color));

                } else {
                    tab_label.setTextColor(getResources().getColor(R.color.small_tab_inactive_text_color));
                    tabView.setBackgroundColor(getResources().getColor(R.color.small_tab_inactive_background_color));
                }

                leaveTablayout.addTab(leaveTablayout.newTab().setCustomView(tabView));
            }


            leaveTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    View tabView = tab.getCustomView();
                    TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                    tab_label.setTextColor(getResources().getColor(R.color.small_tab_active_text_color));
                    tabView.setBackgroundColor(getResources().getColor(R.color.small_tab_active_background_color));
                    setLeaveLayout(tab);

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    View tabView = tab.getCustomView();
                    TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                    tab_label.setTextColor(getResources().getColor(R.color.small_tab_inactive_text_color));
                    tabView.setBackgroundColor(getResources().getColor(R.color.small_tab_inactive_background_color));
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


            TabLayout.Tab leaveTab = leaveTablayout.getTabAt(0);
            leaveTab.select();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAttendanceTabLayout() {
        try {
            for (int i = 0; i < durationNavLabels.length; i++) {

                LinearLayout tabView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.small_nav_tab, null);
                TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);


                tab_label.setText(durationNavLabels[i]);

                if (i == 0) {
                    tab_label.setTextColor(getResources().getColor(R.color.small_tab_active_text_color));
                    tabView.setBackgroundColor(getResources().getColor(R.color.small_tab_active_background_color));

                } else {
                    tab_label.setTextColor(getResources().getColor(R.color.small_tab_inactive_text_color));
                    tabView.setBackgroundColor(getResources().getColor(R.color.small_tab_inactive_background_color));
                }

                attendanceTablayout.addTab(attendanceTablayout.newTab().setCustomView(tabView));
            }


            attendanceTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    View tabView = tab.getCustomView();
                    TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                    tab_label.setTextColor(getResources().getColor(R.color.small_tab_active_text_color));
                    tabView.setBackgroundColor(getResources().getColor(R.color.small_tab_active_background_color));
                    setAttendanceLayout(tab);
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    View tabView = tab.getCustomView();
                    TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
                    tab_label.setTextColor(getResources().getColor(R.color.small_tab_inactive_text_color));
                    tabView.setBackgroundColor(getResources().getColor(R.color.small_tab_inactive_background_color));
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


            TabLayout.Tab attendanceTab = attendanceTablayout.getTabAt(0);
            attendanceTab.select();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initNavigationMenu() {

        try {

            for (int i = 0; i < categoryLables.length; i++) {

                LinearLayout tab = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.small_nav_tab, null);
                TextView tab_label = (TextView) tab.findViewById(R.id.nav_label);


                tab_label.setText(categoryLables[i]);

                if (i == 0) {
                    tab_label.setTextColor(getResources().getColor(R.color.small_tab_active_text_color));
                    tab.setBackgroundColor(getResources().getColor(R.color.small_tab_active_background_color));

                } else {
                    tab_label.setTextColor(getResources().getColor(R.color.small_tab_inactive_text_color));
                    tab.setBackgroundColor(getResources().getColor(R.color.small_tab_inactive_background_color));
                }

                navigation.addTab(navigation.newTab().setCustomView(tab));
            }


            navigation.addOnTabSelectedListener(this);


            TabLayout.Tab tab = navigation.getTabAt(0);
            tab.select();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initListeners() {

        try {

            earlyLogoutsLayout.setOnClickListener(this);
            lateLoginsLayout.setOnClickListener(this);
            permissionsLayout.setOnClickListener(this);
            lessThanWorkingHoursLayout.setOnClickListener(this);
            permissionsLayout1.setOnClickListener(this);
            lessThanWorkingHoursLayout1.setOnClickListener(this);
            exceptionsLayout.setOnClickListener(this);
            autoCheckOutLayout.setOnClickListener(this);
            casualLayout.setOnClickListener(this);
            optionalLeaveLayout.setOnClickListener(this);
            lopsLayout.setOnClickListener(this);
            closeImage.setOnClickListener(this);
            filterDateLayout.setOnClickListener(this);
            dateLayout.setOnClickListener(this);
            closeFilterImage.setOnClickListener(this);
            sickLayout.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {

        try {


            context = ((NewAttendanceActivity) getActivity()).getContext();
            activity = ((NewAttendanceActivity) getActivity()).getActivity();
            sharedPreferences = context.getSharedPreferences(SharePreferenceKeys.SP_NAME, Context.MODE_PRIVATE);
            navigation = rootView.findViewById(R.id.navigation);
            attendanceTablayout = rootView.findViewById(R.id.attendanceTablayout);
            leaveTablayout = rootView.findViewById(R.id.leaveTablayout);
            apiService = ApiClient.getClient().create(ApiInterface.class);
            customProgressBar = new CustomProgressBar(context);


            profileImage = rootView.findViewById(R.id.profileImage);
            closeImage = rootView.findViewById(R.id.closeImage);
            closeFilterImage = rootView.findViewById(R.id.closeFilterImage);
            employee_name_tv = rootView.findViewById(R.id.employee_name_tv);
            name_of_month_tv = rootView.findViewById(R.id.name_of_month_tv);
            listLayout = rootView.findViewById(R.id.listLayout);
            leaveLayout = rootView.findViewById(R.id.leaveLayout);
            attendanceLayout = rootView.findViewById(R.id.attendanceLayout);
            dateLayout = rootView.findViewById(R.id.dateLayout);
            filterDateLayout = rootView.findViewById(R.id.filterDateLayout);
            filterDate = rootView.findViewById(R.id.filterDate);
            comp_off_title = rootView.findViewById(R.id.comp_off_title);
            comp_off_title1 = rootView.findViewById(R.id.comp_off_title1);

            earlyLogoutsLayout = rootView.findViewById(R.id.earlyLogoutsLayout);
            lateLoginsLayout = rootView.findViewById(R.id.lateLoginsLayout);
            permissionsLayout = rootView.findViewById(R.id.permissionsLayout);
            lessThanWorkingHoursLayout = rootView.findViewById(R.id.lessThanWorkingHoursLayout);

            permissionsLayout1 = rootView.findViewById(R.id.permissionsLayout1);
            lessThanWorkingHoursLayout1 = rootView.findViewById(R.id.lessThanWorkingHoursLayout1);

            exceptionsLayout = rootView.findViewById(R.id.exceptionsLayout);
            autoCheckOutLayout = rootView.findViewById(R.id.autoCheckOutLayout);
            sickLayout = rootView.findViewById(R.id.sickLayout);
            casualLayout = rootView.findViewById(R.id.casualLayout);
            optionalLeaveLayout = rootView.findViewById(R.id.optionalLeaveLayout);
            lopsLayout = rootView.findViewById(R.id.lopsLayout);


            working_days_tv = rootView.findViewById(R.id.working_days_tv);
            permissions_tv = rootView.findViewById(R.id.permissions_tv);
            leaves_tv = rootView.findViewById(R.id.leaves_tv);


            lops_tv = rootView.findViewById(R.id.lops_tv);
            early_logouts_tv = rootView.findViewById(R.id.early_logouts_tv);
            late_logins_tv = rootView.findViewById(R.id.late_logins_tv);
            permissions_count_tv = rootView.findViewById(R.id.permissions_count_tv);
            lessathan_working_hours = rootView.findViewById(R.id.lessathan_working_hours);

            sick_tv = rootView.findViewById(R.id.sick_tv);
            casual_tv = rootView.findViewById(R.id.casual_tv);
            optional_tv = rootView.findViewById(R.id.optional_tv);
            exception_tv = rootView.findViewById(R.id.exception_tv);
            auto_checkout_tv = rootView.findViewById(R.id.auto_checkout_tv);
            noDataImage = rootView.findViewById(R.id.noDataImage);
            //swipe_refresh = rootView.findViewById(R.id.swipe_refresh);
            refreshImage = rootView.findViewById(R.id.refreshImage);


            lops_tv1 = rootView.findViewById(R.id.lops_tv1);

            permissions_count_tv1 = rootView.findViewById(R.id.permissions_count_tv1);
            lessathan_working_hours1 = rootView.findViewById(R.id.lessathan_working_hours1);


            attendanceRecycleview = rootView.findViewById(R.id.attendance_recycler_view);
            attendanceRecycleview.setNestedScrollingEnabled(false);
            attendanceRecycleview.setHasFixedSize(true);
            attendanceLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            attendanceRecycleview.setLayoutManager(attendanceLayoutManager);
            attendanceAdapter = new AttendanceAdapter(context, attendanceList, AttendanceFragment.this);
            attendanceRecycleview.setAdapter(attendanceAdapter);
            attendanceRecycleview.setNestedScrollingEnabled(false);

            compoffRecycleview = rootView.findViewById(R.id.comp_off_recycler_view);
            compoffRecycleview.setNestedScrollingEnabled(false);
            compoffRecycleview.setHasFixedSize(true);
            compoffLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            compoffRecycleview.setLayoutManager(compoffLayoutManager);
            compoffAdapter = new CompOffAdapter(context, compOffDetailsList, AttendanceFragment.this);
            compoffRecycleview.setAdapter(compoffAdapter);
            compoffRecycleview.setNestedScrollingEnabled(false);

            compoffRecycleview1 = rootView.findViewById(R.id.comp_off_recycler_view1);
            compoffRecycleview1.setNestedScrollingEnabled(false);
            compoffRecycleview1.setHasFixedSize(true);
            compoffLayoutManager1 = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
            compoffRecycleview1.setLayoutManager(compoffLayoutManager1);
            compoffAdapter1 = new CompOffAdapter(context, compOffDetailsList1, AttendanceFragment.this);
            compoffRecycleview1.setAdapter(compoffAdapter1);
            compoffRecycleview1.setNestedScrollingEnabled(false);

            stackChartWidth = (int) (getResources().getDimension(R.dimen.stack_chart_width) / getResources().getDisplayMetrics().density);
            stackChartHeight = (int) (getResources().getDimension(R.dimen.stack_chart_height) / getResources().getDisplayMetrics().density);

            Thread t = new Thread() {
                public void run() {

                    updateCurrentMonthViews();
                }
            };
            t.start();

            // updateCurrentMonthViews();

            refreshImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isRefresh){
                        return;
                    }
                    if (isListClicked) {
                        refreshAttendanceList();
                    } else if (isAttendanceClicked) {
                        refreshAttendanceReports();
                    } else if (isLeaveReportsClicked) {
                        refreshLeaveReports();
                    } else {
                        //swipe_refresh.setRefreshing(false);
                    }
                }
            });
            /*swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {

                    if (Utilities.isNetworkAvailable(context)) {
                        swipe_refresh.setColorSchemeResources(android.R.color.holo_red_light, android.R.color.holo_blue_bright, android.R.color.holo_orange_light, android.R.color.holo_green_light);
                        if (isListClicked) {
                            refreshAttendanceList();
                        } else if (isAttendanceClicked) {
                            refreshAttendanceReports();
                        } else if (isLeaveReportsClicked) {
                            refreshLeaveReports();
                        } else {
                            swipe_refresh.setRefreshing(false);
                        }

                    } else {
                        swipe_refresh.setRefreshing(false);
                    }

                }
            });*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateCurrentMonthViews() {
        try {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Calendar calendar = Calendar.getInstance();
                    String currentDate = sdf.format(calendar.getTime());
                    name_of_month_tv.setText(" " + currentDate.substring(3, 6) + "," + currentDate.substring(6) + " ");

                    String day = new SimpleDateFormat("dd").format(calendar.getTime());
                    current_day = Integer.parseInt(day);
                    String months = new SimpleDateFormat("MM").format(calendar.getTime());
                    current_month = Integer.parseInt(months);
                    String currentYear = new SimpleDateFormat("yyyy").format(calendar.getTime());
                    current_year = Integer.parseInt(currentYear);
                    next_year = Integer.parseInt(currentYear + 1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getRequiredFilterDate(String date) throws ParseException {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        try {

            if (tab.getPosition() == 0) {
                dateLayout.setVisibility(View.VISIBLE);
            } else {
                dateLayout.setVisibility(View.INVISIBLE);
                filterDateLayout.setVisibility(View.GONE);
            }

            View tabView = tab.getCustomView();
            TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
            tab_label.setTextColor(getResources().getColor(R.color.small_tab_active_text_color));
            tabView.setBackgroundColor(getResources().getColor(R.color.small_tab_active_background_color));

            setLayout(tab);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

        try {

            View tabView = tab.getCustomView();
            TextView tab_label = (TextView) tabView.findViewById(R.id.nav_label);
            tab_label.setTextColor(getResources().getColor(R.color.small_tab_inactive_text_color));
            tabView.setBackgroundColor(getResources().getColor(R.color.small_tab_inactive_background_color));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void setLayout(TabLayout.Tab tab) {
        try {
            switch (tab.getPosition()) {
                case 0:
                    refreshImage.setVisibility(View.VISIBLE);
                    //swipe_refresh.setEnabled(true);
                    listLayout.setVisibility(View.VISIBLE);
                    attendanceLayout.setVisibility(View.GONE);
                    leaveLayout.setVisibility(View.GONE);
                    isListCalled = true;
                    isAttendanceClicked = false;
                    isLeaveReportsClicked = false;
                    if (!isListClicked) {

                        thisMonth();
                        callSelfAttendance();
                    } else {
                        if (attendanceList == null || attendanceList.size() == 0) {
                            noDataImage.setVisibility(View.VISIBLE);

                        }
                    }

                    break;
                case 1:
                    // swipe_refresh.setEnabled(false);
                    refreshImage.setVisibility(View.INVISIBLE);
                    noDataImage.setVisibility(View.GONE);
                    listLayout.setVisibility(View.GONE);
                    attendanceLayout.setVisibility(View.VISIBLE);
                    leaveLayout.setVisibility(View.GONE);
                    isListCalled = false;
                    isAttendanceClicked = true;
                    isLeaveReportsClicked = false;
                    if (!isAttendanceCalled) {
                        TabLayout.Tab attendanceTab = attendanceTablayout.getTabAt(0);
                        attendanceTab.select();
                        thisMonth();
                        isAttendanceThisMonthClicked = true;
                        isAttendanceThisYearClicked = false;


                        callAttendanceReports();

                    }

                    break;
                case 2:
                    //  swipe_refresh.setEnabled(false);
                    refreshImage.setVisibility(View.INVISIBLE);
                    noDataImage.setVisibility(View.GONE);
                    listLayout.setVisibility(View.GONE);
                    attendanceLayout.setVisibility(View.GONE);
                    leaveLayout.setVisibility(View.VISIBLE);
                    isListCalled = false;
                    isAttendanceClicked = false;
                    isLeaveReportsClicked = true;
                    if (!isLeaveReportsCalled) {
                        TabLayout.Tab leaveTab = leaveTablayout.getTabAt(0);
                        leaveTab.select();
                        thisMonth();
                        isLeaveThisMonthClicked = false;
                        isLeaveThisYearClicked = true;
                        callLeaveReports();

                    }

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callLeaveReports() {
        try {

            if (Utilities.isNetworkAvailable(context)) {
                isLeaveReportsCalled = true;
                isLeaveReportsClicked = true;
                isListClicked = false;
                isAttendanceClicked = false;
                callLeaveReportApi();
            } else {

                // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAttendanceLayout(TabLayout.Tab tab) {
        try {
            switch (tab.getPosition()) {
                case 0:

                    isAttendanceThisMonthClicked = true;
                    isAttendanceThisYearClicked = false;
                    thisMonth();
                    callAttendanceReports();
                    break;
                case 1:

                    isAttendanceThisMonthClicked = false;
                    isAttendanceThisYearClicked = true;
                    thisYear();
                    callAttendanceReports();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAttendanceReports() {
        try {

            if (Utilities.isNetworkAvailable(context)) {
                isAttendanceCalled = true;
                isAttendanceClicked = true;
                isListClicked = false;
                isLeaveReportsClicked = false;
                callAttendanceReportApi();
            } else {
                // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setLeaveLayout(TabLayout.Tab tab) {
        try {
            switch (tab.getPosition()) {
                case 0:
                    isLeaveThisMonthClicked = true;
                    isLeaveThisYearClicked = false;
                    thisMonth();
                    callLeaveReports();
                    break;
                case 1:
                    isLeaveThisMonthClicked = false;
                    isLeaveThisYearClicked = true;
                    thisYear();
                    callLeaveReports();
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.sickLayout:
                    if (sick != 0) {
                        openDialog("Sick Leaves", Constants.LEAVE_SICK_REPORT);
                    }
                    break;
                case R.id.casualLayout:
                    if (casual != 0) {
                        openDialog("Casual Leaves", Constants.LEAVE_CASUAL_REPORT);
                    }
                    break;
                    case R.id.optionalLeaveLayout:
                        if (optional != 0) {
                            openDialog("Optional Leaves", Constants.LEAVE_OPTIONAL_REPORT);
                        }
                    break;
                case R.id.lopsLayout:
                    if (lops != 0) {
                        openDialog("LOPS", Constants.LOP_REPORT);
                    }
                    break;

                case R.id.earlyLogoutsLayout:
                    if (earlyLogouts != 0) {
                        openDialog("Early Logouts", Constants.EARLY_LOGOUT_REPORT);
                    }

                    break;
                case R.id.lateLoginsLayout:
                    if (lateLogins != 0) {
                        openDialog("Late Logins", Constants.LATE_LOGIN_REPORT);
                    }

                    break;
                case R.id.permissionsLayout:
                    if (permissions != 0) {
                        openDialog("Permissions", Constants.PERMISSION_REPORT);
                    }
                    break;
                case R.id.lessThanWorkingHoursLayout:
                    if (lessThanWorkingHours != 0) {
                        openDialog("Less than working hours", Constants.LESS_WORK_HRS_REPORT);
                    }

                    break;

                case R.id.permissionsLayout1:
                    if (permissions != 0) {
                        openDialog("Permissions", Constants.PERMISSION_REPORT);
                    }

                    break;
                case R.id.lessThanWorkingHoursLayout1:
                    if (lessThanWorkingHours != 0) {
                        openDialog("Less than working hours", Constants.LESS_WORK_HRS_REPORT);
                    }

                    break;
                case R.id.exceptionsLayout:
                    if (exception != 0) {
                        openDialog("Exceptions", Constants.EXCEPTION_REPORT);
                    }

                    break;
                case R.id.autoCheckOutLayout:
                    if (autoCheckOut != 0) {
                        openDialog("Leaves", Constants.LEAVE_REPORT);
                    }
                    break;
                case R.id.closeImage:
                    activity.onBackPressed();
                    break;
                case R.id.dateLayout:
                    try {
                        monthYearDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.closeFilterImage:
                    dateLayout.setVisibility(View.VISIBLE);
                    filterDateLayout.setVisibility(View.GONE);
                    //swipe_refresh.setEnabled(true);
                    refreshImage.setVisibility(View.VISIBLE);
                    refreshAttendanceList();
                    //  callSelfAttendance();
                    break;
                case R.id.filterDateLayout:
                    dateLayout.setVisibility(View.VISIBLE);
                    filterDateLayout.setVisibility(View.GONE);
                    //swipe_refresh.setEnabled(true);
                    refreshImage.setVisibility(View.VISIBLE);
                    refreshAttendanceList();
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshAttendanceToUpdate() {
        try {
            if (Utilities.isNetworkAvailable(context)) {
                if (listLayout.getVisibility() == View.VISIBLE) {

                    isRefresh = true;
                    //refreshImage.setVisibility(View.VISIBLE);
                    //swipe_refresh.setRefreshing(true);
                    callToselfAttendence(fromDate, toDate);
                } else {
                    //refreshImage.setVisibility(View.GONE);
                    //swipe_refresh.setRefreshing(false);
                }

            } else {
                //swipe_refresh.setRefreshing(false);
                Toast.makeText(context, "Please check network connection", Toast.LENGTH_SHORT).show();
                // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshAttendanceList() {
        try {
            if (Utilities.isNetworkAvailable(context)) {
                if (listLayout.getVisibility() == View.VISIBLE) {

                    isRefresh = true;

                    thisMonth();
                    Thread t = new Thread() {
                        public void run() {

                            updateCurrentMonthViews();
                        }
                    };
                    t.start();
                    updateCurrentMonthViews();

                    //swipe_refresh.setRefreshing(true);
                    callToselfAttendence(fromDate, toDate);
                } else {
                    //swipe_refresh.setRefreshing(false);
                }

            } else {
                //swipe_refresh.setRefreshing(false);
                Toast.makeText(context, "Please check network connection", Toast.LENGTH_SHORT).show();
                // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshAttendanceReports() {
        try {
            if (Utilities.isNetworkAvailable(context)) {

                isRefresh = true;
                if (isAttendanceThisMonthClicked) {
                    thisMonth();

                } else if (isAttendanceThisYearClicked) {
                    thisYear();

                } else {
                    //swipe_refresh.setRefreshing(false);
                }

                if (isAttendanceThisMonthClicked || isAttendanceThisYearClicked) {
                    //swipe_refresh.setRefreshing(true);

                    callAttendanceReportApi();
                }


            } else {
                //swipe_refresh.setRefreshing(false);
                Toast.makeText(context, "Please check network connection", Toast.LENGTH_SHORT).show();
                // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshLeaveReports() {
        try {
            if (Utilities.isNetworkAvailable(context)) {

                isRefresh = true;
                if (isLeaveThisMonthClicked) {
                    thisMonth();

                } else if (isLeaveThisYearClicked) {
                    thisYear();

                } else {
                    //swipe_refresh.setRefreshing(false);
                }

                if (isLeaveThisMonthClicked || isLeaveThisYearClicked) {
                    //swipe_refresh.setRefreshing(true);
                    /*openProgress();*/
                    callLeaveReportApi();
                }


            } else {
                //swipe_refresh.setRefreshing(false);
                Toast.makeText(context, "Please check network connection", Toast.LENGTH_SHORT).show();
                // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callToselfAttendence(String f_date, String e_date) {
        try {

            fromDate = f_date;
            toDate = e_date;

            /*if (!isRefresh) {

            } else {
                isRefresh = false;
            }*/
            openProgress();

            Call<SelfAttendenceApiResponce> call = apiService.getSelefAttendence(
                    userId,
                    token,
                    f_date,
                    e_date,
                    attendance_filter,
                    filte_working_time);

            call.enqueue(new Callback<SelfAttendenceApiResponce>() {
                @Override
                public void onResponse(Call<SelfAttendenceApiResponce> call, Response<SelfAttendenceApiResponce> response) {
                    SelfAttendenceApiResponce apiResponse = response.body();
                    //swipe_refresh.setRefreshing(false);
                    if (apiResponse != null) {
                        closeProgress();

                        if (apiResponse.getSuccess()) {
                            if (userId.equals(loginUserId)) {
                                MyApplication.selfAttendenceApiResponce = apiResponse;
                            }

                            if (apiResponse.getAttendance() != null && apiResponse.getAttendance().size() > 0) {
                                noDataImage.setVisibility(View.GONE);
                                boolean isFirstTime = true;
                                if (attendanceList != null && attendanceList.size() > 0) {
                                    attendanceList.clear();
                                    isFirstTime = false;
                                }
                                attendanceRecycleview.setVisibility(View.VISIBLE);
                                attendanceList.addAll(apiResponse.getAttendance());
                                if (!isFirstTime) {
                                    attendanceAdapter.setList(attendanceList);
                                }
                                attendanceAdapter.notifyDataSetChanged();


                            } else {
                                attendanceRecycleview.setVisibility(View.GONE);
                                noDataImage.setVisibility(View.VISIBLE);
                            }

                        }
                    } else {

                        closeProgress();
                        // Utilities.ShowSnackbar(context, attendanceRecycleview, getResources().getString(R.string.somthing_went_wrong));
                    }

                }

                @Override
                public void onFailure(Call<SelfAttendenceApiResponce> call, Throwable t) {
                    closeProgress();
                    //swipe_refresh.setRefreshing(false);
                    // Utilities.ShowSnackbar(context, attendanceRecycleview, getResources().getString(R.string.somthing_went_wrong));
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callAttendanceReportApi() {

        try {

            /*if (!isRefresh) {
                openProgress();
            } else {
                isRefresh = false;
            }*/
            openProgress();
            Call<AttendanceReportResponse> call = apiService.getAttendanceReport(
                    userId,
                    token,
                    startDate,
                    endDate);
            call.enqueue(new Callback<AttendanceReportResponse>() {
                @Override
                public void onResponse(Call<AttendanceReportResponse> call, Response<AttendanceReportResponse> response) {
                    final AttendanceReportResponse apiResponse = response.body();
                    //swipe_refresh.setRefreshing(false);

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {

                            if (apiResponse.getAttendanceReport() != null) {
                                Thread t = new Thread() {
                                    public void run() {

                                        updateAttendanceReportViews(apiResponse.getAttendanceReport());

                                    }
                                };
                                t.start();
                                //updateAttendanceReportViews(apiResponse.getAttendanceReport());
                                if (apiResponse.getAttendanceReport().getCompOffData() != null && apiResponse.getAttendanceReport().getCompOffData().size() > 0) {
                                    if (compOffDetailsList != null && compOffDetailsList.size() > 0) {
                                        compOffDetailsList.clear();
                                    }
                                    compOffDetailsList.addAll(apiResponse.getAttendanceReport().getCompOffData());
                                    compoffAdapter.notifyDataSetChanged();
                                    comp_off_title.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            //Utilities.ShowSnackbar(context, compoffRecycleview, apiResponse.getMessage());
                        }

                    } else {
                        closeProgress();
                        // Utilities.ShowSnackbar(context, compoffRecycleview, getResources().getString(R.string.somthing_went_wrong));
                    }

                }

                @Override
                public void onFailure(Call<AttendanceReportResponse> call, Throwable t) {
                    closeProgress();
                    //swipe_refresh.setRefreshing(false);
                    //  Utilities.ShowSnackbar(context, compoffRecycleview, getResources().getString(R.string.somthing_went_wrong));
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callEmployeeSummary(final int type, final Dialog dialog) {


        try {

            if (!isRefresh) {
                openProgress();
            } else {
                isRefresh = false;
            }
            Call<EmployeeSummaryResponse> call = apiService.getEmployeeSummary(
                    userId,
                    token,
                    fromDate,
                    toDate,
                    type + ""
            );
            call.enqueue(new Callback<EmployeeSummaryResponse>() {
                @Override
                public void onResponse(Call<EmployeeSummaryResponse> call, Response<EmployeeSummaryResponse> response) {
                    EmployeeSummaryResponse apiResponse = response.body();
                    //swipe_refresh.setRefreshing(false);
                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {


                            if (apiResponse.getEmployeeData() != null && apiResponse.getEmployeeData().size() > 0) {
                                if (employeeDataList != null && employeeDataList.size() > 0) {
                                    employeeDataList.clear();
                                }
                                employeeDataList.addAll(apiResponse.getEmployeeData());
                                if(type==Constants.LOP_REPORT)
                                {
                                    lopsAdapter.setType(type);
                                    lopsAdapter.notifyDataSetChanged();
                                }
                                else
                                {
                                    adapter.setType(type);
                                    adapter.notifyDataSetChanged();
                                }

                                if (!dialog.isShowing()) {
                                    dialog.show();
                                }


                            } else {
                                if (employeeDataList != null && employeeDataList.size() > 0) {
                                    employeeDataList.clear();
                                    if(type==Constants.LOP_REPORT)
                                    {
                                        lopsAdapter.notifyDataSetChanged();
                                    }
                                    else
                                    {
                                        adapter.notifyDataSetChanged();
                                    }


                                }
                            }


                        } else {
                            //  Utilities.ShowSnackbar(context, recyclerView, getResources().getString(R.string.somthing_went_wrong));
                        }


                    } else {
                        closeProgress();
                        //  Utilities.ShowSnackbar(context, recyclerView, getResources().getString(R.string.somthing_went_wrong));
                    }

                }

                @Override
                public void onFailure(Call<EmployeeSummaryResponse> call, Throwable t) {
                    closeProgress();
                    //swipe_refresh.setRefreshing(false);
                    //  Utilities.ShowSnackbar(context, recyclerView, getResources().getString(R.string.somthing_went_wrong));

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callEmployeePermissionSummary(final int type, final Dialog dialog) {


        try {
            if (!isRefresh) {
                openProgress();
            } else {
                isRefresh = false;
            }
            Call<EmployeePermissionSummaryResponse> call = apiService.getEmployeePermissionSummary(
                    userId,
                    token,
                    startDate,
                    endDate,
                    type + ""
            );
            call.enqueue(new Callback<EmployeePermissionSummaryResponse>() {
                @Override
                public void onResponse(Call<EmployeePermissionSummaryResponse> call, Response<EmployeePermissionSummaryResponse> response) {
                    EmployeePermissionSummaryResponse apiResponse = response.body();
                    //swipe_refresh.setRefreshing(false);
                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {

                            if (apiResponse.getPermissionData() != null && apiResponse.getPermissionData().size() > 0) {
                                if (permissionDataList != null && permissionDataList.size() > 0) {
                                    permissionDataList.clear();
                                }
                                permissionDataList.addAll(apiResponse.getPermissionData());
                                permissionAdapter.notifyDataSetChanged();
                                if (type == Constants.LEAVE_REPORT) {
                                    isLeavesDialogOpen = true;
                                }
                                if (type == Constants.PERMISSION_REPORT) {
                                    isPermssionsDialogOpen = true;
                                }
                                if (dialog != null) {
                                    if (!dialog.isShowing()) {
                                        dialog.show();
                                    }
                                }


                            } else {
                                if (permissionDataList != null && permissionDataList.size() > 0) {
                                    permissionDataList.clear();
                                    permissionAdapter.notifyDataSetChanged();
                                }
                            }

                        }


                    } else {
                        closeProgress();
                        //   Utilities.ShowSnackbar(context, recyclerView, getResources().getString(R.string.somthing_went_wrong));
                    }

                }

                @Override
                public void onFailure(Call<EmployeePermissionSummaryResponse> call, Throwable t) {
                    closeProgress();
                    //swipe_refresh.setRefreshing(false);
                    //  Utilities.ShowSnackbar(context, recyclerView, getResources().getString(R.string.somthing_went_wrong));

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callAutoCheckOutDetailsApi(final String date, final String attendanceId, final String checkOutTime, final String status, final String checkOut, final String empId, final String isAutoCheckOut) {

        try {
            openProgress();
            Call<AutoCheckDetailsResponse> call = apiService.getAutoCheckOutDetails(
                    token,
                    userId,
                    date,
                    attendanceId

            );
            call.enqueue(new Callback<AutoCheckDetailsResponse>() {
                @Override
                public void onResponse(Call<AutoCheckDetailsResponse> call, Response<AutoCheckDetailsResponse> response) {
                    AutoCheckDetailsResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {


                            if (apiResponse.getData() != null) {

                                if (apiResponse.getData().getComments() != null && apiResponse.getData().getComments().size() > 0) {
                                    if (commentsAdapter != null) {
                                        if (comments != null && comments.size() > 0) {
                                            comments.clear();
                                        }
                                        comments.addAll(apiResponse.getData().getComments());
                                        commentsAdapter.notifyDataSetChanged();
                                    }
                                }
                                if (!isAutoCheckOutDialogOpen) {
                                    isAutoCheckOutDialogOpen = true;

                                    openAutocheckOutDialog(apiResponse.getData(), date, attendanceId, checkOutTime, status, checkOut, empId, isAutoCheckOut);


                                }

                            } else {
                                Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                            // Utilities.ShowSnackbar(context, recyclerView, getResources().getString(R.string.somthing_went_wrong));
                        }


                    } else {
                        closeProgress();

                    }

                }

                @Override
                public void onFailure(Call<AutoCheckDetailsResponse> call, Throwable t) {
                    closeProgress();

                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callExceptionDetailsApi(final String date, final String attendanceId, final String checkOutTime, final String status, final String checkOut, final String empId, final String isAutoCheckOut, final String exceptionType, final String checkIn) {

        try {
            openProgress();
            Call<ExceptionDetailsResponse> call = apiService.getExceptionDetails(
                    token,
                    userId,
                    date,
                    attendanceId,
                    exceptionType

            );
            call.enqueue(new Callback<ExceptionDetailsResponse>() {
                @Override
                public void onResponse(Call<ExceptionDetailsResponse> call, Response<ExceptionDetailsResponse> response) {
                    ExceptionDetailsResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        closeProgress();


                        if (apiResponse.getData() != null) {

                            if (apiResponse.getData().getComments() != null && apiResponse.getData().getComments().size() > 0) {
                                if (commentsAdapter != null) {
                                    if (comments != null && comments.size() > 0) {
                                        comments.clear();
                                    }
                                    comments.addAll(apiResponse.getData().getComments());
                                    commentsAdapter.notifyDataSetChanged();
                                }
                            }

                            if (!isExceptionDialogOpen) {
                                isExceptionDialogOpen = true;
                                openExceptionDialog(apiResponse.getData(), date, attendanceId, checkOutTime, status, checkOut, empId, isAutoCheckOut, exceptionType, checkIn);
                            }


                        }


                    } else {
                        closeProgress();

                    }

                }

                @Override
                public void onFailure(Call<ExceptionDetailsResponse> call, Throwable t) {
                    closeProgress();

                }

            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callExceptionStatusdDetailsApi(final String date, final String attendanceId, final String checkOutTime, final String status, final String checkOut, final String empId, final String isAutoCheckOut, final String exceptionType, final String checkIn) {

        try {
            openProgress();
            Call<ExceptionStatusResponse> call = apiService.getExceptionStatus(
                    token,
                    userId,
                    attendanceId,
                    exceptionType,
                    status,
                    date

            );
            call.enqueue(new Callback<ExceptionStatusResponse>() {
                @Override
                public void onResponse(Call<ExceptionStatusResponse> call, Response<ExceptionStatusResponse> response) {
                    ExceptionStatusResponse apiResponse = response.body();
                    if (apiResponse != null) {
                        closeProgress();

                        if (apiResponse.isSuccess()) {

                            if (apiResponse.getData() != null) {

                                openExceptionStatusDialog(apiResponse.getData(), date, attendanceId, checkOutTime, status, checkOut, empId, isAutoCheckOut, exceptionType, checkIn);
                            }
                        } else {
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }


                    } else {
                        closeProgress();
                        Toast.makeText(context, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();

                    }

                }

                @Override
                public void onFailure(Call<ExceptionStatusResponse> call, Throwable t) {
                    closeProgress();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void updateAttendanceReportViews(final AttendanceReport attendanceReport) {

        try {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (attendanceReport != null) {
                        workingDays = attendanceReport.getWorkingDays();
                        permissions = attendanceReport.getPermissionCount();

                        leaves = attendanceReport.getLeaveCount();
                        lops = Double.valueOf(attendanceReport.getLopCount());
                        earlyLogouts = attendanceReport.getEarlyLogoutCount();
                        lateLogins = attendanceReport.getLateLoginCount();
                        lessThanWorkingHours = attendanceReport.getLessWorkingDaysCount();


                        updateTextViewCount(working_days_tv, workingDays);
                        updateTextViewCount(permissions_count_tv, permissions);
                        updateTextViewCount(permissions_tv, permissions);
                        updateTextViewCountDouble(leaves_tv, leaves);
                        updateTextViewCountDouble(lops_tv, lops);
                        updateTextViewCount(early_logouts_tv, earlyLogouts);
                        updateTextViewCount(late_logins_tv, lateLogins);
                        updateTextViewCount(lessathan_working_hours, lessThanWorkingHours);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateLeaveReportViews(final LeaveReport leaveReport) {

        try {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (leaveReport != null) {
                        sick = Double.valueOf(leaveReport.getSickCount() != null ? leaveReport.getSickCount() : "0");
                        casual = Double.valueOf(leaveReport.getCasualCount() != null ? leaveReport.getCasualCount() : "0");
                        permissions = leaveReport.getPermissionCount();
                        lops = Double.valueOf(leaveReport.getLopCount() != null ? leaveReport.getLopCount() : "0");
                        earlyLogouts = leaveReport.getEarlyLogoutCount();
                        lateLogins = leaveReport.getLateLoginCount();
                        lessThanWorkingHours = leaveReport.getLessWorkingDaysCount();
                        optional = Double.valueOf(leaveReport.getOptionalCount() != null ? leaveReport.getOptionalCount() : "0");

                        autoCheckOut = Double.valueOf(leaveReport.getTotalLeaveCount() != null ? leaveReport.getTotalLeaveCount() : "0");
                        exception = leaveReport.getException();

                        updateTextViewCountDouble(sick_tv, sick);
                        updateTextViewCountDouble(optional_tv, optional);
                        updateTextViewCountDouble(casual_tv, casual);
                        updateTextViewCountDouble(lops_tv1, lops);
                        updateTextViewCount(lessathan_working_hours1, lessThanWorkingHours);
                        updateTextViewCount(permissions_count_tv1, permissions);
                        updateTextViewCount(exception_tv, exception);
                        updateTextViewCountDouble(auto_checkout_tv, autoCheckOut);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateTextViewCount(TextView textView, int count) {
        try {
            if (count == 0) {
                textView.setText("0");
            } else if (count <= 9) {
                textView.setText("0" + count);
            } else {
                textView.setText(count + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTextViewCountDouble(TextView textView, Double count) {

        try {
            if (count == 0) {
                textView.setText("0");
            } else {

                // textView.setText(count+"");
                String numberD = String.valueOf(count);
                String countStr = String.valueOf(count);
                ;
                numberD = numberD.substring(numberD.indexOf("."));
                if (numberD.replace(".", "").equals("0")) {
                    String string_form = new Double(count).toString().substring(0, countStr.indexOf('.'));
                    textView.setText(string_form);
                } else {
                    textView.setText(count + "");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callLeaveReportApi() {


        try {

            /*if (!isRefresh) {
                openProgress();
            } else {
                isRefresh = false;
            }*/
            openProgress();
            Call<LeaveReportResponse> call = apiService.getLeaveReport(
                    userId,
                    token,
                    startDate,
                    endDate);
            call.enqueue(new Callback<LeaveReportResponse>() {
                @Override
                public void onResponse(Call<LeaveReportResponse> call, Response<LeaveReportResponse> response) {
                    final LeaveReportResponse apiResponse = response.body();
                    //swipe_refresh.setRefreshing(false);
                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {

                            if (apiResponse.getLeaveReport() != null) {

                                Thread t = new Thread() {
                                    public void run() {

                                        updateLeaveReportViews(apiResponse.getLeaveReport());

                                    }
                                };
                                t.start();

                                // updateLeaveReportViews(apiResponse.getLeaveReport());
                                if (apiResponse.getLeaveReport().getCompoffData() != null && apiResponse.getLeaveReport().getCompoffData().size() > 0) {
                                    if (compOffDetailsList1 != null && compOffDetailsList1.size() > 0) {
                                        compOffDetailsList1.clear();
                                    }
                                    compOffDetailsList1.addAll(apiResponse.getLeaveReport().getCompoffData());
                                    compoffAdapter1.notifyDataSetChanged();
                                    comp_off_title1.setVisibility(View.VISIBLE);
                                }


                            }
                        }


                    } else {
                        closeProgress();

                    }

                }

                @Override
                public void onFailure(Call<LeaveReportResponse> call, Throwable t) {
                    closeProgress();
                    //swipe_refresh.setRefreshing(false);


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProfile() {
        try {
            RequestOptions options = new RequestOptions()
                    .circleCropTransform()
                    .error(R.drawable.avatar_placeholder_light)
                    .priority(Priority.HIGH);
            if (user_avatar != null && !user_avatar.trim().isEmpty()) {
                Glide.with(context).load(MyApplication.AWS_BASE_URL + user_avatar)
                        .apply(options).into(profileImage);
            } else {
                Glide.with(context).load(R.drawable.avatar_placeholder_light).into(profileImage);
            }
            employee_name_tv.setText(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareprefernceData() {
        try {

            Bundle bundle = getArguments();
            if (bundle != null) {

                userId = bundle.getString("employee_user_id");
                name = bundle.getString("employee_user_name");
                user_avatar = bundle.getString("employee_image_path");
                type = bundle.getString("type");
                if (type != null && !type.trim().isEmpty()) {
                    if (type.equals("1")) {
                        closeImage.setVisibility(View.VISIBLE);
                    } else {
                        closeImage.setVisibility(View.INVISIBLE);
                    }
                }

            }


            //  userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            //  name = sharedPreferences.getString(SharePreferenceKeys.USER_NAME, "");
            //  user_avatar = sharedPreferences.getString(SharePreferenceKeys.USER_AVATAR, "");
            email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
            notification_num = sharedPreferences.getInt(SharePreferenceKeys.NOTIFICATION_COUNT, 0);
            token = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
            loginUserId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            servicecount = sharedPreferences.getInt("countforservice", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void thisMonth() {
        try {
            Date today = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(today);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String thismonthFirst = dateFormat.format(cal.getTime());
            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            String thismonthLast = dateFormat.format(cal.getTime());
            startDate = thismonthFirst;
            endDate = thismonthLast;
            fromDate = thismonthFirst;
            toDate = thismonthLast;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void thisYear() {
        try {
            Date today = new Date();
            Calendar cal = new GregorianCalendar();
            cal.setTime(today);
            cal.set(Calendar.DAY_OF_YEAR, cal.getActualMinimum(Calendar.DAY_OF_YEAR));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String thisyearFirst = dateFormat.format(cal.getTime());
            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
            String thisyearLast = dateFormat.format(cal.getTime());
            startDate = thisyearFirst;
            endDate = thisyearLast;
            fromDate = thisyearFirst;
            toDate = thisyearLast;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void openDialog(String titleName, final int type) {

        try {


            dialog = new Dialog(context, R.style.DialogTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);


            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            lp.windowAnimations = R.style.slide_up_down;
            dialog.getWindow().setAttributes(lp);

            dialog.setContentView(R.layout.attendance_summary_dialog);


            ImageView cancelImage = dialog.findViewById(R.id.cancelImage);
            title_tv = dialog.findViewById(R.id.title_tv);
            emp_name_tv = dialog.findViewById(R.id.emp_name_tv);

            emp_name_tv.setText(name);


            if (type == Constants.EARLY_LOGOUT_REPORT || type == Constants.LATE_LOGIN_REPORT || type == Constants.LESS_WORK_HRS_REPORT ||
                    type == Constants.EXCEPTION_REPORT ||type==Constants.LOP_REPORT ) {
                recyclerView = dialog.findViewById(R.id.attendance_summary_recycler_view);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutManager);
                if(type==Constants.LOP_REPORT)
                {
                    lopsAdapter = new LopsAdapter(context, employeeDataList, AttendanceFragment.this);
                    recyclerView.setAdapter(lopsAdapter);
                }
                else
                {
                    adapter = new AttendanceSummaryAdapter(context, employeeDataList, AttendanceFragment.this);
                    recyclerView.setAdapter(adapter);
                }

                recyclerView.setNestedScrollingEnabled(false);

                if (Utilities.isNetworkAvailable(context)) {
                    callEmployeeSummary(type, dialog);
                } else {
                    Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                    // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                }
            }
           else if (type == Constants.PERMISSION_REPORT || type == Constants.LEAVE_REPORT ||
                    type==Constants.LEAVE_SICK_REPORT || type==Constants.LEAVE_CASUAL_REPORT || type==Constants.LEAVE_OPTIONAL_REPORT) {


                permissionRecyclerView = dialog.findViewById(R.id.permission_summary_recycler_view);
                permissionRecyclerView.setNestedScrollingEnabled(false);
                permissionRecyclerView.setHasFixedSize(true);
                permissionLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
                permissionRecyclerView.setLayoutManager(permissionLayoutManager);
                permissionAdapter = new PermissionSummaryAdapter(context, permissionDataList, AttendanceFragment.this, startDate, endDate, user_avatar, userId);
                permissionRecyclerView.setAdapter(permissionAdapter);
                permissionRecyclerView.setNestedScrollingEnabled(false);

                if (Utilities.isNetworkAvailable(context)) {
                    callEmployeePermissionSummary(type, dialog);
                } else {
                    Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                    // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                }
            }


            title_tv.setText(titleName);
            cancelImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (employeeDataList != null && employeeDataList.size() > 0) {
                        employeeDataList.clear();
                    }
                    if (permissionDataList != null && permissionDataList.size() > 0) {
                        permissionDataList.clear();
                    }
                    if (type == Constants.LEAVE_REPORT) {
                        isLeavesDialogOpen = false;
                    }
                    if (type == Constants.PERMISSION_REPORT) {
                        isPermssionsDialogOpen = false;
                    }
                    dialog.cancel();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAutoCheckOutDetails(String date, String attendanceId, String checkOutTime, String status, String checkOut, String empId, String isAutoCheckOut) {
        try {
            if (Utilities.isNetworkAvailable(context)) {
                callAutoCheckOutDetailsApi(date, attendanceId, checkOutTime, status, checkOut, empId, isAutoCheckOut);
            } else {
                Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getExceptionDetails(String date, String attendanceId, String checkOutTime, String status, String checkOut, String empId, String isAutoCheckOut, String exceptionType, String checkIn) {
        try {
            if (Utilities.isNetworkAvailable(context)) {
                callExceptionDetailsApi(date, attendanceId, checkOutTime, status, checkOut, empId, isAutoCheckOut, exceptionType, checkIn);
            } else {
                Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getExceptionStatusDetails(String date, String attendanceId, String checkOutTime, String status, String checkOut, String empId, String isAutoCheckOut, String exceptionType, String checkIn) {
        try {
            if (Utilities.isNetworkAvailable(context)) {
                callExceptionStatusdDetailsApi(date, attendanceId, checkOutTime, status, checkOut, empId, isAutoCheckOut, exceptionType, checkIn);
            } else {
                Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openAutocheckOutDialog(AutoCheckDetails details, final String date, final String attendanceId, final String time, final String status, final String checkOut, final String empId, final String isAutoCheckOut) {

        try {
            final Dialog dialog = new Dialog(context, R.style.DialogTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);

            String exceptionId = "", reportingUsers = "";


            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            lp.windowAnimations = R.style.slide_up_down;
            dialog.getWindow().setAttributes(lp);

            dialog.setContentView(R.layout.autocheckout_details_dialog);

            TextView reporting_manager_names_tv, hr_names_tv, name_tv, exception_title_tv, action_by_tv, action_by_name_tv;
            LinearLayout statusLayout;


            cancelImage = dialog.findViewById(R.id.cancelImage);
            name_tv = dialog.findViewById(R.id.name_tv);
            reporting_manager_names_tv = dialog.findViewById(R.id.reporting_manager_names_tv);
            hr_names_tv = dialog.findViewById(R.id.hr_names_tv);
            time_tv = dialog.findViewById(R.id.time_tv);
            exception_title_tv = dialog.findViewById(R.id.exception_title_tv);
            action_by_tv = dialog.findViewById(R.id.action_by_tv);
            action_by_name_tv = dialog.findViewById(R.id.action_by_name_tv);
            commentEditText = dialog.findViewById(R.id.comment);
            send = dialog.findViewById(R.id.send);
            approveLayout = dialog.findViewById(R.id.approveLayout);
            rejectLayout = dialog.findViewById(R.id.rejectLayout);
            approveRejectLayout = dialog.findViewById(R.id.approveRejectLayout);
            comments_recycler_view = dialog.findViewById(R.id.comments_recycler_view);
            timeLayout = dialog.findViewById(R.id.timeLayout);
            statusLayout = dialog.findViewById(R.id.statusLayout);

            if (comments != null && comments.size() > 0) {
                comments.clear();
            }

            comments_recycler_view.setHasFixedSize(true);
            manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            comments_recycler_view.setLayoutManager(manager);
            comments_recycler_view.setNestedScrollingEnabled(false);
            commentsAdapter = new CommentsAdapter(context, comments);
            comments_recycler_view.setAdapter(commentsAdapter);

            boolean isPresent = false;
            changedTime = "";


            time_tv.setText(time);

            if (details != null) {
                exception_title_tv.setText("Auto CheckOut by ");
                name_tv.setText(details.getName());
                reporting_manager_names_tv.setText(details.getRequestToUsers());
                hr_names_tv.setText(details.getRequestCcUsers());
                exceptionId = details.getException_id();
                reportingUsers = details.getReportingBossId();
                if (details.getComments() != null && details.getComments().size() > 0) {
                    comments.addAll(details.getComments());
                    commentsAdapter.notifyDataSetChanged();
                }

                if (details.getStatus() != null && !details.getStatus().trim().isEmpty() && details.getStatus().equals("1")) {
                    statusLayout.setVisibility(View.VISIBLE);
                    action_by_tv.setText("Approved by ");
                } else if (details.getStatus() != null && !details.getStatus().trim().isEmpty() && details.getStatus().equals("2")) {
                    statusLayout.setVisibility(View.VISIBLE);
                    action_by_tv.setText("Rejected by ");
                }

                if (details.getAccepted_by() != null && !details.getAccepted_by().trim().isEmpty()) {
                    action_by_name_tv.setText(details.getAccepted_by());
                }

                if (details.getCheck_out() != null && !details.getCheck_out().trim().isEmpty() && !details.getCheck_out().equals("0000-00-00 00:00:00")) {
                    String checkInTime = getAttendenceTime(details.getCheck_out());
                    time_tv.setText(checkInTime + " " + AM_PM);

                }

            }

            if (reportingUsers != null && !reportingUsers.trim().isEmpty()) {

                String[] splits = reportingUsers.trim().split(",");
                if (splits != null && splits.length > 0) {
                    for (int i = 0; i < splits.length; i++) {
                        if (loginUserId.equals(splits[i])) {
                            isPresent = true;
                        }
                    }
                }
            }

            boolean is_full_access = sharedPreferences.getBoolean(SharePreferenceKeys.FULL_ACCESS, false);
            if (sharedPreferences.getInt(SharePreferenceKeys.USER_ROLE, 0) == Constants.UserRole.ADMIN || is_full_access) {
                if (status != null && !status.trim().isEmpty() && Integer.parseInt(status) == Constants.REQUEST_STATUS_PENDING) {
                    if (!userId.equals(loginUserId) && isPresent) {
                        approveRejectLayout.setVisibility(View.VISIBLE);
                        timeLayout.setEnabled(true);
                        timeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectTime();
                            }
                        });
                    }

                }

            } else if (sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false)) {
                if (status != null && !status.trim().isEmpty() && Integer.parseInt(status) == Constants.REQUEST_STATUS_PENDING) {
                    if (!userId.equals(loginUserId) && isPresent) {
                        approveRejectLayout.setVisibility(View.VISIBLE);
                        timeLayout.setEnabled(true);
                        timeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectTime();
                            }
                        });
                    }

                }
            } else {
                approveRejectLayout.setVisibility(View.GONE);
            }

            commentEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    commentEditText.setFocusable(true);
                    commentEditText.setCursorVisible(true);
                    commentEditText.requestFocus();

                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showSoftInput(commentEditText, InputMethodManager.SHOW_FORCED);
                    return true;
                }
            });


            commentEditText.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View view, MotionEvent event) {
                    commentEditText.setCursorVisible(true);
                    // TODO Auto-generated method stub
                    if (view.getId() == R.id.comment) {
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_UP:
                                view.getParent().requestDisallowInterceptTouchEvent(false);
                                break;

                            case MotionEvent.ACTION_DOWN:
                                view.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }
                    }
                    return false;
                }
            });

            final String finalExceptionId = exceptionId;
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    validateForm(finalExceptionId, String.valueOf(Constants.ExceptionAutoCheckoutComments.EXCEPTION_TYPE_AUTOCHECK_OUT));
               /* if(commentEditText!=null && !commentEditText.getText().toString().trim().isEmpty() && commentEditText.getText().toString().length()>0){
                    getAutoCheckOutDetails(date,attendanceId,time,status,checkOut,empId,isAutoCheckOut);
                }*/


                }
            });

            approveLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    approveValidateForm(date, empId, isAutoCheckOut, attendanceId, checkOut, finalExceptionId, dialog);
                }
            });


            cancelImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isAutoCheckOutDialogOpen = false;
                    dialog.cancel();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void openExceptionDialog(final ExceptionDetails details, final String date, final String attendanceId, final String time, final String status, final String checkOut, final String empId, final String isAutoCheckOut, final String exceptionType, final String checkIn) {

        try {
            final Dialog dialog = new Dialog(context, R.style.DialogTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);

            String exceptionId = "";


            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            lp.windowAnimations = R.style.slide_up_down;
            dialog.getWindow().setAttributes(lp);

            dialog.setContentView(R.layout.exception_details_dialog);

            TextView time_title_tv, exception_title_tv;


            cancelImage = dialog.findViewById(R.id.cancelImage);
            name_tv = dialog.findViewById(R.id.name_tv);
            reporting_manager_names_tv = dialog.findViewById(R.id.reporting_manager_names_tv);
            hr_names_tv = dialog.findViewById(R.id.hr_names_tv);
            time_tv = dialog.findViewById(R.id.time_tv);
            commentEditText = dialog.findViewById(R.id.comment);
            send = dialog.findViewById(R.id.send);
            approveLayout = dialog.findViewById(R.id.approveLayout);
            rejectLayout = dialog.findViewById(R.id.rejectLayout);
            approveRejectLayout = dialog.findViewById(R.id.approveRejectLayout);
            timeLayout = dialog.findViewById(R.id.timeLayout);
            comments_recycler_view = dialog.findViewById(R.id.comments_recycler_view);
            time_title_tv = dialog.findViewById(R.id.time_title_tv);
            exception_title_tv = dialog.findViewById(R.id.exception_title_tv);

            if (comments != null && comments.size() > 0) {
                comments.clear();
            }

            comments_recycler_view.setHasFixedSize(true);
            manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            comments_recycler_view.setLayoutManager(manager);
            comments_recycler_view.setNestedScrollingEnabled(false);
            commentsAdapter = new CommentsAdapter(context, comments);
            comments_recycler_view.setAdapter(commentsAdapter);


            changedTime = "";
            time_tv.setText(time);
            String reportingUsers = "";
            boolean isPresent = false;


            if (details != null) {
                reportingUsers = details.getReportingBossId();
                if (exceptionType.equals("1")) {
                    exception_title_tv.setText("CheckIn Exception by ");
                    name_tv.setText(details.getName());
                    time_title_tv.setText("Check In Time");
                } else {
                    exception_title_tv.setText("CheckOut Exception by ");
                    name_tv.setText(details.getName());
                    time_title_tv.setText("Check Out Time");
                }

                reporting_manager_names_tv.setText(details.getRequestToUsers());
                hr_names_tv.setText(details.getRequestCcUsers());
                exceptionId = details.getExceptionId();
                if (details.getComments() != null && details.getComments().size() > 0) {

                    comments.addAll(details.getComments());
                    commentsAdapter.notifyDataSetChanged();
                }
            }
            if (reportingUsers != null && !reportingUsers.trim().isEmpty()) {

                String[] splits = reportingUsers.trim().split(",");
                if (splits != null && splits.length > 0) {
                    for (int i = 0; i < splits.length; i++) {
                        if (loginUserId.equals(splits[i])) {
                            isPresent = true;
                        }
                    }
                }
            }


            boolean is_full_access = sharedPreferences.getBoolean(SharePreferenceKeys.FULL_ACCESS, false);
            if (sharedPreferences.getInt(SharePreferenceKeys.USER_ROLE, 0) == Constants.UserRole.ADMIN || is_full_access) {
                if (status != null && !status.trim().isEmpty() && Integer.parseInt(status) == Constants.REQUEST_STATUS_PENDING) {
                    if (!userId.equals(loginUserId) && isPresent) {
                        approveRejectLayout.setVisibility(View.VISIBLE);
                        timeLayout.setEnabled(true);
                        timeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectTime();
                            }
                        });

                    }

                }

            } else if (sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false)) {
                if (status != null && !status.trim().isEmpty() && Integer.parseInt(status) == Constants.REQUEST_STATUS_PENDING) {
                    if (!userId.equals(loginUserId) && isPresent) {
                        approveRejectLayout.setVisibility(View.VISIBLE);
                        timeLayout.setEnabled(true);
                        timeLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectTime();
                            }
                        });

                    }

                }
            } else {
                approveRejectLayout.setVisibility(View.GONE);
                timeLayout.setEnabled(false);
            }

            commentEditText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    commentEditText.setFocusable(true);
                    commentEditText.setCursorVisible(true);
                    commentEditText.requestFocus();

                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showSoftInput(commentEditText, InputMethodManager.SHOW_FORCED);
                    return true;
                }
            });


            commentEditText.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View view, MotionEvent event) {
                    commentEditText.setCursorVisible(true);
                    // TODO Auto-generated method stub
                    if (view.getId() == R.id.comment) {
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_UP:
                                view.getParent().requestDisallowInterceptTouchEvent(false);
                                break;

                            case MotionEvent.ACTION_DOWN:
                                view.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }
                    }
                    return false;
                }
            });

            final String finalExceptionId = exceptionId;
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (exceptionType.equals("1")) {
                        validateForm(finalExceptionId, String.valueOf(Constants.ExceptionAutoCheckoutComments.EXCEPTION_TYPE_CHECK_IN_EXCEPTION));
                    } else {
                        validateForm(finalExceptionId, String.valueOf(Constants.ExceptionAutoCheckoutComments.EXCEPTION_TYPE_CHECK_OUT_EXCEPTION));
                    }
/*
                if(commentEditText!=null && !commentEditText.getText().toString().trim().isEmpty() && commentEditText.getText().toString().length()>0){
                    getExceptionDetails(date,attendanceId,time,status,checkOut,empId,isAutoCheckOut,exceptionType,checkIn);
                }*/
                }
            });


            approveLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    approveExceptiopnValidateForm(date, empId, details.getExceptionId(), attendanceId, checkOut, checkIn, "1", exceptionType, dialog);
                }
            });

            rejectLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    approveExceptiopnValidateForm(date, empId, details.getExceptionId(), attendanceId, checkOut, checkIn, "2", exceptionType, dialog);
                }
            });


            cancelImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    isExceptionDialogOpen = false;
                    dialog.cancel();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openExceptionStatusDialog(final ExceptionStatusDetails details, final String date, final String attendanceId, String time, String status, final String checkOut, final String empId, final String isAutoCheckOut, final String exceptionType, final String checkIn) {

        try {
            final Dialog dialog = new Dialog(context, R.style.DialogTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);

            String requestId = "";


            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();

            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            lp.windowAnimations = R.style.slide_up_down;
            dialog.getWindow().setAttributes(lp);

            dialog.setContentView(R.layout.exception_status_details_dialog);


            TextView time_title_tv, exception_title_tv;

            cancelImage = dialog.findViewById(R.id.cancelImage);
            name_tv = dialog.findViewById(R.id.name_tv);
            reporting_manager_names_tv = dialog.findViewById(R.id.reporting_manager_names_tv);
            hr_names_tv = dialog.findViewById(R.id.hr_names_tv);
            time_tv = dialog.findViewById(R.id.time_tv);
            updated_time_tv = dialog.findViewById(R.id.updated_time_tv);
            action_by_tv = dialog.findViewById(R.id.action_by_tv);
            action_by_name_tv = dialog.findViewById(R.id.action_by_name_tv);
            reason_tv = dialog.findViewById(R.id.reason_tv);
            comment_tv = dialog.findViewById(R.id.comment_tv);
            time_title_tv = dialog.findViewById(R.id.time_title_tv);
            exception_title_tv = dialog.findViewById(R.id.exception_title_tv);


            timeLayout = dialog.findViewById(R.id.timeLayout);
            UpdatedTimeLayout = dialog.findViewById(R.id.UpdatedTimeLayout);
            comments_recycler_view = dialog.findViewById(R.id.comments_recycler_view);

            if (comments != null && comments.size() > 0) {
                comments.clear();
            }

            comments_recycler_view.setHasFixedSize(true);
            manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            comments_recycler_view.setLayoutManager(manager);
            comments_recycler_view.setNestedScrollingEnabled(false);
            commentsAdapter = new CommentsAdapter(context, comments);
            comments_recycler_view.setAdapter(commentsAdapter);


            changedTime = "";
            String reportingUsers = "";
            boolean isPresent = false;


            if (details != null) {
                reportingUsers = details.getReportingBossId();
                if (exceptionType.equals("1")) {
                    exception_title_tv.setText("Check In Exception by ");
                    name_tv.setText(details.getName());
                    time_title_tv.setText("Check In Time");
                } else {
                    exception_title_tv.setText("Check Out Exception by ");
                    name_tv.setText(details.getName());
                    time_title_tv.setText("Check Out Time");
                }

                if (status.equals("1")) {
                    action_by_tv.setText("Approved by ");
                } else {
                    action_by_tv.setText("Rejected by ");
                }

                if (details.getAcceptedBy() != null && !details.getAcceptedBy().trim().isEmpty()) {
                    action_by_name_tv.setText(details.getAcceptedBy());
                }

                if (details.getReason() != null && !details.getReason().trim().isEmpty()) {
                    reason_tv.setText(details.getReason());
                }

                if (details.getUserComment() != null && !details.getUserComment().trim().isEmpty()) {
                    comment_tv.setText(details.getUserComment());
                }

                if (details.getComments() != null && details.getComments().size() > 0) {
                    comments.addAll(details.getComments());
                    commentsAdapter.notifyDataSetChanged();
                }

                reporting_manager_names_tv.setText(details.getRequestToUsers());
                hr_names_tv.setText(details.getRequestCcUsers());
                // requestId=details.getR;

                if (exceptionType.equals("1")) {
                    if (details.getCheckIn() != null && !details.getCheckIn().trim().isEmpty() && !details.getCheckIn().equals("0000-00-00 00:00:00")) {
                        String updatedCheckIn = getAttendenceTime(details.getCheckIn());
                        time_tv.setText(updatedCheckIn + " " + AM_PM);
                    }


                    if (details.getCheckOut() != null && !details.getCheckOut().trim().isEmpty() && !details.getCheckOut().equals("0000-00-00 00:00:00")) {

                        String checkInTime = getAttendenceTime(details.getCheckIn());
                        time_tv.setText(checkInTime + " " + AM_PM);

                    }

                } else {
                    if (details.getCheckOut() != null && !details.getCheckOut().trim().isEmpty() && !details.getCheckOut().equals("0000-00-00 00:00:00")) {
                        String updatedCheckIn = getAttendenceTime(details.getUpdatedCheckOut());
                        updated_time_tv.setText(updatedCheckIn + " " + AM_PM);
                    }

                    if (details.getCheckOut() != null && !details.getCheckOut().trim().isEmpty() && !details.getCheckOut().equals("0000-00-00 00:00:00")) {
                        String checkInTime = getAttendenceTime(details.getCheckOut());
                        time_tv.setText(checkInTime + " " + AM_PM);

                    }

                }

            }


            cancelImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dialog.cancel();
                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectTime() {
        try {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(android.widget.TimePicker timePicker, int selectedHour, int selectedMinute) {

                    if (selectedMinute < 10 && selectedHour < 12) {
                        if (selectedHour < 10) {
                            changedTime = "0" + selectedHour + ":0" + selectedMinute + ":00";
                        } else {
                            changedTime = selectedHour + ":0" + selectedMinute + ":00";
                        }

                    } else if (selectedMinute < 10 && selectedHour >= 12) {
                        changedTime = selectedHour + ":0" + selectedMinute + ":00";
                    } else if (selectedMinute > 10 && selectedHour < 12) {
                        if (selectedHour < 10) {
                            changedTime = "0" + selectedHour + ":" + selectedMinute + ":00";
                        } else {
                            changedTime = selectedHour + ":" + selectedMinute + ":00";
                        }

                    } else if (selectedMinute > 10 && selectedHour >= 12) {
                        changedTime = selectedHour + ":" + selectedMinute + ":00";
                    } else {
                        changedTime = selectedHour + ":" + selectedMinute + ":00";
                    }



                    if (time_tv != null) {
                        if (selectedMinute < 10 && selectedHour < 12) {
                            if (selectedHour < 10) {
                                time_tv.setText("0" + selectedHour + ":" + "0" + selectedMinute + " am");
                            } else {
                                time_tv.setText(selectedHour + ":" + "0" + selectedMinute + " am");
                            }


                        } else if (selectedMinute < 10 && selectedHour >= 12) {
                            if (selectedHour > 12) {
                                if (selectedHour > 12) {
                                    selectedHour = selectedHour - 12;
                                }
                                if (selectedHour < 10) {
                                    time_tv.setText("0" + selectedHour + ":" + "0" + selectedMinute + " pm");
                                } else {
                                    time_tv.setText(selectedHour + ":" + "0" + selectedMinute + " pm");
                                }

                            } else {
                                if (selectedHour < 12) {
                                    time_tv.setText(selectedHour + ":" + "0" + selectedMinute + " am");
                                } else {
                                    time_tv.setText(selectedHour + ":" + "0" + selectedMinute + " pm");
                                }

                            }

                        } else if (selectedMinute > 10 && selectedHour < 12) {
                            if (selectedHour < 10) {
                                time_tv.setText("0" + selectedHour + ":" + selectedMinute + " am");
                            } else {
                                time_tv.setText(selectedHour + ":" + selectedMinute + " am");
                            }

                        } else if (selectedMinute > 10 && selectedHour >= 12) {
                            if (selectedHour > 12) {
                                selectedHour = selectedHour - 12;

                                if (selectedHour < 10) {
                                    time_tv.setText("0" + selectedHour + ":" + selectedMinute + " pm");
                                } else {
                                    time_tv.setText(selectedHour + ":" + selectedMinute + " pm");
                                }


                            } else {
                                if (selectedHour < 12) {
                                    if (selectedHour < 10) {
                                        time_tv.setText("0" + selectedHour + ":" + selectedMinute + " am");
                                    } else {
                                        time_tv.setText(selectedHour + ":" + selectedMinute + " am");
                                    }

                                } else {
                                    time_tv.setText(selectedHour + ":" + selectedMinute + " pm");
                                }

                            }
                            //time_tv.setText(selectedHour + ":" + selectedMinute);

                        } else if (selectedMinute < 10 && selectedHour > 12) {

                            selectedHour = selectedHour - 12;

                            if (selectedHour < 10) {
                                time_tv.setText("0" + selectedHour + ":" + "0" + selectedMinute + " pm");
                            } else {
                                time_tv.setText(selectedHour + ":" + "0" + selectedMinute + " pm");
                            }


                        }

                    }

                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void validateForm(String requestId, String exceptionType) {
        try {
            send.setClickable(false);
            if (commentEditText != null && !commentEditText.getText().toString().trim().isEmpty() && commentEditText.getText().toString().length() > 0) {
                if (Utilities.isNetworkAvailable(context)) {
                    sendComment(commentEditText.getText().toString(), "0", requestId, exceptionType);

                } else {
                    send.setClickable(true);

                    Toast.makeText(context, "Please check network connection", Toast.LENGTH_SHORT).show();

                    // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                }


            } else {

                Utilities.showSnackBar(commentEditText, "Please enter comment");
                send.setClickable(true);
                commentEditText.setFocusable(true);
                commentEditText.setCursorVisible(true);
                commentEditText.requestFocus();

                ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE))
                        .showSoftInput(commentEditText, InputMethodManager.SHOW_FORCED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void approveValidateForm(String date, String empId, String isAutoCheckOut, String attendanceId, String checkOut, String exceptionId, Dialog dialog) {
        try {
            approveLayout.setClickable(false);
            if (commentEditText != null && !commentEditText.getText().toString().trim().isEmpty() && commentEditText.getText().toString().length() > 0) {
                if (Utilities.isNetworkAvailable(context)) {
                    approveAutoCheckOut(commentEditText.getText().toString().trim(), date, empId, isAutoCheckOut, attendanceId, checkOut, exceptionId, dialog);

                } else {
                    approveLayout.setClickable(true);

                    Toast.makeText(context, "Please check network connection", Toast.LENGTH_SHORT).show();
                    //  Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                }


            } else {


                if (Utilities.isNetworkAvailable(context)) {
                    approveAutoCheckOut("", date, empId, isAutoCheckOut, attendanceId, checkOut, exceptionId, dialog);

                } else {
                    approveLayout.setClickable(true);

                    Toast.makeText(context, "Please check network connection", Toast.LENGTH_SHORT).show();
                    //  Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                }

/*
            Utilities.showSnackBar(commentEditText, "Please enter comment");
            approveLayout.setClickable(true);
            commentEditText.setFocusable(true);
            commentEditText.setCursorVisible(true);
            commentEditText.requestFocus();

            ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .showSoftInput(commentEditText, InputMethodManager.SHOW_FORCED);*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void approveExceptiopnValidateForm(String date, String empId, String exception_id, String attendanceId, String checkOut, String checkIn, String status, String exception_type, Dialog dialog) {
        try {
            approveLayout.setClickable(false);
            rejectLayout.setClickable(false);
            if (status.equals("2")) {
                if (commentEditText != null && !commentEditText.getText().toString().trim().isEmpty() && commentEditText.getText().toString().length() > 0) {
                    if (Utilities.isNetworkAvailable(context)) {
                        approveException(commentEditText.getText().toString().trim(), date, empId, exception_id, attendanceId, checkOut, checkIn, status, exception_type, dialog);

                    } else {
                        approveLayout.setClickable(true);
                        rejectLayout.setClickable(true);

                        Toast.makeText(context, "Please check network connection", Toast.LENGTH_SHORT).show();
                        //  Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                    }


                } else {
                    approveLayout.setClickable(true);
                    rejectLayout.setClickable(true);
                    Toast.makeText(context, "Please enter comment", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (commentEditText != null && !commentEditText.getText().toString().trim().isEmpty() && commentEditText.getText().toString().length() > 0) {
                    if (Utilities.isNetworkAvailable(context)) {
                        approveException(commentEditText.getText().toString().trim(), date, empId, exception_id, attendanceId, checkOut, checkIn, status, exception_type, dialog);

                    } else {
                        approveLayout.setClickable(true);
                        rejectLayout.setClickable(true);

                        Toast.makeText(context, "Please check network connection", Toast.LENGTH_SHORT).show();
                        //  Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                    }


                } else {


                    if (Utilities.isNetworkAvailable(context)) {
                        approveException("", date, empId, exception_id, attendanceId, checkOut, checkIn, status, exception_type, dialog);

                    } else {
                        approveLayout.setClickable(true);
                        rejectLayout.setClickable(true);

                        Toast.makeText(context, "Please check network connection", Toast.LENGTH_SHORT).show();
                        // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void approveAutoCheckOut(String comment, final String date, String empId, String isAutoCheckOut, String attendanceId, String checkOut, String exceptionId, final Dialog dialog1) {

        try {
            Helper.getInstance().closeKeyBoard(context, commentEditText);


            openProgress();

            if (changedTime != null && !changedTime.trim().isEmpty()) {
                //2019-04-15 14:28:30
                String newCheckOut = checkOut.substring(0, 11) + changedTime;
                checkOut = newCheckOut;

            }

            Call<CommonResponse> call = apiService.approveAutoCheckOutException(token, loginUserId, isAutoCheckOut, comment, attendanceId, checkOut, empId,
                    date, exceptionId);
            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    CommonResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        approveLayout.setClickable(true);

                        if (apiResponse.isSuccess()) {
                            commentEditText.setText(null);
                            commentEditText.setCursorVisible(false);
                            Utilities.showSnackBar(commentEditText, apiResponse.getMessage());
                            approveRejectLayout.setVisibility(View.GONE);
                            if (isListClicked) {
                                refreshAttendanceToUpdate();
                            } else if (isLeaveReportsClicked) {


                                if (Utilities.isNetworkAvailable(context)) {
                                    callEmployeeSummary(Constants.EXCEPTION_REPORT, dialog);
                                } else {
                                    Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                                    // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                                }

                            }


                            isAutoCheckOutDialogOpen = false;
                            dialog1.dismiss();
                        } else {

                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(context, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();

                        closeProgress();
                        approveLayout.setClickable(true);
                    }

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {

                    approveLayout.setClickable(true);
                    closeProgress();


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void approveException(String comment, String date, String empId, String exception_id, String attendanceId, String checkOut, String checkIn, String status, String exception_type, final Dialog dialog1) {
        try {
            Helper.getInstance().closeKeyBoard(context, commentEditText);

            openProgress();

            if (exception_type.equals("1")) {
                if (changedTime != null && !changedTime.trim().isEmpty()) {
                    //2019-04-15 14:28:30
                    String newCheckIn = checkIn.substring(0, 11) + changedTime;
                    checkIn = newCheckIn;

                }
            } else {
                if (changedTime != null && !changedTime.trim().isEmpty()) {
                    //2019-04-15 14:28:30
                    String newCheckOut = checkOut.substring(0, 11) + changedTime;
                    checkOut = newCheckOut;

                }
            }

            Call<CommonResponse> call = apiService.approveException(token, loginUserId, exception_id, comment, attendanceId, checkOut, empId,
                    date, checkIn, status, exception_type);
            call.enqueue(new Callback<CommonResponse>() {
                @Override
                public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                    CommonResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        approveLayout.setClickable(true);
                        rejectLayout.setClickable(true);

                        if (apiResponse.isSuccess()) {


                            commentEditText.setText(null);
                            commentEditText.setCursorVisible(false);
                            Utilities.showSnackBar(commentEditText, apiResponse.getMessage());
                            approveRejectLayout.setVisibility(View.GONE);
                            // refreshAttendanceList();
                            if (isListClicked) {
                                refreshAttendanceToUpdate();
                            } else if (isLeaveReportsClicked) {


                                if (Utilities.isNetworkAvailable(context)) {
                                    callEmployeeSummary(Constants.EXCEPTION_REPORT, dialog);
                                } else {
                                    Toast.makeText(context, "Please check internet connection", Toast.LENGTH_LONG).show();
                                    // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.no_internet_connection));
                                }

                            }

                            isExceptionDialogOpen = false;
                            dialog1.dismiss();
                        } else {
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(context, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();
                        // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.somthing_went_wrong));
                        closeProgress();
                        approveLayout.setClickable(true);
                        rejectLayout.setClickable(true);
                    }

                }

                @Override
                public void onFailure(Call<CommonResponse> call, Throwable t) {
                    approveLayout.setClickable(true);
                    rejectLayout.setClickable(true);
                    closeProgress();


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendComment(String comment, String commentType, final String requestId, String exceptionType) {

        try {

            Helper.getInstance().closeKeyBoard(context, commentEditText);
            openProgress();
            String id = "0";
            Call<SendCommentResponse> call = apiService.sendExceptionAutoCheckoutComment(token, loginUserId, requestId, comment, commentType, exceptionType);
            call.enqueue(new Callback<SendCommentResponse>() {
                @Override
                public void onResponse(Call<SendCommentResponse> call, Response<SendCommentResponse> response) {
                    SendCommentResponse apiResponse = response.body();
                    closeProgress();
                    if (apiResponse != null) {

                        send.setClickable(true);
                        if (apiResponse.isSuccess()) {
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                            commentEditText.setText(null);
                            commentEditText.setCursorVisible(false);
                            if (Utilities.isNetworkAvailable(context)) {
                                callCommentsApi(requestId);

                            } else {
                                Toast.makeText(context, "Please check network connection", Toast.LENGTH_SHORT).show();

                            }


                        } else {
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(context, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();
                        // Utilities.ShowSnackbar(context,attendanceRecycleview, getResources().getString(R.string.somthing_went_wrong));

                        send.setClickable(true);
                    }

                }

                @Override
                public void onFailure(Call<SendCommentResponse> call, Throwable t) {
                    Toast.makeText(context, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();

                    closeProgress();
                    send.setClickable(true);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAttendenceTime(String timestamp) {

        String time = "N/A";
        try {
            if (!timestamp.contains("0000-00-00")) {
                Date d1 = null;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat form = null;
                if (timestamp != null && !timestamp.equals("null") && !timestamp.trim().isEmpty() && timestamp.contains(".")) {
                    form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                } else {
                    form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                }
                if (timestamp != null) {
                    d1 = form.parse(timestamp);
                } else {
                    String date = form.format(calendar.getTime());
                    d1 = form.parse(date);
                }
                Calendar postDate = Calendar.getInstance();
                postDate.setTime(d1);
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                String date = dateFormat.format(d1);

                String hour = "", mins = "";

                if (postDate.get(Calendar.HOUR) == 0) {
                    hour = "12";
                } else if (postDate.get(Calendar.HOUR) < 10) {
                    hour = "0" + postDate.get(Calendar.HOUR);
                } else {
                    hour = postDate.get(Calendar.HOUR) + "";
                }

                if (postDate.get(Calendar.MINUTE) < 10) {
                    mins = "0" + postDate.get(Calendar.MINUTE);
                } else {
                    mins = postDate.get(Calendar.MINUTE) + "";
                }


                AM_PM = ((postDate.get(Calendar.AM_PM)) == 0 ? "am" : "pm");


                time = hour + ":" + mins;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    private void callCommentsApi(String exceptionId) {


        try {
            Call<RequestCommentsResponse> call = apiService.getExceptionComments(token, loginUserId, exceptionId);
            call.enqueue(new Callback<RequestCommentsResponse>() {
                @Override
                public void onResponse(Call<RequestCommentsResponse> call, Response<RequestCommentsResponse> response) {
                    RequestCommentsResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();


                        if (apiResponse.isSuccess()) {


                            if (apiResponse.getComments() != null && !apiResponse.getComments().isEmpty() && apiResponse.getComments().size() > 0) {
                                if (commentsAdapter != null) {
                                    if (comments != null && comments.size() > 0) {
                                        comments.clear();
                                    }
                                    comments.addAll(apiResponse.getComments());
                                    commentsAdapter.notifyDataSetChanged();
                                }

                            }

                        }

                    } else {
                        closeProgress();
                    }

                }

                @Override
                public void onFailure(Call<RequestCommentsResponse> call, Throwable t) {
                    closeProgress();


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void monthYearDialog() {

        try {

            String[] monthNames = {
                    "Jan",
                    "Feb",
                    "Mar",
                    "Apr",
                    "May",
                    "Jun",
                    "Jul",
                    "Aug",
                    "Sep",
                    "Oct",
                    "Nov",
                    "Dec"

            };

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();

            final Calendar cal = Calendar.getInstance();

            View dialog = inflater.inflate(R.layout.date_picker_dialog, null);
            final NumberPicker monthPicker = (NumberPicker) dialog.findViewById(R.id.picker_month);
            final NumberPicker yearPicker = (NumberPicker) dialog.findViewById(R.id.picker_year);

            monthPicker.setMinValue(1);
            monthPicker.setMaxValue(12);
            monthPicker.setValue(cal.get(Calendar.MONTH) + 1);
            monthPicker.setDisplayedValues(monthNames);


            final int year = 2016;
            final int current_year = cal.get(Calendar.YEAR);
            yearPicker.setMinValue(year);
            yearPicker.setMaxValue(current_year);
            yearPicker.setValue(current_year);

            builder.setView(dialog)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {

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
                            dialog.cancel();
                        }
                    }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initStackedBarChart() {

        stackedBarChart = rootView.findViewById(R.id.stackedBarChart);
        stackedBarChart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        stackedBarChart.setMaxVisibleValueCount(40);

        // scaling can now only be done on x- and y-axis separately
        stackedBarChart.setPinchZoom(false);

        stackedBarChart.setDrawGridBackground(false);
        stackedBarChart.setDrawBarShadow(false);

        stackedBarChart.setDrawValueAboveBar(false);
        stackedBarChart.setHighlightFullBarEnabled(false);
        stackedBarChart.setClickable(false);
        stackedBarChart.setTouchEnabled(true);


        stackedBarChart.getDescription().setEnabled(false);
        stackedBarChart.setScaleEnabled(true);


        YAxis leftAxis = stackedBarChart.getAxisLeft();
        leftAxis.setEnabled(false);

        // change the position of the y-labels
        YAxis rightAxis = stackedBarChart.getAxisRight();
        rightAxis.setValueFormatter(new MyAxisValueFormatter());
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        rightAxis.setDrawGridLines(false);
        rightAxis.setStartAtZero(true);
        rightAxis.setAxisMaximum(24);
        rightAxis.setAxisMinimum(0);
        rightAxis.setLabelCount(24);
        rightAxis.setGranularity(1f);

        // leftAxis.setLabelCount(3, false);
        //leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

        XAxis xAxis = stackedBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMaximum(31);
        xAxis.setAxisMinimum(0);
        xAxis.setLabelCount(31);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        // stackedBarChart.setDrawXLabels(false);
        // stackedBarChart.setDrawYLabels(false);

        Legend l = stackedBarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);
        l.setEnabled(true);

        setData();
    }

    private void gooleStackChart() {
        webView = rootView.findViewById(R.id.stackChart);
        noData = rootView.findViewById(R.id.noData);

        for (int i = 0; i <= 31; i++) {
            dateArray.put(i);
            workArray.put(i);
        }

        try {

            webView.setClickable(false);
            webView.setFocusableInTouchMode(false);
            webView.setFocusable(false);
            webView.clearCache(true);
            webView.clearHistory();
            webView.setVerticalScrollBarEnabled(false);
            webView.setHorizontalScrollBarEnabled(false);

            webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return (event.getAction() == MotionEvent.ACTION_MOVE);
                }
            });

            webView.addJavascriptInterface(new WebAppInterface(), "Android");
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.setWebChromeClient(new WebChromeClient() {

            });
            chartUrl = "file:///android_asset/stackChart.html";
            drawStackBarChart();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setData() {


        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 1; i < 32; i++) {
            float mult = (10);
            float val1 = (float) (Math.random() * mult) + mult / 1;
            float val2 = (float) (Math.random() * mult) + mult / 2;
            float val3 = (float) (Math.random() * mult) + mult / 3;
            float val4 = (float) (Math.random() * mult) + mult / 4;

            yVals1.add(new BarEntry(
                    i,
                    new float[]{val1, val2, val3, val4},
                    getResources().getDrawable(R.drawable.menu)));
        }

        BarDataSet set1;

        if (stackedBarChart.getData() != null && stackedBarChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) stackedBarChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            set1.setAxisDependency(YAxis.AxisDependency.RIGHT);
            stackedBarChart.getData().notifyDataChanged();
            stackedBarChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");
            set1.setDrawIcons(false);

            ArrayList<Integer> colors = new ArrayList<Integer>();
            colors.add(getResources().getColor(R.color.late_color));
            colors.add(getResources().getColor(R.color.work_color));
            colors.add(getResources().getColor(R.color.break_color));
            colors.add(getResources().getColor(R.color.ot_color));

            set1.setColors(colors);
            set1.setStackLabels(new String[]{"LATE", "WORK", "BREAK", "OT"});

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueFormatter(new MyValueFormatter());
            data.setValueTextColor(Color.TRANSPARENT);


            stackedBarChart.setData(data);
        }

        stackedBarChart.setFitBars(true);
        stackedBarChart.invalidate();
    }

    private void drawStackBarChart() {
        try {
            webView.loadUrl(chartUrl);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public class WebAppInterface {

        @JavascriptInterface
        public JSONArray getDateArray() {
            return dateArray;
        }

        @JavascriptInterface
        public JSONArray getWorkArray() {
            return workArray;
        }

        @JavascriptInterface
        public JSONArray getLateArray() {
            return lateArray;
        }

        @JavascriptInterface
        public JSONArray getOverTimeArray() {
            return overTimeArray;
        }

        @JavascriptInterface
        public JSONArray getBreakArray() {
            return breakArray;
        }

        @JavascriptInterface
        public JSONArray getTimeArray() {
            return timeArray;
        }

        @JavascriptInterface
        public int getHeight() {
            return stackChartHeight;
        }

        @JavascriptInterface
        public int getWidth() {
            return stackChartWidth;
        }

        @JavascriptInterface
        public int getValue() {
            return 10;
        }

    }

}
