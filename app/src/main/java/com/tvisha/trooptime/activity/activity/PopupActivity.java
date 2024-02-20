package com.tvisha.trooptime.activity.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tvisha.trooptime.activity.activity.api_Helper.ApiCallHelper;
import com.tvisha.trooptime.activity.activity.helper.Navigation;
import com.tvisha.trooptime.activity.activity.helper.ServerUrls;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.model.PhoneNumberModel;
import com.tvisha.trooptime.activity.activity.service.MyAlarmService;
import com.tvisha.trooptime.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class PopupActivity extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    TextView tv_permission, tv_leave, tv_onthe_way, call_manager, call_hr, tv_permission_submit, tv_start_date, tv_end_date, check_in_done, checked_time;
    int mMinHour = 9, mMinMinute = 0, mMaxHour = 12, mMaxMinute = 0;
    RelativeLayout wrapper_permission_layout, wrapper_ontheway_layout, checkin_success_layout, top_wrapper;
    Spinner sp_permission, sp_ontheway;
    EditText et_reason;
    Typeface light_typeface, medium_typeface;
    String userId, email, first_name, select_date, date, reason, leaveTodate, leaveFromdate, permissionTime,
            onTheWayTime, submit_time, reporting_boss, today_date, checkIn_time;
    long current_date;
    DatePickerDialog datePickerDialog;
    LinearLayout linear_layout;
    boolean isChecked = false;
    List<String> dateList, checkinList;
    List<PhoneNumberModel> phoneNumberModels = new ArrayList<>();
    Set<String> phone_set, name_set;
    List<String> stringList, namelist;
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceData();
        setContentView(R.layout.simple_popup);
        /*if (reporting_boss.equals(userId))
        {
            setContentView(R.layout.simple_popup);
        }else {
            setContentView(R.layout.permission_dialog);
        }*/
       /* if (isChecked) {
            finish();
        }
*/
        this.setFinishOnTouchOutside(false);

        light_typeface = Typeface.createFromAsset(getAssets(), "font/poppins/Poppins-Light.ttf");
        medium_typeface = Typeface.createFromAsset(getAssets(), "font/poppins/Poppins-Medium.ttf");

        initializeWidgets();
        onclickListener();
        addTheSpinnerData();

        Calendar calendar = Calendar.getInstance();
        current_date = System.currentTimeMillis();
        date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        tv_start_date.setText(date);
        tv_end_date.setText(date);


       /* AlarmManager aManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(PopupActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        aManager.cancel(pIntent);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(20000);
                    if (!isChecked) {
                        // getTheCheckinDetail();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getTheCheckinDetail() {
        dateList = new ArrayList<>();
        checkinList = new ArrayList<>();
        if (Utilities.getConnectivityStatus(PopupActivity.this) > 0) {
            final Map<String, String> data = new HashMap();
            data.put("user_id", userId);
            data.put("security_key", "ba5d400506348b7d54088203324f5224");
            final ApiCallHelper apiCallHelper = new ApiCallHelper(PopupActivity.this, ServerUrls.Self_Attendence, data);
            apiCallHelper.execute();
            apiCallHelper.setApiCallBackListener(new ApiCallHelper.ApiCallBackListener() {
                @Override
                public void onCompleteTask(String responce) {
                    try {
                        JSONObject object = new JSONObject(responce);
                        String success = object.optString("success");
                        String message = object.optString("message");
                        if (message.equals("Success") && success.equals("true")) {

                            JSONArray array = new JSONArray(object.optString("data"));
                            for (int i = 0; i < array.length(); i++) {
                                JSONArray array1 = array.getJSONArray(i);
                                for (int j = 0; j < array1.length(); j++) {
                                    JSONObject jsonObject = array1.getJSONObject(j);
                                    today_date = jsonObject.getString("date");
                                    checkIn_time = jsonObject.getString("check_in");
                                    dateList.add(jsonObject.getString("date"));
                                    checkinList.add(jsonObject.getString("check_in"));
                                }
                            }
                        }

                        if (dateList.size() > 0) {
                            for (int i = 0; i < dateList.size(); i++) {
                                String list_date = dateList.get(i);
                                if (list_date.trim().toLowerCase().equals(date.trim().toLowerCase())) {
                                    String checkin = checkinList.get(i);
                                    if (checkin != null && !checkin.isEmpty()) {
                                        top_wrapper.setVisibility(View.GONE);
                                        checkin_success_layout.setVisibility(View.VISIBLE);
                                        checked_time.setText(checkin);
                                    } else {
                                    }
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailedToCompleteTask(Exception e) {
                }

                @Override
                public void onRequestTimeout(String res) {

                }
            });
        }
    }

    private void sharedPreferenceData() {
        shared = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
        userId = (shared.getString("user_id", ""));
        email = (shared.getString("email", ""));
        first_name = (shared.getString("first_name", ""));
        String last_name = (shared.getString("last_name", ""));
        reporting_boss = (shared.getString("reporting_boss_id", ""));
        isChecked = (shared.getBoolean("", false));
        phone_set = (shared.getStringSet("phone_model", phone_set));
        name_set = (shared.getStringSet("name_set", name_set));
        /*stringList = new ArrayList<>();
        stringList.addAll(phone_set);
        namelist = new ArrayList<>();
        namelist.addAll(name_set);*/


    }

    private void addTheSpinnerData() {
        String[] permission = getResources().getStringArray(R.array.permission);
        ArrayAdapter<String> perm_adapter = new ArrayAdapter<String>(PopupActivity.this, android.R.layout.simple_spinner_dropdown_item, permission);
        perm_adapter.setDropDownViewResource(R.layout.singlerow1);
        sp_permission.setAdapter(perm_adapter);
        String[] ontheway = getResources().getStringArray(R.array.ontheway);
        ArrayAdapter<String> way_adapter = new ArrayAdapter<String>(PopupActivity.this, android.R.layout.simple_spinner_dropdown_item, ontheway);
        way_adapter.setDropDownViewResource(R.layout.singlerow1);
        sp_ontheway.setAdapter(way_adapter);
    }

    private void onclickListener() {
        tv_leave.setOnClickListener(this);
        tv_onthe_way.setOnClickListener(this);
        tv_permission.setOnClickListener(this);
        call_hr.setOnClickListener(this);
        call_manager.setOnClickListener(this);
        sp_permission.setOnItemSelectedListener(this);
        sp_ontheway.setOnItemSelectedListener(this);
        tv_permission_submit.setOnClickListener(this);
        tv_end_date.setOnClickListener(this);
        check_in_done.setOnClickListener(this);
    }

    private void initializeWidgets() {
        tv_permission = (TextView) findViewById(R.id.tv_permission);
        tv_leave = (TextView) findViewById(R.id.tv_leave);
        tv_onthe_way = (TextView) findViewById(R.id.tv_onthe_way);
        call_hr = (TextView) findViewById(R.id.call_hr);
        call_manager = (TextView) findViewById(R.id.call_manager);
        tv_permission_submit = (TextView) findViewById(R.id.tv_permission_submit);
        tv_start_date = (TextView) findViewById(R.id.tv_start_date);
        tv_end_date = (TextView) findViewById(R.id.tv_end_date);
        check_in_done = (TextView) findViewById(R.id.check_in_done);
        checked_time = (TextView) findViewById(R.id.checked_time);

        wrapper_ontheway_layout = (RelativeLayout) findViewById(R.id.wrapper_ontheway_layout);
        wrapper_permission_layout = (RelativeLayout) findViewById(R.id.wrapper_permission_layout);
        checkin_success_layout = (RelativeLayout) findViewById(R.id.checkin_success_layout);
        top_wrapper = (RelativeLayout) findViewById(R.id.top_wrapper);

        sp_permission = (Spinner) findViewById(R.id.sp_permission);
        sp_ontheway = (Spinner) findViewById(R.id.sp_ontheway);

        et_reason = (EditText) findViewById(R.id.et_reason);
        linear_layout = (LinearLayout) findViewById(R.id.linear_layout);


    }

    @Override
    public void onBackPressed() {
        if (!isChecked) {
            Intent a = new Intent(PopupActivity.this, PopupActivity.class);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            finish();
        }
        super.onBackPressed();

    }

    @Override
    public void onClick(View v) {
        TextView alert_title, positive_button, negative_button;
        switch (v.getId()) {

            case R.id.check_in_done:
                NotificationManager nMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nMgr.cancel(0);
                setAlaramManger();
                finish();
                break;

            case R.id.tv_permission:

                Navigation.getInstance().permissionRequest(PopupActivity.this);

               /* tv_end_date.setText(date);
                wrapper_permission_layout.setVisibility(View.VISIBLE);
                et_reason.getText().clear();
                reason = "";
                et_reason.setVisibility(View.GONE);
                sp_ontheway.setSelection(0);
                wrapper_ontheway_layout.setVisibility(View.GONE);
                tv_permission_submit.setVisibility(View.VISIBLE);
                linear_layout.setVisibility(View.GONE);*/
                break;
            case R.id.tv_leave:
                Navigation.getInstance().leaveRequest(PopupActivity.this);
            /*    et_reason.setVisibility(View.VISIBLE);
                wrapper_ontheway_layout.setVisibility(View.GONE);
                wrapper_permission_layout.setVisibility(View.GONE);
                sp_ontheway.setSelection(0);
                et_reason.setError(null);
                tv_permission_submit.setVisibility(View.VISIBLE);
                sp_permission.setSelection(0);
                linear_layout.setVisibility(View.VISIBLE);*/

                break;
            case R.id.tv_onthe_way:
             /*   tv_end_date.setText(date);
                wrapper_permission_layout.setVisibility(View.GONE);
                wrapper_ontheway_layout.setVisibility(View.VISIBLE);
                sp_permission.setSelection(0);
                et_reason.getText().clear();
                reason = "";
                linear_layout.setVisibility(View.GONE);
                tv_permission_submit.setVisibility(View.VISIBLE);
                et_reason.setVisibility(View.GONE);*/
                break;
            case R.id.call_hr:
                Navigation.getInstance().openCallLog(this);
              /*  tv_end_date.setText(date);
                tv_permission_submit.setVisibility(View.GONE);
                et_reason.setVisibility(View.GONE);
                et_reason.getText().clear();
                sp_ontheway.setSelection(0);
                sp_permission.setSelection(0);
                linear_layout.setVisibility(View.GONE);
                wrapper_ontheway_layout.setVisibility(View.GONE);
                wrapper_permission_layout.setVisibility(View.GONE);

                final Dialog hr_dialog = new Dialog(PopupActivity.this);
                hr_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                hr_dialog.setContentView(R.layout.alert_dialog_bg);
                hr_dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                hr_dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(PopupActivity.this, android.R.color.transparent));

                alert_title = (TextView) hr_dialog.findViewById(R.id.alert_title);
                alert_title.setText("Call to HR");

                negative_button = (TextView) hr_dialog.findViewById(R.id.negative_button);
                negative_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hr_dialog.dismiss();
                    }
                });

                positive_button = (TextView) hr_dialog.findViewById(R.id.positive_button);
                positive_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hr_dialog.dismiss();
                        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                        phoneIntent.setData(Uri.parse("tel:8374466508"));
                        if (ActivityCompat.checkSelfPermission(PopupActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        startActivity(phoneIntent);
                    }
                });
                hr_dialog.show();*/
                break;
            case R.id.call_manager:

                Navigation.getInstance().openCallLog(this);
               /* tv_end_date.setText(date);
                tv_permission_submit.setVisibility(View.GONE);
                et_reason.setVisibility(View.GONE);
                sp_ontheway.setSelection(0);
                sp_permission.setSelection(0);
                linear_layout.setVisibility(View.GONE);
                wrapper_ontheway_layout.setVisibility(View.GONE);
                wrapper_permission_layout.setVisibility(View.GONE);


                phoneNumberModels = dbHelper.getPhoneNumberData();

                final Dialog dialog = new Dialog(PopupActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_for_phone_number);
                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(PopupActivity.this, android.R.color.transparent));

                RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.phone_recyclerview);
                CustomAdapter adater = new CustomAdapter(PopupActivity.this, phoneNumberModels);
                LinearLayoutManager layoutManager = new LinearLayoutManager(PopupActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adater);


                dialog.show();
*/
                break;
            case R.id.tv_permission_submit:

                getStringResources();

                Calendar calendar = Calendar.getInstance();
                String date = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
                submit_time = new SimpleDateFormat("HH:mm").format(calendar.getTime());
                final String time_of_send = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());

                Map<String, String> data = new HashMap<>();
                data.put("user_id", userId);
                data.put("date", date);
                data.put("security_key", "ba5d400506348b7d54088203324f5224");

                if (reason != null && !reason.isEmpty()) {
                    leaveFromdate = tv_start_date.getText().toString();
                    leaveTodate = tv_end_date.getText().toString();

                    data.put("to_day_date", leaveFromdate);
                    data.put("leave_last_date", leaveTodate);
                    data.put("reason", reason);
                } else if (permissionTime != null && !permissionTime.isEmpty()) {
                    data.put("permission_time", permissionTime);
                } else if (onTheWayTime != null && !onTheWayTime.isEmpty()) {
                    data.put("ontheway_time", onTheWayTime);
                }
                if ((reason != null && !reason.isEmpty()) || (permissionTime != null && !permissionTime.isEmpty()) || (onTheWayTime != null && !onTheWayTime.isEmpty())) {
                    if (Utilities.getConnectivityStatus(PopupActivity.this) > 0) {
                        isChecked = true;
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putBoolean("ischecked", isChecked);
                        editor.apply();
                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        manager.cancel(0);

                        ApiCallHelper apiCallHelper = new ApiCallHelper(PopupActivity.this, ServerUrls.BaseUrl, data);
                        apiCallHelper.execute();
                        apiCallHelper.setApiCallBackListener(new ApiCallHelper.ApiCallBackListener() {
                            @Override
                            public void onCompleteTask(String responce) {
                                try {
                                    JSONObject responce_object = new JSONObject(responce);
                                    String success = responce_object.getString("success");
                                    if (success.equals("true")) {
                                        Toast.makeText(PopupActivity.this, "Send successfully", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailedToCompleteTask(Exception e) {

                            }

                            @Override
                            public void onRequestTimeout(String res) {

                            }
                        });

                        Intent intent1 = new Intent(PopupActivity.this, MyAlarmService.class);
                        if (permissionTime != null && !permissionTime.isEmpty()) {
                            intent1.putExtra("permission_time", permissionTime);
                        } else if (onTheWayTime != null && !onTheWayTime.isEmpty()) {
                            intent1.putExtra("onthe_way_time", onTheWayTime);
                        } else if (reason != null && !reason.isEmpty()) {
                            intent1.putExtra("from_day", leaveFromdate);
                            intent1.putExtra("to_day", leaveTodate);
                        }
                        intent1.putExtra("send_time", time_of_send);
                        intent1.putExtra("select", "select");
                        startService(intent1);
                        finish();
                    } else {
                        Utilities.ShowSnackbar(PopupActivity.this, tv_permission_submit, "Please Check Network Connection");
                        SharedPreferences.Editor editor = shared.edit();
                        editor.putBoolean("ischecked", isChecked);
                        editor.apply();
                        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        manager.cancel(0);

                        Intent intent1 = new Intent(PopupActivity.this, MyAlarmService.class);
                        if (permissionTime != null && !permissionTime.isEmpty()) {
                            intent1.putExtra("permission_time", permissionTime);
                        } else if (onTheWayTime != null && !onTheWayTime.isEmpty()) {
                            intent1.putExtra("onthe_way_time", onTheWayTime);
                        } else if (reason != null && !reason.isEmpty()) {
                            intent1.putExtra("from_day", leaveFromdate);
                            intent1.putExtra("to_day", leaveTodate);
                        }
                        intent1.putExtra("send_time", time_of_send);
                        intent1.putExtra("select", "select");
                        startService(intent1);
                        finish();
                    }
                } else {
                    Toast.makeText(PopupActivity.this, "Please Take permission", Toast.LENGTH_LONG).show();
                }
               /* ApiCallHelper apiCallHelper = new ApiCallHelper(PopupActivity.this,serverUrls.s3Url,data);
                apiCallHelper.execute();
                apiCallHelper.setApiCallBackListener(new ApiCallHelper.ApiCallBackListener() {
                    @Override
                    public void onCompleteTask(String responce)
                    {

                        try {
                            JSONObject jsonResponce = new JSONObject(responce);
                            String success          = jsonResponce.optString("success");
                            String message          = jsonResponce.getString("message");
                            if (message.equals("Success") && success.equals("true"))
                            {
                                Intent intent1 = new Intent(PopupActivity.this,AlarmReceiver.class);
                                if (permissionTime!=null && !permissionTime.isEmpty())
                                {
                                    intent1.putExtra("permission_time",permissionTime);
                                }else if (onTheWayTime!=null && !onTheWayTime.isEmpty())
                                {
                                    intent1.putExtra("onthe_way_time",onTheWayTime);
                                }else if (reason!=null && !reason.isEmpty())
                                {
                                    intent1.putExtra("from_day",leaveFromdate);
                                    intent1.putExtra("to_day",leaveTodate);
                                }
                                intent1.putExtra("send_time",time_of_send);
                                sendBroadcast(intent1);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailedToCompleteTask(Exception e) {

                    }

                    @Override
                    public void onRequestTimeout(String res) {

                    }
                });*/
                break;
            case R.id.tv_end_date:

                Calendar calendar1 = Calendar.getInstance();
                int d_year = calendar1.get(Calendar.YEAR);
                int d_month = calendar1.get(Calendar.MONTH);
                int d_day = calendar1.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(PopupActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String delivered_date = "" + String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        datePickerDialog.getDatePicker().setMinDate(current_date);
                        Date date1 = null;
                        try {
                            date1 = dateFormat.parse(delivered_date);
                            dateFormat = new SimpleDateFormat("EEE yyyy-MM-dd");
                            String day = dateFormat.format(date1);

                            if (day.contains("Sat")) {
                                select_date = "" + String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth + 2);
                                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = dateFormat.parse(select_date);
                                select_date = dateFormat.format(date);
                                tv_end_date.setText(select_date);

                            } else if (day.contains("Sun")) {
                                select_date = "" + String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(dayOfMonth + 1);
                                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = dateFormat.parse(select_date);
                                select_date = dateFormat.format(date);
                                tv_end_date.setText(select_date);

                            } else {
                                dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = dateFormat.parse(delivered_date);
                                select_date = dateFormat.format(date);
                                tv_end_date.setText(select_date);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    }
                }, d_year, d_month, d_day);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    datePickerDialog.getDatePicker().setMinDate(Calendar.getInstance().getTimeInMillis());
                }
                datePickerDialog.show();
                break;
        }
    }

    public void setAlaramManger() {
        Calendar alarm_cal = Calendar.getInstance();
        alarm_cal.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String to_date = new SimpleDateFormat("yyyy-MM-dd").format(alarm_cal.getTime());
        try {
            Date next_date = dateFormat.parse(to_date + "-09-00-00");
            Date tomorrow = new Date(next_date.getTime() + (1000 * 60 * 60 * 24));

            String ss = dateFormat.format(tomorrow);

            String[] split_date = ss.split("-");
            int year = Integer.parseInt(split_date[0]);
            int month = Integer.parseInt(split_date[1]);
            int day = Integer.parseInt(split_date[2]);
            int hours = Integer.parseInt(split_date[3]);
            int minutes = Integer.parseInt(split_date[4]);
            int seconds = Integer.parseInt(split_date[5]);

            alarm_cal.set(year, (month - 1), day, hours, minutes, seconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long alaram_set = alarm_cal.getTimeInMillis();
        long system_time = alarm_cal.getTimeInMillis();
        Intent intent = new Intent(PopupActivity.this, MyAlarmService.class);
        intent.putExtra("select", "");
        intent.putExtra("newalarm_time", alaram_set);
        intent.putExtra("calendar_time", system_time);
        startService(intent);
    }

    private void getStringResources() {

        String permissiontime = sp_permission.getSelectedItem().toString();
        if (permissiontime.trim().toLowerCase().equals("select")) {
            permissionTime = "";
        } else {
            if (permissiontime.trim().toLowerCase().equals("1 hour")) {
                permissionTime = "01:00:00";
            } else if (permissiontime.trim().toLowerCase().equals("2 hours")) {
                permissionTime = "02:00:00";
            } else if (permissiontime.trim().toLowerCase().equals("half day")) {
                permissionTime = "half day";
            }
        }
        String ontheway = sp_ontheway.getSelectedItem().toString();
        if (ontheway.trim().toLowerCase().equals("select")) {
            onTheWayTime = "";
        } else {
            if (ontheway.trim().toLowerCase().equals("15 mins")) {
                onTheWayTime = "00:15:00";
            } else if (ontheway.trim().toLowerCase().equals("30 mins")) {
                onTheWayTime = "00:30:00";
            }
        }

        if (permissionTime.isEmpty() && onTheWayTime.isEmpty()) {
            if (!et_reason.getText().toString().isEmpty()) {
                reason = et_reason.getText().toString();
            } else {
                et_reason.setError("Please enter the reason");
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView t = (TextView) view;

        /*((t) parent.getChildAt(0))*/
        t.setTextColor(ContextCompat.getColor(PopupActivity.this, R.color.select_text));
        /*((t) parent.getChildAt(0))*/
        t.setTypeface(light_typeface);

        switch (parent.getId()) {
            case R.id.sp_permission:
                permissionTime = sp_permission.getSelectedItem().toString();
                break;
            case R.id.sp_ontheway:
                onTheWayTime = sp_ontheway.getSelectedItem().toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyHolder> {
        List<PhoneNumberModel> phoneNumberModelslist;
        Context context;

        public CustomAdapter(Context applicationContext, List<PhoneNumberModel> phoneNumberModels) {
            context = applicationContext;
            phoneNumberModelslist = phoneNumberModels;
        }

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.phone_dialog, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, final int position) {
            PhoneNumberModel model = phoneNumberModelslist.get(position);
            String number = model.getReportPhoneNumber();
            String name = model.getReportName();
            holder.name.setText(name);
            holder.number.setText(number);
            holder.phone_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PhoneNumberModel phoneNumberModel = phoneNumberModels.get(position);
                    String selectedFromList = phoneNumberModel.getReportPhoneNumber();
                    Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                    phoneIntent.setData(Uri.parse("tel:" + selectedFromList));
                    if (ActivityCompat.checkSelfPermission(PopupActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(phoneIntent);

                }
            });
        }

        @Override
        public int getItemCount() {
            return phoneNumberModelslist.size();
        }

        public class MyHolder extends RecyclerView.ViewHolder {
            TextView name, number;
            LinearLayout phone_layout;

            public MyHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                number = (TextView) itemView.findViewById(R.id.number);
                phone_layout = (LinearLayout) itemView.findViewById(R.id.phone_layout);

            }
        }
    }
}
