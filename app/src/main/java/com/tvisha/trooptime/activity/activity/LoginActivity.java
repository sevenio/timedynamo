package com.tvisha.trooptime.activity.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tvisha.trooptime.activity.activity.ApiPostModels.LoginApi;
import com.tvisha.trooptime.activity.activity.ApiPostModels.User;
import com.tvisha.trooptime.activity.activity.ApiPostModels.UserInfo;
import com.tvisha.trooptime.activity.activity.Dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.Helper.Constants;
import com.tvisha.trooptime.activity.activity.Helper.Helper;
import com.tvisha.trooptime.activity.activity.Helper.Navigation;
import com.tvisha.trooptime.activity.activity.Helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.Helper.Utilities;
import com.tvisha.trooptime.activity.activity.Helper.Values;
import com.tvisha.trooptime.activity.activity.Model.PhoneNumberModel;
import com.tvisha.trooptime.activity.activity.handlers.HandlerHolder;
import com.tvisha.trooptime.R;

import org.json.JSONObject;

import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends Activity implements View.OnClickListener {
    EditText et_email, et_password;
    TextView tv_forget, bt_login, button_signin;
    String email, password, check_email, check_password, refreshedToken, deviceId = "";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;
    String device_Id;
    int count = 1;
    DbHelper dbHelper;
    List<PhoneNumberModel> phone_set;
    Set<String> phonenumber_set, name_set;
    boolean val = false;
    ScrollView scrollView;
    ImageView password_visible;
    LinearLayout activity_login;
    RelativeLayout formLayout;
    CustomProgressBar customProgressBar;
    long mLastClicked = 0;

    public void openProgress() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(LoginActivity.this).isFinishing()) {
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
                        if (!(LoginActivity.this).isFinishing()) {
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


       /* new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("Are you sure?")
                .setMessage("Do you really want to close the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {



                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();

                    }

                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onRestart();
                    }

                })
                .show();*/
        //  super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        try {
            customProgressBar = new CustomProgressBar(LoginActivity.this);
            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            clearTheData();

            device_Id = Settings.System.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            sharedPreferences.edit().putString(SharePreferenceKeys.DEVICE_ID, device_Id).apply();
            initializeWidgets();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void clearTheData() {
        try {
            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            if (sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGOUT_STATUS, false)) {
                sharedPreferences.edit().clear().apply();
            } else {
                String fcmtoken = sharedPreferences.getString(SharePreferenceKeys.FCM_TOKEN, "");
                String api_key = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
                String user_id = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");

                sharedPreferences.edit().clear().apply();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SharePreferenceKeys.SP_LOGOUT_STATUS, false);
                editor.putString(SharePreferenceKeys.USER_ID, user_id);
                editor.putString(SharePreferenceKeys.FCM_TOKEN, fcmtoken);
                editor.putString(SharePreferenceKeys.API_KEY, api_key);
                editor.apply();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void eventHandlings() {
        try {
            bt_login.setOnClickListener(this);
            tv_forget.setOnClickListener(this);
            password_visible.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initializeWidgets() {
        try {
            dbHelper = new DbHelper(LoginActivity.this);
            et_email = (EditText) findViewById(R.id.et_email);
            et_password = (EditText) findViewById(R.id.et_password);
            bt_login = (TextView) findViewById(R.id.bt_login);
            tv_forget = (TextView) findViewById(R.id.tv_forget);
            formLayout = findViewById(R.id.formLayout);
            scrollView = findViewById(R.id.scrollView);


            activity_login = (LinearLayout) findViewById(R.id.activity_login);

            password_visible = (ImageView) findViewById(R.id.password_visible);
            et_password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().trim().length() > 0) {
                        password_visible.setVisibility(View.VISIBLE);
                    } else {
                        password_visible.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            et_email.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                        email = et_email.getText().toString().trim();
                        if (email.length() == 0) {
                            Utilities.showSnackBar(et_email, "Please enter Email/Mobile Number");
                            Helper.getInstance().openKeyboard(LoginActivity.this, et_email);
                            return true;
                        } else if (!Utilities.validEmail(email) && Utilities.isNumeric(email) && email.length() < 10) {
                            Utilities.showSnackBar(et_email, "Please enter valid Mobile Number");
                            Helper.getInstance().openKeyboard(LoginActivity.this, et_email);
                            return true;

                        } else if (!Utilities.validEmail(email) && !Utilities.isNumeric(email)) {
                            Utilities.showSnackBar(et_email, "Please enter valid Email");
                            Helper.getInstance().openKeyboard(LoginActivity.this, et_email);
                            return true;

                        }

                    }
                    return false;
                }
            });

            et_password.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                        validateForm();
                    }
                    return false;
                }
            });
            eventHandlings();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.bt_login:
                    validateForm();
                    break;
                case R.id.tv_forget:
                    tv_forget.setClickable(false);
                    Navigation.getInstance().openForgotPasswordPage(LoginActivity.this);

                    tv_forget.setClickable(true);
                    break;
                case R.id.password_visible:
                    togglePasswordVisiblity();
                    break;


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void validateForm() {

        try {

            if (SystemClock.elapsedRealtime() - mLastClicked < 100) {
                return;
            }
            mLastClicked = SystemClock.elapsedRealtime();

            try {

                email = et_email.getText().toString().trim();
                password = et_password.getText().toString().trim();
                if (email.length() == 0) {

                    Utilities.showSnackBar(et_email, "Please enter Email/Mobile Number");
                    Helper.getInstance().openKeyboard(LoginActivity.this, et_email);


                } else if (!Utilities.validEmail(email) && Utilities.isNumeric(email) && email.length() < 10) {
                    Utilities.showSnackBar(et_email, "Please enter valid Mobile Number");
                    Helper.getInstance().openKeyboard(LoginActivity.this, et_email);

                } else if (!Utilities.validEmail(email) && !Utilities.isNumeric(email)) {
                    Utilities.showSnackBar(et_email, "Please enter valid Email");
                    Helper.getInstance().openKeyboard(LoginActivity.this, et_email);

                } else if (password == null || password.trim().isEmpty() || password.equals("null") || password.length() == 0) {
                    Utilities.showSnackBar(et_password, "Please enter  Password");
                    Helper.getInstance().openKeyboard(LoginActivity.this, et_password);

                } else {
                    closeKeyboard();
                    if (Utilities.getConnectivityStatus(LoginActivity.this) > 0) {
                        bt_login.setVisibility(View.GONE);
                        tv_forget.setVisibility(View.GONE);

                        callToLoginServer();
                    } else {
                        Utilities.showSnackBar(bt_login, getResources().getString(R.string.network_check));
                    }

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void callToLoginServer() {
        try {
            openProgress();
            try {
                retrofit2.Call<String> call = LoginApi.getApiService().getLoginDetails(email, password, Constants.TOKEN);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response.code() == Constants.RESPONCE_SUCCESSFUL) {
                            closeProgress();
                            try {
                                JSONObject apiresponce =new  JSONObject(response.body());
                                if (apiresponce != null) {
                                    if (apiresponce.optBoolean("success")) {

                                        //Utilities.showSnackBar(bt_login, apiresponce.getMessage());

                                        User user = new Gson().fromJson(apiresponce.optJSONObject("user").toString(),User.class);// apiresponce.getUser();
                                        Log.e("user==> ", "" + new Gson().toJson(apiresponce)+"   "+new Gson().toJson(response.body()));
                                        editor = sharedPreferences.edit();
                                        try {
                                            if (user != null) {
                                                editor.putString(SharePreferenceKeys.USER_AVATAR, (user.getUserAvatar() != null) ? user.getUserAvatar() : "");
                                                editor.putBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, true);
                                                editor.putString(SharePreferenceKeys.USER_ID, user.getUserId());
                                                editor.putString(SharePreferenceKeys.USER_NAME, user.getName());
                                                editor.putString(SharePreferenceKeys.USER_EMAIL, user.getEmail());
                                                editor.putString(SharePreferenceKeys.USER_MOBILE, user.getMobile());
                                                editor.putString(SharePreferenceKeys.COMPANY_ID, user.getCompanyId());
                                                editor.putInt(SharePreferenceKeys.USER_ROLE, Integer.parseInt(user.getRole()));
                                                editor.putString(SharePreferenceKeys.API_KEY, user.getApiKey());
                                                editor.putString(SharePreferenceKeys.COMPANY_NAME, user.getCompanyName());
                                                editor.putString(SharePreferenceKeys.USER_DEPARTMENT, user.getDepartment());
                                                editor.putString(SharePreferenceKeys.USER_DESIGNATION, user.getDesignation());
                                                editor.putString(SharePreferenceKeys.REPORTING_BOSS_ID, user.getReporting_boss_id());
                                                editor.putString(SharePreferenceKeys.COMPANY_LOCATION, user.getComapanyBranch());
                                                editor.putBoolean(SharePreferenceKeys.SP_LOGOUT_STATUS, false);
                                                editor.putBoolean(SharePreferenceKeys.IS_CC_USER, user.isIs_cc_exist());

                                                if (apiresponce.has("reportingUsers") && apiresponce.optJSONArray("reportingUsers").length() > 0) {

                                                    editor.putBoolean(SharePreferenceKeys.TEAM_LEAD, true);
                                                } else if (user.isIs_to_exists()) {
                                                    editor.putBoolean(SharePreferenceKeys.TEAM_LEAD, true);
                                                } else {
                                                    editor.putBoolean(SharePreferenceKeys.TEAM_LEAD, false);
                                                }

                                                if (apiresponce.has("is_full_access")  && apiresponce.optString("is_full_access").equals("1")) {
                                                    editor.putBoolean(SharePreferenceKeys.FULL_ACCESS, true);

                                                } else {
                                                    editor.putBoolean(SharePreferenceKeys.FULL_ACCESS, false);

                                                }
                                                if (apiresponce.has("user_info")) {
                                                    UserInfo userInfo = user.getUserInfo();
                                                    if (userInfo != null) {
                                                        editor.putBoolean(SharePreferenceKeys.attendance_all_tab, userInfo.isAttendance_all_tab());
                                                        editor.putBoolean(SharePreferenceKeys.attendance_team_tab, userInfo.isAttendance_team_tab());
                                                        editor.putBoolean(SharePreferenceKeys.request_all_tab, userInfo.isAttendance_all_tab());
                                                        editor.putBoolean(SharePreferenceKeys.request_team_tab, userInfo.isRequest_team_tab());
                                                    }
                                                }

                                                editor.putInt("countforservice", count);
                                                editor.apply();


                                                if (HandlerHolder.applicationHandlers != null) {
                                                    HandlerHolder.applicationHandlers.obtainMessage(Constants.LOGIN_SUCCESSFUL).sendToTarget();
                                                }

                                /*MyApplication application=(MyApplication) getApplication();
                                application.initSocket();*/
                                                SharedPreferences sp1 = getSharedPreferences("userrecord", 0);
                                                SharedPreferences.Editor editor1 = sp1.edit();
                                                editor1.putBoolean("isLogin", true);
                                                editor1.apply();

                                                Navigation.getInstance().openHomePage(LoginActivity.this);

                                                finish();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Utilities.showSnackBar(bt_login, apiresponce.optString("message"));
                                        bt_login.setVisibility(View.VISIBLE);
                                        tv_forget.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Utilities.showSnackBar(bt_login, getResources().getString(R.string.somthing_went_wrong));
                                    bt_login.setVisibility(View.VISIBLE);
                                    tv_forget.setVisibility(View.VISIBLE);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        closeProgress();
                        Log.e("error==> ",""+t.getMessage());
                        bt_login.setVisibility(View.VISIBLE);
                        tv_forget.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                closeProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void togglePasswordVisiblity() {
        try {
            if (et_password.getText().toString().length() > 0) {
                if (et_password.getInputType() == 129) {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password_visible.setImageResource(R.drawable.ic_hide);
                } else {
                    password_visible.setImageResource(R.drawable.ic_view);
                    et_password.setInputType(129);
                }
                et_password.setSelection(et_password.getText().toString().length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setupUI(View view) {


        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    closeKeyboard();
                    return false;
                }
            });
        }

    }

    private void getDeviceId() {

        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},
                    Values.RequestPermission.READ_PHONE_STATE);
        } else {

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = telephonyManager.getDeviceId();
        }

    }

}
