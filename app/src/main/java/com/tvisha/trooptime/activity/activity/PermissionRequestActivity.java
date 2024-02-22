package com.tvisha.trooptime.activity.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PermissionRequestActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {

    EditText resonForPermission;
    TextView actionLable, permisionDate, selectTime, fromTime, toTime;
    String USER_ID = "", selectedDate = "", early, other, late,
            token = "", type = "", reason = "", to = "", cc = "", userId, email, apiKey;
    LinearLayout earlyLogoutLayout, extendedLogoutLayout, lateLoginLayout, dateLayout, selectTimeLayout, fromToLayout, fromTimeLayout, toTimeLayout;
    ImageView earlyLogoutImg, extendedLogoutImg, lateLoginImg;
    Button sendRequest;
    long curTimeInMills = 0;
    List<Users> usersArrayList = new ArrayList<>();
    RecyclerView toNamesRecyclerView, ccNamesRecyclerView;
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
    ;
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
    SimpleDateFormat sdf1 = new SimpleDateFormat(dateFormat1, Locale.US);
    int startHour = -1, startMin = -1, endHour = -1, endMin = -1;
    SharedPreferences sharedPreferences;
    CustomProgressBar customProgressBar;

    public void openProgress() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(PermissionRequestActivity.this).isFinishing()) {
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
                        if (!(PermissionRequestActivity.this).isFinishing()) {
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
        setContentView(R.layout.activity_permission);

        try {

            shareprefernceData();
            initViews();
            initListeners();
            TimeHelper.getInstance().timeDifference(null, null);
            Calendar calendar = Calendar.getInstance();
            permisionDate.setText(sdf.format(calendar.getTime()));
            selectedDate = sdf1.format(calendar.getTime());

            callToCC();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callToCC() {

        try {

            if (Utilities.isNetworkAvailable(PermissionRequestActivity.this)) {

                callToAndCcDetailsApi();

            } else {
                Toast.makeText(PermissionRequestActivity.this, "Please check network connection", Toast.LENGTH_SHORT).show();
            }
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
                case R.id.selectTimeLayout:
                    selectTime();
                    break;
                case R.id.fromTimeLayout:
                    fromTime();
                    break;
                case R.id.toTimeLayout:
                    toTime();
                    break;
                case R.id.earlyLogoutLayout:
                    selectTime.setText("");
                    earlyLogout();
                    break;
                case R.id.extendedLogoutLayout:
                    extendedLogout();
                    break;
                case R.id.lateLoginLayout:
                    selectTime.setText("");
                    lateLogin();
                    break;
                case R.id.dateLayout:
                    date();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        try {
            sendRequest.setOnClickListener(this);
            selectTimeLayout.setOnClickListener(this);
            fromTimeLayout.setOnClickListener(this);
            toTimeLayout.setOnClickListener(this);
            earlyLogoutLayout.setOnClickListener(this);
            extendedLogoutLayout.setOnClickListener(this);
            lateLoginLayout.setOnClickListener(this);
            dateLayout.setOnClickListener(this);
            resonForPermission.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        try {
            customProgressBar = new CustomProgressBar(PermissionRequestActivity.this);
            apiService = new ApiClient().getInstance();
            actionLable = (TextView) findViewById(R.id.actionLable);
            actionLable.setText("Request Permission");
            sendRequest = findViewById(R.id.submitForm);
            permisionDate = findViewById(R.id.permisionDate);
            resonForPermission = (EditText) findViewById(R.id.resonForPermission);
            earlyLogoutLayout = findViewById(R.id.earlyLogoutLayout);
            dateLayout = findViewById(R.id.dateLayout);
            extendedLogoutLayout = findViewById(R.id.extendedLogoutLayout);
            lateLoginLayout = findViewById(R.id.lateLoginLayout);
            earlyLogoutImg = findViewById(R.id.earlyLogoutImg);
            extendedLogoutImg = findViewById(R.id.extendedLogoutImg);
            lateLoginImg = findViewById(R.id.lateLoginImg);
            selectTimeLayout = findViewById(R.id.selectTimeLayout);
            fromTimeLayout = findViewById(R.id.fromTimeLayout);
            toTimeLayout = findViewById(R.id.toTimeLayout);
            fromToLayout = findViewById(R.id.fromToLayout);
            fromTime = findViewById(R.id.fromTime);
            toTime = findViewById(R.id.toTime);
            selectTime = findViewById(R.id.selectTime);
            toUserName = findViewById(R.id.toUserName);
            ccUserName = findViewById(R.id.ccUserName);

            toNamesRecyclerView = findViewById(R.id.to_recycler_view);
            toNamesRecyclerView.setHasFixedSize(true);
            toNamesLayoutManager = new GridLayoutManager(PermissionRequestActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
            toNamesRecyclerView.setLayoutManager(toNamesLayoutManager);
            toNamesAdapter = new ToNamesAdapter(PermissionRequestActivity.this, toNamesArrayList);
            toNamesRecyclerView.setAdapter(toNamesAdapter);


            ccNamesRecyclerView = findViewById(R.id.cc_recycler_view);
            ccNamesRecyclerView.setHasFixedSize(true);
            ccNamesLayoutManager = new GridLayoutManager(PermissionRequestActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
            ccNamesRecyclerView.setLayoutManager(ccNamesLayoutManager);
            ccNamesAdapter = new CcNamesAdapter(PermissionRequestActivity.this, ccNamesArrayList);
            ccNamesRecyclerView.setAdapter(ccNamesAdapter);
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
            mTimePicker = new TimePickerDialog(PermissionRequestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(android.widget.TimePicker timePicker, int selectedHour, int selectedMinute) {


                    if (selectTime != null) {
                        if (selectedMinute < 10 && selectedHour < 10) {
                            selectTime.setText("0" + selectedHour + ":" + "0" + selectedMinute);
                        } else if (selectedHour < 10) {
                            selectTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else if (selectedMinute < 10) {
                            selectTime.setText(selectedHour + ":" + "0" + selectedMinute);
                        } else {
                            selectTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }

                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select End Time");
            mTimePicker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toTime() {
        try {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(PermissionRequestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(android.widget.TimePicker timePicker, int selectedHour, int selectedMinute) {

                    if (startHour != -1 && startMin != -1) {
                        boolean isValid = false;
                        if (selectedHour > startHour) {
                            isValid = true;
                        }
                        if (selectedHour == startHour && selectedMinute > startMin) {
                            isValid = true;
                        }

                        if (isValid) {
                            endHour = selectedHour;
                            endMin = selectedMinute;
                            if (selectedMinute < 10 && selectedHour < 10) {
                                toTime.setText("0" + selectedHour + ":" + "0" + selectedMinute);
                            } else if (selectedHour < 10) {
                                toTime.setText("0" + selectedHour + ":" + selectedMinute);
                            } else if (selectedMinute < 10) {
                                toTime.setText(selectedHour + ":" + "0" + selectedMinute);
                            } else {
                                toTime.setText(selectedHour + ":" + selectedMinute);
                            }
                        } else {
                            Toast.makeText(PermissionRequestActivity.this, "To time should be greater than from time", Toast.LENGTH_LONG).show();
                            toTime.setText(null);
                        }


                    } else {
                        endHour = selectedHour;
                        endMin = selectedMinute;
                        if (selectedMinute < 10 && selectedHour < 10) {
                            toTime.setText("0" + selectedHour + ":" + "0" + selectedMinute);
                        } else if (selectedHour < 10) {
                            toTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else if (selectedMinute < 10) {
                            toTime.setText(selectedHour + ":" + "0" + selectedMinute);
                        } else {
                            toTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }


                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select End Time");
            mTimePicker.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void fromTime() {
        try {
            Calendar calendar = Calendar.getInstance();
            curTimeInMills = calendar.getTimeInMillis();
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();
            final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            final int minute = mcurrentTime.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(PermissionRequestActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(android.widget.TimePicker timePicker, int selectedHour, int selectedMinute) {


                    startHour = selectedHour;
                    startMin = selectedMinute;
                    boolean isValid = false;


                    if (endHour == -1 && endMin == -1) {
                        isValid = true;
                    } else if ((endHour != -1 && endMin != -1) && (selectedHour < endHour)) {
                        isValid = true;
                    } else if ((endHour != -1 && endMin != -1) && (selectedHour == endHour && selectedMinute < endMin)) {
                        isValid = true;
                    }

                    if (isValid) {
                        if (selectedMinute < 10 && selectedHour < 10) {
                            fromTime.setText("0" + selectedHour + ":" + "0" + selectedMinute);

                        } else if (selectedHour < 10) {

                            fromTime.setText("0" + selectedHour + ":" + selectedMinute);
                        } else if (selectedMinute < 10) {
                            fromTime.setText(selectedHour + ":" + "0" + selectedMinute);
                        } else {
                            fromTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    } else {
                        Toast.makeText(PermissionRequestActivity.this, "From time should be less than to time", Toast.LENGTH_LONG).show();
                        fromTime.setText(null);
                    }


                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.setTitle("Select Start Time");
            mTimePicker.show();

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

            early = earlyLogoutImg.getTag().toString();
            other = extendedLogoutImg.getTag().toString();
            late = lateLoginImg.getTag().toString();

            if (resonForPermission.getText().toString().trim().length() == 0) {
                //ToastUtil.showToast(this, "Please enter the reason");
                Helper.getInstance().openKeyboard(PermissionRequestActivity.this,resonForPermission);
                Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, "Please enter the reason");
                sendRequest.setClickable(true);

            } else if (permisionDate.getText().toString().trim().length() == 0) {
                //ToastUtil.showToast(this, "Please select permission date");
                Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, "Please select permission date");
                sendRequest.setClickable(true);

            } else if (early.equals("0") && other.equals("0") && late.equals("0")) {
                // Toast.makeText(PermissionRequestActivity.this,"Please select permission request type",Toast.LENGTH_SHORT).show();
                Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, "Please select permission request type");
                sendRequest.setClickable(true);
            } else if (Integer.parseInt(other) == 1 && (fromTime == null || fromTime.getText().toString().trim().isEmpty() || toTime == null || toTime.getText().toString().trim().isEmpty())) {

                if (fromTime == null || fromTime.getText().toString().trim().isEmpty()) {
                    // ToastUtil.showToast(this,"Please select from time");
                    Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, "Please select from time");
                    sendRequest.setClickable(true);
                } else if (toTime == null || toTime.getText().toString().trim().isEmpty()) {
                    // ToastUtil.showToast(this,"Please select to time");
                    Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, "Please select to time");
                    sendRequest.setClickable(true);
                }


            } else if (Integer.parseInt(other) == 1 && (fromTime != null || !fromTime.getText().toString().trim().isEmpty() || toTime != null || !toTime.getText().toString().trim().isEmpty()) && (endHour < startHour || (startHour == endHour && startMin > endMin))) {

                if (endHour < startHour || (startHour == endHour && startMin > endMin)) {
                    //ToastUtil.showToast(this,"To time should be greater than from time");
                    Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, "To time should be greater than from time");
                    sendRequest.setClickable(true);
                }

            } else if (Integer.parseInt(early) == 1 && (selectTime == null || selectTime.getText().toString().trim().isEmpty())) {


                if (selectTime == null || selectTime.getText().toString().trim().isEmpty()) {
                    // ToastUtil.showToast(this,"Please select time");
                    Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, "Please select time");
                    sendRequest.setClickable(true);
                }


            } else if (Integer.parseInt(late) == 1 && (selectTime == null || selectTime.getText().toString().trim().isEmpty())) {


                if (selectTime == null || selectTime.getText().toString().trim().isEmpty()) {
                    // ToastUtil.showToast(this,"Please select  time");
                    Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, "Please select time");
                    sendRequest.setClickable(true);
                }


            } else if (early.equals("1") && (fromTime == null || fromTime.getText().toString().trim().length() < 0) || toTime == null || toTime.getText().toString().trim().length() < 0) {

                if (fromTime == null || fromTime.getText().toString().trim().length() < 0) {
                    // ToastUtil.showToast(this,"Please select  time");
                    Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, "Please select time");
                    sendRequest.setClickable(true);
                } else if (toTime == null || toTime.getText().toString().trim().length() < 0) {
                    // ToastUtil.showToast(this,"Please select  time");
                    Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, "Please select time");
                    sendRequest.setClickable(true);
                }


            } else {

                if (Utilities.isNetworkAvailable(PermissionRequestActivity.this)) {
                    sendRequest.setClickable(false);
                    callSavePermission();

                } else {
                    // Toast.makeText(PermissionRequestActivity.this,"Please check network connection",Toast.LENGTH_SHORT).show();
                    Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, getResources().getString(R.string.no_internet_connection));
                    sendRequest.setClickable(true);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void callSavePermission() {
        try {
            openProgress();
            String type;
            String startTime = "", endTime = "";

            if (early.equals("1")) {
                type = "1";
                endTime = selectTime.getText().toString();
            } else if (other.equals("1")) {
                type = "4";
                startTime = fromTime.getText().toString();
                endTime = toTime.getText().toString();
            } else {
                type = "3";
                startTime = selectTime.getText().toString();
            }

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
                Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, getResources().getString(R.string.no_to_users_msg));
                return;
            }


            for (int i = 0; i < ccNamesArrayList.size(); i++) {
                if (i == ccNamesArrayList.size() - 1) {
                    cc = cc + ccNamesArrayList.get(i).getUser_id();
                } else {
                    cc = cc + ccNamesArrayList.get(i).getUser_id() + ",";
                }

            }

            JSONObject time = new JSONObject();

            try {
                time.put("start_time", startTime);
                time.put("end_time", endTime);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Map<String, String> data = new HashMap<>();
            data.put("user_id", userId);
            data.put("token", token);
            data.put("to_employees", to);
            data.put("cc_employees", cc);

            data.put("type", type);
            data.put("reason", reason);
            data.put("date", selectedDate);
            data.put("data", String.valueOf(time));

            Call<LeaveRequestResponse> call = apiService.SavePermissionRequest(apiKey, userId, to, cc, type, resonForPermission.getText().toString().trim(), selectedDate, String.valueOf(time));
            call.enqueue(new Callback<LeaveRequestResponse>() {
                @Override
                public void onResponse(Call<LeaveRequestResponse> call, Response<LeaveRequestResponse> response) {
                    LeaveRequestResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {

                            Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, apiResponse.getMessage());
                            Intent intent = new Intent(PermissionRequestActivity.this, RequestActivity.class);
                            intent.putExtra("type", "1");
                            intent.putExtra("requestType", "");
                            intent.putExtra("fromDate", "");
                            intent.putExtra("toDate", "");
                            intent.putExtra("users", "");
                            startActivity(intent);
                            finish();

                        } else {
                            sendRequest.setClickable(true);

                            Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, apiResponse.getMessage());

                        }

                    } else {
                        closeProgress();
                        sendRequest.setClickable(true);
                        Utilities.ShowSnackbar(PermissionRequestActivity.this, resonForPermission, getResources().getString(R.string.somthing_went_wrong));
                        // Toast.makeText(PermissionRequestActivity.this, "Something went wrong try again later.", Toast.LENGTH_LONG).show();


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


    public void resetForm() {
        try {
            Calendar calendar = Calendar.getInstance();
            permisionDate.setText(sdf.format(calendar.getTime()));
            selectedDate = sdf1.format(calendar.getTime());

            resonForPermission.setText("");
            earlyLogoutImg.setTag("1");
            extendedLogoutImg.setTag("0");
            lateLoginImg.setTag("0");
            earlyLogoutImg.setImageResource(R.drawable.check);
            extendedLogoutImg.setImageResource(R.drawable.uncheck);
            lateLoginImg.setImageResource(R.drawable.uncheck);
            ccNamesArrayList.clear();
            ccNamesAdapter.setList(ccNamesArrayList);
            ccNamesAdapter.notifyDataSetChanged();
            toNamesArrayList.clear();
            toNamesAdapter.setList(toNamesArrayList);
            toNamesAdapter.notifyDataSetChanged();
            fromTime.setText("");
            toTime.setText("");
            selectTime.setText("");

            selectTimeLayout.setVisibility(View.VISIBLE);
            fromToLayout.setVisibility(View.GONE);

            callToCC();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void lateLogin() {

        try {
            // See here
            String tag = lateLoginImg.getTag().toString();
            Integer integer = Integer.valueOf(tag);

            switch (integer) {
                case 0:


                    lateLoginImg.setImageResource(R.drawable.check);
                    lateLoginImg.setTag("1");

                    selectTimeLayout.setVisibility(View.VISIBLE);
                    fromToLayout.setVisibility(View.GONE);

                    extendedLogoutImg.setImageResource(R.drawable.uncheck);
                    extendedLogoutImg.setTag("0");

                    earlyLogoutImg.setImageResource(R.drawable.uncheck);
                    earlyLogoutImg.setTag("0");


                    break;
                case 1:

                    lateLoginImg.setImageResource(R.drawable.uncheck);
                    lateLoginImg.setTag("0");
                    break;

                default:

                    extendedLogoutImg.setImageResource(R.drawable.uncheck);
                    extendedLogoutImg.setTag("0");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void extendedLogout() {

        try {
            // See here
            String tag = extendedLogoutImg.getTag().toString();
            Integer integer = Integer.valueOf(tag);

            switch (integer) {
                case 0:

                    extendedLogoutImg.setImageResource(R.drawable.check);
                    extendedLogoutImg.setTag("1");

                    selectTimeLayout.setVisibility(View.GONE);
                    fromToLayout.setVisibility(View.VISIBLE);

                    earlyLogoutImg.setImageResource(R.drawable.uncheck);
                    earlyLogoutImg.setTag("0");
                    lateLoginImg.setImageResource(R.drawable.uncheck);
                    lateLoginImg.setTag("0");


                    break;
                case 1:

                    extendedLogoutImg.setImageResource(R.drawable.uncheck);
                    extendedLogoutImg.setTag("0");
                    break;

                default:

                    extendedLogoutImg.setImageResource(R.drawable.uncheck);
                    extendedLogoutImg.setTag("0");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void earlyLogout() {

        try {
            // See here
            String tag = earlyLogoutImg.getTag().toString();
            Integer integer = Integer.valueOf(tag);

            switch (integer) {
                case 0:

                    earlyLogoutImg.setImageResource(R.drawable.check);
                    earlyLogoutImg.setTag("1");


                    selectTimeLayout.setVisibility(View.VISIBLE);
                    fromToLayout.setVisibility(View.GONE);

                    extendedLogoutImg.setImageResource(R.drawable.uncheck);
                    extendedLogoutImg.setTag("0");
                    lateLoginImg.setImageResource(R.drawable.uncheck);
                    lateLoginImg.setTag("0");


                    break;
                case 1:

                    earlyLogoutImg.setImageResource(R.drawable.uncheck);
                    earlyLogoutImg.setTag("0");
                    break;

                default:

                    earlyLogoutImg.setImageResource(R.drawable.uncheck);
                    earlyLogoutImg.setTag("0");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void date() {
        try {
            final Calendar mcurrentDate = Calendar.getInstance();
            int mYear = mcurrentDate.get(Calendar.YEAR);
            int mMonth = mcurrentDate.get(Calendar.MONTH);
            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog mDatePicker = new DatePickerDialog(
                    PermissionRequestActivity.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker,
                                      int selectedyear, int selectedmonth,
                                      int selectedday) {

                    mcurrentDate.set(Calendar.YEAR, selectedyear);
                    mcurrentDate.set(Calendar.MONTH, selectedmonth);
                    mcurrentDate.set(Calendar.DAY_OF_MONTH,
                            selectedday);
                    permisionDate.setText(sdf.format(mcurrentDate.getTime()));
                    selectedDate = sdf1.format(mcurrentDate.getTime());


                }
            }, mYear, mMonth, mDay);

            mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
            mDatePicker.show();
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
        //user = new ArrayList<>();
        //user = populateUserData(user);
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

            userAdapter = new UserAdapter(PermissionRequestActivity.this, user, new UserAdapter.UserAdapterListener() {
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

            ccUsersAdapter = new UserAdapter(PermissionRequestActivity.this, ccUsers, new UserAdapter.UserAdapterListener() {
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

            // ccNamesArrayList.clear();
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
