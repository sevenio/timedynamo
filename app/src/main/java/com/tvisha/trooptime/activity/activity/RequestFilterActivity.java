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
import android.widget.Toast;

import com.tvisha.trooptime.activity.activity.apiPostModels.Attendance;
import com.tvisha.trooptime.activity.activity.apiPostModels.CcEmpResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.EmployeeDetails;
import com.tvisha.trooptime.activity.activity.apiPostModels.SelfRequest;
import com.tvisha.trooptime.activity.activity.apiPostModels.UserRequestListResponse;
import com.tvisha.trooptime.activity.activity.dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.helper.Constants;
import com.tvisha.trooptime.activity.activity.helper.Helper;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.model.EmployeeModel;
import com.tvisha.trooptime.activity.activity.model.TimeAndAttendenceModel;
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
import static android.view.View.VISIBLE;

public class RequestFilterActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnTouchListener, AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener {
    ImageView back_button;
    TextView cler_button, date_text, to_date_text, employee_names, lastThirtyDays, thisMonth, lastMonth;
    Button tv_search;
    RelativeLayout employees, date_picker, to_date_picker;
    Spinner filter_request;
    String userId, email, apiKey, break_time = "", requestType = "", remarks = "", emp_id, emp_user_id, select_date, get_date, mul_emp_ids,
            fromDate = "", toDate = "";
    ;
    boolean teamLead = false;
    int filte_working_time = 0;
    List<SelfRequest> teamRequestArrayList = new ArrayList<>();
    List<SelfRequest> selfRequestArrayList = new ArrayList<>();
    LinearLayout parent_of_textview, employeeLayout;
    List<EmployeeModel> employeeModels;
    List<EmployeeModel> employeeModels1 = new ArrayList<>();
    List<Attendance> attendence_arrayList;
    List<String> employee_ids_array, employee_names_array, names_array;
    List<String> selected_string = new ArrayList<>();
    List<String> selected_userids = new ArrayList<>();
    Dialog popup_dialog = null;


    TimeAndAttendenceModel model;
    Set<String> hasset;
    DatePickerDialog dialog;
    int pos = 0, filter_type = 2, year, month, day;
    SharedPreferences sharedPreferences;
    RadioGroup radio_group;
    RadioButton rb_latelogin, rb_early_logout;
    Calendar calendar;
    long timeInMills = 0, fromDateTimeInMills = 0, toDateTimeInMills = 0;
    View employeeLine;
    boolean self = false, team = false, admin = false, cc = false, isEmpApiCall = false;
    SearchAdapters adapter;
    ApiInterface apiService;
    EditText auto_serachview;
    CustomProgressBar customProgressBar;
    long lastCliked = 0;
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_req_layout);
        customProgressBar = new CustomProgressBar(RequestFilterActivity.this);
        apiService = ApiClient.getInstance();
        sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
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

        dataAddtoAdapter();
        getTheTroopTimeEmpData();

    }

    public void openProgress() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(RequestFilterActivity.this).isFinishing()) {
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
                        if (!(RequestFilterActivity.this).isFinishing()) {
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
    protected void onDestroy() {
        super.onDestroy();

    }

    private void getTheTroopTimeEmpData() {
        try {
            if (Utilities.getConnectivityStatus(RequestFilterActivity.this) > 0) {
                employeeModels = new ArrayList<>();
                employee_ids_array = new ArrayList<>();
                employee_names_array = new ArrayList<>();
                names_array = new ArrayList<>();

                if (team) {

                    if (!self) {
                        employeeLayout.setVisibility(VISIBLE);
                        employeeLine.setVisibility(VISIBLE);

                    } else {
                        employeeLayout.setVisibility(GONE);
                        employeeLine.setVisibility(GONE);
                    }

                } else if (admin) {

                    employeeLayout.setVisibility(VISIBLE);
                    employeeLine.setVisibility(VISIBLE);
                } else if (cc) {

                    employeeLayout.setVisibility(VISIBLE);
                    employeeLine.setVisibility(VISIBLE);
                } else {
                    employeeLayout.setVisibility(GONE);
                    employeeLine.setVisibility(GONE);

                }
            } else {
                Utilities.ShowSnackbar(RequestFilterActivity.this, tv_search, "Please Check Network Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeWidgets() {
        try {
            back_button = (ImageView) findViewById(R.id.back_button);
            cler_button = (TextView) findViewById(R.id.cler_button);
            employees = (RelativeLayout) findViewById(R.id.employees);
            radio_group = (RadioGroup) findViewById(R.id.radio_group);
            rb_latelogin = (RadioButton) findViewById(R.id.rb_latelogin);
            rb_early_logout = (RadioButton) findViewById(R.id.rb_early_logout);

            radio_group.setOnCheckedChangeListener(this);

            filter_request = (Spinner) findViewById(R.id.filter_request);
            tv_search = (Button) findViewById(R.id.tv_search);
            date_picker = (RelativeLayout) findViewById(R.id.date_picker);
            date_text = (TextView) findViewById(R.id.date_text);
            to_date_picker = (RelativeLayout) findViewById(R.id.to_date_picker);
            to_date_text = (TextView) findViewById(R.id.to_date_text);
            employee_names = (TextView) findViewById(R.id.employee_names);
            lastThirtyDays = (TextView) findViewById(R.id.lastThirtyDays);
            thisMonth = (TextView) findViewById(R.id.thisMonth);
            lastMonth = (TextView) findViewById(R.id.lastMonth);
            employeeLayout = findViewById(R.id.employeeLayout);
            employeeLine = findViewById(R.id.employeeLine);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*if (SystemClock.elapsedRealtime() - mLastClickTime < 100) {
        return;
    }
    mLastClickTime = SystemClock.elapsedRealtime();*/
    @Override
    public void onClick(View view) {

        try {

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
                    toDate = "";
                    if (date_text == null) {
                        fromDate = "";
                    }
                    if (date_text != null && date_text.getText().toString().length() < 3) {
                        fromDate = "";
                    }
                    clearDate();
                    pickToDate();
                    break;

                case R.id.back_button:

                    onBackPressed();
                    break;
                case R.id.cler_button:

                    resetForm();

                    break;
                case R.id.tv_search:
                    // Utilities.ShowSnackbar(RequestFilterActivity.this,tv_search, "Button clicked");
                    validateForm();
                    break;
                case R.id.employeeLayout:

                    if (SystemClock.elapsedRealtime() - lastCliked < 100) {
                        return;
                    }
                    lastCliked = SystemClock.elapsedRealtime();

                    showEmployeePopup();

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateForm() {

        try {
            getStringResource();
            if (fromDate == null || fromDate.length() < 3) {
                Utilities.ShowSnackbar(RequestFilterActivity.this, date_text, "Please Select From Date");
            } else if (toDate == null || toDate.length() < 3) {
                Utilities.ShowSnackbar(RequestFilterActivity.this, to_date_text, "Please Select To Date");
            } else {


                if (selected_userids != null && selected_userids.size() > 0 && employeeModels != null && employeeModels.size() > 0) {
                    mul_emp_ids = selected_userids.toString().trim().replace(" ", "").trim();
                } else {

                    mul_emp_ids = "";

                }
                if (self) {
                    if (Utilities.isNetworkAvailable(RequestFilterActivity.this)) {
                        callUserRequestListApi();
                    } else {
                        Toast.makeText(RequestFilterActivity.this, "Please check internet connection", Toast.LENGTH_LONG).show();
                    }


                } else if (team) {


                    if (Utilities.isNetworkAvailable(RequestFilterActivity.this)) {
                        callTeamRequestListApi();
                    } else {
                        Toast.makeText(RequestFilterActivity.this, "Please check internet connection", Toast.LENGTH_LONG).show();
                    }


                } else if (admin) {


                    if (Utilities.isNetworkAvailable(RequestFilterActivity.this)) {
                        callAllRequestListApi();
                    } else {
                        Toast.makeText(RequestFilterActivity.this, "Please check internet connection", Toast.LENGTH_LONG).show();
                    }

                } else if (cc) {


                    if (Utilities.isNetworkAvailable(RequestFilterActivity.this)) {
                        callCcRequestListApi();
                    } else {
                        Toast.makeText(RequestFilterActivity.this, "Please check internet connection", Toast.LENGTH_LONG).show();
                    }

                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callTeam() {

        try {

            Intent intent = new Intent(RequestFilterActivity.this, RequestActivity.class);
            intent.putExtra("type", "3");
            intent.putExtra("requestType", requestType);
            intent.putExtra("fromDate", fromDate);
            intent.putExtra("toDate", toDate);
            intent.putExtra("users", mul_emp_ids);
            intent.putExtra("filter", true);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void callSelf() {
        try {
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_ADMIN_CLICKED, false).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_SELF_CLICKED, true).apply();
            sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_TEAM_CLICKED, false).apply();
            Intent intent = new Intent(RequestFilterActivity.this, RequestActivity.class);
            intent.putExtra("type", "1");
            intent.putExtra("requestType", requestType);
            intent.putExtra("fromDate", fromDate);
            intent.putExtra("toDate", toDate);
            intent.putExtra("users", mul_emp_ids);
            //intent.putExtra("filter",false);
            intent.putExtra("filter", true);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void getStringResource() {
        try {
            hasset = new HashSet<>();
            select_date = date_text.getText().toString();
            break_time = "";

            requestType = filter_request.getSelectedItem().toString().trim();
            if (requestType.equals("Leave")) {
                filte_working_time = Constants.WORKING_HOURS_LESS_THAN_8;
                requestType = "1";
            } else if (requestType.equals("Permission")) {
                filte_working_time = Constants.WORKING_HOURS_MORE_THAN_8;
                requestType = "2";
            } else if (requestType.equals("Comp Off")) {
                filte_working_time = Constants.WORKING_HOURS_ANY;
                requestType = "3";
            } else if (requestType.equals("Swap")) {
                filte_working_time = Constants.WORKING_HOURS_ANY;
                requestType = "4";
            } else if (requestType.equals("Over Time")) {
                filte_working_time = Constants.WORKING_HOURS_ANY;
                requestType = "5";
            } else {
                filte_working_time = Constants.WORKING_HOURS_MORE_THAN_10;
                requestType = "";
            }

            remarks = "";
            String employeename = employee_names.getText().toString();
            if (employeename != null && !employeename.trim().isEmpty()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            switch (adapterView.getId()) {
                case R.id.filter_check_in:

                    break;
                case R.id.filter_check_out:

                    break;

                case R.id.filter_request:

                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        try {
            String name = adapterView.getItemAtPosition(i).toString();
            for (int j = 0; j < employee_names_array.size(); j++) {
                String emp_name = employee_names_array.get(j);
                if (emp_name.trim().toLowerCase().equals(name.trim().toLowerCase())) {
                    emp_user_id = employee_ids_array.get(j);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    private void shareprefernceData() {

        try {

            userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
            apiKey = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
            teamLead = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_LEAD, false);
            self = sharedPreferences.getBoolean(SharePreferenceKeys.SELF_CLICKED, false);
            team = sharedPreferences.getBoolean(SharePreferenceKeys.TEAM_CLICKED, false);
            admin = sharedPreferences.getBoolean(SharePreferenceKeys.ADMIN_CLICKED, false);
            cc = sharedPreferences.getBoolean(SharePreferenceKeys.CC_CLICKED, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dataAddtoAdapter() {


        String[] work_time = getResources().getStringArray(R.array.request_type);
        ArrayAdapter<String> work_adapter = new ArrayAdapter<String>(RequestFilterActivity.this, R.layout.singlerow1, work_time);
        work_adapter.setDropDownViewResource(R.layout.singlerow1);
        filter_request.setAdapter(work_adapter);
    }

    private void eventHandlings() {
        try {
            back_button.setOnClickListener(this);
            cler_button.setOnClickListener(this);
            tv_search.setOnClickListener(this);
            date_picker.setOnClickListener(this);
            to_date_picker.setOnClickListener(this);
            employeeLayout.setOnClickListener(this);
            thisMonth.setOnClickListener(this);
            lastThirtyDays.setOnClickListener(this);
            lastMonth.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void lastThirtyDays() {
        try {


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
        } catch (Exception e) {

        }
        tv_search.setClickable(true);
    }

    private void thisMonth() {

        try {

            date_text.setText(null);
            to_date_text.setText(null);
            // See here
            String tag = thisMonth.getTag().toString();
            Integer integer = Integer.valueOf(tag);

            switch (integer) {
                case 0:

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

                    thisMonth.setBackgroundResource(R.drawable.req_filter_active_bg);
                    thisMonth.setTag("1");

                    lastThirtyDays.setBackgroundResource(R.drawable.request_title_bg);
                    lastThirtyDays.setTag("0");

                    lastMonth.setBackgroundResource(R.drawable.request_title_bg);
                    lastMonth.setTag("0");


                    break;
                case 1:

                    thisMonth.setBackgroundResource(R.drawable.request_title_bg);
                    thisMonth.setTag("0");
                    fromDate = "";
                    toDate = "";
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_search.setClickable(true);
    }

    private void lastMonth() {
        try {


            date_text.setText(null);
            to_date_text.setText(null);
            // See here
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
        } catch (Exception e) {

        }
        tv_search.setClickable(true);
    }

    private void clearDate() {
        try {
            lastMonth.setBackgroundResource(R.drawable.request_title_bg);
            lastMonth.setTag("0");

            lastThirtyDays.setBackgroundResource(R.drawable.request_title_bg);
            lastThirtyDays.setTag("0");

            thisMonth.setBackgroundResource(R.drawable.request_title_bg);
            thisMonth.setTag("0");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetForm() {
        try {

            filter_request.setSelection(0);
            date_text.setText(null);
            date_text.setHint("From");
            to_date_text.setText(null);
            to_date_text.setHint("To");
            fromDate = "";
            toDate = "";
            clearDate();
            employee_names.setText(null);
            selected_string.clear();
            selected_userids.clear();
            tv_search.setClickable(true);
            pos = 0;
            employee_names_array.removeAll(employee_names_array);
            employee_names_array.addAll(names_array);
            employee_names.setVisibility(GONE);
            radio_group.clearCheck();
            if (employeeModels != null && !employeeModels.isEmpty() && employeeModels.size() > 0) {
                for (int i = 0; i < employeeModels.size(); i++) {
                    employeeModels.get(i).setChecked(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showEmployeePopup() {

        try {

            if (!isEmpApiCall) {


                if (team) {
                    if (Utilities.isNetworkAvailable(RequestFilterActivity.this)) {

                        //callToserverToGetThedata("");
                        openProgress();
                        callgetTeamEmployeesApi();

                    } else {
                        Toast.makeText(RequestFilterActivity.this, "Please check network connection", Toast.LENGTH_SHORT).show();
                    }
                } else if (cc) {
                    if (Utilities.isNetworkAvailable(RequestFilterActivity.this)) {
                        openProgress();

                        callgetCcEmployeesApi();
                    } else {
                        Toast.makeText(RequestFilterActivity.this, "Please check network connection", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (Utilities.isNetworkAvailable(RequestFilterActivity.this)) {

                        //callToserverToAllGetThedata("");
                        openProgress();
                        callgetAllEmployeesApi();
                    } else {
                        Toast.makeText(RequestFilterActivity.this, "Please check network connection", Toast.LENGTH_SHORT).show();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPopup() {
        try {
            isEmpApiCall = true;
            if (popup_dialog != null && popup_dialog.isShowing()) {
                return;
            }

            popup_dialog = new Dialog(RequestFilterActivity.this, android.R.style.Theme_Light);
            popup_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            popup_dialog.setContentView(R.layout.popupemployee);
            ImageView noDataImage = popup_dialog.findViewById(R.id.noDataImage);
            noDataImage.setVisibility(GONE);
            auto_serachview = (EditText) popup_dialog.findViewById(R.id.auto_serachview);
            TextView done_button = (TextView) popup_dialog.findViewById(R.id.done_button);
            TextView check_cler_button = (TextView) popup_dialog.findViewById(R.id.check_cler_button);
            ImageView back_button = (ImageView) popup_dialog.findViewById(R.id.back_button);
            parent_of_textview = (LinearLayout) popup_dialog.findViewById(R.id.parent_of_textview);
            RecyclerView employees_list = (RecyclerView) popup_dialog.findViewById(R.id.employees_list);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RequestFilterActivity.this);
            employees_list.setLayoutManager(linearLayoutManager);
            employees_list.setVisibility(VISIBLE);
            adapter = new SearchAdapters(RequestFilterActivity.this, employeeModels);
            employees_list.setAdapter(adapter);

            if (employeeModels == null || employeeModels.size() == 0) {
                auto_serachview.setVisibility(View.INVISIBLE);
                noDataImage.setVisibility(VISIBLE);
                employees_list.setVisibility(GONE);
            } else {
                auto_serachview.setVisibility(View.VISIBLE);
                employees_list.setVisibility(VISIBLE);
                noDataImage.setVisibility(GONE);
            }


            back_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popup_dialog.dismiss();
                }
            });

            done_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    employee_names.setVisibility(VISIBLE);
                    Helper.getInstance().closeKeyBoard(RequestFilterActivity.this,auto_serachview);
                    String strings = selected_string.toString().replace("[", "").replace("]", "").trim();
                    if (strings != null && !strings.isEmpty()) {
                        employee_names.setText(strings + "  ");
                    } else {
                        employee_names.setVisibility(GONE);
                    }
                    popup_dialog.dismiss();

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pickToDate() {
        try {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            dialog = new DatePickerDialog(RequestFilterActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        //dialog.getDatePicker().setMinDate(timeInMills);
                    }

                    //dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    //dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
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
                    //  dialog.getDatePicker().setMinDate(timeInMills);
                }

            }
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pickFromDate() {

        try {

            dialog = new DatePickerDialog(RequestFilterActivity.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    String started_date = "" + String.valueOf(i) + "-" + String.valueOf(i1 + 1) + "-" + String.valueOf(i2);
                    // dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    timeInMills = calendar.getTimeInMillis();

                    try {
                        Date date = dateFormat.parse(started_date);
                        String select_date = dateFormat.format(date);
                        date_text.setText(select_date);
                        fromDate = select_date;
                        if (toDate != null && !toDate.trim().isEmpty()) {
                            Date t_date = dateFormat.parse(toDate);
                            if (date.after(t_date)) {
                                to_date_text.setText(select_date);
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }, year, month, day);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //dialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
            }
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callUserRequestListApi() {
        try {
            openProgress();
            Call<UserRequestListResponse> call = apiService.getUserRequestList(apiKey, userId, fromDate, toDate, requestType, "", 20, 0);
            call.enqueue(new Callback<UserRequestListResponse>() {
                @Override
                public void onResponse(Call<UserRequestListResponse> call, Response<UserRequestListResponse> response) {
                    UserRequestListResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {

                            if (apiResponse.getRequestData() != null && apiResponse.getRequestData().size() > 0) {
                                selfRequestArrayList = apiResponse.getRequestData();
                                sharedPreferences.edit().putString(SharePreferenceKeys.USER_AVATAR,apiResponse.getRequestData().get(0).getRequest_by_user_avatar()).apply();
                                callSelf();
                            } else {
                                Toast.makeText(RequestFilterActivity.this, "No data found", Toast.LENGTH_LONG).show();
                            }


                        } else {
                            Toast.makeText(RequestFilterActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                            closeProgress();
                        }

                    } else {
                        closeProgress();
                        Toast.makeText(RequestFilterActivity.this, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<UserRequestListResponse> call, Throwable t) {
                    closeProgress();

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callAllRequestListApi() {
        try {
            openProgress();

            if (requestType == null) {
                requestType = "";

            } else if (requestType.trim().isEmpty()) {
                requestType = "";
            }


            Call<UserRequestListResponse> call = apiService.getAllRequestList(apiKey, userId, fromDate, toDate, requestType, "", 20, 0);
            call.enqueue(new Callback<UserRequestListResponse>() {
                @Override
                public void onResponse(Call<UserRequestListResponse> call, Response<UserRequestListResponse> response) {
                    UserRequestListResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {

                            if (apiResponse.getRequestData() != null && apiResponse.getRequestData().size() > 0) {
                                teamRequestArrayList = apiResponse.getRequestData();
                                sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_ADMIN_CLICKED, true).apply();
                                sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_SELF_CLICKED, false).apply();
                                sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_TEAM_CLICKED, false).apply();
                                sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_CC_CLICKED, false).apply();
                                callTeam();
                            } else {
                                Toast.makeText(RequestFilterActivity.this, "No data found", Toast.LENGTH_LONG).show();
                            }


                        } else {
                            closeProgress();
                            Toast.makeText(RequestFilterActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();

                        }

                    } else {
                        closeProgress();
                        Toast.makeText(RequestFilterActivity.this, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<UserRequestListResponse> call, Throwable t) {
                    closeProgress();


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callTeamRequestListApi() {
        try {
            openProgress();
            if (requestType == null) {
                requestType = "";

            } else if (requestType.trim().isEmpty()) {
                requestType = "";
            }

            Call<UserRequestListResponse> call = apiService.getTeamRequestList(apiKey, userId, fromDate, toDate, requestType, "", 20, 0);
            call.enqueue(new Callback<UserRequestListResponse>() {
                @Override
                public void onResponse(Call<UserRequestListResponse> call, Response<UserRequestListResponse> response) {
                    UserRequestListResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {

                            if (apiResponse.getRequestData() != null && apiResponse.getRequestData().size() > 0) {
                                teamRequestArrayList = apiResponse.getRequestData();
                                sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_ADMIN_CLICKED, false).apply();
                                sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_SELF_CLICKED, false).apply();
                                sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_CC_CLICKED, false).apply();
                                sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_TEAM_CLICKED, true).apply();
                                callTeam();
                            } else {
                                Toast.makeText(RequestFilterActivity.this, "No data found", Toast.LENGTH_LONG).show();
                            }


                        } else {
                            closeProgress();
                            Toast.makeText(RequestFilterActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();

                        }

                    } else {

                        closeProgress();

                    }

                }

                @Override
                public void onFailure(Call<UserRequestListResponse> call, Throwable t) {
                    closeProgress();


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callCcRequestListApi() {
        try {
            openProgress();
            if (requestType == null) {
                requestType = "";

            } else if (requestType.trim().isEmpty()) {
                requestType = "";
            }

            Call<UserRequestListResponse> call = apiService.getCcRequestList(apiKey, userId, fromDate, toDate, requestType, "", 20, 0);
            call.enqueue(new Callback<UserRequestListResponse>() {
                @Override
                public void onResponse(Call<UserRequestListResponse> call, Response<UserRequestListResponse> response) {
                    UserRequestListResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {

                            if (apiResponse.getRequestData() != null && apiResponse.getRequestData().size() > 0) {
                                teamRequestArrayList = apiResponse.getRequestData();
                                sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_ADMIN_CLICKED, false).apply();
                                sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_SELF_CLICKED, false).apply();
                                sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_TEAM_CLICKED, false).apply();
                                sharedPreferences.edit().putBoolean(SharePreferenceKeys.FILTER_CC_CLICKED, true).apply();
                                callTeam();
                            } else {
                                Toast.makeText(RequestFilterActivity.this, "No data found", Toast.LENGTH_LONG).show();
                            }


                        } else {
                            closeProgress();
                            Toast.makeText(RequestFilterActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();

                        }

                    } else {

                        closeProgress();

                    }

                }

                @Override
                public void onFailure(Call<UserRequestListResponse> call, Throwable t) {
                    closeProgress();


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callgetCcEmployeesApi() {

        try {

            Call<CcEmpResponse> call = apiService.getCcEmployess(apiKey, userId);
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


                        } else {
                            showPopup();
                        }


                    }


                }

                @Override
                public void onFailure(Call<CcEmpResponse> call, Throwable t) {

                    closeProgress();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callgetAllEmployeesApi() {

        try {


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


                        } else {
                            showPopup();
                        }


                    }


                }

                @Override
                public void onFailure(Call<CcEmpResponse> call, Throwable t) {

                    closeProgress();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callgetTeamEmployeesApi() {

        try {


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


                        } else {
                            showPopup();
                        }


                    }


                }

                @Override
                public void onFailure(Call<CcEmpResponse> call, Throwable t) {

                    closeProgress();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class SearchAdapters extends RecyclerView.Adapter<SearchAdapters.ViewHolder> {

        List<EmployeeModel> employeeModels;
        List<EmployeeModel> employeeModelList = new ArrayList<>();
        Context context;

        public SearchAdapters(Context context, List<EmployeeModel> employeeModels) {
            this.context = context;
            this.employeeModels = employeeModels;
            if (employeeModels != null && employeeModels.size() > 0)
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
            try {
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
                holder.check_box.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean checked = holder.check_box.isChecked();
                        if (checked && !model.getChecked()) {
                   /*     employeeModels.get(position).setChecked(true);
                        employeeModels1.get(position).setChecked(true);
                        selected_string.add(holder.check_box.getText().toString());
                        selected_userids.add(employeeModels.get(position).getEmployee_Id());
                        holder.check_box.setChecked(true);
                        model.setChecked(true);
                        notifyItemChanged(position,model);*/

                            employeeModels.get(position).setChecked(true);
                            selected_string.add(holder.check_box.getText().toString());
                            selected_userids.add(employeeModels.get(position).getEmployee_Id());
                            holder.check_box.setChecked(true);
                            model.setChecked(true);
                            notifyItemChanged(position, model);
                        } else if (!checked && model.getChecked()) {
                      /*  selected_string.remove(holder.check_box.getText().toString());
                        selected_userids.remove(employeeModels.get(position).getEmployee_Id());
                        employeeModels.get(position).setChecked(false);
                        employeeModels1.get(position).setChecked(false);
                        model.setChecked(false);
                        holder.check_box.setChecked(false);
                        notifyItemChanged(position,model);*/
                            selected_string.remove(holder.check_box.getText().toString());
                            selected_userids.remove(employeeModels.get(position).getEmployee_Id());
                            employeeModels.get(position).setChecked(false);
                            model.setChecked(false);
                            holder.check_box.setChecked(false);
                            notifyItemChanged(position, model);
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
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
