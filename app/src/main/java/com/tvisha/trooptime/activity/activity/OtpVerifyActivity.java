package com.tvisha.trooptime.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tvisha.trooptime.activity.activity.ApiPostModels.ForgotPasswordResponce;
import com.tvisha.trooptime.activity.activity.ApiPostModels.VerifyOtpResponse;
import com.tvisha.trooptime.activity.activity.Dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.Helper.Helper;
import com.tvisha.trooptime.activity.activity.Helper.Navigation;
import com.tvisha.trooptime.activity.activity.Helper.Utilities;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpVerifyActivity extends AppCompatActivity implements View.OnClickListener {
    ApiInterface apiService;
    EditText otpEditText;
    TextView next, login, resendOtp;
    String otp = "", userId = "", number = "";
    CustomProgressBar customProgressBar;
    long mLastClicked = 0;

    public void openProgress() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(OtpVerifyActivity.this).isFinishing()) {
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
                        if (!(OtpVerifyActivity.this).isFinishing()) {
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
        setContentView(R.layout.activity_otp_verify);

        initViews();
        initListeners();


        otpEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {

                    validateForm();
                }
                return false;
            }
        });

        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Navigation.getInstance().openLoginPage(OtpVerifyActivity.this);

                return true;
            }
        });


    }

    private void initViews() {
        try {
            customProgressBar = new CustomProgressBar(OtpVerifyActivity.this);
            apiService = ApiClient.getClient().create(ApiInterface.class);
            otpEditText = findViewById(R.id.otp);
            login = findViewById(R.id.tv_login);
            resendOtp = findViewById(R.id.resendOtp);
            next = findViewById(R.id.next);
            Intent intent = getIntent();
            userId = intent.getStringExtra("userId");
            number = intent.getStringExtra("number");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListeners() {
        next.setOnClickListener(this);
        resendOtp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.next:
                    next.setClickable(false);
                    validateForm();
                    break;
                case R.id.resendOtp:
                    next.setClickable(false);
                    resendOtp.setClickable(false);
                    openProgress();
                    getOtp();
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
            if (otpEditText != null) {
                otp = otpEditText.getText().toString();
                if (otp.length() < 3) {
                    next.setClickable(true);
                    Utilities.ShowSnackbar(OtpVerifyActivity.this, otpEditText, "Please enter valid otp");
                    Helper.getInstance().openKeyboard(OtpVerifyActivity.this, otpEditText);

                } else {
                    openProgress();
                    verifyOtp();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void verifyOtp() {

        try {

            Helper.getInstance().closeKeyBoard(OtpVerifyActivity.this, otpEditText);
            Call<VerifyOtpResponse> call = apiService.verifyOtp(otp, number);
            call.enqueue(new Callback<VerifyOtpResponse>() {
                @Override
                public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                    VerifyOtpResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {


                            next.setClickable(true);

                            Intent intent = new Intent(OtpVerifyActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("userId", userId);
                            intent.putExtra("number", number);
                            startActivity(intent);
                            finish();



                        } else {
                            Utilities.ShowSnackbar(OtpVerifyActivity.this, otpEditText, apiResponse.getMessage());


                            next.setClickable(true);
                        }

                    } else {
                        Helper.getInstance().closeKeyBoard(OtpVerifyActivity.this, otpEditText);
                        Utilities.ShowSnackbar(OtpVerifyActivity.this, otpEditText, getResources().getString(R.string.somthing_went_wrong));
                        closeProgress();
                    }


                }

                @Override
                public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                    next.setClickable(true);
                    closeProgress();
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getOtp() {

        try {
            Call<ForgotPasswordResponce> call = apiService.resendOtp(number, userId);
            call.enqueue(new Callback<ForgotPasswordResponce>() {
                @Override
                public void onResponse(Call<ForgotPasswordResponce> call, Response<ForgotPasswordResponce> response) {
                    ForgotPasswordResponce apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {
                            next.setClickable(true);
                            resendOtp.setClickable(true);

                            Utilities.ShowSnackbar(OtpVerifyActivity.this, otpEditText, apiResponse.getMessage());


                        } else {
                            resendOtp.setClickable(true);
                            next.setClickable(true);

                            // Utilities.showSnackBar(mobileNumber,apiResponse.getMessage());
                            Toast.makeText(OtpVerifyActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    } else {
                        resendOtp.setClickable(true);
                        next.setClickable(true);
                        Toast.makeText(OtpVerifyActivity.this, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();
                        closeProgress();


                    }

                }

                @Override
                public void onFailure(Call<ForgotPasswordResponce> call, Throwable t) {
                    next.setClickable(true);
                    resendOtp.setClickable(true);

                    closeProgress();


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
