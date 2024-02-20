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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tvisha.trooptime.activity.activity.adapter.CcNamesAdapter;
import com.tvisha.trooptime.activity.activity.adapter.SwapNameAdapter;
import com.tvisha.trooptime.activity.activity.adapter.ToNamesAdapter;
import com.tvisha.trooptime.activity.activity.apiPostModels.CcEmpResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.Cc_employess;
import com.tvisha.trooptime.activity.activity.apiPostModels.Leave;
import com.tvisha.trooptime.activity.activity.apiPostModels.LeaveRequestResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.ToAndCcDetailsResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.To_employee;
import com.tvisha.trooptime.activity.activity.apiPostModels.Users;
import com.tvisha.trooptime.activity.activity.apiPostModels.WeekOffsResponse;
import com.tvisha.trooptime.activity.activity.autoComplete.User;
import com.tvisha.trooptime.activity.activity.autoComplete.UserAdapter;
import com.tvisha.trooptime.activity.activity.dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.helper.Helper;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.model.DateModel;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.R;

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


public class SwapActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener
        , View.OnTouchListener {


    public TextView actionLable;
    String USER_ID = "", to = "", date = "", cc = "", userId, email, apiKey, frmDt = "", toDt = "", type = "1", swapUserId = "";
    Button sendRequest;
    EditText resonForLeave;
    ArrayList<Leave> datesArrayList = new ArrayList<>();

    RecyclerView toNamesRecyclerView, ccNamesRecyclerView, swap_recycler_view;
    LinearLayoutManager linearLayoutManager;
    List<Users> usersArrayList = new ArrayList<>();
    RecyclerView.LayoutManager toNamesLayoutManager, ccNamesLayoutManager;
    ToNamesAdapter toNamesAdapter;
    CcNamesAdapter ccNamesAdapter;
    SwapNameAdapter swapNameAdapter;
    List<Cc_employess> ccNamesArrayList = new ArrayList<>();
    List<To_employee> toNamesArrayList = new ArrayList<>();
    List<Users> selectedSwapNamesArrayList = new ArrayList<>();
    ApiInterface apiService;
    UserAdapter userAdapter, ccUsersAdapter, allUsersAdapter;
    AutoCompleteTextView toUserName, ccUserName, swapWithName, swapDate, yourWeekOffDate;
    ArrayList<User> user = new ArrayList<>();
    ArrayList<User> ccUsers = new ArrayList<>();
    ArrayList<User> allUsers = new ArrayList<>();
    String dateFormat1 = "yyyy-MM-dd", dateFormat = "dd MMM yy";
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1, Locale.US);
    long timeInMills = 0;
    boolean checked = false, isSelf = true;
    Intent intent;
    SharedPreferences sharedPreferences;
    String[] items = new String[3];
    CustomProgressBar customProgressBar;
    Typeface poppins_medium;
    Spinner weekOffDateSpinner, swapDateSpinner;
    String week_off_date = "", swap_week_off_date = "";
    List<Users> swapNamesArrayList = new ArrayList<>();

    List<DateModel> weekDateList = new ArrayList<>();
    List<DateModel> swapDateList = new ArrayList<>();

    String[] weekOffDateArray = new String[0];
    String[] swapDateArray = new String[0];
    String swapUserWeekOff = "";

    LinearLayout weekOffDropDownLayout, swapDateDropDownLayout;

    int weekOffDateSelectedPosition = -1, swapDateSelectedPosition = -1;

    public void openProgress() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(SwapActivity.this).isFinishing()) {
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
                        if (!(SwapActivity.this).isFinishing()) {
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
        setContentView(R.layout.activity_swap);
        shareprefernceData();
        initViews();
        initListeners();
        processActivity();
    }

    private void processActivity() {
        try {
            // updateDatesRecycleView();


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
            callToCc();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callToCc() {
        try {
            if (Utilities.isNetworkAvailable(SwapActivity.this)) {

                callToAndCcDetailsApi();

                if (weekDateList == null || weekDateList.size() == 0) {
                    isSelf = true;
                    callGetUserWeekOffs(userId);
                }

                //getAllEmployees();

            } else {
                Toast.makeText(SwapActivity.this, "Please check network connection", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initListeners() {
        try {
            sendRequest.setOnClickListener(this);
            weekOffDropDownLayout.setOnClickListener(this);
            swapDateDropDownLayout.setOnClickListener(this);
            resonForLeave.setOnTouchListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initViews() {

        try {

            Typeface.createFromAsset(getAssets(), "font/poppins/Poppins-Medium.ttf");

            customProgressBar = new CustomProgressBar(SwapActivity.this);
            linearLayoutManager = new LinearLayoutManager(SwapActivity.this, 1, false);
            sendRequest = findViewById(R.id.sendRequest);


            apiService = ApiClient.getInstance();
            actionLable = (TextView) findViewById(R.id.actionLable);
            actionLable.setText("Request Swap");
            toUserName = findViewById(R.id.toUserName);
            ccUserName = findViewById(R.id.ccUserName);
            swapWithName = findViewById(R.id.swapWithName);
            swapDate = findViewById(R.id.swapDate);
            yourWeekOffDate = findViewById(R.id.yourWeekOffDate);
            weekOffDateSpinner = findViewById(R.id.weekOffDateSpinner);
            swapDateSpinner = findViewById(R.id.swapDateSpinner);
            weekOffDropDownLayout = findViewById(R.id.weekOffDropDownLayout);
            swapDateDropDownLayout = findViewById(R.id.swapDateDropDownLayout);


            toNamesRecyclerView = findViewById(R.id.to_recycler_view);
            toNamesRecyclerView.setHasFixedSize(true);
            toNamesLayoutManager = new GridLayoutManager(SwapActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
            toNamesRecyclerView.setLayoutManager(toNamesLayoutManager);
            toNamesAdapter = new ToNamesAdapter(SwapActivity.this, toNamesArrayList);
            toNamesRecyclerView.setAdapter(toNamesAdapter);


            ccNamesRecyclerView = findViewById(R.id.cc_recycler_view);
            ccNamesRecyclerView.setHasFixedSize(true);
            ccNamesLayoutManager = new GridLayoutManager(SwapActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
            ccNamesRecyclerView.setLayoutManager(ccNamesLayoutManager);
            ccNamesAdapter = new CcNamesAdapter(SwapActivity.this, ccNamesArrayList);
            ccNamesRecyclerView.setAdapter(ccNamesAdapter);


            resonForLeave = (EditText) findViewById(R.id.resonForLeave);


            items[0] = "Casual Leave";
            items[1] = "Sick Leave";
            // items[2]="Optional Holiday";
            items[2] = "Optional Leave";

            swap_recycler_view = findViewById(R.id.swap_recycler_view);
            swap_recycler_view.setHasFixedSize(true);
            ccNamesLayoutManager = new GridLayoutManager(SwapActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
            swap_recycler_view.setLayoutManager(ccNamesLayoutManager);
            swapNameAdapter = new SwapNameAdapter(SwapActivity.this, swapNamesArrayList);
            swap_recycler_view.setAdapter(swapNameAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void callSaveSwap() {
        try {
            openProgress();
            if(toNamesArrayList!=null && toNamesArrayList.size()>0) {


                for (int i = 0; i < toNamesArrayList.size(); i++) {
                    if (i == toNamesArrayList.size() - 1) {
                        to = to + toNamesArrayList.get(i).getUser_id();
                    } else {
                        to = to + toNamesArrayList.get(i).getUser_id() + ",";
                    }

                }
            }else
            {
                Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, getResources().getString(R.string.no_to_users_msg));
                sendRequest.setClickable(true);
                return;
            }

            for (int i = 0; i < ccNamesArrayList.size(); i++) {
                if (i == ccNamesArrayList.size() - 1) {
                    cc = cc + ccNamesArrayList.get(i).getUser_id();
                } else {
                    cc = cc + ccNamesArrayList.get(i).getUser_id() + ",";
                }

            }


            Call<LeaveRequestResponse> call = apiService.SaveSwapRequest(apiKey, userId, to, cc, type, resonForLeave.getText().toString().trim(), week_off_date, swap_week_off_date, "", "4", swapUserId);
            call.enqueue(new Callback<LeaveRequestResponse>() {
                @Override
                public void onResponse(Call<LeaveRequestResponse> call, Response<LeaveRequestResponse> response) {
                    LeaveRequestResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {
                            Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, apiResponse.getMessage());
                            Intent intent = new Intent(SwapActivity.this, RequestActivity.class);
                            intent.putExtra("type", "1");
                            intent.putExtra("requestType", "");
                            intent.putExtra("fromDate", "");
                            intent.putExtra("toDate", "");
                            intent.putExtra("users", "");
                            startActivity(intent);
                            finish();

                        } else {

                            Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, apiResponse.getMessage());
                        }

                    } else {
                        closeProgress();

                        sendRequest.setClickable(true);

                        Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, getResources().getString(R.string.somthing_went_wrong));
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

    private void initWeekOffSpinner() {
        try {

            if (weekDateList != null && weekDateList.size() > 0) {
                weekOffDateArray = new String[weekDateList.size()];

                for (int i = 0; i < weekDateList.size(); i++) {
                    weekOffDateArray[i] = weekDateList.get(i).getDisplayDate();
                }

          /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.item_spiner, weekOffDateArray) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTypeface(poppins_medium);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.req_text_color));
                    return v;
                }


                public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                    View v =super.getDropDownView(position, convertView, parent);
                    ((TextView) v).setTypeface(poppins_medium);
                    ((TextView) v).setTextColor(getResources().getColor(R.color.req_text_color));
                    return v;
                }
            };*/

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(SwapActivity.this, R.layout.date_spinner_item, weekOffDateArray);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                weekOffDateSpinner.setAdapter(adapter);
                weekOffDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        String item = adapterView.getItemAtPosition(i).toString();
                        weekOffDateSelectedPosition = i;
                        if (weekDateList != null && weekDateList.size() > 0) {
                            week_off_date = weekDateList.get(i).getDate();
                        }

                        yourWeekOffDate.setText(week_off_date);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSwapWeekOffSpinner() {

        try {


            if (swapDateList != null && swapDateList.size() > 0) {
                swapDateArray = new String[swapDateList.size()];

                for (int i = 0; i < swapDateList.size(); i++) {
                    swapDateArray[i] = swapDateList.get(i).getDisplayDate();
                }


                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(SwapActivity.this, R.layout.date_spinner_item, swapDateArray);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                swapDateSpinner.setAdapter(adapter);

                swapDateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        String item = adapterView.getItemAtPosition(i).toString();
                        swapDateSelectedPosition = i;
                        if (swapDateList != null && swapDateList.size() > 0) {
                            swap_week_off_date = swapDateList.get(i).getDate();
                        }

                        swapDate.setText(swap_week_off_date);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
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

            userAdapter = new UserAdapter(SwapActivity.this, user, new UserAdapter.UserAdapterListener() {
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

            ccUsersAdapter = new UserAdapter(SwapActivity.this, ccUsers, new UserAdapter.UserAdapterListener() {
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

            allUsersAdapter = new UserAdapter(SwapActivity.this, allUsers, new UserAdapter.UserAdapterListener() {
                @Override
                public void onResult(User user) {


                    swapWithName.setText("");
                    USER_ID = user.getUserId();
                    swapWithName.clearListSelection();
                    swapWithName.dismissDropDown();
                    if (!userId.equals(USER_ID))
                        addSwapEmployee(user.getName(), user.getUserId());
                }
            });
            swapWithName.setAdapter(allUsersAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void fromDate() {

        try {
            final Calendar mcurrentDate = Calendar.getInstance();
            int mYear = mcurrentDate.get(Calendar.YEAR);
            int mMonth = mcurrentDate.get(Calendar.MONTH);
            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog mDatePicker = new DatePickerDialog(
                    SwapActivity.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker,
                                      int selectedyear, int selectedmonth,
                                      int selectedday) {

                    mcurrentDate.set(Calendar.YEAR, selectedyear);
                    mcurrentDate.set(Calendar.MONTH, selectedmonth);
                    mcurrentDate.set(Calendar.DAY_OF_MONTH,
                            selectedday);
                    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);


                    sharedPreferences.edit().putString("fromDate", sdf1.format(mcurrentDate.getTime()));
                    frmDt = sdf1.format(mcurrentDate.getTime());
                    toDt = sdf1.format(mcurrentDate.getTime());
                    timeInMills = mcurrentDate.getTimeInMillis();
                    try {
                        if (!toDt.equals("") && toDt.length() > 3)
                            updateDatesRecycleView();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }, mYear, mMonth, mDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateDatesRecycleView() throws ParseException {

        try {

            datesArrayList.clear();
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
                Toast.makeText(SwapActivity.this, "To date should not lessthan From date", Toast.LENGTH_SHORT).show();
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

                    apiDate = frmDt;
                } else if (i == days - 1) {

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
                    SwapActivity.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker,
                                      int selectedyear, int selectedmonth,
                                      int selectedday) {

                    mcurrentDate.set(Calendar.YEAR, selectedyear);
                    mcurrentDate.set(Calendar.MONTH, selectedmonth);
                    mcurrentDate.set(Calendar.DAY_OF_MONTH,
                            selectedday);
                    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);


                    toDt = sdf1.format(mcurrentDate.getTime());
                    try {
                        updateDatesRecycleView();

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

    public String getDisplayDate(String date) throws ParseException {
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            Date nextDate = c.getTime();
            return sdf.format(c.getTime());
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

    private void callGetUserWeekOffs(String userId) {

        try {


            // openProgress();

            if (isSelf) {
                if (weekDateList != null && weekDateList.size() > 0) {
                    weekDateList.clear();
                }
            } else {
                if (swapDateList != null && swapDateList.size() > 0) {
                    swapDateList.clear();
                }
            }

            Call<WeekOffsResponse> call = apiService.getWeekOffDates(apiKey, userId);
            call.enqueue(new Callback<WeekOffsResponse>() {
                @Override
                public void onResponse(Call<WeekOffsResponse> call, Response<WeekOffsResponse> response) {
                    WeekOffsResponse apiResponse = response.body();

                    closeProgress();

                    if (apiResponse != null) {
                        // Toast.makeText(SwapActivity.this,apiResponse.getMessage(),Toast.LENGTH_LONG).show();
                        if (apiResponse.isSuccess()) {

                            if (apiResponse.getWeekOffDays() != null && apiResponse.getWeekOffDays().size() > 0) {
                                setweekOffDates(apiResponse.getWeekOffDays());
                            }


                        }
                    }

                }

                @Override
                public void onFailure(Call<WeekOffsResponse> call, Throwable t) {
                    closeProgress();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setweekOffDates(List<String> weekOffDays) {

        // weekOffDays="2019-01-26,2019-02-23,2019-03-23,2019-02-02,2019-03-02,2019-02-09,2019-03-09,2019-02-16,2019-03-16,2019-01-27,2019-02-24,2019-02-03,2019-03-03,2019-02-10,2019-03-10,2019-02-17,2019-03-17";

        try {

            if (weekOffDays != null && weekOffDays.size() > 0) {

                for (int i = 0; i < weekOffDays.size(); i++) {
                    String weekOff = weekOffDays.get(i);
                    DateModel dateModel = new DateModel();
                    dateModel.setDate(weekOff);
                    try {
                        dateModel.setDisplayDate(getDisplayDate(weekOff));
                        if (isSelf) {
                            weekDateList.add(dateModel);
                            initWeekOffSpinner();
                        } else {
                            swapDateList.add(dateModel);
                            initSwapWeekOffSpinner();
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAllEmployees() {

        try {

            Call<CcEmpResponse> call = apiService.getAllEmployess(apiKey, userId);
            call.enqueue(new Callback<CcEmpResponse>() {
                @Override
                public void onResponse(Call<CcEmpResponse> call, Response<CcEmpResponse> response) {
                    CcEmpResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        Toast.makeText(SwapActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        if (apiResponse.isSuccess()) {
                        }
                    }

                }

                @Override
                public void onFailure(Call<CcEmpResponse> call, Throwable t) {


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
            if (swapNamesArrayList != null && swapNamesArrayList.size() > 0) {
                swapNamesArrayList.clear();
            }

            if (selectedSwapNamesArrayList != null && selectedSwapNamesArrayList.size() > 0) {
                selectedSwapNamesArrayList.clear();
            }

            if (user != null && user.size() > 0) {
                user.clear();
            }

            if (ccUsers != null && ccUsers.size() > 0) {
                ccUsers.clear();
            }

            if (allUsers != null && allUsers.size() > 0) {
                allUsers.clear();
            }


            usersArrayList = apiResponse.getUsers();
            toNamesArrayList = apiResponse.getTo_employees();
            ccNamesArrayList = apiResponse.getCc_employess();
            swapNamesArrayList = apiResponse.getUsers();

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

            if (swapNamesArrayList != null && swapNamesArrayList.size() > 0) {
                for (int i = 0; i < swapNamesArrayList.size(); i++) {
                    String uid = swapNamesArrayList.get(i).getUser_id();
                    String name = swapNamesArrayList.get(i).getName();
                    String displayName = swapNamesArrayList.get(i).getDisplay_name();
                    String email = swapNamesArrayList.get(i).getEmail();
                    User obj = new User(uid, name, displayName, email);

                    if (!uid.equals(userId)) {
                        allUsers.add(obj);
                    }

                }
            }


            updateToNamesRecycleView();
            updateCcNamesRecycleView();
            //updateSwapNamesRecycleView();
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

    private void addSwapEmployee(String userName, String userId) {

        try {

            Helper.getInstance().closeKeyBoard(SwapActivity.this, swapWithName);
            boolean present = false;

            if (selectedSwapNamesArrayList.size() > 0) {
                removeSwapEmployee(0);
            }


            for (int i = 0; i < selectedSwapNamesArrayList.size(); i++) {
                String uid = selectedSwapNamesArrayList.get(i).getUser_id();
                if (uid.equals(userId)) {
                    present = true;
                    break;
                }
            }


            if (!present) {
                swapUserId = userId;
                swap_recycler_view.setVisibility(View.VISIBLE);
                Users employee = new Users();
                employee.setName(userName);
                employee.setUser_id(userId);
                selectedSwapNamesArrayList.add(employee);
                swapNameAdapter.setList(selectedSwapNamesArrayList);
                swapNameAdapter.notifyDataSetChanged();
                swapWithName.setVisibility(View.GONE);
                if (Utilities.isNetworkAvailable(SwapActivity.this)) {


                    isSelf = false;
                    callGetUserWeekOffs(userId);
                    //  getAllEmployees();

                } else {
                    Toast.makeText(SwapActivity.this, "Please check network connection", Toast.LENGTH_SHORT).show();
                }
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

    private void updateSwapNamesRecycleView() {

        try {

            if (swapNamesArrayList != null && swapNamesArrayList.size() > 0) {
                swapNameAdapter.setList(swapNamesArrayList);
                swapNameAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {

                case R.id.fromDate:
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

                case R.id.swapDateDropDownLayout:
                    if (swapDateList != null && swapDateList.size() > 0) {
                        swapDateSpinner.performClick();
                    } else if (selectedSwapNamesArrayList != null && selectedSwapNamesArrayList.size() == 0) {
                        Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, "Please select Employee Name");
                    } else {
                        //  Utilities.ShowSnackbar(SwapActivity.this,resonForLeave,"No week offs available");
                    }

                    break;
                case R.id.weekOffDropDownLayout:
                    if (weekDateList != null && weekDateList.size() > 0) {
                        weekOffDateSpinner.performClick();
                    } else {
                        Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, "No week offs available");
                    }
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

      /*  if (resonForLeave.getText().toString().length() == 0) {
            resonForLeave.requestFocus();
            sendRequest.setClickable(true);
            ToastUtil.showToast(this, "Please enter the reason");

        } else if (week_off_date.trim().length() == 0) {
            sendRequest.setClickable(true);
            ToastUtil.showToast(this, "Please select start time");

        }
        else if (week_off_date.trim().length() == 0) {
            sendRequest.setClickable(true);
            ToastUtil.showToast(this, "Please select start time");

        }else if (swap_week_off_date.trim().length() == 0) {
            sendRequest.setClickable(true);
            ToastUtil.showToast(this, "Please select end time");

        }
        else
        {
            if(Utilities.isNetworkAvailable(SwapActivity.this))
            {
                sendRequest.setClickable(false);
                callSaveSwap();

            }
            else
            {
                Toast.makeText(SwapActivity.this,"Please check network connection",Toast.LENGTH_SHORT).show();
                sendRequest.setClickable(true);
            }

        }*/

        try {

            String reason = resonForLeave.getText().toString().trim();


            if (weekDateList != null && weekDateList.size() == 0) {
                //Toast.makeText(SwapActivity.this,"No week offs available",Toast.LENGTH_LONG).show();
                Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, "No week offs available");
                sendRequest.setClickable(true);
            } else if (week_off_date.equals("") || week_off_date.length() < 1) {
                //makeToast("Please select your week off date");
                Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, "Please select your week off date");
                sendRequest.setClickable(true);

            } else if (selectedSwapNamesArrayList != null && selectedSwapNamesArrayList.size() == 0) {
                // makeToast("Please select swap with name");
                Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, "Please select Employee Name");
                sendRequest.setClickable(true);
            } else if (swapDateList != null && swapDateList.size() == 0) {
                //Toast.makeText(SwapActivity.this,"No week offs available",Toast.LENGTH_LONG).show();
                Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, "No week offs available");
                sendRequest.setClickable(true);
            } else if (swap_week_off_date.equals("") || swap_week_off_date.length() < 1) {
                // makeToast("Please select swap date");
                Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, "Please select swap date");
                sendRequest.setClickable(true);

            } else if (swapUserId == null || swapUserId.equals("")) {
                // makeToast("Please select swap with user name");
                Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, "Please select Employee Name");
                sendRequest.setClickable(true);
            } else if (week_off_date.equals(swap_week_off_date)) {
                //makeToast("Your week off date and swap date must be different");
                Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, "Your week off date and swap date must be different");
                sendRequest.setClickable(true);

            } else if (reason.equals("") || reason.length() < 1) {
                // makeToast("Please write comment");
                Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, "Please enter reason");
                sendRequest.setClickable(true);

            } else {

                if (Utilities.isNetworkAvailable(SwapActivity.this)) {
                    sendRequest.setClickable(false);
                    callSaveSwap();

                } else {
                    //  Toast.makeText(SwapActivity.this,"Please check network connection",Toast.LENGTH_SHORT).show();
                    Utilities.ShowSnackbar(SwapActivity.this, resonForLeave, getResources().getString(R.string.no_internet_connection));
                    sendRequest.setClickable(true);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makeToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SwapActivity.this, msg, Toast.LENGTH_LONG).show();

            }
        });
    }

    public void resetForm() {
        try {


            resonForLeave.setText("");

            week_off_date = "";
            swap_week_off_date = "";
            datesArrayList.clear();

            swapWithName.setText("");
            swapWithName.setVisibility(View.VISIBLE);
            swapUserId = "";

            ccNamesArrayList.clear();
            ccNamesAdapter.setList(ccNamesArrayList);
            ccNamesAdapter.notifyDataSetChanged();

            toNamesArrayList.clear();
            toNamesAdapter.setList(toNamesArrayList);
            toNamesAdapter.notifyDataSetChanged();

            selectedSwapNamesArrayList.clear();
            swapNameAdapter.setList(selectedSwapNamesArrayList);
            swapNameAdapter.notifyDataSetChanged();


            swapDateSpinner.setAdapter(null);


            if (swapDateList != null && swapDateList.size() > 0) {
                swapDateList.clear();
            }

            /*weekOffDateSpinner.setAdapter(null);
            if(weekDateList!=null && weekDateList.size()>0){
                weekDateList.clear();
            }

            if(weekDateList!=null && weekDateList.size()>0){
                initWeekOffSpinner();
            }
            else
            {
                weekOffDateSpinner.setAdapter(null);
            }*/
            if (weekOffDateSpinner != null && weekDateList != null && weekDateList.size() > 0) {
                weekOffDateSpinner.setSelection(0);
                week_off_date=weekDateList.get(0).getDate();

            }

            callToCc();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void removeSwapEmployee(int i) {

        try {
            if (selectedSwapNamesArrayList != null && selectedSwapNamesArrayList.size() > 0) {
                if (i != -1) {
                    selectedSwapNamesArrayList.remove(i);
                    swapUserId = "";
                    if (swapNameAdapter != null) {
                        swapNameAdapter.notifyDataSetChanged();
                    }
                    if (i == 0) {
                        swap_recycler_view.setVisibility(View.GONE);
                        swapWithName.setVisibility(View.VISIBLE);

                    }


                    swapDateSpinner.setAdapter(null);
                    swapDateList.clear();
                    swap_week_off_date = "";
                    swapUserWeekOff = "";
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
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
}
