package com.tvisha.trooptime.activity.activity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.tvisha.trooptime.activity.activity.adapter.CcNamesAdapter;
import com.tvisha.trooptime.activity.activity.adapter.LeaveDatesAdapter;
import com.tvisha.trooptime.activity.activity.adapter.ToNamesAdapter;
import com.tvisha.trooptime.activity.activity.apiPostModels.Cc_employess;
import com.tvisha.trooptime.activity.activity.apiPostModels.Leave;
import com.tvisha.trooptime.activity.activity.apiPostModels.LeaveRequestResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.ToAndCcDetailsResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.To_employee;
import com.tvisha.trooptime.activity.activity.apiPostModels.Users;
import com.tvisha.trooptime.activity.activity.autoComplete.User;
import com.tvisha.trooptime.activity.activity.autoComplete.UserAdapter;
import com.tvisha.trooptime.activity.activity.dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.helper.Helper;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.TimeHelper;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaveRequestActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener
        , View.OnTouchListener, com.borax12.materialdaterangepicker.date.DatePickerDialog.OnDateSetListener {

    public TextView fromDate, toDate, totalDays, actionLable;
    Spinner leaveType;
    ;
    String USER_ID = "", to = "", date = "", cc = "", userId, email, apiKey, frmDt = "", toDt = "", type = "1";
    Button sendRequest;
    EditText resonForLeave;
    ArrayList<Leave> datesArrayList = new ArrayList<>();
    CheckBox halfday_checkbox;
    RelativeLayout spinnerLayout;
    RecyclerView days_recyclerview, toNamesRecyclerView, ccNamesRecyclerView;
    LinearLayoutManager linearLayoutManager;
    LeaveDatesAdapter leaveDatesAdapter;
    List<Users> usersArrayList = new ArrayList<>();
    RecyclerView.LayoutManager toNamesLayoutManager, ccNamesLayoutManager;
    ToNamesAdapter toNamesAdapter;
    CcNamesAdapter ccNamesAdapter;
    List<Cc_employess> ccNamesArrayList = new ArrayList<>();
    List<To_employee> toNamesArrayList = new ArrayList<>();
    ApiInterface apiService;
    UserAdapter userAdapter, ccUsersAdapter;
    AutoCompleteTextView toUserName, ccUserName;
    ArrayList<User> user = new ArrayList<>();
    ArrayList<User> ccUsers = new ArrayList<>();
    String dateFormat1 = "yyyy-MM-dd", dateFormat = "dd MMM yy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1, Locale.US);
    long timeInMills = 0;
    boolean checked = false;
    Intent intent;
    SharedPreferences sharedPreferences;
    String[] items = new String[3];
    CustomProgressBar customProgressBar;
    Typeface poppins_medium;

    public void openProgress() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(LeaveRequestActivity.this).isFinishing()) {
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
                        if (!(LeaveRequestActivity.this).isFinishing()) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request);

        shareprefernceData();
        initViews();
        initListeners();
        processActivity();

    }

    private void processActivity() {

        try {


            intent = getIntent();
            date = intent.getStringExtra("date");

            Calendar calendar = null;
            if (date.length() > 3) {
                Date d = null;
                try {
                    d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
                    calendar = Calendar.getInstance();
                    calendar.setTime(d);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                calendar = Calendar.getInstance();

            }
            if (calendar != null) {
                fromDate.setText(sdf.format(calendar.getTime()));
                frmDt = sdf1.format(calendar.getTime());
                timeInMills = calendar.getTimeInMillis();
                toDate.setText(sdf.format(calendar.getTime()));
                toDt = sdf1.format(calendar.getTime());
            }

            totalDays.setText("1 Day");
            try {
                updateDatesRecycleView();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spiner, items) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTypeface(poppins_medium);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.req_text_color));
                    return v;
                }


                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);
                    ((TextView) v).setTypeface(poppins_medium);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.req_text_color));
                    return v;
                }
            };

            leaveType.setAdapter(adapter);
            if (date.length() > 3) {
                leaveType.setSelection(2);
                type = "3";
            }

            callToCc();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void callToCc() {
        if (Utilities.isNetworkAvailable(LeaveRequestActivity.this)) {

            callToAndCcDetailsApi();

        } else {
            Toast.makeText(LeaveRequestActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selected_val = leaveType.getSelectedItem().toString();
        type = String.valueOf(position + 1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initListeners() {
        try {
            sendRequest.setOnClickListener(this);
            fromDate.setOnClickListener(this);
            toDate.setOnClickListener(this);
            leaveType.setOnItemSelectedListener(this);
            resonForLeave.setOnTouchListener(this);
            spinnerLayout.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {

        try {

            Typeface.createFromAsset(getAssets(), "font/poppins/Poppins-Medium.ttf");

            customProgressBar = new CustomProgressBar(LeaveRequestActivity.this);
            halfday_checkbox = (CheckBox) findViewById(R.id.halfday_checkbox);
            halfday_checkbox.setTypeface(poppins_medium);
            days_recyclerview = (RecyclerView) findViewById(R.id.days_recyclerview);
            linearLayoutManager = new LinearLayoutManager(LeaveRequestActivity.this, 1, false);
            days_recyclerview.setLayoutManager(linearLayoutManager);
            leaveDatesAdapter = new LeaveDatesAdapter(LeaveRequestActivity.this, datesArrayList);
            days_recyclerview.setAdapter(leaveDatesAdapter);
            sendRequest = findViewById(R.id.sendRequest);
            spinnerLayout = findViewById(R.id.spinnerLayout);


            apiService = new ApiClient().getInstance();
            actionLable = (TextView) findViewById(R.id.actionLable);
            actionLable.setText("Request Leave");
            toUserName = findViewById(R.id.toUserName);
            ccUserName = findViewById(R.id.ccUserName);


            toNamesRecyclerView = findViewById(R.id.to_recycler_view);
            toNamesRecyclerView.setHasFixedSize(true);
            toNamesLayoutManager = new GridLayoutManager(LeaveRequestActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
            toNamesRecyclerView.setLayoutManager(toNamesLayoutManager);
            toNamesAdapter = new ToNamesAdapter(LeaveRequestActivity.this, toNamesArrayList);
            toNamesRecyclerView.setAdapter(toNamesAdapter);


            ccNamesRecyclerView = findViewById(R.id.cc_recycler_view);
            ccNamesRecyclerView.setHasFixedSize(true);
            ccNamesLayoutManager = new GridLayoutManager(LeaveRequestActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
            ccNamesRecyclerView.setLayoutManager(ccNamesLayoutManager);
            ccNamesAdapter = new CcNamesAdapter(LeaveRequestActivity.this, ccNamesArrayList);
            ccNamesRecyclerView.setAdapter(ccNamesAdapter);

            halfday_checkbox.setOnCheckedChangeListener(this);
            leaveType = (Spinner) findViewById(R.id.leaveType);
            resonForLeave = (EditText) findViewById(R.id.resonForLeave);
            fromDate = (TextView) findViewById(R.id.fromDate);
            toDate = (TextView) findViewById(R.id.toDate);
            totalDays = (TextView) findViewById(R.id.totalDays);


            items[0] = "Sick Leave";
            items[1] = "Casual Leave";
            // items[2]="Optional Holiday";
            items[2] = "Optional Leave";

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callSaveLeave() {
        try {

            openProgress();
            if(toNamesArrayList!=null && toNamesArrayList.size()>0){
                for (int i = 0; i < toNamesArrayList.size(); i++) {
                    if (i == toNamesArrayList.size() - 1) {
                        to = to + toNamesArrayList.get(i).getUser_id();
                    } else {
                        to = to + toNamesArrayList.get(i).getUser_id() + ",";
                    }

                }
            }else
            {
                sendRequest.setClickable(true);
               Utilities.ShowSnackbar(LeaveRequestActivity.this, resonForLeave, getResources().getString(R.string.no_to_users_msg));
                return;
            }

            for (int i = 0; i < ccNamesArrayList.size(); i++) {
                if (i == ccNamesArrayList.size() - 1) {
                    cc = cc + ccNamesArrayList.get(i).getUser_id();
                } else {
                    cc = cc + ccNamesArrayList.get(i).getUser_id() + ",";
                }

            }




            boolean changed = false;
            if (checked) {

                for (int i = 0; i < datesArrayList.size(); i++) {
                    if (datesArrayList.get(i).getKey().equals("2") || datesArrayList.get(i).getKey().equals("3")) {
                        changed = true;

                        break;
                    }
                }


            } else {
                changed = false;

            }


            JSONObject dates = new JSONObject();
            String data;
            String is_half_day = "";
            if (changed) {
                for (int i = 0; i < datesArrayList.size(); i++) {
                    String date = datesArrayList.get(i).getApiDate();
                    String value = datesArrayList.get(i).getKey();
                    try {
                        dates.put(date, value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                data = String.valueOf(dates);
                is_half_day = "1";
            } else {
                data = " ";
                is_half_day = "0";
            }


            Call<LeaveRequestResponse> call = apiService.SaveLeaveRequest(apiKey, userId, to, cc, type, resonForLeave.getText().toString().trim(), frmDt, toDt, data, is_half_day);
            call.enqueue(new Callback<LeaveRequestResponse>() {
                @Override
                public void onResponse(Call<LeaveRequestResponse> call, Response<LeaveRequestResponse> response) {
                    LeaveRequestResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {
                            Utilities.ShowSnackbar(LeaveRequestActivity.this, resonForLeave, apiResponse.getMessage());
                            Intent intent = new Intent(LeaveRequestActivity.this, RequestActivity.class);
                            intent.putExtra("type", "1");
                            intent.putExtra("requestType", "");
                            intent.putExtra("fromDate", "");
                            intent.putExtra("toDate", "");
                            intent.putExtra("users", "");
                            startActivity(intent);
                            finish();

                        } else {
                            sendRequest.setClickable(true);

                            Utilities.ShowSnackbar(LeaveRequestActivity.this, resonForLeave, apiResponse.getMessage());

                        }

                    } else {
                        closeProgress();
                        Utilities.ShowSnackbar(LeaveRequestActivity.this, resonForLeave, getResources().getString(R.string.somthing_went_wrong));
                        sendRequest.setClickable(true);

                    }

                }

                @Override
                public void onFailure(Call<LeaveRequestResponse> call, Throwable t) {

                    closeProgress();

                    sendRequest.setClickable(true);


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void shareprefernceData() {
        try {
            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
            apiKey = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void updateDatesArrayList(int i, int position) {
        try {
            if (datesArrayList != null && datesArrayList.size() > 0) {
                if (i != -1) {

                    datesArrayList.get(i).setKey(String.valueOf(position));

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initAutocomplete() {

        try {

            if (user.size() == 0) {
                CountDownTimer timer1 = new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        initAutocomplete();
                    }
                };
                timer1.start();
                return;
            }

            userAdapter = new UserAdapter(LeaveRequestActivity.this, user, new UserAdapter.UserAdapterListener() {
                @Override
                public void onResult(User user) {
                    if (toUserName.hasFocus()) {
                        toUserName.setText("");
                        USER_ID = user.getUserId();
                        toUserName.clearListSelection();
                        toUserName.dismissDropDown();
                        if (!userId.equals(USER_ID)) {
                            addToEmployee(user.getName(), user.getUserId());
                        }

                    }


                }
            });
            toUserName.setAdapter(userAdapter);

            ccUsersAdapter = new UserAdapter(LeaveRequestActivity.this, ccUsers, new UserAdapter.UserAdapterListener() {
                @Override
                public void onResult(User user) {


                    ccUserName.setText("");
                    USER_ID = user.getUserId();
                    ccUserName.clearListSelection();
                    ccUserName.dismissDropDown();
                    if (!userId.equals(USER_ID))
                        addCcEmployee(user.getName(), user.getUserId());


                }
            });
            ccUserName.setAdapter(ccUsersAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void fromDatePicker(){
    /*    Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                LeaveRequestActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        Calendar mcurrentDate = Calendar.getInstance();
        mcurrentDate.add(Calendar.MONTH, -3);
        dpd.setMinDate(mcurrentDate);
        dpd.show(getFragmentManager(), "Datepickerdialog");*/
    }

    private void fromDate() {
        try {
            Calendar mcurrentDate = Calendar.getInstance();
            int cMonth = mcurrentDate.get(Calendar.MONTH);
            int cYear = mcurrentDate.get(Calendar.YEAR);
            int cDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
            mcurrentDate.add(Calendar.MONTH, -3);
            int mMonth = mcurrentDate.get(Calendar.MONTH);
            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
            int mYear = mcurrentDate.get(Calendar.YEAR);
            long pastTime = mcurrentDate.getTimeInMillis();


            DatePickerDialog mDatePicker = new DatePickerDialog(
                    LeaveRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker,
                                      int selectedyear, int selectedmonth,
                                      int selectedday) {
                    Calendar mcurrentDate = Calendar.getInstance();
                    mcurrentDate.set(Calendar.YEAR, selectedyear);
                    mcurrentDate.set(Calendar.MONTH, selectedmonth);
                    mcurrentDate.set(Calendar.DAY_OF_MONTH,
                            selectedday);
                    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

                    fromDate.setText(sdf.format(mcurrentDate.getTime()));
                    toDate.setText(sdf.format(mcurrentDate.getTime()));
                    sharedPreferences.edit().putString("fromDate", sdf1.format(mcurrentDate.getTime()));
                    frmDt = sdf1.format(mcurrentDate.getTime());
                    toDt = sdf1.format(mcurrentDate.getTime());
                    timeInMills = mcurrentDate.getTimeInMillis();
                    try {
                        if (!toDt.equals("") && toDt.length() > 3)
                            updateDatesRecycleView();
                        updateCount();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }, mYear, mMonth, mDay);

            //  mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.getDatePicker().updateDate(cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMinDate(pastTime);
            mDatePicker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void toDate() {
        try {
            final Calendar mcurrentDate = Calendar.getInstance();
            int mYear = mcurrentDate.get(Calendar.YEAR);
            int mMonth = mcurrentDate.get(Calendar.MONTH);
            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog mDatePicker = new DatePickerDialog(
                    LeaveRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker,
                                      int selectedyear, int selectedmonth,
                                      int selectedday) {

                    mcurrentDate.set(Calendar.YEAR, selectedyear);
                    mcurrentDate.set(Calendar.MONTH, selectedmonth);
                    mcurrentDate.set(Calendar.DAY_OF_MONTH,
                            selectedday);
                    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

                    toDate.setText(sdf.format(mcurrentDate.getTime()));
                    toDt = sdf1.format(mcurrentDate.getTime());
                    try {
                        updateDatesRecycleView();
                        updateCount();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }, mYear, mMonth, mDay);

            mDatePicker.getDatePicker().setMinDate(timeInMills);
            mDatePicker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void updateDatesRecycleView() throws ParseException {

        try {

            datesArrayList.clear();
            leaveDatesAdapter.setList(datesArrayList);
            leaveDatesAdapter.notifyDataSetChanged();


            String start = frmDt;
            String end = toDt;
            Date startDate = sdf1.parse(start);
            Date endDate = sdf1.parse(end);
            long diff = endDate.getTime() - startDate.getTime();
            if (diff < 0) {
                return;
            }
            int days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            if (days < 0) {
                Toast.makeText(LeaveRequestActivity.this, "To date should not lessthan From date", Toast.LENGTH_SHORT).show();
                return;
            } else {

            }

            String date = "";
            String nextDate = "";
            String apiDate = "";
            String apiNextdate = "";
            days++;
            for (int i = 0; i < days; i++) {
                Leave leave = new Leave();

                if (i == 0) {
                    date = fromDate.getText().toString();
                    apiDate = frmDt;
                } else if (i == days - 1) {
                    date = toDate.getText().toString();
                    apiDate = toDt;
                } else {
                    nextDate = incrementDateByOne(date);
                    date = nextDate;
                    apiNextdate = incrementDateByOne1(apiDate);
                    apiDate = apiNextdate;
                }
                if (date != null) {
                    leave.setDate(date);
                    leave.setApiDate(apiDate);
                    leave.setKey("1");
                } else {
                    leave.setDate("");
                    leave.setApiDate("");
                    leave.setKey("1");
                }

                datesArrayList.add(leave);
            }

            leaveDatesAdapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public String incrementDateByOne(String date) throws ParseException {
        try {
            Date d = new SimpleDateFormat("dd MMM yy", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.DATE, 1);
            Date nextDate = c.getTime();
            return sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String incrementDateByOne1(String date) throws ParseException {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.DATE, 1);
            Date nextDate = c.getTime();
            return sdf1.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void removeCcEmployee(int i) {
        try {
            if (ccNamesArrayList != null && ccNamesArrayList.size() > 0) {
                if (i != -1) {
                    ccNamesArrayList.remove(i);
                    if (ccNamesAdapter != null) {
                        ccNamesAdapter.setList(ccNamesArrayList);
                        ccNamesAdapter.notifyDataSetChanged();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeToEmployee(int i) {
        try {
            if (toNamesArrayList != null && toNamesArrayList.size() > 0) {
                if (i != -1) {
                    toNamesArrayList.remove(i);
                    if (toNamesAdapter != null) {
                        toNamesAdapter.setList(toNamesArrayList);
                        toNamesAdapter.notifyDataSetChanged();
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callToAndCcDetailsApi() {

        try {

            Call<ToAndCcDetailsResponse> call = apiService.getToAndCcDetails(apiKey, userId);
            call.enqueue(new Callback<ToAndCcDetailsResponse>() {
                @Override
                public void onResponse(Call<ToAndCcDetailsResponse> call, Response<ToAndCcDetailsResponse> response) {
                    ToAndCcDetailsResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        if (apiResponse.isSuccess()) {


                            updateViews(apiResponse);

                        }

                    }

                }

                @Override
                public void onFailure(Call<ToAndCcDetailsResponse> call, Throwable t) {

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateViews(ToAndCcDetailsResponse apiResponse) {

        try {

            if (usersArrayList != null && usersArrayList.size() > 0) {
                usersArrayList.clear();
            }
            if (toNamesArrayList != null && toNamesArrayList.size() > 0) {
                toNamesArrayList.clear();
            }

            if (ccNamesArrayList != null && ccNamesArrayList.size() > 0) {
                ccNamesArrayList.clear();
            }

            if (user != null && user.size() > 0) {
                user.clear();
            }

            if (ccUsers != null && ccUsers.size() > 0) {
                ccUsers.clear();
            }


            usersArrayList = apiResponse.getUsers();
            toNamesArrayList = apiResponse.getTo_employees();
            ccNamesArrayList = apiResponse.getCc_employess();
            if (usersArrayList != null && usersArrayList.size() > 0) {
                for (int i = 0; i < usersArrayList.size(); i++) {

                    String uid = usersArrayList.get(i).getUser_id();
                    String name = usersArrayList.get(i).getName();
                    String displayName = usersArrayList.get(i).getDisplay_name();
                    String email = usersArrayList.get(i).getEmail();
                    User obj = new User(uid, name, displayName, email);
                    boolean present = false;
                    if (!uid.equals(userId)) {
                        user.add(obj);
                    } else {
                        present = true;
                    }

                    if (toNamesArrayList != null && toNamesArrayList.size() > 0) {
                        for (int j = 0; j < toNamesArrayList.size(); j++) {
                            String user_id = toNamesArrayList.get(j).getUser_id();
                            if (uid.equals(user_id)) {
                                present = true;
                            }
                        }
                    }
                    if (!present) {
                        ccUsers.add(obj);
                    }


                }

            }


            updateToNamesRecycleView();
            updateCcNamesRecycleView();
            initAutocomplete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void updateToNamesRecycleView() {

        try {

            if (toNamesArrayList != null && toNamesArrayList.size() > 0) {
                toNamesAdapter.setList(toNamesArrayList);
            } else {
                sendRequest.setClickable(false);
                sendRequest.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }


            toNamesAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void addToEmployee(String userName, String userId) {

        try {

            boolean present = false;


            for (int i = 0; i < toNamesArrayList.size(); i++) {
                String uid = toNamesArrayList.get(i).getUser_id();
                if (uid.equals(userId)) {
                    present = true;
                    break;
                }
            }
            for (int i = 0; i < ccNamesArrayList.size(); i++) {
                String uid = ccNamesArrayList.get(i).getUser_id();
                if (uid.equals(userId)) {
                    present = true;
                    break;
                }
            }

            if (!present) {

                To_employee employee = new To_employee();
                employee.setName(userName);
                employee.setUser_id(userId);
                toNamesArrayList.add(employee);

                toNamesAdapter.setList(toNamesArrayList);
                toNamesAdapter.notifyDataSetChanged();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addCcEmployee(String userName, String userId) {

        try {
            boolean present = false;


            for (int i = 0; i < ccNamesArrayList.size(); i++) {
                String uid = ccNamesArrayList.get(i).getUser_id();
                if (uid.equals(userId)) {
                    present = true;
                    break;
                }
            }
            for (int i = 0; i < toNamesArrayList.size(); i++) {
                String uid = toNamesArrayList.get(i).getUser_id();
                if (uid.equals(userId)) {
                    present = true;
                    break;
                }
            }

            if (!present) {
                Cc_employess employee = new Cc_employess();
                employee.setName(userName);
                employee.setUser_id(userId);
                ccNamesArrayList.add(employee);

                ccNamesAdapter.setList(ccNamesArrayList);
                ccNamesAdapter.notifyDataSetChanged();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCcNamesRecycleView() {

        try {

            if (ccNamesArrayList != null && ccNamesArrayList.size() > 0) {
                ccNamesAdapter.setList(ccNamesArrayList);
            }


            ccNamesAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {

                case R.id.fromDate:
                    //fromDatePicker();
                    fromDate();
                    break;
                case R.id.toDate:
                    toDate();
                    break;
                case R.id.actionBack:
                    onBackPressed();
                    break;
                case R.id.actionButton1:
                    resetForm();
                    break;
                case R.id.sendRequest:
                    sendRequest.setClickable(false);
                    validateForm();
                    break;
                case R.id.spinnerLayout:
                    leaveType.performClick();
                    break;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onDestroy() {
        Helper.getInstance().detachProgress(this);
        super.onDestroy();
    }

    public void validateForm() {

        try {

            if (resonForLeave.getText().toString().trim().length() == 0) {
                resonForLeave.requestFocus();
                sendRequest.setClickable(true);
                Utilities.ShowSnackbar(LeaveRequestActivity.this, resonForLeave, "Please enter the reason");
                // ToastUtil.showToast(this, "Please enter the reason");

            } else if (fromDate.getText().toString().length() == 0) {
                sendRequest.setClickable(true);
                Utilities.ShowSnackbar(LeaveRequestActivity.this, resonForLeave, "Please select start date");
                // ToastUtil.showToast(this, "Please select start time");

            } else if (toDate.getText().toString().length() == 0) {
                sendRequest.setClickable(true);
                Utilities.ShowSnackbar(LeaveRequestActivity.this, resonForLeave, "Please select end date");
                // ToastUtil.showToast(this, "Please select end time");

            } else {
                if (Utilities.isNetworkAvailable(LeaveRequestActivity.this)) {
                    sendRequest.setClickable(false);
                    callSaveLeave();

                } else {
                    Utilities.ShowSnackbar(LeaveRequestActivity.this, resonForLeave, getResources().getString(R.string.no_internet_connection));
                    //  Toast.makeText(LeaveRequestActivity.this,"Please check network connection",Toast.LENGTH_SHORT).show();
                    sendRequest.setClickable(true);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resetForm() {
        try {
            fromDate.setText("");
            toDate.setText("");
            resonForLeave.setText("");
            totalDays.setText("0 Days");
            frmDt = "";
            toDt = "";
            leaveType.setSelection(0);
            datesArrayList.clear();
            leaveDatesAdapter.setList(datesArrayList);
            leaveDatesAdapter.notifyDataSetChanged();
            halfday_checkbox.setChecked(false);
            days_recyclerview.setVisibility(View.GONE);

            ccNamesArrayList.clear();
            ccNamesAdapter.setList(ccNamesArrayList);
            ccNamesAdapter.notifyDataSetChanged();
            toNamesArrayList.clear();
            toNamesAdapter.setList(toNamesArrayList);
            toNamesAdapter.notifyDataSetChanged();
            callToCc();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        try {

            switch (buttonView.getId()) {
                case R.id.halfday_checkbox:
                    if (isChecked) {
                        days_recyclerview.setVisibility(View.VISIBLE);
                        if (datesArrayList.size() > 0) {
                            //leaveDatesAdapter.setList(datesArrayList);
                            leaveDatesAdapter.notifyDataSetChanged();
                        }
                        checked = true;

                    } else {

                        checked = false;
                        days_recyclerview.setVisibility(View.GONE);
                    }

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCount() {
        try {
            int nOfDays = 0;
            if (fromDate.getText().toString().length() != 0 && toDate.getText().toString().length() != 0) {
                nOfDays = TimeHelper.getInstance().get_count_of_days(frmDt, toDt);
                nOfDays = (int) getDaysCount(frmDt, toDt);
                nOfDays++;
                if (nOfDays >= 1) {
                    totalDays.setText(nOfDays + " Day" + (nOfDays == 1 ? "" : "s"));
                } else if (frmDt.equals(toDt)) {
                    totalDays.setText("1 Day");
                } else {
                    totalDays.setText("0 Days");
                    //fromDate.setText("");
                    // toDate.setText("");
                }
            } else if (fromDate.getText().toString().length() != 0) {
                totalDays.setText("1 Day");
            } else if (toDate.getText().toString().length() != 0) {
                fromDate.setText(toDate.getText().toString());
                totalDays.setText("1 Day");
            } else {
                totalDays.setText("0 Days");
            }
            if (nOfDays > 0) {
                if (halfday_checkbox.isChecked()) {
                    if (days_recyclerview.getVisibility() == View.GONE) {
                        days_recyclerview.setVisibility(View.VISIBLE);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getDaysCount(String startTime, String endTime) {

        try {
            long seconds = 0, mins = 0, days = 0;

            Date startDate, endDate;

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

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


                seconds = different / secondsInMilli;
                mins = different / minutesInMilli;
                days = different / daysInMilli;


            } catch (ParseException e) {
                e.printStackTrace();


            }

            return days;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            switch (v.getId()) {
                case R.id.resonForLeave:
                    if (v.getId() == R.id.resonForLeave) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_UP:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                break;

                            case MotionEvent.ACTION_DOWN:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onDateSet(com.borax12.materialdaterangepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {


        Calendar mcurrentDate = Calendar.getInstance();
        mcurrentDate.set(Calendar.YEAR, year);
        mcurrentDate.set(Calendar.MONTH, monthOfYear);
        mcurrentDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        fromDate.setText(sdf.format(mcurrentDate.getTime()));
        toDate.setText(sdf.format(mcurrentDate.getTime()));
        sharedPreferences.edit().putString("fromDate", sdf1.format(mcurrentDate.getTime()));
        frmDt = sdf1.format(mcurrentDate.getTime());
        toDt = sdf1.format(mcurrentDate.getTime());
        timeInMills = mcurrentDate.getTimeInMillis();
        try {
            if (!toDt.equals("") && toDt.length() > 3)
                updateDatesRecycleView();
                updateCount();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mcurrentDate.set(Calendar.YEAR, yearEnd);
        mcurrentDate.set(Calendar.MONTH, monthOfYearEnd);
        mcurrentDate.set(Calendar.DAY_OF_MONTH,dayOfMonthEnd);


        toDate.setText(sdf.format(mcurrentDate.getTime()));
        toDt = sdf1.format(mcurrentDate.getTime());
        try {
            updateDatesRecycleView();
            updateCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
