package com.tvisha.trooptime.activity.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tvisha.trooptime.activity.activity.adapter.CcNamesAdapter;
import com.tvisha.trooptime.activity.activity.adapter.ToNamesAdapter;
import com.tvisha.trooptime.activity.activity.apiPostModels.Cc_employess;
import com.tvisha.trooptime.activity.activity.apiPostModels.LeaveRequestResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.ToAndCcDetailsResponse;
import com.tvisha.trooptime.activity.activity.apiPostModels.To_employee;
import com.tvisha.trooptime.activity.activity.apiPostModels.Users;
import com.tvisha.trooptime.activity.activity.autoComplete.User;
import com.tvisha.trooptime.activity.activity.autoComplete.UserAdapter;
import com.tvisha.trooptime.activity.activity.helper.Helper;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.TimeHelper;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RequestCompoffActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    EditText resonForPermission;
    TextView actionLable, dayWorkedDate, compOffDate;
    LinearLayout dayWorkedLayout, compOffLayout;
    Button sendRequest;

    String login_user_id, USER_ID = "", dayWorked = "", compOff = "", to = "", cc = "";
    ;
    List<Users> usersArrayList = new ArrayList<>();
    RecyclerView.LayoutManager toNamesLayoutManager;
    ToNamesAdapter toNamesAdapter;
    RecyclerView.LayoutManager ccNamesLayoutManager;
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
    SharedPreferences sharedPreferences;
    String userId, email, apiKey;
    private RecyclerView toNamesRecyclerView;
    private RecyclerView ccNamesRecyclerView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reuestcompoff_layout);

        TimeHelper.getInstance().timeDifference(null, null);

        initViews();
        initListeners();
        callToCC();

    }

    private void callToCC() {

        if (Utilities.isNetworkAvailable(RequestCompoffActivity.this)) {
            callToAndCcDetailsApi();
        } else {
            Toast.makeText(RequestCompoffActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
        }

    }

    private void initListeners() {
        try {
            sendRequest.setOnClickListener(this);
            dayWorkedLayout.setOnClickListener(this);
            compOffLayout.setOnClickListener(this);
            resonForPermission.setOnTouchListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {

        try {

            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            shareprefernceData();
            login_user_id = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");

            actionLable = (TextView) findViewById(R.id.actionLable);
            dayWorkedLayout = findViewById(R.id.dayWorkedLayout);
            compOffLayout = findViewById(R.id.compOffLayout);
            dayWorkedDate = findViewById(R.id.dayWorkedDate);
            compOffDate = findViewById(R.id.compOffDate);
            sendRequest = findViewById(R.id.submitForm);
            resonForPermission = (EditText) findViewById(R.id.resonForPermission);
            apiService = ApiClient.getInstance();
            toUserName = findViewById(R.id.toUserName);
            ccUserName = findViewById(R.id.ccUserName);

            actionLable.setText("Comp Off Permission");

            toNamesRecyclerView = findViewById(R.id.to_recycler_view);
            toNamesRecyclerView.setHasFixedSize(true);
            toNamesLayoutManager = new GridLayoutManager(RequestCompoffActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
            toNamesRecyclerView.setLayoutManager(toNamesLayoutManager);
            toNamesAdapter = new ToNamesAdapter(RequestCompoffActivity.this, toNamesArrayList);
            toNamesRecyclerView.setAdapter(toNamesAdapter);


            ccNamesRecyclerView = findViewById(R.id.cc_recycler_view);
            ccNamesRecyclerView.setHasFixedSize(true);
            ccNamesLayoutManager = new GridLayoutManager(RequestCompoffActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
            ccNamesRecyclerView.setLayoutManager(ccNamesLayoutManager);
            ccNamesAdapter = new CcNamesAdapter(RequestCompoffActivity.this, ccNamesArrayList);
            ccNamesRecyclerView.setAdapter(ccNamesAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.actionBack:
                    onBackPressed();
                    break;

                case R.id.submitForm:
                    sendRequest.setClickable(false);
                    validateForm();
                    break;
                case R.id.actionButton1:
                    resetForm();
                    break;
                case R.id.dayWorkedLayout:
                    dayWorkedDate();
                    break;
                case R.id.compOffLayout:
                    compOffDate();
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

            if (toNamesArrayList.size() == 0) {
                sendRequest.setClickable(true);
                // Toast.makeText(RequestCompoffActivity.this,"Please add one member to TO",Toast.LENGTH_SHORT).show();
                Utilities.ShowSnackbar(RequestCompoffActivity.this, resonForPermission, "Please add one member to TO");
            } else if (dayWorked.equals("") || dayWorked.length() < 1) {
                sendRequest.setClickable(true);
                // Toast.makeText(RequestCompoffActivity.this,"Please select  day worked date",Toast.LENGTH_SHORT).show();
                Utilities.ShowSnackbar(RequestCompoffActivity.this, resonForPermission, "Please select day worked date");
            } else if (compOff.equals("") || compOff.length() < 1) {
                sendRequest.setClickable(true);
                // Toast.makeText(RequestCompoffActivity.this,"Please select compoff date",Toast.LENGTH_SHORT).show();
                Utilities.ShowSnackbar(RequestCompoffActivity.this, resonForPermission, "Please select comp off date");
            } else if (dayWorked.equals(compOff)) {
                sendRequest.setClickable(true);
                //Toast.makeText(RequestCompoffActivity.this,"Day worked and Comp off date must be different",Toast.LENGTH_SHORT).show();
                Utilities.ShowSnackbar(RequestCompoffActivity.this, resonForPermission, "Day worked and Comp off date must be different");
            } else if (resonForPermission.getText().toString().trim().length() == 0) {
                sendRequest.setClickable(true);
                //  Toast.makeText(RequestCompoffActivity.this,"Please write reason",Toast.LENGTH_SHORT).show();
                Utilities.ShowSnackbar(RequestCompoffActivity.this, resonForPermission, "Please enter reason");
            } else {
                if (Utilities.isNetworkAvailable(RequestCompoffActivity.this)) {
                    callSaveCompOff();
                } else {
                    sendRequest.setClickable(true);
                    //Toast.makeText(RequestCompoffActivity.this,"No internet connection",Toast.LENGTH_SHORT).show();
                    Utilities.ShowSnackbar(RequestCompoffActivity.this, resonForPermission, getResources().getString(R.string.no_internet_connection));
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void resetForm() {
        try {
            dayWorkedDate.setText("");
            compOffDate.setText("");
            resonForPermission.setText("");
            dayWorked = "";
            compOff = "";
            ccNamesArrayList.clear();
            ccNamesAdapter.setList(ccNamesArrayList);
            ccNamesAdapter.notifyDataSetChanged();
            toNamesArrayList.clear();
            toNamesAdapter.setList(toNamesArrayList);
            toNamesAdapter.notifyDataSetChanged();
            callToCC();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callSaveCompOff() {
        try {
            Helper.getInstance().openProgress(RequestCompoffActivity.this);
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
                sendRequest.setClickable(true);
                Utilities.ShowSnackbar(RequestCompoffActivity.this, resonForPermission, getResources().getString(R.string.no_to_users_msg));
                return;
            }
            for (int i = 0; i < ccNamesArrayList.size(); i++) {
                if (i == ccNamesArrayList.size() - 1) {
                    cc = cc + ccNamesArrayList.get(i).getUser_id();
                } else {
                    cc = cc + ccNamesArrayList.get(i).getUser_id() + ",";
                }

            }

            Call<LeaveRequestResponse> call = apiService.SaveCompOffRequest(apiKey, userId, to, cc, resonForPermission.getText().toString().trim(), dayWorked, compOff);
            call.enqueue(new Callback<LeaveRequestResponse>() {
                @Override
                public void onResponse(Call<LeaveRequestResponse> call, Response<LeaveRequestResponse> response) {
                    LeaveRequestResponse apiResponse = response.body();
                    Helper.getInstance().closeProgress(RequestCompoffActivity.this);
                    if (apiResponse != null) {
                        if (apiResponse.isSuccess()) {
                            Utilities.ShowSnackbar(RequestCompoffActivity.this, resonForPermission, apiResponse.getMessage());
                            Intent intent = new Intent(RequestCompoffActivity.this, RequestActivity.class);
                            intent.putExtra("type", "1");
                            intent.putExtra("requestType", "");
                            intent.putExtra("fromDate", "");
                            intent.putExtra("toDate", "");
                            intent.putExtra("users", "");
                            startActivity(intent);


                            // Toast.makeText(RequestCompoffActivity.this,apiResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        } else {
                            sendRequest.setClickable(true);
                            Utilities.ShowSnackbar(RequestCompoffActivity.this, resonForPermission, apiResponse.getMessage());
                        }

                    } else {
                        sendRequest.setClickable(true);
                        Utilities.ShowSnackbar(RequestCompoffActivity.this, resonForPermission, getResources().getString(R.string.somthing_went_wrong));
                    }

                }

                @Override
                public void onFailure(Call<LeaveRequestResponse> call, Throwable t) {
                    sendRequest.setClickable(true);
                    Helper.getInstance().closeProgress(RequestCompoffActivity.this);

                }

            });
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

            userAdapter = new UserAdapter(RequestCompoffActivity.this, user, new UserAdapter.UserAdapterListener() {
                @Override
                public void onResult(User user) {


                    // toUserName.setCursorVisible(false);

                    if (toUserName.hasFocus()) {

                        // toUserName.setCursorVisible(false);
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

            ccUsersAdapter = new UserAdapter(RequestCompoffActivity.this, ccUsers, new UserAdapter.UserAdapterListener() {
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

    private void updateToNamesRecycleView() {

        try {

            if (toNamesArrayList != null && toNamesArrayList.size() > 0) {
                toNamesAdapter.setList(toNamesArrayList);
                toNamesAdapter.notifyDataSetChanged();
            } else {
                sendRequest.setClickable(false);
                sendRequest.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }

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

    private void compOffDate() {
        try {
            final Calendar mcurrentDate = Calendar.getInstance();
            int cYear = mcurrentDate.get(Calendar.YEAR);
            int cMonth = mcurrentDate.get(Calendar.MONTH);
            int cDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
            mcurrentDate.add(Calendar.MONTH, -3);
            int mMonth = mcurrentDate.get(Calendar.MONTH);
            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
            int mYear = mcurrentDate.get(Calendar.YEAR);
            long pastTime = mcurrentDate.getTimeInMillis();

            final Calendar mcurrentDate1 = Calendar.getInstance();
            mcurrentDate1.add(Calendar.MONTH, 3);
            long futureTime = mcurrentDate1.getTimeInMillis();



            DatePickerDialog mDatePicker = new DatePickerDialog(
                    RequestCompoffActivity.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker,
                                      int selectedyear, int selectedmonth,
                                      int selectedday) {

                    mcurrentDate.set(Calendar.YEAR, selectedyear);
                    mcurrentDate.set(Calendar.MONTH, selectedmonth);
                    mcurrentDate.set(Calendar.DAY_OF_MONTH,
                            selectedday);
                    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
                    compOff = sdf1.format(mcurrentDate.getTime());
                    compOffDate.setText(sdf.format(mcurrentDate
                            .getTime()));
                }
            }, mYear, mMonth, mDay);

            mDatePicker.getDatePicker().setMaxDate(futureTime);
            mDatePicker.getDatePicker().updateDate(cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMinDate(pastTime);

            mDatePicker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dayWorkedDate() {
        try {
            final Calendar mcurrentDate = Calendar.getInstance();
            int cYear = mcurrentDate.get(Calendar.YEAR);
            int cMonth = mcurrentDate.get(Calendar.MONTH);
            int cDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
            mcurrentDate.add(Calendar.MONTH, -3);
            int mMonth = mcurrentDate.get(Calendar.MONTH);
            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
            int mYear = mcurrentDate.get(Calendar.YEAR);
            long pastTime = mcurrentDate.getTimeInMillis();


            DatePickerDialog mDatePicker = new DatePickerDialog(
                    RequestCompoffActivity.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker,
                                      int selectedyear, int selectedmonth,
                                      int selectedday) {

                    mcurrentDate.set(Calendar.YEAR, selectedyear);
                    mcurrentDate.set(Calendar.MONTH, selectedmonth);
                    mcurrentDate.set(Calendar.DAY_OF_MONTH,
                            selectedday);
                    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
                    dayWorked = sdf1.format(mcurrentDate.getTime());

                    dayWorkedDate.setText(sdf.format(mcurrentDate
                            .getTime()));
                }
            }, mYear, mMonth, mDay);

            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.getDatePicker().updateDate(cYear, cMonth, cDay);
            mDatePicker.getDatePicker().setMinDate(pastTime);
            mDatePicker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void shareprefernceData() {
        try {
            userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            email = sharedPreferences.getString(SharePreferenceKeys.USER_EMAIL, "");
            apiKey = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            switch (v.getId()) {
                case R.id.resonForPermission:
                    if (v.getId() == R.id.resonForPermission) {
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
