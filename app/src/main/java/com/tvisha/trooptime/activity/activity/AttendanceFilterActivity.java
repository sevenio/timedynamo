package com.tvisha.trooptime.activity.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.tvisha.trooptime.activity.activity.ApiPostModels.Attendance;
import com.tvisha.trooptime.activity.activity.ApiPostModels.CcEmpResponse;
import com.tvisha.trooptime.activity.activity.ApiPostModels.EmployeeDetails;
import com.tvisha.trooptime.activity.activity.ApiPostModels.FilterAllAttendanceApi;
import com.tvisha.trooptime.activity.activity.ApiPostModels.FilterAttendanceApi;
import com.tvisha.trooptime.activity.activity.ApiPostModels.SelfAttendenceApi;
import com.tvisha.trooptime.activity.activity.Dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.Helper.Constants;
import com.tvisha.trooptime.activity.activity.Helper.Helper;
import com.tvisha.trooptime.activity.activity.Helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.Helper.Utilities;
import com.tvisha.trooptime.activity.activity.Model.EmployeeModel;
import com.tvisha.trooptime.activity.activity.Model.TimeAndAttendenceModel;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.OnClickListener;
import static android.view.View.OnTouchListener;
import static android.view.View.VISIBLE;

public class AttendanceFilterActivity extends Activity implements OnClickListener, AdapterView.OnItemSelectedListener,
        AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener, OnTouchListener {
    ImageView back_button, noDataImage;
    TextView cler_button, date_text, employee_names, to_date_text, custom_date;
    Button tv_search;
    Dialog popup_dialog = null;

    LinearLayout employees, customLayout, monthLayout, customFromToLayout, parent_of_textview;
    ;
    Spinner filter_work;
    String userId, email, apiKey, break_time = "", work_time = "", remarks = "", emp_user_id, select_date, get_date, mul_emp_ids = "",
            userName = "", fromDate = "", toDate = "";
    ;

    TextView lastThirtyDays, thisMonth, lastMonth;
    RelativeLayout date_picker, to_date_picker, custom_date_picker;
    int filte_working_time = 0, year, month, day;
    ;

    List<EmployeeModel> employeeModels;
    List<EmployeeModel> employeeModels1 = new ArrayList<>();
    List<Attendance> attendence_arrayList;
    List<String> employee_ids_array, employee_names_array, names_array;
    List<String> selected_string = new ArrayList<>();
    List<String> selected_userids = new ArrayList<>();


    TimeAndAttendenceModel model;
    Set<String> hasset;
    DatePickerDialog dialog;
    int pos = 0, filter_type = 2;
    SharedPreferences sharedPreferences;
    RadioGroup radio_group;
    RadioButton rb_latelogin, rb_early_logout;
    Calendar calendar;
    long timeInMills = 0;
    boolean teamLead = false, self = false, team = false, admin = false, isEmpApiCall = false;
    View workTimeLine, empLine, customDateLine;
    int attendance_filter = 0;
    EditText auto_serachview;
    CustomProgressBar customProgressBar;
    ApiInterface apiService;
    long lastCliked = 0;
    private long mLastClickTime = 0;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_layout);
        customProgressBar = new CustomProgressBar(AttendanceFilterActivity.this);
        sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        teamLead = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false);
        userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
        if (getIntent().getExtras() != null) {
            filter_type = getIntent().getExtras().getInt("filter_type");

        }
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        initializeWidgets();
        eventHandlings();

        shareprefernceData();
        if (self) {
            monthLayout.setVisibility(VISIBLE);
            customFromToLayout.setVisibility(VISIBLE);
            customLayout.setVisibility(GONE);
        } else {
            monthLayout.setVisibility(GONE);
            customFromToLayout.setVisibility(GONE);
            customLayout.setVisibility(VISIBLE);
        }
        dataAddtoAdapter();
        getTheTroopTimeEmpData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void getTheTroopTimeEmpData() {
        if (Utilities.getConnectivityStatus(AttendanceFilterActivity.this) > 0) {
            employeeModels = new ArrayList<>();
            employeeModels1 = new ArrayList<>();
            employee_ids_array = new ArrayList<>();
            employee_names_array = new ArrayList<>();
            names_array = new ArrayList<>();

            if (team) {
                workTimeLine.setVisibility(GONE);
                empLine.setVisibility(VISIBLE);
                customDateLine.setVisibility(VISIBLE);


            } else if (admin) {
                workTimeLine.setVisibility(GONE);
                empLine.setVisibility(VISIBLE);
                customDateLine.setVisibility(VISIBLE);

            } else {
                empLine.setVisibility(GONE);
                workTimeLine.setVisibility(VISIBLE);
                employees.setVisibility(GONE);
                customDateLine.setVisibility(GONE);
            }
        } else {
            Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, "Please Check Network Connection");
        }
    }

    private void callgetAllEmployeesApi() {


        Call<CcEmpResponse> call = apiService.getAllEmployess(apiKey, userId);
        call.enqueue(new Callback<CcEmpResponse>() {
            @Override
            public void onResponse(Call<CcEmpResponse> call, Response<CcEmpResponse> response) {
                CcEmpResponse apiResponse = response.body();
                closeProgress();

                if (apiResponse != null) {
                    if (apiResponse.isSuccess()) {

                        if (apiResponse.getEmployee_list() != null && apiResponse.getEmployee_list().size() > 0) {


                            List<EmployeeDetails> attendanceList = apiResponse.getEmployee_list();
                            if (attendanceList != null && attendanceList.size() > 0) {

                                if (employeeModels != null && employeeModels.size() > 0) {
                                    employeeModels.clear();
                                }
                                if (employeeModels1 != null && employeeModels1.size() > 0) {
                                    employeeModels1.clear();
                                }

                                for (int i = 0; i < attendanceList.size(); i++) {
                                    EmployeeModel employeeModel = new EmployeeModel();
                                    employeeModel.setEmployee_Id(attendanceList.get(i).getUser_id());
                                    employeeModel.setEmployee_name(attendanceList.get(i).getName());
                                    employeeModel.setChecked(false);
                                    employee_ids_array.add(attendanceList.get(i).getUser_id());
                                    employee_names_array.add(attendanceList.get(i).getName());
                                    names_array.add(attendanceList.get(i).getName());
                                    employeeModels.add(employeeModel);
                                    employeeModels1.add(employeeModel);
                                }
                                showPopup();
                            }
                        } else {
                            showPopup();
                        }


                    }



                }


            }

            @Override
            public void onFailure(Call<CcEmpResponse> call, Throwable t) {

                closeProgress();
            }

        });
    }

    private void callgetTeamEmployeesApi() {


        Call<CcEmpResponse> call = apiService.getTeamEmployess(apiKey, userId);
        call.enqueue(new Callback<CcEmpResponse>() {
            @Override
            public void onResponse(Call<CcEmpResponse> call, Response<CcEmpResponse> response) {
                CcEmpResponse apiResponse = response.body();
                closeProgress();

                if (apiResponse != null) {
                    if (apiResponse.isSuccess()) {

                        if (apiResponse.getEmployee_list() != null && apiResponse.getEmployee_list().size() > 0) {


                            List<EmployeeDetails> attendanceList = apiResponse.getEmployee_list();
                            if (attendanceList != null && attendanceList.size() > 0) {

                                if (employeeModels != null && employeeModels.size() > 0) {
                                    employeeModels.clear();
                                }
                                if (employeeModels1 != null && employeeModels1.size() > 0) {
                                    employeeModels1.clear();
                                }

                                for (int i = 0; i < attendanceList.size(); i++) {
                                    EmployeeModel employeeModel = new EmployeeModel();
                                    employeeModel.setEmployee_Id(attendanceList.get(i).getUser_id());
                                    employeeModel.setEmployee_name(attendanceList.get(i).getName());
                                    employeeModel.setChecked(false);
                                    employee_ids_array.add(attendanceList.get(i).getUser_id());
                                    employee_names_array.add(attendanceList.get(i).getName());
                                    names_array.add(attendanceList.get(i).getName());
                                    employeeModels.add(employeeModel);
                                    employeeModels1.add(employeeModel);
                                }
                                showPopup();
                            }
                        } else {
                            showPopup();
                        }


                    }
/*

                    {
                        showPopup();
                    }
*/


                }


            }

            @Override
            public void onFailure(Call<CcEmpResponse> call, Throwable t) {

                closeProgress();
            }

        });
    }

    private void shareprefernceData() {

        userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
        email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
        apiKey = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
        self = sharedPreferences.getBoolean(SharePreferenceKeys.SELF_CLICKED, false);
        team = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_CLICKED, false);
        admin = sharedPreferences.getBoolean(SharePreferenceKeys.ADMIN_CLICKED, false);
        userName = sharedPreferences.getString(SharePreferenceKeys.USER_NAME, "");

    }

    private void dataAddtoAdapter() {

        String[] work_time = getResources().getStringArray(R.array.work_time);
        ArrayAdapter<String> work_adapter = new ArrayAdapter<String>(AttendanceFilterActivity.this, R.layout.singlerow1, work_time);
        work_adapter.setDropDownViewResource(R.layout.singlerow1);
        filter_work.setAdapter(work_adapter);
    }

    private void eventHandlings() {
        back_button.setOnClickListener(this);
        cler_button.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        tv_search.setOnTouchListener(this);
        date_picker.setOnClickListener(this);
        employees.setOnClickListener(this);

        date_picker.setOnClickListener(this);
        to_date_picker.setOnClickListener(this);
        custom_date_picker.setOnClickListener(this);

        thisMonth.setOnClickListener(this);
        lastThirtyDays.setOnClickListener(this);
        lastMonth.setOnClickListener(this);
    }

    private void initializeWidgets() {
        back_button = (ImageView) findViewById(R.id.back_button);
        cler_button = (TextView) findViewById(R.id.cler_button);
        employees = findViewById(R.id.employees);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        rb_latelogin = (RadioButton) findViewById(R.id.rb_latelogin);
        rb_early_logout = (RadioButton) findViewById(R.id.rb_early_logout);

        radio_group.setOnCheckedChangeListener(this);
        /*filter_check_in = (Spinner) findViewById(R.id.filter_check_in);
        filter_check_out = (Spinner) findViewById(R.id.filter_check_out);*/
        filter_work = (Spinner) findViewById(R.id.filter_work);
        tv_search = (Button) findViewById(R.id.tv_search);
        date_picker = (RelativeLayout) findViewById(R.id.date_picker);
        date_text = (TextView) findViewById(R.id.date_text);
        employee_names = (TextView) findViewById(R.id.employee_names);
        workTimeLine = findViewById(R.id.workTimeLine);
        customDateLine = findViewById(R.id.customDateLine);
        empLine = findViewById(R.id.employeesLine);
        date_picker = (RelativeLayout) findViewById(R.id.date_picker);

        to_date_picker = (RelativeLayout) findViewById(R.id.to_date_picker);
        custom_date_picker = (RelativeLayout) findViewById(R.id.custom_date_picker);
        to_date_text = (TextView) findViewById(R.id.to_date_text);
        custom_date = (TextView) findViewById(R.id.custom_date);
        lastThirtyDays = (TextView) findViewById(R.id.lastThirtyDays);
        thisMonth = (TextView) findViewById(R.id.thisMonth);
        lastMonth = (TextView) findViewById(R.id.lastMonth);
        customLayout = findViewById(R.id.customLayout);
        monthLayout = findViewById(R.id.monthLayout);
        customFromToLayout = findViewById(R.id.customFromToLayout);


    }

    /*  if (SystemClock.elapsedRealtime() - mLastClickTime < 800) {
     return;
 }
 mLastClickTime = SystemClock.elapsedRealtime();*/
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.lastThirtyDays:
                lastThirtyDays();
                break;
            case R.id.thisMonth:
                thisMonth();
                break;
            case R.id.lastMonth:
                lastMonth();
                break;

            case R.id.date_picker:
                fromDate = "";
                if (to_date_text == null) {
                    toDate = "";
                }
                if (to_date_text != null && to_date_text.getText().toString().length() < 3) {
                    toDate = "";
                }

                clearDate();
                pickFromDate();
                break;
            case R.id.to_date_picker:
                if (date_text == null) {
                    fromDate = "";
                }
                if (date_text != null && date_text.getText().toString().length() < 3) {
                    fromDate = "";
                }
                toDate = "";
                clearDate();
                pickToDate();
                break;

            case R.id.custom_date_picker:
                fromDate = "";
                toDate = "";
                clearDate();
                pickCustomDate();

                break;
            case R.id.back_button:
                sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_CLICKED, true).apply();
                onBackPressed();
                break;
            case R.id.cler_button:

                resetForm();
                break;
            case R.id.tv_search:
                validateForm();
                break;
            case R.id.employees:


                if (SystemClock.elapsedRealtime() - lastCliked < 100) {
                    return;
                }
                lastCliked = SystemClock.elapsedRealtime();


                if (!isEmpApiCall) {
                    if (employeeModels != null && employeeModels.size() > 0) {
                        employeeModels.clear();
                    }
                    if (employeeModels1 != null && employeeModels1.size() > 0) {
                        employeeModels1.clear();
                    }


                    if (team) {
                        if (Utilities.isNetworkAvailable(AttendanceFilterActivity.this)) {


                            openProgress();
                            callgetTeamEmployeesApi();

                        } else {
                            Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, getResources().getString(R.string.no_internet_connection));

                        }
                    } else {
                        if (Utilities.isNetworkAvailable(AttendanceFilterActivity.this)) {

                            openProgress();
                            callgetAllEmployeesApi();
                        } else {
                            Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, getResources().getString(R.string.no_internet_connection));

                        }
                    }

                } else {
                    if (employeeModels != null && employeeModels.size() > 0) {
                        employeeModels.clear();
                    }
                    employeeModels.addAll(employeeModels1);

                    if (selected_userids != null && selected_userids.size() > 0 && employeeModels != null && employeeModels.size() > 0) {

                        try {
                            for (int i = 0; i < selected_userids.size(); i++) {
                                String name = selected_userids.get(i);
                                for (int e = 0; e < employeeModels.size(); e++) {
                                    if (name.trim().toLowerCase().equals(employeeModels.get(e).getEmployee_Id().trim().toLowerCase())) {
                                        employeeModels.get(e).setChecked(true);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    showPopup();
                }

                break;
        }
    }

    private void validateForm() {
        getStringResource();
        if (admin || team) {
            if (select_date != null && !select_date.trim().isEmpty()) {

                if (admin) {

                    tv_search.setClickable(false);
                    if (Utilities.isNetworkAvailable(AttendanceFilterActivity.this)) {
                        openProgress();
                        sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_TEAM_CLICKED, false).apply();
                        sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_ADMIN_CLICKED, true).apply();
                        sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_SELF_CLICKED, false).apply();
                        gotoTheAllAttendenceApi();

                    } else {

                        tv_search.setClickable(true);
                        Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, getResources().getString(R.string.no_internet_connection));

                    }

                } else if (team) {

                    if (Utilities.isNetworkAvailable(AttendanceFilterActivity.this)) {

                        tv_search.setClickable(false);
                        openProgress();
                        sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_TEAM_CLICKED, true).apply();
                        sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_ADMIN_CLICKED, false).apply();
                        sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_SELF_CLICKED, false).apply();
                        gotoTheTeamAttendenceApi();

                    } else {

                        tv_search.setClickable(true);
                        Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, getResources().getString(R.string.no_internet_connection));

                    }


                }


            } else {

                Utilities.ShowSnackbar(AttendanceFilterActivity.this, custom_date, "Please Select Date");
            }
        } else {
            if (fromDate.length() < 3) {
                Utilities.ShowSnackbar(AttendanceFilterActivity.this, date_text, "Please Select From Date");
            } else if (toDate.length() < 3) {
                Utilities.ShowSnackbar(AttendanceFilterActivity.this, to_date_text, "Please Select To Date");
            } else {
                if (Utilities.isNetworkAvailable(AttendanceFilterActivity.this)) {

                    tv_search.setClickable(false);
                    openProgress();
                    sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_SELF_CLICKED, true).apply();
                    sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_TEAM_CLICKED, false).apply();
                    sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_ADMIN_CLICKED, false).apply();
                    gotoTheSelfAttendenceApi();


                } else {

                    tv_search.setClickable(true);
                    Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, getResources().getString(R.string.no_internet_connection));

                }

            }
        }
    }

    private void showPopup() {
        isEmpApiCall = true;
        if (popup_dialog != null && popup_dialog.isShowing()) {
            return;
        }
        popup_dialog = new Dialog(AttendanceFilterActivity.this, android.R.style.Theme_Light);
        popup_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popup_dialog.setContentView(R.layout.popupemployee);

        auto_serachview = (EditText) popup_dialog.findViewById(R.id.auto_serachview);
        TextView done_button = (TextView) popup_dialog.findViewById(R.id.done_button);
        TextView check_cler_button = (TextView) popup_dialog.findViewById(R.id.check_cler_button);
        ImageView back_button = (ImageView) popup_dialog.findViewById(R.id.back_button);
        parent_of_textview = (LinearLayout) popup_dialog.findViewById(R.id.parent_of_textview);
        RecyclerView employees_list = (RecyclerView) popup_dialog.findViewById(R.id.employees_list);
        ImageView noDataImage = popup_dialog.findViewById(R.id.noDataImage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AttendanceFilterActivity.this);
        employees_list.setLayoutManager(linearLayoutManager);

        if (employeeModels == null || employeeModels.isEmpty() || employeeModels.size() < 0) {
            auto_serachview.setVisibility(View.GONE);
            noDataImage.setVisibility(VISIBLE);
            employees_list.setVisibility(GONE);
        } else {
            auto_serachview.setVisibility(View.VISIBLE);
            noDataImage.setVisibility(GONE);
            employees_list.setVisibility(VISIBLE);
        }

        final SearchAdapters adapter = new SearchAdapters(AttendanceFilterActivity.this, employeeModels);
        employees_list.setAdapter(adapter);


        back_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_CLICKED, true).apply();
                popup_dialog.dismiss();

            }
        });

        done_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Helper.getInstance().closeKeyBoard(AttendanceFilterActivity.this,auto_serachview);
                popup_dialog.dismiss();



                employee_names.setVisibility(VISIBLE);
                String strings = selected_string.toString().replace("[", "").replace("]", "").trim();
                if (strings != null && !strings.isEmpty()) {
                    employee_names.setText(strings + " ");
                } else {
                    employee_names.setVisibility(GONE);
                }


            }
        });
        check_cler_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (employeeModels != null && !employeeModels.isEmpty() && employeeModels.size() > 0) {
                    for (int i = 0; i < employeeModels.size(); i++) {
                        employeeModels.get(i).setChecked(false);
                    }
                }

                if (employeeModels1 != null && !employeeModels1.isEmpty() && employeeModels1.size() > 0) {
                    for (int i = 0; i < employeeModels1.size(); i++) {
                        employeeModels1.get(i).setChecked(false);
                    }
                }
                adapter.notifyDataSetChanged();
                employee_names.setText(null);
                selected_string.clear();
                selected_userids.clear();

                pos = 0;
                employee_names_array.removeAll(employee_names_array);
                employee_names_array.addAll(names_array);
                employee_names.setVisibility(GONE);

                if (auto_serachview != null) {
                    auto_serachview.setText("");
                }

                if (adapter != null) {
                    adapter.search("");
                }


            }
        });


        auto_serachview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 1) {
                    Pattern pattern = Pattern.compile("[a-zA-Z]");
                    Matcher matcher = pattern.matcher(charSequence.toString());
                    if (!matcher.find()) {
                        auto_serachview.setText("");
                    }
                }
                if (charSequence.toString().length() > 0) {
                    if (adapter != null) {
                        adapter.search(charSequence.toString());
                    }

                } else {
                    if (adapter != null) {
                        adapter.search("");
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        popup_dialog.show();
    }

    private void pickCustomDate() {

        dialog = new DatePickerDialog(AttendanceFilterActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String started_date = "" + String.valueOf(i) + "-" + String.valueOf(i1 + 1) + "-" + String.valueOf(i2);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                timeInMills = calendar.getTimeInMillis();
                try {
                    Date date = dateFormat.parse(started_date);
                    String select_date = dateFormat.format(date);
                    custom_date.setText(select_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, year, month, day);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        }
        dialog.show();
    }

    private void resetForm() {
        filter_work.setSelection(0);
        custom_date.setText(null);
        custom_date.setHint("yyyy-mm-dd");
        select_date = "";
        employee_names.setText(null);
        selected_string.clear();
        selected_userids.clear();

        pos = 0;
        employee_names_array.removeAll(employee_names_array);
        employee_names_array.addAll(names_array);
        employee_names.setVisibility(GONE);
        radio_group.clearCheck();
        date_text.setText(null);
        date_text.setHint("To");
        to_date_text.setText(null);
        to_date_text.setHint("To");
        fromDate = "";
        toDate = "";
        if (employeeModels != null && !employeeModels.isEmpty() && employeeModels.size() > 0) {
            for (int i = 0; i < employeeModels.size(); i++) {
                employeeModels.get(i).setChecked(false);
            }
        }

        clearDate();
    }

    private void gotoTheAllAttendenceApi() {


        try {

            if (selected_userids != null && selected_userids.size() > 0) {
                mul_emp_ids = selected_userids.toString().trim().replace(" ", "").trim();
            } else {
                mul_emp_ids = "";
            }


            if (rb_latelogin.isChecked()) {
                attendance_filter = 1;
            } else if (rb_early_logout.isChecked()) {
                attendance_filter = 2;
            } else {
                attendance_filter = 0;
            }
            attendence_arrayList = new ArrayList<>();

            if (Utilities.getConnectivityStatus(AttendanceFilterActivity.this) > 0) {
                try {

                    openProgress();
                    retrofit2.Call<FilterAllAttendanceApi.FilterAllAttendenceApiResponce> call = FilterAllAttendanceApi.getApiService()
                            .getFilterAllAttendence(userId, select_date, sharedPreferences.getString(SharePreferenceKeys.API_KEY, ""),
                                    mul_emp_ids.trim(), attendance_filter, filte_working_time);

                    call.enqueue(new Callback<FilterAllAttendanceApi.FilterAllAttendenceApiResponce>() {
                        @Override
                        public void onResponse(@NonNull Call<FilterAllAttendanceApi.FilterAllAttendenceApiResponce> call, @NonNull Response<FilterAllAttendanceApi.FilterAllAttendenceApiResponce> response) {
                            if (response.code() == Constants.RESPONCE_SUCCESSFUL) {
                                tv_search.setClickable(true);

                                closeProgress();
                                FilterAllAttendanceApi.FilterAllAttendenceApiResponce apiResponce = response.body();
                                if (apiResponce != null) {

                                    if (apiResponce.getSuccess()) {


                                        List<Attendance> attendanceList = apiResponce.getAttendance();
                                        attendence_arrayList.addAll(attendanceList);


                                        if (attendence_arrayList.size() > 0) {

                                            Intent intent = new Intent(AttendanceFilterActivity.this, AttendanceActivity.class);
                                            //progressDialog.dismiss();
                                            intent.putExtra("selected_date", select_date);
                                            String s = userName;

                                            if (s != null && !s.isEmpty()) {

                                                intent.putExtra("employee_name", s);
                                            }


                                            if (work_time != null && !work_time.isEmpty()) {

                                                intent.putExtra("work_time", work_time);
                                            } else {

                                                intent.putExtra("work_time", filter_work.getSelectedItem().toString());
                                            }

                                            intent.putExtra("employee_userid", mul_emp_ids);
                                            intent.putExtra("filte_working_time", String.valueOf(filte_working_time));
                                            intent.putExtra("attendance_filter", String.valueOf(attendance_filter));
                                            intent.putExtra("filter_type", filter_type);
                                            intent.putExtra("fromDate", fromDate);
                                            intent.putExtra("toDate", toDate);
                                            intent.putExtra("filterType", true);
                                            startActivity(intent);



                                        } else {


                                            Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, "No Data Found");
                                            radio_group.clearCheck();
                                            filter_work.setSelection(0);
                                        }
                                    } else {

                                        Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, "No Data Found");

                                    }
                                } else {


                                    Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, getResources().getString(R.string.somthing_went_wrong));
                                }
                            } else {

                                tv_search.setClickable(true);
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<FilterAllAttendanceApi.FilterAllAttendenceApiResponce> call, @NonNull Throwable t) {
                            closeProgress();

                            tv_search.setClickable(true);

                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();

                    tv_search.setClickable(true);
                }

            } else {

                tv_search.setClickable(true);
                Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, getResources().getString(R.string.network_check));
            }
        } catch (Exception e) {

            tv_search.setClickable(true);
            e.printStackTrace();
        }
    }


    private void gotoTheTeamAttendenceApi() {
        if (selected_userids != null && selected_userids.size() > 0) {
            mul_emp_ids = selected_userids.toString().trim().replace(" ", "").trim();
        } else {
            mul_emp_ids = "";
        }


        if (rb_latelogin.isChecked()) {
            attendance_filter = 1;
        } else if (rb_early_logout.isChecked()) {
            attendance_filter = 2;
        } else {
            attendance_filter = 0;
        }
        attendence_arrayList = new ArrayList<>();
        if (Utilities.getConnectivityStatus(AttendanceFilterActivity.this) > 0) {
            try {

                openProgress();

                retrofit2.Call<FilterAttendanceApi.FilterAttendenceApiResponce> call = FilterAttendanceApi.getApiService()
                        .getFilterAttendence(userId, select_date, sharedPreferences.getString(SharePreferenceKeys.API_KEY, ""),
                                mul_emp_ids.trim(), attendance_filter + "", filte_working_time + "");

                call.enqueue(new Callback<FilterAttendanceApi.FilterAttendenceApiResponce>() {
                    @Override
                    public void onResponse(@NonNull Call<FilterAttendanceApi.FilterAttendenceApiResponce> call, @NonNull Response<FilterAttendanceApi.FilterAttendenceApiResponce> response) {
                        if (response.code() == Constants.RESPONCE_SUCCESSFUL) {
                            tv_search.setClickable(true);

                            closeProgress();
                            FilterAttendanceApi.FilterAttendenceApiResponce apiResponce = response.body();
                            if (apiResponce != null) {
                                if (apiResponce.getSuccess()) {

                                    List<Attendance> attendanceList = apiResponce.getAttendance();
                                    attendence_arrayList.addAll(attendanceList);

                                    if (attendence_arrayList.size() > 0) {

                                        Intent intent = new Intent(AttendanceFilterActivity.this, AttendanceActivity.class);

                                        intent.putExtra("selected_date", select_date);
                                        String s = userName;

                                        if (s != null && !s.isEmpty()) {

                                            intent.putExtra("employee_name", s);
                                        }


                                        if (work_time != null && !work_time.isEmpty()) {

                                            intent.putExtra("work_time", work_time);
                                        } else {

                                            intent.putExtra("work_time", filter_work.getSelectedItem().toString());
                                        }

                                        String af;
                                        if (attendance_filter == 1) {
                                            af = "1";
                                        } else if (attendance_filter == 2) {
                                            af = "2";
                                        } else {
                                            af = "0";
                                        }

                                        intent.putExtra("employee_userid", mul_emp_ids);
                                        intent.putExtra("filte_working_time", String.valueOf(filte_working_time));
                                        intent.putExtra("attendance_filter", String.valueOf(attendance_filter));
                                        intent.putExtra("date", get_date);
                                        intent.putExtra("filter_type", filter_type);
                                        intent.putExtra("fromDate", fromDate);
                                        intent.putExtra("toDate", toDate);
                                        intent.putExtra("filterType", true);
                                        startActivity(intent);



                                    } else {
                                        Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, "No Data Found");

                                        radio_group.clearCheck();

                                        filter_work.setSelection(0);
                                    }
                                } else {

                                    Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, "No Data Found");

                                }
                            } else {


                                Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, getResources().getString(R.string.somthing_went_wrong));
                            }
                        } else {

                            tv_search.setClickable(true);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FilterAttendanceApi.FilterAttendenceApiResponce> call, @NonNull Throwable t) {
                        closeProgress();

                        tv_search.setClickable(true);

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                tv_search.setClickable(true);

            }

        } else {

            tv_search.setClickable(true);
            Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, getResources().getString(R.string.network_check));
        }
    }

    private void gotoTheSelfAttendenceApi() {
        mul_emp_ids = userId;

        if (rb_latelogin.isChecked()) {
            attendance_filter = 1;
        } else if (rb_early_logout.isChecked()) {
            attendance_filter = 2;
        } else {
            attendance_filter = 0;
        }
        attendence_arrayList = new ArrayList<>();
        if (Utilities.getConnectivityStatus(AttendanceFilterActivity.this) > 0) {
            try {
                openProgress();
                retrofit2.Call<SelfAttendenceApi.SelfAttendenceApiResponce> call =
                        SelfAttendenceApi.getApiService().getSelefAttendence(userId, sharedPreferences.getString(SharePreferenceKeys.API_KEY, ""),
                                fromDate, toDate, String.valueOf(attendance_filter), String.valueOf(filte_working_time));

                call.enqueue(new Callback<SelfAttendenceApi.SelfAttendenceApiResponce>() {
                    @Override
                    public void onResponse(@NonNull Call<SelfAttendenceApi.SelfAttendenceApiResponce> call, @NonNull Response<SelfAttendenceApi.SelfAttendenceApiResponce> response) {
                        if (response.code() == Constants.RESPONCE_SUCCESSFUL) {
                            tv_search.setClickable(true);


                            closeProgress();
                            SelfAttendenceApi.SelfAttendenceApiResponce apiResponce = response.body();
                            if (apiResponce != null) {
                                if (apiResponce.getSuccess()) {

                                    List<Attendance> attendanceList = apiResponce.getAttendance();
                                    attendence_arrayList.addAll(attendanceList);
                                    if (attendence_arrayList != null && attendence_arrayList.size() > 0) {

                                        Intent intent = new Intent(AttendanceFilterActivity.this, AttendanceActivity.class);
                                        intent.putExtra("selected_date", select_date);
                                        String s = userName;

                                        if (s != null && !s.isEmpty()) {
                                            intent.putExtra("employee_name", s);
                                        }
                                        intent.putExtra("employee_userid", "user");

                                        if (work_time != null && !work_time.isEmpty()) {
                                            intent.putExtra("work_time", work_time);
                                        } else {

                                            intent.putExtra("work_time", filter_work.getSelectedItem().toString());
                                        }


                                        intent.putExtra("filte_working_time", String.valueOf(filte_working_time));
                                        intent.putExtra("attendance_filter", String.valueOf(attendance_filter));
                                        intent.putExtra("date", get_date);
                                        intent.putExtra("filter_type", filter_type);
                                        intent.putExtra("fromDate", fromDate);
                                        intent.putExtra("toDate", toDate);
                                        intent.putExtra("filterType", true);
                                        startActivity(intent);



                                    } else {


                                        Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, "No Data Found");
                                        radio_group.clearCheck();
                                        filter_work.setSelection(0);
                                    }
                                } else {
                                    tv_search.setClickable(true);
                                    Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, "No Data Found");


                                }
                            } else {

                                tv_search.setClickable(true);


                                Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, getResources().getString(R.string.somthing_went_wrong));
                            }
                        } else {
                            tv_search.setClickable(true);

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SelfAttendenceApi.SelfAttendenceApiResponce> call, @NonNull Throwable t) {


                        tv_search.setClickable(true);

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                tv_search.setClickable(true);

            }

        } else {

            tv_search.setClickable(true);
            Utilities.ShowSnackbar(AttendanceFilterActivity.this, tv_search, getResources().getString(R.string.network_check));
        }
    }


    private void getStringResource() {
        hasset = new HashSet<>();
        select_date = custom_date.getText().toString();


        break_time = "";

        work_time = filter_work.getSelectedItem().toString().trim().toLowerCase();
        if (work_time.equals("< 8hrs")) {
            filte_working_time = Constants.WORKING_HOURS_LESS_THAN_8;
            work_time = "< 8";
        } else if (work_time.equals("> 8hrs")) {
            filte_working_time = Constants.WORKING_HOURS_MORE_THAN_8;
            work_time = "8-10";
        } else if (work_time.equals("any")) {
            filte_working_time = Constants.WORKING_HOURS_ANY;
            work_time = "";
        } else {
            filte_working_time = Constants.WORKING_HOURS_MORE_THAN_10;
            work_time = "more";
        }

        remarks = "";
        String employeename = employee_names.getText().toString();
        if (employeename != null && !employeename.isEmpty()) {
            String[] splits = employeename.trim().split(",");
            for (int i = 0; i < splits.length; i++) {
                String name = splits[i];
                for (int a = 0; a < names_array.size(); a++) {
                    String emp_name = names_array.get(a);
                    if (name.trim().toLowerCase().contains(emp_name.trim().toLowerCase())) {
                        emp_user_id = employee_ids_array.get(a);
                        hasset.add(emp_user_id);
                    }
                }
            }

        } else {
            emp_user_id = "";
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.filter_check_in:

                break;
            case R.id.filter_check_out:

                break;

            case R.id.filter_work:
                String work = filter_work.getSelectedItem().toString();
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String name = adapterView.getItemAtPosition(i).toString();
        for (int j = 0; j < employee_names_array.size(); j++) {
            String emp_name = employee_names_array.get(j);
            if (emp_name.trim().toLowerCase().equals(name.trim().toLowerCase())) {
                emp_user_id = employee_ids_array.get(j);
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //validateForm();
        return false;
    }

    private void lastThirtyDays() {
        date_text.setText(null);
        to_date_text.setText(null);
        // See here
        String tag = lastThirtyDays.getTag().toString();
        Integer integer = Integer.valueOf(tag);

        switch (integer) {
            case 0:


                lastThirtyDays.setBackgroundResource(R.drawable.req_filter_active_bg);
                lastThirtyDays.setTag("1");

                thisMonth.setBackgroundResource(R.drawable.request_title_bg);
                thisMonth.setTag("0");

                lastMonth.setBackgroundResource(R.drawable.request_title_bg);
                lastMonth.setTag("0");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date today = new Date();
                Calendar cal = new GregorianCalendar();
                cal.setTime(today);
                String currentDate = dateFormat.format(cal.getTime());

                toDate = currentDate;
                cal.add(Calendar.DAY_OF_MONTH, -30);

                String today30 = dateFormat.format(cal.getTime());

                fromDate = today30;


                break;
            case 1:

                lastThirtyDays.setBackgroundResource(R.drawable.request_title_bg);
                lastThirtyDays.setTag("0");
                fromDate = "";
                toDate = "";
                break;

            default:

                lastThirtyDays.setBackgroundResource(R.drawable.request_title_bg);
                lastThirtyDays.setTag("0");

                break;
        }
    }

    private void thisMonth() {
        // See here
        date_text.setText(null);
        to_date_text.setText(null);
        String tag = thisMonth.getTag().toString();
        Integer integer = Integer.valueOf(tag);

        switch (integer) {
            case 0:

                thisMonth.setBackgroundResource(R.drawable.req_filter_active_bg);
                thisMonth.setTag("1");

                lastThirtyDays.setBackgroundResource(R.drawable.request_title_bg);
                lastThirtyDays.setTag("0");

                lastMonth.setBackgroundResource(R.drawable.request_title_bg);
                lastMonth.setTag("0");

                Date today = new Date();
                Calendar cal = new GregorianCalendar();
                cal.setTime(today);
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String thismonthFirst = dateFormat.format(cal.getTime());
                // set actual maximum date of previous month
                cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
                String thismonthLast = dateFormat.format(cal.getTime());

                fromDate = thismonthFirst;

                toDate = thismonthLast;


                break;
            case 1:

                thisMonth.setBackgroundResource(R.drawable.request_title_bg);
                thisMonth.setTag("0");
                fromDate = "";
                toDate = "";
                break;

            default:

                thisMonth.setBackgroundResource(R.drawable.request_title_bg);
                thisMonth.setTag("0");
                break;
        }
    }

    private void lastMonth() {
        // See here
        date_text.setText(null);
        to_date_text.setText(null);
        String tag = lastMonth.getTag().toString();
        Integer integer = Integer.valueOf(tag);

        switch (integer) {
            case 0:

                lastMonth.setBackgroundResource(R.drawable.req_filter_active_bg);
                lastMonth.setTag("1");

                lastThirtyDays.setBackgroundResource(R.drawable.request_title_bg);
                lastThirtyDays.setTag("0");

                thisMonth.setBackgroundResource(R.drawable.request_title_bg);
                thisMonth.setTag("0");

                Calendar aCalendar = Calendar.getInstance();
                aCalendar.add(Calendar.MONTH, -1);
                aCalendar.set(Calendar.DATE, 1);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String lastmonthFirst = dateFormat.format(aCalendar.getTime());
                aCalendar.set(Calendar.DATE, aCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                String lastmonthLast = dateFormat.format(aCalendar.getTime());

                fromDate = lastmonthFirst;

                toDate = lastmonthLast;


                break;
            case 1:

                lastMonth.setBackgroundResource(R.drawable.request_title_bg);
                lastMonth.setTag("0");
                fromDate = "";
                toDate = "";
                break;

            default:

                lastMonth.setBackgroundResource(R.drawable.request_title_bg);
                lastMonth.setTag("0");
                break;
        }
    }

    private void clearDate() {
        lastMonth.setBackgroundResource(R.drawable.request_title_bg);
        lastMonth.setTag("0");

        lastThirtyDays.setBackgroundResource(R.drawable.request_title_bg);
        lastThirtyDays.setTag("0");

        thisMonth.setBackgroundResource(R.drawable.request_title_bg);
        thisMonth.setTag("0");
    }

    private void pickToDate() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        dialog = new DatePickerDialog(AttendanceFilterActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String started_date = "" + String.valueOf(i) + "-" + String.valueOf(i1 + 1) + "-" + String.valueOf(i2);
                if (date_text != null && date_text.getText().toString() != null && !date_text.getText().toString().trim().isEmpty()) {
                    try {
                        Date date = dateFormat.parse(date_text.getText().toString());

                        dialog.getDatePicker().setMinDate(date.getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    dialog.getDatePicker().setMinDate(timeInMills);
                }

                dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                timeInMills = calendar.getTimeInMillis();


                try {
                    Date date = dateFormat.parse(started_date);
                    String select_date = dateFormat.format(date);
                    to_date_text.setText(select_date);
                    toDate = select_date;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, year, month, day);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (date_text != null && date_text.getText().toString() != null && !date_text.getText().toString().trim().isEmpty()) {
                try {
                    Date date = dateFormat.parse(date_text.getText().toString());
                    dialog.getDatePicker().setMinDate(date.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                dialog.getDatePicker().setMinDate(timeInMills);
            }

            dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        }
        dialog.show();
    }

    public void openProgress() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(AttendanceFilterActivity.this).isFinishing()) {
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
                        if (!(AttendanceFilterActivity.this).isFinishing()) {
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

    private void pickFromDate() {

        dialog = new DatePickerDialog(AttendanceFilterActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                String started_date = "" + String.valueOf(i) + "-" + String.valueOf(i1 + 1) + "-" + String.valueOf(i2);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                timeInMills = calendar.getTimeInMillis();

                try {
                    Date date = dateFormat.parse(started_date);
                    String select_date = dateFormat.format(date);
                    date_text.setText(select_date);
                    fromDate = select_date;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }, year, month, day);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        }
        dialog.show();
    }

    private class SearchAdapters extends RecyclerView.Adapter<SearchAdapters.ViewHolder> {

        List<EmployeeModel> employeeModels;
        List<EmployeeModel> employeeModelList = new ArrayList<>();
        Context context;

        public SearchAdapters(Context context, List<EmployeeModel> employeeModels) {
            this.context = context;
            this.employeeModels = employeeModels;
            employeeModelList.addAll(employeeModels);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.custom_single_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final EmployeeModel model = employeeModels.get(position);


            holder.check_box.setText(model.getEmployee_name());
            if (model.getChecked()) {
                holder.check_box.setChecked(true);
            } else {
                holder.check_box.setChecked(false);
            }
            if (position % 2 == 0) {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.first_item_col));
            } else {
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.second_item_col));
            }

            holder.check_box.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = holder.check_box.isChecked();
                    if (checked && !model.getChecked()) {
                     /*   employeeModels.get(position).setChecked(true);
                        employeeModels1.get(position).setChecked(true);
                        selected_string.add(holder.check_box.getText().toString());
                        selected_userids.add(employeeModels.get(position).getEmployee_Id());
                        holder.check_box.setChecked(true);
                        model.setChecked(true);
                        notifyItemChanged(position,model);*/
                        employeeModels.get(position).setChecked(true);
                        //  employeeModels1.get(position).setChecked(true);
                        selected_string.add(holder.check_box.getText().toString());
                        selected_userids.add(employeeModels.get(position).getEmployee_Id());
                        holder.check_box.setChecked(true);
                        model.setChecked(true);
                        notifyItemChanged(position, model);
                    } else if (!checked && model.getChecked()) {
                       /* selected_string.remove(holder.check_box.getText().toString());
                        selected_userids.remove(employeeModels.get(position).getEmployee_Id());
                        employeeModels.get(position).setChecked(false);
                        employeeModels1.get(position).setChecked(false);
                        model.setChecked(false);
                        holder.check_box.setChecked(false);
                        notifyItemChanged(position,model);*/
                        selected_string.remove(holder.check_box.getText().toString());
                        selected_userids.remove(employeeModels.get(position).getEmployee_Id());
                        employeeModels.get(position).setChecked(false);
                        //  employeeModels1.get(position).setChecked(false);
                        model.setChecked(false);
                        holder.check_box.setChecked(false);
                        notifyItemChanged(position, model);
                    }
                }
            });

         /*   holder.check_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    try
                    {
                        if (isChecked && !model.getChecked()){

                            employeeModels.get(position).setChecked(true);
                            employeeModels1.get(position).setChecked(true);
                            selected_string.add(holder.check_box.getText().toString());
                            selected_userids.add(employeeModels.get(position).getEmployee_Id());
                            holder.check_box.setChecked(true);
                            model.setChecked(true);
                            notifyItemChanged(position,model);
                        }else if (!isChecked && model.getChecked()){
                            selected_string.remove(holder.check_box.getText().toString());
                            selected_userids.remove(employeeModels.get(position).getEmployee_Id());
                            employeeModels.get(position).setChecked(false);
                            employeeModels1.get(position).setChecked(false);
                            model.setChecked(false);
                            holder.check_box.setChecked(false);
                            notifyItemChanged(position,model);
                        }
                        else if(isChecked && model.getChecked())
                        {

                            employeeModels.get(position).setChecked(true);
                            employeeModels1.get(position).setChecked(true);

                            holder.check_box.setChecked(true);
                            model.setChecked(true);
                            notifyItemChanged(position,model);
                        }
                        else if(!isChecked && !model.getChecked())
                        {

                            employeeModels.get(position).setChecked(false);
                            employeeModels1.get(position).setChecked(false);

                            holder.check_box.setChecked(false);
                            model.setChecked(false);
                            notifyItemChanged(position,model);
                        }



                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            });*/
        }

        @Override
        public int getItemCount() {
            return employeeModels.size();
        }

        public void search(String searchQuery) {
            try {
                if (employeeModels != null && employeeModels.size() > 0) {
                    employeeModels.clear();
                    notifyDataSetChanged();
                }
                if (searchQuery.length() > 0) {
                    for (EmployeeModel employee : employeeModelList) {
                        if (employee.getEmployee_name() != null && employee.getEmployee_name().toLowerCase().contains(searchQuery.toLowerCase())) {
                            employeeModels.add(employee);
                        }
                    }
                }
                if (employeeModels.size() == 0) {
                    employeeModels.addAll(employeeModelList);


                }
                notifyDataSetChanged();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox check_box;

            public ViewHolder(View itemView) {
                super(itemView);
                check_box = (CheckBox) itemView.findViewById(R.id.check_box);
            }
        }
    }

}
