package com.tvisha.trooptime.activity.activity;

import android.os.Bundle;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tvisha.trooptime.activity.activity.apiPostModels.ForgotPasswordResponce;
import com.tvisha.trooptime.activity.activity.dialog.CustomProgressBar;
import com.tvisha.trooptime.activity.activity.helper.Helper;
import com.tvisha.trooptime.activity.activity.helper.Navigation;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;
import com.tvisha.trooptime.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    ApiInterface apiService;
    EditText mobileNumber;
    TextView next;
    String number = "";
    TextView login;
    CustomProgressBar customProgressBar;
    long mLastClicked = 0;

    public void openProgress() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(ForgotPasswordActivity.this).isFinishing()) {
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
                        if (!(ForgotPasswordActivity.this).isFinishing()) {
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
        setContentView(R.layout.activity_forgot_password);
        initViews();
        initListeners();


        mobileNumber.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    Helper.getInstance().openKeyboard(ForgotPasswordActivity.this, mobileNumber);
                    next.performClick();
                    next.setClickable(false);
                    return true;
                } else {
                    return false;
                }

            }
        });


    }

    private void initListeners() {
        try {
            next.setOnClickListener(this);
            login.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getOtpApi() {
        try {
            if (Utilities.isNetworkAvailable(ForgotPasswordActivity.this)) {

                getOtp();
            } else {
                next.setClickable(true);
                Utilities.showSnackBar(mobileNumber, getResources().getString(R.string.no_internet_connection));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.next:
                    next.setClickable(false);
                    validateForm();
                    if (mobileNumber != null)
                        break;
                case R.id.tv_login:
                    login.setClickable(false);
                    Navigation.getInstance().openLoginPage(ForgotPasswordActivity.this);

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void validateForm() {

        try {


            Helper.getInstance().closeKeyBoard(ForgotPasswordActivity.this, mobileNumber);

            if (SystemClock.elapsedRealtime() - mLastClicked < 100) {
                return;
            }
            mLastClicked = SystemClock.elapsedRealtime();

            number = mobileNumber.getText().toString().trim();
            if (number.length() == 0) {
                next.setClickable(true);
                Utilities.showSnackBar(mobileNumber, "Please enter Email/Mobile Number");
                Helper.getInstance().openKeyboard(ForgotPasswordActivity.this, mobileNumber);

            } else if (!Utilities.validEmail(number) && Utilities.isNumeric(number) && number.length() < 10) {
                next.setClickable(true);
                Utilities.showSnackBar(mobileNumber, "Please enter valid Mobile Number");
                Helper.getInstance().openKeyboard(ForgotPasswordActivity.this, mobileNumber);

            } else if (!Utilities.validEmail(number) && !Utilities.isNumeric(number)) {
                next.setClickable(true);
                Utilities.showSnackBar(mobileNumber, "Please enter valid Email");
                Helper.getInstance().openKeyboard(ForgotPasswordActivity.this, mobileNumber);
            } else {

                getOtpApi();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        try {
            customProgressBar = new CustomProgressBar(ForgotPasswordActivity.this);
            apiService = new ApiClient().getInstance();
            mobileNumber = findViewById(R.id.mobileNumber);
            login = findViewById(R.id.tv_login);
            next = findViewById(R.id.next);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getOtp() {

        try {

            Helper.getInstance().closeKeyBoard(ForgotPasswordActivity.this, mobileNumber);
            openProgress();

            Call<ForgotPasswordResponce> call = apiService.getOtp(number);

            call.enqueue(new Callback<ForgotPasswordResponce>() {
                @Override
                public void onResponse(Call<ForgotPasswordResponce> call, Response<ForgotPasswordResponce> response) {
                    ForgotPasswordResponce apiResponse = response.body();
                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {

                            Toast.makeText(ForgotPasswordActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                            Navigation.getInstance().openOtpVerify(ForgotPasswordActivity.this, apiResponse.getUserId(), number);

                            next.setClickable(true);

                        } else {
                            next.setClickable(true);
                            Utilities.ShowSnackbar(ForgotPasswordActivity.this, mobileNumber, apiResponse.getMessage());

                        }

                    } else {
                        next.setClickable(true);
                        Utilities.ShowSnackbar(ForgotPasswordActivity.this, mobileNumber, getResources().getString(R.string.somthing_went_wrong));
                        ;
                        closeProgress();


                    }

                }

                @Override
                public void onFailure(Call<ForgotPasswordResponce> call, Throwable t) {
                    next.setClickable(true);
                    closeProgress();


                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
