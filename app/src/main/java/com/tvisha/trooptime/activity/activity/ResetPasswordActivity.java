package com.tvisha.trooptime.activity.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    ApiInterface apiService;
    EditText passwordEditText, confirmPasswordEditText;
    TextView next, login;
    String userId = "", number = "", password = "", confirmPassword = "";
    ImageView password_visible, confirm_password_visible;
    CustomProgressBar customProgressBar;
    long mLastClicked = 0;

    public void openProgress() {

        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (!(ResetPasswordActivity.this).isFinishing()) {
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
                        if (!(ResetPasswordActivity.this).isFinishing()) {
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
        setContentView(R.layout.activity_reset_password);
        initViews();
        initLiteners();


        try {


            confirmPasswordEditText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                        validateForm();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            passwordEditText.addTextChangedListener(new TextWatcher() {
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

            confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().trim().length() > 0) {
                        confirm_password_visible.setVisibility(View.VISIBLE);
                    } else {
                        confirm_password_visible.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initLiteners() {
        try {
            next.setOnClickListener(this);
            password_visible.setOnClickListener(this);
            confirm_password_visible.setOnClickListener(this);
            login.setOnTouchListener(this);
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
                    break;
                case R.id.password_visible:
                    togglePasswordVisiblity();
                    break;
                case R.id.confirm_password_visible:
                    toggleConfirmPasswordVisiblity();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        try {
            customProgressBar = new CustomProgressBar(ResetPasswordActivity.this);
            apiService = ApiClient.getClient().create(ApiInterface.class);
            passwordEditText = findViewById(R.id.password);
            login = findViewById(R.id.tv_login);
            confirmPasswordEditText = findViewById(R.id.confirmPassword);
            confirm_password_visible = findViewById(R.id.confirm_password_visible);
            password_visible = findViewById(R.id.password_visible);
            next = findViewById(R.id.next);
            Intent intent = getIntent();
            userId = intent.getStringExtra("userId");
            number = intent.getStringExtra("number");
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
            if (passwordEditText != null && confirmPasswordEditText != null) {
                password = passwordEditText.getText().toString().trim().replace(" ", "");
                confirmPassword = confirmPasswordEditText.getText().toString().trim().replace(" ", "");
                if (password.trim().isEmpty()) {
                    next.setClickable(true);
                    //  Toast.makeText(ResetPasswordActivity.this,"Please enter password",Toast.LENGTH_LONG).show();
                    Utilities.ShowSnackbar(ResetPasswordActivity.this, passwordEditText, "Please enter password");
                    Helper.getInstance().openKeyboard(ResetPasswordActivity.this, passwordEditText);
                } else if (confirmPassword.isEmpty()) {
                    next.setClickable(true);
                    // Toast.makeText(ResetPasswordActivity.this,"Please enter confirm password...",Toast.LENGTH_LONG).show();
                    Utilities.ShowSnackbar(ResetPasswordActivity.this, passwordEditText, "Please enter confirm password...");
                    Helper.getInstance().openKeyboard(ResetPasswordActivity.this, confirmPasswordEditText);
                } else if (!password.equals(confirmPassword)) {
                    next.setClickable(true);
                    Utilities.ShowSnackbar(ResetPasswordActivity.this, passwordEditText, "password and confirm password must be same");
                    //Toast.makeText(ResetPasswordActivity.this,"password and confirm password must be same",Toast.LENGTH_LONG).show();
                } else {

                    openProgress();
                    if (Utilities.isNetworkAvailable(ResetPasswordActivity.this)) {
                        resetPassword();
                    } else {
                        next.setClickable(true);
                        Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG).show();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void resetPassword() {
        try {
            closeKeyboard();
            Call<VerifyOtpResponse> call = apiService.resetPassword(userId, number, password, confirmPassword);
            call.enqueue(new Callback<VerifyOtpResponse>() {
                @Override
                public void onResponse(Call<VerifyOtpResponse> call, Response<VerifyOtpResponse> response) {
                    VerifyOtpResponse apiResponse = response.body();

                    if (apiResponse != null) {
                        closeProgress();
                        if (apiResponse.isSuccess()) {
                            next.setClickable(true);


                            Toast.makeText(ResetPasswordActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();
                            Navigation.getInstance().openLoginPage(ResetPasswordActivity.this);



                        } else {
                            next.setClickable(true);

                            Toast.makeText(ResetPasswordActivity.this, apiResponse.getMessage(), Toast.LENGTH_LONG).show();


                        }

                    } else {
                        next.setClickable(true);
                        closeProgress();

                        Toast.makeText(ResetPasswordActivity.this, getResources().getString(R.string.somthing_went_wrong), Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onFailure(Call<VerifyOtpResponse> call, Throwable t) {
                    closeProgress();

                    next.setClickable(true);

                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeKeyboard() {
        try {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void togglePasswordVisiblity() {
        try {
            if (passwordEditText.getText().toString().length() > 0) {
                if (passwordEditText.getInputType() == 129) {
                    passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password_visible.setImageResource(R.drawable.ic_hide);
                } else {
                    password_visible.setImageResource(R.drawable.ic_view);
                    passwordEditText.setInputType(129);
                }
                passwordEditText.setSelection(passwordEditText.getText().toString().length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toggleConfirmPasswordVisiblity() {
        try {
            if (confirmPasswordEditText.getText().toString().length() > 0) {
                if (confirmPasswordEditText.getInputType() == 129) {
                    confirmPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirm_password_visible.setImageResource(R.drawable.ic_hide);
                } else {
                    confirm_password_visible.setImageResource(R.drawable.ic_view);
                    confirmPasswordEditText.setInputType(129);
                }
                confirmPasswordEditText.setSelection(confirmPasswordEditText.getText().toString().length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            switch (v.getId()) {
                case R.id.tv_login:
                    Navigation.getInstance().openLoginPage(ResetPasswordActivity.this);

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
