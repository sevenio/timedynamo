package com.tvisha.trooptime.activity.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.navigation.NavigationView;
import com.tvisha.trooptime.R;
import com.tvisha.trooptime.activity.activity.adapter.TroopTimeAdapter;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.activity.activity.apiPostModels.AllAttendenceApiResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.Attendance;
import com.tvisha.trooptime.activity.activity.apiPostModels.SelfAttendenceApiResponce;
import com.tvisha.trooptime.activity.activity.apiPostModels.TeamAttendenceApiResponce;
import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.helper.Navigation;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.model.CalenderModel;
import com.tvisha.trooptime.activity.activity.model.SelfAttenedModel;
import com.tvisha.trooptime.activity.activity.model.TeamAttenedModel;
import com.tvisha.trooptime.activity.activity.model.TimeAndAttendenceModel;

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
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;


public class Employee_SelfAttendence extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, SwipeRefreshLayout.OnRefreshListener {
    TextView text_self, text_team, logger_name, logger_email, next_month, previous_month, month_name, name_of_month, loggin_name, more_information,
            total_emp_leaves, emp_lossofpay, available_leaves, last_month_leaves, employeeName, notification_count;
    FrameLayout image_filter;
    ImageView close_emp_name, reset_data, title_text, text_nodata;
    RecyclerView emp_list_recyclerview;
    boolean isNetAvailable = false, teamtext_selected = false, close_emp = false;
    List<SelfAttenedModel> self_data_arrayList = new ArrayList<>();
    List<TeamAttenedModel> team_data_arrayList = new ArrayList<>();

    View underline_view;
    int filter_type = 2;

    List<Attendance> attendence_arrayList = new ArrayList<>();

    String userId, email, name, user_avatar, emp_id, success, start_mon, end_dat, clciked_date, employee_usrId, employee_userName, get_date, select_day, select_year, start_date, end_date;
    int array_length = 0, selected_position, month_position, notification_num;
    Calendar _calendar;
    RecyclerView month_calendar;
    DatePickerDialog pic_dialog;
    int month, year, current_month, current_day, current_year, next_year;
    GridCellAdapter adapter;
    RelativeLayout nav_drawer, employee_cal_relative, notification;
    LinearLayout employee_detail_layout;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    List<CalenderModel> calenderModels;
    SwipeRefreshLayout swipe_refresh;
    LinearLayoutManager manager;
    TimeAndAttendenceModel model;
    SharedPreferences sharedPreferences;
    Bundle bundle;
    TroopTimeAdapter timeAdapter;
    LinearLayoutManager linearLayoutManager;
    LinearLayout buttonsLayout;
    ImageView backImage, filterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_employee_attendance_details);
        try {
            bundle = getIntent().getExtras();
            if (bundle != null) {
                employee_usrId = bundle.getString("employee_user_id");
                employee_userName = bundle.getString("employee_user_name");
                filter_type = bundle.getInt("filter_type");
            }

            initializeWidgets();
            eventHandlings();


            employee_detail_layout.setVisibility(View.VISIBLE);
            shareprefernceData();

            if (Utilities.getConnectivityStatus(Employee_SelfAttendence.this) > 0) {
                getTheTroopTimeData(employee_usrId, "", "");
            } else {
                Utilities.ShowSnackbar(Employee_SelfAttendence.this, emp_list_recyclerview, "Please Check Network Connection");
            }

       /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            title_text.setText(Html.fromHtml("<font color='#ffffff'><b>TROOP</b></font><font color='#f06b7c'><b> TIME</b></font>", Html.FROM_HTML_MODE_LEGACY));
        } else {
            title_text.setText(Html.fromHtml("<font color='#ffffff'><b>TROOP</b></font><font color='#f06b7c'><b> TIME</b></font>"));
        }*/


            text_team.setBackgroundColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_team_selected));
            text_team.setTextColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_sleceted));

            text_self.setBackgroundColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_team_notselected));
            text_self.setTextColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_notselected));

            Calendar calendar = Calendar.getInstance();
            String day = new SimpleDateFormat("dd").format(calendar.getTime());
            current_day = Integer.parseInt(day);
            selected_position = Integer.parseInt(day) - 1;
            String month = new SimpleDateFormat("MM").format(calendar.getTime());
            String current_mon = new SimpleDateFormat("MMM").format(calendar.getTime());

            current_month = Integer.parseInt(month);
            month_position = Integer.parseInt(month) - 1;
            String current_year = new SimpleDateFormat("yyyy").format(calendar.getTime());
            year = Integer.parseInt(current_year);
            this.current_year = Integer.parseInt(current_year);
            next_year = Integer.parseInt(current_year + 1);
            name_of_month.setText(current_mon + ", " + year);

            selfisOnedCalenderView(year);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void selfisOnedCalenderView(int year) {
        try {
            month_name.setText(" " + year + " ");
            _calendar = Calendar.getInstance(Locale.getDefault());
            month = _calendar.get(Calendar.MONTH) + 1;
            year = _calendar.get(Calendar.YEAR);


            String[] months = new DateFormatSymbols().getMonths();
            calenderModels = new ArrayList<>();
            int count = 0;
            int month_no = 1;
            for (int i = 0; i < months.length; i++) {
                String month = months[i];
                String month_name = month.substring(0, 3);
                Calendar calendar = Calendar.getInstance();
                calendar.set(this.year, count, 1);
                int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                String start_mon = "" + this.year + "-" + month_no + "-01";
                String end_mon = "" + this.year + "-" + month_no + "-" + numDays;

                count++;
                month_no++;
                CalenderModel model = new CalenderModel();
                model.setMonthStartDate(start_mon);
                model.setMonthEndDate(end_mon);
                model.setMonth_Name(month_name);
                model.setYears(year);
                calenderModels.add(model);
            }
            adapter = new GridCellAdapter(Employee_SelfAttendence.this, month, this.year, "self");
            adapter.notifyDataSetChanged();
            manager = new LinearLayoutManager(Employee_SelfAttendence.this, LinearLayoutManager.HORIZONTAL, false);
            month_calendar.setLayoutManager(manager);
            month_calendar.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setGridCellAdapterToDate(int month, int year) {
        try {
            adapter = new GridCellAdapter(Employee_SelfAttendence.this, month, year, "team");
            _calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
            adapter.notifyDataSetChanged();
            month_calendar.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void shareprefernceData() {
        try {
            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
            name = sharedPreferences.getString(SharePreferenceKeys.USER_NAME, "");
            user_avatar = sharedPreferences.getString(SharePreferenceKeys.USER_AVATAR, "");
            notification_num = sharedPreferences.getInt(SharePreferenceKeys.NOTIFICATION_COUNT, 0);
            notification_count.setText("" + notification_num);
            employeeName.setText(employee_userName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getTheTroopTimeData(String user_id, String f_date, String e_date) {
        try {
            start_mon = f_date;
            end_dat = e_date;
            swipe_refresh.setRefreshing(true);
            swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_purple, android.R.color.holo_orange_light, android.R.color.holo_red_light);
            self_data_arrayList = new ArrayList<>();
            if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                attendence_arrayList.removeAll(attendence_arrayList);
                if (timeAdapter != null) {
                    timeAdapter.notifyDataSetChanged();
                }
            }
            attendence_arrayList = new ArrayList<>();

            callToselfAttendence(user_id, f_date, e_date);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callToselfAttendence(String user_id, String f_date, String e_date) {
        try {
            retrofit2.Call<SelfAttendenceApiResponce> call = ApiClient.getInstance()
                    .getSelefAttendence(user_id, sharedPreferences.getString(SharePreferenceKeys.API_KEY, ""), f_date, e_date, "", "");
            call.enqueue(new retrofit2.Callback<SelfAttendenceApiResponce>() {
                @Override
                public void onResponse(@NonNull Call<SelfAttendenceApiResponce> call, @NonNull Response<SelfAttendenceApiResponce> response) {
                    if (response.code() == Constants.RESPONCE_SUCCESSFUL) {
                        SelfAttendenceApiResponce attendenceApiResponce = response.body();
                        if (attendenceApiResponce != null) {
                            if (attendenceApiResponce.getSuccess()) {
                                if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                                    attendence_arrayList.clear();
                                }
                                attendence_arrayList.addAll(attendenceApiResponce.getAttendance());
                                addTheDataToAdapter("self");
                            }
                        } else {
                            swipe_refresh.setRefreshing(false);
                            Utilities.showSnackBar(emp_list_recyclerview, getResources().getString(R.string.somthing_went_wrong));
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SelfAttendenceApiResponce> call, @NonNull Throwable t) {
                    swipe_refresh.setRefreshing(false);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eventHandlings() {
        try {
            image_filter.setOnClickListener(this);
            nav_drawer.setOnClickListener(this);
            backImage.setOnClickListener(this);
            swipe_refresh.setOnRefreshListener(this);
            previous_month.setOnClickListener(this);
            text_self.setOnClickListener(this);
            text_team.setOnClickListener(this);
            next_month.setOnClickListener(this);
            reset_data.setOnClickListener(this);
            notification.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initializeWidgets() {
        try {
            text_self = (TextView) findViewById(R.id.text_self);
            text_team = (TextView) findViewById(R.id.text_team);
            image_filter = findViewById(R.id.image_request);
            emp_list_recyclerview = (RecyclerView) findViewById(R.id.emp_list_recyclerview);
            //button_switch         = (SwitchCompat) findViewById(R.id.button_switch);
            text_nodata = findViewById(R.id.text_nodata);
            month_calendar = (RecyclerView) findViewById(R.id.month_caledar);
            title_text = findViewById(R.id.title_text);
            nav_drawer = (RelativeLayout) findViewById(R.id.nav_drawer);
            backImage = findViewById(R.id.navigation_image);
            filterImage = findViewById(R.id.filterImage);
            filterImage.setVisibility(View.GONE);
            backImage.setImageResource(R.drawable.blue_color_back);
            navigationView = (NavigationView) findViewById(R.id.nav_view_3);
            logger_name = (TextView) findViewById(R.id.logger_name);
            logger_email = (TextView) findViewById(R.id.logger_email);
            swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);


            next_month = (TextView) findViewById(R.id.next_month);
            previous_month = (TextView) findViewById(R.id.previous_month);
            month_name = (TextView) findViewById(R.id.month_name);
            name_of_month = (TextView) findViewById(R.id.name_of_month);


            total_emp_leaves = (TextView) findViewById(R.id.total_emp_leaves);
            last_month_leaves = (TextView) findViewById(R.id.last_month_leaves);
            available_leaves = (TextView) findViewById(R.id.available_leaves);
            emp_lossofpay = (TextView) findViewById(R.id.emp_lossofpay);
            employee_detail_layout = (LinearLayout) findViewById(R.id.employee_detail_layout);
            employeeName = (TextView) findViewById(R.id.employeeName);
            close_emp_name = (ImageView) findViewById(R.id.close_emp_name);
            employee_cal_relative = (RelativeLayout) findViewById(R.id.employee_cal_relative);
            employee_cal_relative.setVisibility(View.GONE);
            underline_view = findViewById(R.id.underline_view);
            underline_view.setVisibility(View.VISIBLE);
            reset_data = (ImageView) findViewById(R.id.reset_data);
            reset_data.setVisibility(View.VISIBLE);
            notification = (RelativeLayout) findViewById(R.id.notification);
            notification_count = (TextView) findViewById(R.id.notification_count);
            buttonsLayout = findViewById(R.id.buttonsLayout);
            buttonsLayout.setVisibility(View.GONE);

            linearLayoutManager = new LinearLayoutManager(Employee_SelfAttendence.this);
            timeAdapter = new TroopTimeAdapter(this, attendence_arrayList, "self", "employee");
            timeAdapter.setTheTabPosition(filter_type);
            //linearLayoutManager.setReverseLayout(true);
            emp_list_recyclerview.setLayoutManager(linearLayoutManager);
            emp_list_recyclerview.setAdapter(timeAdapter);

            //loggin_name           = (TextView) findViewById(R.id.loggin_name);
            //more_information      = (TextView) findViewById(R.id.more_information);
            //more_information.setVisibility(View.VISIBLE);



        /*horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .startDate(startDate.getTime())
                .endDate(endDate.getTime())
                .datesNumberOnScreen(7)
                .dayFormat("EEE")
                .dayNumberFormat("dd")
                .centerToday(true)
                .build();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.notification:
                    notification_num = 0;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("notification_count", notification_num);
                    editor.apply();
                    notification_count.setText("" + notification_num);
                    break;
                case R.id.reset_data:
                    employee_detail_layout.setVisibility(View.GONE);
                    close_emp = true;
                    teamtext_selected = true;
                    getTeamAttendanceData();
                    break;
                case R.id.name_of_month:
                    final Calendar pic_calendar = Calendar.getInstance();
                    final int pic_year = pic_calendar.get(Calendar.YEAR);
                    final int pic_month = pic_calendar.get(Calendar.MONTH);
                    int pic_day = pic_calendar.get(Calendar.DAY_OF_MONTH);


                /*DatePickerFragment datePickerFragment  = new DatePickerFragment();
                datePickerFragment.show(getFragmentManager(),"my date");*/

                    //createDialogWithoutDateField(pic_year,pic_month,pic_day).show();


                    pic_dialog = new DatePickerDialog(Employee_SelfAttendence.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int pi_year, int day_month, int dayOfMonth) {
                            if (view.isShown()) {
                                if ((day_month + 1) < 10) {
                                    String mon = 0 + "" + (day_month + 1);
                                    start_date = "" + String.valueOf(pi_year) + "-" + mon + "-" + 01;
                                } else {
                                    start_date = "" + String.valueOf(pi_year) + "-" + String.valueOf(day_month + 1) + "-" + 01;
                                }

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(pi_year, day_month, dayOfMonth);

                                int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

                                if ((day_month + 1) < 10) {
                                    String mon = "0" + "" + (day_month + 1);
                                    end_date = "" + String.valueOf(pi_year) + "-" + mon + "-" + days;
                                } else {
                                    end_date = "" + String.valueOf(pi_year) + "-" + String.valueOf(day_month + 1) + "-" + days;
                                }
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
                                if (teamtext_selected) {
                                    setToNameOfMonth(month, year);
                                    Map<String, String> data = new HashMap<String, String>();

                                    if (close_emp) {
                                        if (filter_type == Constants.Tabclick.TEAM) {
                                            getTheTroopTeamData(userId, select_day, true);
                                        } else {
                                            getTheTroopTeamData(userId, select_day, false);
                                        }
                                    } else {
                                        getTheTroopTimeData(employee_usrId, start_date, end_date);
                                    }
                                } else {
                                    setToNameOfMonth(month, year);
                                    Map<String, String> data = new HashMap<String, String>();
                                    if (close_emp) {
                                        if (filter_type == Constants.Tabclick.TEAM) {
                                            getTheTroopTeamData(userId, select_day, true);
                                        } else {
                                            getTheTroopTeamData(userId, select_day, false);
                                        }
                                    } else {
                                        getTheTroopTimeData(employee_usrId, start_date, end_date);
                                    }


                                    //getSelefAttendenceData();
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

                    break;
            /*case R.id.more_information:
                break;*/
                case R.id.close_emp_name:
                    employee_detail_layout.setVisibility(View.GONE);

                    close_emp = true;
                    teamtext_selected = true;
                    //if (filter_type==Constants.Tabclick.TEAM)
                    //getTeamAttendanceData();
                    onBackPressed();
                    break;
                case R.id.text_self:
                    //button_switch.setChecked(false);
                    //self_leave_layout.setVisibility(View.VISIBLE);
                    teamtext_selected = false;
                    text_self.setBackgroundColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_team_selected));
                    text_self.setTextColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_sleceted));

                    text_team.setBackgroundColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_team_notselected));
                    text_team.setTextColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_notselected));

                    Intent main_intent = new Intent(Employee_SelfAttendence.this, AttendanceActivity.class);
                    main_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    main_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    main_intent.putExtra("type", "2");
                    startActivity(main_intent);
                    finish();


                    //getSelefAttendenceData();
                    break;
                case R.id.text_team:

                    employee_detail_layout.setVisibility(View.GONE);
                    teamtext_selected = true;
                    text_team.setBackgroundColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_team_selected));
                    text_team.setTextColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_sleceted));

                    text_self.setBackgroundColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_team_notselected));
                    text_self.setTextColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_notselected));

                    getTeamAttendanceData();
                    //button_switch.setChecked(true);
                    break;
                case R.id.todo_task:
                    Intent intent1 = new Intent(Employee_SelfAttendence.this, AttendanceActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.putExtra("type", "2");
                    startActivity(intent1);

                    finish();
                    break;
                case R.id.image_request:
                    Intent filter_intent = new Intent(Employee_SelfAttendence.this, AttendanceFilterActivity.class);
                    startActivity(filter_intent);

                    break;
                case R.id.nav_drawer:
                    onBackPressed();
                    break;
                case R.id.navigation_image:
                    onBackPressed();
                    break;
                case R.id.logout:
                    Navigation.getInstance().login(this);

                    break;
                case R.id.previous_month:
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
                        if (clciked_date != null) {
                            String[] split_date = clciked_date.split("-");
                            day = split_date[2];
                            if (month < 10) {
                                String mon = "0" + "" + month;
                                clciked_date = year + "-" + mon + "-" + day;
                            } else {
                                clciked_date = year + "-" + month + "-" + day;
                            }
                            selected_position = Integer.parseInt(day) - 1;
                        } else {
                            clciked_date = cur_date;
                            selected_position = Integer.parseInt(days) - 1;
                        }
                        if (Utilities.getConnectivityStatus(Employee_SelfAttendence.this) > 0) {
                            if (filter_type == Constants.Tabclick.TEAM) {
                                getTheTroopTeamData(employee_usrId, clciked_date, true);
                            } else {
                                getTheTroopTeamData(employee_usrId, clciked_date, false);
                            }
                        } else {
                            Utilities.ShowSnackbar(Employee_SelfAttendence.this, emp_list_recyclerview, "Please Check Network Connection");
                        }
                    } else {

                        year--;
                        month_name.setText(" " + year + " ");
                        selfisOnedCalenderView(year);
                        Calendar calendar = Calendar.getInstance();
                        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        String mont = new SimpleDateFormat("MM").format(calendar.getTime());

                        String mon = "", day = "", s_date = "", e_date = "", e_mon = "", e_day = "";
                        Map<String, String> data = new HashMap<>();
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
                        //isNetAvailable = isNetWorkAvailable();
                        if (Utilities.getConnectivityStatus(Employee_SelfAttendence.this) > 0) {
                            getTheTroopTimeData(employee_usrId, s_date, e_date);
                        } else {
                            Utilities.ShowSnackbar(Employee_SelfAttendence.this, emp_list_recyclerview, "Please Check Network Connection");
                        }
                    }
                    break;
                case R.id.next_month:
                    if (text_team.isSelected()) {
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
                        if (clciked_date != null) {
                            String[] split_date = clciked_date.split("-");
                            day = split_date[2];
                            if (month < 10) {
                                String mon = "0" + "" + month;
                                clciked_date = year + "-" + mon + "-" + day;
                            } else {
                                clciked_date = year + "-" + month + "-" + day;
                            }
                            selected_position = Integer.parseInt(day) - 1;
                        } else {
                            clciked_date = cur_date;
                            selected_position = Integer.parseInt(days) - 1;
                        }
                        if (Utilities.getConnectivityStatus(Employee_SelfAttendence.this) > 0) {
                            if (filter_type == Constants.Tabclick.TEAM) {
                                getTheTroopTeamData(userId, clciked_date, true);
                            } else {
                                getTheTroopTeamData(userId, clciked_date, false);
                            }
                        } else {
                            Utilities.ShowSnackbar(Employee_SelfAttendence.this, emp_list_recyclerview, "Please Check Network Connection");
                        }

                    } else {
                        year++;
                        name_of_month.setText(" " + year + " ");
                        selfisOnedCalenderView(year);

                        Calendar calendar = Calendar.getInstance();
                        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                        String mont = new SimpleDateFormat("MM").format(calendar.getTime());

                        String mon = "", day = "", s_date = "", e_date = "", e_mon = "", e_day = "";
                        Map<String, String> data = new HashMap<>();
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
                        if (Utilities.getConnectivityStatus(Employee_SelfAttendence.this) > 0) {
                            getTheTroopTimeData(employee_usrId, s_date, e_date);
                        } else {
                            Utilities.ShowSnackbar(Employee_SelfAttendence.this, emp_list_recyclerview, "Please Check Network Connection");
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setToNameOfMonth(int month, int year) {
        try {
            if (month == 1) {
                name_of_month.setText("jan, " + year);
                month_name.setText("" + this.year);
            } else if (month == 2) {
                name_of_month.setText("feb, " + year);
                month_name.setText("" + year);
            } else if (month == 3) {
                name_of_month.setText("mar, " + year);
                month_name.setText("" + year);
            } else if (month == 4) {
                name_of_month.setText("apr, " + year);
                month_name.setText("" + year);
            } else if (month == 5) {
                name_of_month.setText("may, " + year);
                month_name.setText("" + year);
            } else if (month == 6) {
                name_of_month.setText("jun, " + year);
                month_name.setText("" + year);
            } else if (month == 7) {
                name_of_month.setText("jul, " + year);
                month_name.setText("" + year);
            } else if (month == 8) {
                name_of_month.setText("aug, " + year);
                month_name.setText("" + year);
            } else if (month == 9) {
                name_of_month.setText("sep, " + year);
                month_name.setText("" + year);
            } else if (month == 10) {
                name_of_month.setText("oct, " + year);
                month_name.setText("" + year);
            } else if (month == 11) {
                name_of_month.setText("nov, " + year);
                month_name.setText("" + year);
            } else if (month == 12) {
                name_of_month.setText("dec, " + year);
                month_name.setText("" + year);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTeamAttendanceData() {
        try {
            setToNameOfMonth(current_month, this.current_year);

            setGridCellAdapterToDate(month, year);
            Calendar calendar = Calendar.getInstance();
            String days = new SimpleDateFormat("dd").format(calendar.getTime());
            String cur_date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            String day = "";

            String[] split_date = cur_date.split("-");
            String year_d = split_date[0];
            int month_d = Integer.parseInt(split_date[1]);
            int day_d = Integer.parseInt(split_date[2]);

            if (month_d < 10) {
                String mon = "0" + month_d;
                if (day_d < 10) {
                    String day_a = "0" + day_d;
                    clciked_date = year_d + "-" + mon + "-" + day_a;
                    selected_position = day_d - 1;
                } else {
                    clciked_date = year_d + "-" + mon + "-" + String.valueOf(day_d);
                    selected_position = day_d - 1;
                }
            } else {
                if (day_d < 10) {
                    String day_a = "0" + day_d;
                    clciked_date = year_d + "-" + String.valueOf(month_d) + "-" + day_a;
                    selected_position = day_d - 1;
                } else {
                    clciked_date = year_d + "-" + String.valueOf(month_d) + "-" + String.valueOf(day_d);
                    selected_position = day_d - 1;
                }
            }
            if (Utilities.getConnectivityStatus(Employee_SelfAttendence.this) > 0) {
                if (filter_type == Constants.Tabclick.TEAM) {
                    getTheTroopTeamData(userId, clciked_date, true);
                } else {
                    getTheTroopTeamData(userId, clciked_date, false);
                }
            } else {
                Utilities.ShowSnackbar(Employee_SelfAttendence.this, emp_list_recyclerview, "Please Check Network Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        try {
            if (isChecked) {
                //loggin_name.setVisibility(View.GONE);
                name_of_month.setVisibility(View.VISIBLE);
                _calendar = Calendar.getInstance(Locale.getDefault());
                month = _calendar.get(Calendar.MONTH) + 1;
                year = _calendar.get(Calendar.YEAR);
                setToNameOfMonth(month, year);
                adapter = new GridCellAdapter(Employee_SelfAttendence.this, month, year, "team");
                adapter.notifyDataSetChanged();
                LinearLayoutManager manager = new LinearLayoutManager(Employee_SelfAttendence.this, LinearLayoutManager.HORIZONTAL, false);
                month_calendar.setLayoutManager(manager);
                month_calendar.setAdapter(adapter);
                //isNetAvailable = isNetWorkAvailable();
                if (Utilities.getConnectivityStatus(Employee_SelfAttendence.this) > 0) {
                    text_team.setTextColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.filter_bg_color));
                    text_self.setTextColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_color_team));
                    team_data_arrayList = new ArrayList<>();
                    if (filter_type == Constants.Tabclick.TEAM) {
                        getTheTroopTeamData(userId, "", true);
                    } else {
                        getTheTroopTeamData(userId, "", false);
                    }

                } else {
                    Toast.makeText(Employee_SelfAttendence.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }
            } else {
                //loggin_name.setVisibility(View.VISIBLE);
                //loggin_name.setText(employee_userName);
                name_of_month.setVisibility(View.GONE);
                text_team.setTextColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_color_team));
                text_self.setTextColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.filter_bg_color));
                //isNetAvailable = isNetWorkAvailable();
                selfisOnedCalenderView(year);
                if (Utilities.getConnectivityStatus(Employee_SelfAttendence.this) > 0) {
                    Map<String, String> data = new HashMap();
                    getTheTroopTimeData(employee_usrId, start_mon, end_dat);
                } else {
                    Toast.makeText(Employee_SelfAttendence.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getTheTroopTeamData(String userId, String date, boolean is_team) {
        try {
            try {
                clciked_date = date;
                swipe_refresh.setRefreshing(true);
                swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_purple, android.R.color.holo_orange_light, android.R.color.holo_red_light);
                if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                    attendence_arrayList.removeAll(attendence_arrayList);
                    if (timeAdapter != null) {
                        timeAdapter.notifyDataSetChanged();
                    }
                }
                attendence_arrayList = new ArrayList<>();
                if (is_team) {
                    callToserverToGetThedata(userId, date);
                } else {
                    callToserverToGetAllThedata(userId, date);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callToserverToGetAllThedata(String userId, String date) {
        try {
            retrofit2.Call<AllAttendenceApiResponce> call = ApiClient.getInstance()
                    .getAllAttendence(userId, sharedPreferences.getString(SharePreferenceKeys.API_KEY, ""), date);
            call.enqueue(new retrofit2.Callback<AllAttendenceApiResponce>() {
                @Override
                public void onResponse(@NonNull Call<AllAttendenceApiResponce> call, @NonNull Response<AllAttendenceApiResponce> response) {
                    if (response.code() == Constants.RESPONCE_SUCCESSFUL) {
                        AllAttendenceApiResponce attendenceApiResponce = response.body();
                        if (attendenceApiResponce != null) {
                            if (attendenceApiResponce.getSuccess()) {
                                if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                                    attendence_arrayList.clear();
                                }
                                attendence_arrayList.addAll(attendenceApiResponce.getAttendance());
                                addTheDataToAdapter("team");
                            }
                        } else {
                            swipe_refresh.setRefreshing(false);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AllAttendenceApiResponce> call, @NonNull Throwable t) {
                    swipe_refresh.setRefreshing(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callToserverToGetThedata(String userId, String date) {
        try {
            retrofit2.Call<TeamAttendenceApiResponce> call = ApiClient.getInstance()
                    .getTeamfAttendence(userId, sharedPreferences.getString(SharePreferenceKeys.API_KEY, ""), date);
            call.enqueue(new retrofit2.Callback<TeamAttendenceApiResponce>() {
                @Override
                public void onResponse(@NonNull Call<TeamAttendenceApiResponce> call, @NonNull Response<TeamAttendenceApiResponce> response) {
                    if (response.code() == Constants.RESPONCE_SUCCESSFUL) {
                        TeamAttendenceApiResponce attendenceApiResponce = response.body();
                        if (attendenceApiResponce != null) {
                            if (attendenceApiResponce.getSuccess()) {
                                if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                                    attendence_arrayList.clear();
                                }
                                attendence_arrayList.addAll(attendenceApiResponce.getAttendance());
                                addTheDataToAdapter("team");
                            }
                        } else {
                            swipe_refresh.setRefreshing(false);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TeamAttendenceApiResponce> call, @NonNull Throwable t) {
                    swipe_refresh.setRefreshing(false);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTheDataToAdapter(String self) {
        try {
            int day = 0;
            swipe_refresh.setRefreshing(false);
            if (clciked_date != null && !clciked_date.isEmpty()) {
                String[] split = clciked_date.split("-");
                String date = split[2];
                day = Integer.parseInt(date);
            }
            if (self.equals("self")) {
                if (year < this.current_year) {
                    next_month.setClickable(true);
                    next_month.setTextColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.next_bt_color));
                    Drawable img = ContextCompat.getDrawable(Employee_SelfAttendence.this, R.drawable.arrow_image);
                    next_month.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                } else {
                    next_month.setClickable(false);
                    Drawable img = ContextCompat.getDrawable(Employee_SelfAttendence.this, R.drawable.light_col_arrow_imag);
                    next_month.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    next_month.setTextColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_color_team));
                }
            } else if (self.equals("team")) {
                Collections.sort(attendence_arrayList, new Comparator<Attendance>() {
                    @Override
                    public int compare(Attendance o1, Attendance o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                if (month < current_month || year < this.current_year) {
                    next_month.setClickable(true);
                    Drawable img = ContextCompat.getDrawable(Employee_SelfAttendence.this, R.drawable.arrow_image);
                    next_month.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    next_month.setTextColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.next_bt_color));
                } else {
                    next_month.setClickable(false);
                    Drawable img = ContextCompat.getDrawable(Employee_SelfAttendence.this, R.drawable.light_col_arrow_imag);
                    next_month.setCompoundDrawablesWithIntrinsicBounds(null, null, img, null);
                    next_month.setTextColor(ContextCompat.getColor(Employee_SelfAttendence.this, R.color.text_color_team));
                }
            }
            if (attendence_arrayList != null && attendence_arrayList.size() > 0) {
                text_nodata.setVisibility(View.GONE);
                emp_list_recyclerview.setVisibility(View.VISIBLE);

                if (timeAdapter != null) {
                    timeAdapter.setTheTabPosition(filter_type);
                    timeAdapter.setarrayList(attendence_arrayList);
                    timeAdapter.setAdapterType(self);
                    timeAdapter.notifyDataSetChanged();
                }
            } else {
                text_nodata.setVisibility(View.VISIBLE);
                emp_list_recyclerview.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {

        try {
            swipe_refresh.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
            String user_id = "";
            if (Utilities.getConnectivityStatus(Employee_SelfAttendence.this) > 0) {
                if (teamtext_selected) {

                    if (close_emp) {
                        user_id = userId;
                    } else {
                        user_id = employee_usrId;
                    }
                    Calendar calendar = Calendar.getInstance();
                    String days = new SimpleDateFormat("dd").format(calendar.getTime());
                    String cur_date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                    String day = "";
                    if (select_day != null && !select_day.isEmpty()) {
                        clciked_date = select_day;
                        select_day = "";
                        start_date = "";
                        end_date = "";
                    } else if (clciked_date != null) {
                        String[] split_date = clciked_date.split("-");
                        day = split_date[2];
                        if (month < 10) {
                            String mo = "0" + "" + month;
                            clciked_date = year + "-" + mo + "-" + day;
                        } else {
                            clciked_date = year + "-" + month + "-" + day;
                        }
                        selected_position = Integer.parseInt(day) - 1;

                    } else {
                        clciked_date = cur_date;
                        selected_position = Integer.parseInt(days) - 1;
                    }

                    try {
                        SimpleDateFormat current_dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String[] spliting_date = clciked_date.split("-");
                        int day1 = Integer.parseInt(spliting_date[2]);
                        int month1 = Integer.parseInt(spliting_date[1]);
                        int year1 = Integer.parseInt(spliting_date[0]);
                        Calendar calendar_s = Calendar.getInstance();
                        //calendar_s.set(year1, (month1 - 1), day1);
                        String serverDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar_s.getTime());
                        Date server_date = current_dateFormat.parse(clciked_date);
                        Date cuurent_date = current_dateFormat.parse(serverDate);
                        if (server_date.after(cuurent_date)) {
                            swipe_refresh.setRefreshing(false);
                            text_nodata.setVisibility(View.VISIBLE);
                            emp_list_recyclerview.setVisibility(View.GONE);
                        } else {
                            if (filter_type == Constants.Tabclick.TEAM) {
                                getTheTroopTeamData(user_id, clciked_date, true);
                            } else {
                                getTheTroopTeamData(user_id, clciked_date, false);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    if (close_emp) {
                        user_id = userId;
                    } else {
                        user_id = employee_usrId;
                    }
                    month_name.setText(" " + year + " ");
                    selfisOnedCalenderView(year);
                    Calendar calendar = Calendar.getInstance();
                    int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                    String mont = new SimpleDateFormat("MM").format(calendar.getTime());

                    String mon = "", day = "", s_date = "", e_date = "", e_mon = "", e_day = "";

                    if (start_date != null && end_date != null) {
                        start_mon = start_date;
                        end_dat = end_date;
                        select_day = "";
                        start_date = "";
                        end_date = "";
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
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        String today_date = new SimpleDateFormat("yyyy-MM-dd").format(to_day.getTime());
                        Date todays_date = dateFormat.parse(today_date);
                        Date sending_date = dateFormat.parse(start_mon);

                        if (sending_date.after(todays_date)) {
                            swipe_refresh.setRefreshing(false);
                            text_nodata.setVisibility(View.VISIBLE);
                            emp_list_recyclerview.setVisibility(View.GONE);
                        } else {
                            getTheTroopTimeData(user_id, start_mon, end_dat);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                swipe_refresh.setRefreshing(false);
                Utilities.ShowSnackbar(Employee_SelfAttendence.this, emp_list_recyclerview, "Please Check Network Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class GridCellAdapter extends RecyclerView.Adapter<GridCellAdapter.MyHolder> implements View.OnClickListener {

        private static final String tag = "GridCellAdapter";
        private static final int DAY_OFFSET = 1;
        private final Context _context;
        private final List<String> list;
        private final String[] weekdays = new String[]{"Sun", "Mon", "Tue",
                "Wed", "Thu", "Fri", "Sat"};
        private final String[] months = {"January", "February", "March",
                "April", "May", "June", "July", "August", "September",
                "October", "November", "December"};
        private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30,
                31, 30, 31};
        private final HashMap<String, Integer> eventsPerMonthMap;
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(
                "dd-MMM-yyyy");
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
            return months[i];
        }

        private String getWeekDayAsString(int i) {
            return weekdays[i];
        }

        private int getNumberOfDaysOfMonth(int i) {
            return daysOfMonth[i];
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
                    list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i)
                            + "-GREY"
                            + "-"
                            + getMonthAsString(prevMonth)
                            + "-"
                            + prevYear);
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

        /**
         * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
         * ALL entries from a SQLite database for that month. Iterate over the
         * List of All entries, and get the dateCreated, which is converted into
         * day.
         *
         * @param year
         * @param month
         * @return
         */
        private HashMap<String, Integer> findNumberOfEventsPerMonth(int year,
                                                                    int month) {
            HashMap<String, Integer> map = new HashMap<String, Integer>();

            return map;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.month_view, parent, false);

            // Get a reference to the Day gridcell

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
                    month_calendar.getLayoutManager().scrollToPosition(selected_position + 4);
                    if (selected_position == position) {
                        if (dayname.equals("Sat")) {
                            holder.day_date.setTextColor(ContextCompat.getColor(_context, R.color.sat_sun_col));
                        } else if (dayname.equals("Sun")) {

                            holder.day_date.setTextColor(ContextCompat.getColor(_context, R.color.sat_sun_col));
                        } else {
                            holder.day_date.setTextColor(ContextCompat.getColor(_context, R.color.colorAccent));
                            holder.day_name.setTextColor(ContextCompat.getColor(_context, R.color.select_text));
                        }
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            holder.day_date.setText(Html.fromHtml("<u>" + model.getDay() + " </u>", Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            holder.day_date.setText(Html.fromHtml("<u>" + model.getDay() + "  </u>"));
                        }
                    } else {
                        if (dayname.equals("Sat")) {
                            holder.day_date.setTextColor(ContextCompat.getColor(_context, R.color.sat_sun_col));
                        } else if (dayname.equals("Sun")) {

                            holder.day_date.setTextColor(ContextCompat.getColor(_context, R.color.sat_sun_col));
                        } else {
                            holder.day_name.setTextColor(ContextCompat.getColor(_context, R.color.text_color_team));
                            holder.day_date.setTextColor(ContextCompat.getColor(_context, R.color.text_color_team));
                        }
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            holder.day_date.setText(Html.fromHtml(model.getDay(), Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            holder.day_date.setText(Html.fromHtml(model.getDay()));
                        }
                    }
                    holder.month_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            notifyItemChanged(selected_position);
                            selected_position = position;
                            notifyItemChanged(selected_position);
                            month_calendar.getLayoutManager().scrollToPosition(selected_position + 4);

                            CalenderModel model1 = calenderModels.get(position);
                            String select_date = model1.getDate();

                            Date date = new Date(select_date);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            clciked_date = simpleDateFormat.format(date);
                            if (Utilities.getConnectivityStatus(Employee_SelfAttendence.this) > 0) {
                                if (filter_type == Constants.Tabclick.TEAM) {
                                    getTheTroopTeamData(userId, clciked_date, true);
                                } else {
                                    getTheTroopTeamData(userId, clciked_date, false);
                                }
                            } else {
                                Utilities.ShowSnackbar(Employee_SelfAttendence.this, emp_list_recyclerview, "Please Check Network Connection");
                            }
                        }
                    });
                } else if (which_one.equals("self")) {
                    final String month_name = model.getMonth_Name();
                    holder.day_name.setText(month_name);
                    holder.day_date.setVisibility(View.GONE);
                    month_calendar.getLayoutManager().scrollToPosition(month_position + 4);
                    if (month_position == position) {
                        // Here I am just highlighting the background
                        holder.day_name.setTextColor(ContextCompat.getColor(_context, R.color.colorAccent));
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            holder.day_name.setText(Html.fromHtml("<u>" + model.getMonth_Name() + " </u>", Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            holder.day_name.setText(Html.fromHtml("<u>" + model.getMonth_Name() + "  </u>"));
                        }
                    } else {
                        holder.day_name.setTextColor(ContextCompat.getColor(_context, R.color.text_color_team));
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            holder.day_name.setText(Html.fromHtml(model.getMonth_Name(), Html.FROM_HTML_MODE_LEGACY));
                        } else {
                            holder.day_name.setText(Html.fromHtml(model.getMonth_Name()));
                        }
                    }
                    holder.month_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            notifyItemChanged(month_position);
                            month_position = position;
                            notifyItemChanged(month_position);
                            CalenderModel model1 = calenderModels.get(position);
                            month_calendar.getLayoutManager().scrollToPosition(month_position + 4);

                            start_mon = model1.getMonthStartDate();
                            end_dat = model1.getMonthEndDate();
                            if (Utilities.getConnectivityStatus(Employee_SelfAttendence.this) > 0) {
                                getTheTroopTimeData(employee_usrId, start_mon, end_dat);
                            } else {
                                Utilities.ShowSnackbar(Employee_SelfAttendence.this, emp_list_recyclerview, "Please Check Network Connection");
                            }
                        }
                    });
                }
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

        public class MyHolder extends RecyclerView.ViewHolder {
            TextView day_name, day_date, month_name;
            LinearLayout month_layout;

            public MyHolder(View itemView) {
                super(itemView);
                day_date = (TextView) itemView.findViewById(R.id.day_date);
                day_name = (TextView) itemView.findViewById(R.id.day_name);
                month_layout = (LinearLayout) itemView.findViewById(R.id.month_layout);

            }
        }
    }
}
