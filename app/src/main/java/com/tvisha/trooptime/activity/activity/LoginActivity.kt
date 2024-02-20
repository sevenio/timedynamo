package com.tvisha.trooptime.activity.activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.browser.customtabs.CustomTabsIntent
import com.google.gson.Gson
import com.tvisha.trooptime.R
import com.tvisha.trooptime.activity.activity.apiPostModels.LoginApi
import com.tvisha.trooptime.activity.activity.apiPostModels.User
import com.tvisha.trooptime.activity.activity.dialog.CustomProgressBar
import com.tvisha.trooptime.activity.activity.helper.*
import com.tvisha.trooptime.activity.activity.model.PhoneNumberModel
import com.tvisha.trooptime.activity.activity.handlers.HandlerHolder


import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity :  Activity(), View.OnClickListener {
    var et_email: EditText? = null
    var et_password: EditText? = null
    var tv_forget: TextView? = null
    var bt_login: TextView? = null
    var button_signin: TextView? = null
    var tv_privacy_policy: TextView? = null
    var email: String? = null
    var password: String? = null
    var check_email: String? = null
    var check_password: String? = null
    var refreshedToken: String? = null
    var deviceId = ""
    private lateinit var sharedPreferences: SharedPreferences
//    var editor: SharedPreferences.Editor? = null
    var progressDialog: ProgressDialog? = null
    var device_Id: String? = null
    var count = 1
    var dbHelper: DbHelper? = null
    var phone_set: List<PhoneNumberModel>? = null
    var phonenumber_set: Set<String>? = null
    var name_set: Set<String>? = null
    var `val` = false
    var scrollView: ScrollView? = null
    var password_visible: ImageView? = null
    var activity_login: LinearLayout? = null
    var formLayout: RelativeLayout? = null
    var customProgressBar: CustomProgressBar? = null
    var mLastClicked: Long = 0
    fun openProgress() {
        try {
            runOnUiThread {
                try {
                    if (!this@LoginActivity.isFinishing) {
                        if (customProgressBar != null && !customProgressBar!!.isShowing) {
                            customProgressBar!!.show()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun closeProgress() {
        try {
            runOnUiThread {
                try {
                    if (!this@LoginActivity.isFinishing) {
                        if (customProgressBar != null && customProgressBar!!.isShowing) {
                            customProgressBar!!.dismiss()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        try {
            customProgressBar = CustomProgressBar(this@LoginActivity)
            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE)
            clearTheData()
            device_Id = Settings.System.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            sharedPreferences.edit().putString(SharePreferenceKeys.DEVICE_ID, device_Id).apply()
            initializeWidgets()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearTheData() {
        try {
            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE)
            if (sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGOUT_STATUS, false)) {
                sharedPreferences.edit().clear().apply()
            } else {
                val fcmtoken = sharedPreferences.getString(SharePreferenceKeys.FCM_TOKEN, "")
                val api_key = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "")
                val user_id = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "")
                sharedPreferences.edit().clear().apply()
                val editor = sharedPreferences.edit()
                editor.putBoolean(SharePreferenceKeys.SP_LOGOUT_STATUS, false)
                editor.putString(SharePreferenceKeys.USER_ID, user_id)
                editor.putString(SharePreferenceKeys.FCM_TOKEN, fcmtoken)
                editor.putString(SharePreferenceKeys.API_KEY, api_key)
                editor.apply()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun eventHandlings() {
        try {
            bt_login!!.setOnClickListener(this)
            tv_forget!!.setOnClickListener(this)
            password_visible!!.setOnClickListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun openLink(url: String) {
        val uri = Uri.parse(url)
        val intentBuilder = CustomTabsIntent.Builder()
        val customTabsIntent = intentBuilder.build()
        customTabsIntent.launchUrl(this, uri)
    }

    private fun setupPrivacyPolicy() {
        tv_privacy_policy = findViewById<View>(R.id.tv_privacy_policy) as TextView
        val ss = SpannableString("Please read our Privacy policy")
        val clickableSpanPrivacy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                openLink("https://www.timedynamo.com/privacy")
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(clickableSpanPrivacy, 16, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_privacy_policy!!.text = ss
        tv_privacy_policy!!.movementMethod = LinkMovementMethod.getInstance()
        tv_privacy_policy!!.highlightColor = Color.TRANSPARENT
    }

    private fun initializeWidgets() {
        try {
            dbHelper = DbHelper(this@LoginActivity)
            setupPrivacyPolicy()
            et_email = findViewById<View>(R.id.et_email) as EditText
            et_password = findViewById<View>(R.id.et_password) as EditText
            bt_login = findViewById<View>(R.id.bt_login) as TextView
            tv_forget = findViewById<View>(R.id.tv_forget) as TextView
            formLayout = findViewById(R.id.formLayout)
            scrollView = findViewById(R.id.scrollView)
            activity_login = findViewById<View>(R.id.activity_login) as LinearLayout
            password_visible = findViewById<View>(R.id.password_visible) as ImageView
            et_password!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().trim { it <= ' ' }.length > 0) {
                        password_visible!!.visibility = View.VISIBLE
                    } else {
                        password_visible!!.visibility = View.INVISIBLE
                    }
                }

                override fun afterTextChanged(s: Editable) {}
            })
            et_email!!.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    email = et_email!!.text.toString().trim { it <= ' ' }
                    if (email!!.length == 0) {
                        Utilities.showSnackBar(et_email, "Please enter Email/Mobile Number")
                        Helper.getInstance().openKeyboard(this@LoginActivity, et_email)
                        return@OnKeyListener true
                    } else if (!Utilities.validEmail(email) && Utilities.isNumeric(email) && email!!.length < 10) {
                        Utilities.showSnackBar(et_email, "Please enter valid Mobile Number")
                        Helper.getInstance().openKeyboard(this@LoginActivity, et_email)
                        return@OnKeyListener true
                    } else if (!Utilities.validEmail(email) && !Utilities.isNumeric(email)) {
                        Utilities.showSnackBar(et_email, "Please enter valid Email")
                        Helper.getInstance().openKeyboard(this@LoginActivity, et_email)
                        return@OnKeyListener true
                    }
                }
                false
            })
            et_password!!.setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    validateForm()
                }
                false
            }
            eventHandlings()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(v: View) {
        try {
            when (v.id) {
                R.id.bt_login -> validateForm()
                R.id.tv_forget -> {
                    tv_forget!!.isClickable = false
                    Navigation.getInstance().openForgotPasswordPage(this@LoginActivity)
                    tv_forget!!.isClickable = true
                }
                R.id.password_visible -> togglePasswordVisiblity()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun validateForm() {
        try {
            if (SystemClock.elapsedRealtime() - mLastClicked < 100) {
                return
            }
            mLastClicked = SystemClock.elapsedRealtime()
            try {
                email = et_email!!.text.toString().trim { it <= ' ' }
                password = et_password!!.text.toString().trim { it <= ' ' }
                if (email!!.length == 0) {
                    Utilities.showSnackBar(et_email, "Please enter Email/Mobile Number")
                    Helper.getInstance().openKeyboard(this@LoginActivity, et_email)
                } else if (!Utilities.validEmail(email) && Utilities.isNumeric(email) && email!!.length < 10) {
                    Utilities.showSnackBar(et_email, "Please enter valid Mobile Number")
                    Helper.getInstance().openKeyboard(this@LoginActivity, et_email)
                } else if (!Utilities.validEmail(email) && !Utilities.isNumeric(email)) {
                    Utilities.showSnackBar(et_email, "Please enter valid Email")
                    Helper.getInstance().openKeyboard(this@LoginActivity, et_email)
                } else if (password == null || password!!.trim { it <= ' ' }
                        .isEmpty() || password == "null" || password!!.length == 0) {
                    Utilities.showSnackBar(et_password, "Please enter  Password")
                    Helper.getInstance().openKeyboard(this@LoginActivity, et_password)
                } else {
                    closeKeyboard()
                    if (Utilities.getConnectivityStatus(this@LoginActivity) > 0) {
                        bt_login!!.visibility = View.GONE
                        tv_forget!!.visibility = View.GONE
                        callToLoginServer()
                    } else {
                        Utilities.showSnackBar(
                            bt_login,
                            resources.getString(R.string.network_check)
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun callToLoginServer() {
        try {
            openProgress()
            try {
                val call =
                    LoginApi.getApiService().getLoginDetails(email, password, Constants.TOKEN)
                call.enqueue(object : Callback<String?> {
                    override fun onResponse(call: Call<String?>, response: Response<String?>) {
                        if (response.code() == Constants.RESPONCE_SUCCESSFUL) {
                            closeProgress()
                            try {
                                val apiresponce = JSONObject(response.body())
                                if (apiresponce != null) {
                                    if (apiresponce.optBoolean("success")) {

                                        //Utilities.showSnackBar(bt_login, apiresponce.getMessage());
                                        val user = Gson().fromJson(
                                            apiresponce.optJSONObject("user").toString(),
                                            User::class.java
                                        ) // apiresponce.getUser();
                                        Log.e(
                                            "user==> ",
                                            "" + Gson().toJson(apiresponce) + "   " + Gson().toJson(
                                                response.body()
                                            )
                                        )
                                        val editor = sharedPreferences!!.edit()
                                        try {
                                            if (user != null) {
                                                editor.putString(
                                                    SharePreferenceKeys.USER_AVATAR,
                                                    if (user.userAvatar != null) user.userAvatar else ""
                                                )
                                                editor.putBoolean(
                                                    SharePreferenceKeys.SP_LOGIN_STATUS,
                                                    true
                                                )
                                                editor.putString(
                                                    SharePreferenceKeys.USER_ID,
                                                    user.userId
                                                )
                                                editor.putString(
                                                    SharePreferenceKeys.USER_NAME,
                                                    user.name
                                                )
                                                editor.putString(
                                                    SharePreferenceKeys.USER_EMAIL,
                                                    user.email
                                                )
                                                editor.putString(
                                                    SharePreferenceKeys.USER_MOBILE,
                                                    user.mobile
                                                )
                                                editor.putString(
                                                    SharePreferenceKeys.COMPANY_ID,
                                                    user.companyId
                                                )
                                                editor.putInt(
                                                    SharePreferenceKeys.USER_ROLE,
                                                    user.role.toInt()
                                                )
                                                editor.putString(
                                                    SharePreferenceKeys.API_KEY,
                                                    user.apiKey
                                                )
                                                editor.putString(
                                                    SharePreferenceKeys.COMPANY_NAME,
                                                    user.companyName
                                                )
                                                editor.putString(
                                                    SharePreferenceKeys.USER_DEPARTMENT,
                                                    user.department
                                                )
                                                editor.putString(
                                                    SharePreferenceKeys.USER_DESIGNATION,
                                                    user.designation
                                                )
                                                editor.putString(
                                                    SharePreferenceKeys.REPORTING_BOSS_ID,
                                                    user.reporting_boss_id
                                                )
                                                editor.putString(
                                                    SharePreferenceKeys.COMPANY_LOCATION,
                                                    user.comapanyBranch
                                                )
                                                editor.putBoolean(
                                                    SharePreferenceKeys.SP_LOGOUT_STATUS,
                                                    false
                                                )
                                                editor.putBoolean(
                                                    SharePreferenceKeys.IS_CC_USER,
                                                    user.isIs_cc_exist
                                                )
                                                if (apiresponce.has("reportingUsers") && apiresponce.optJSONArray(
                                                        "reportingUsers"
                                                    ).length() > 0
                                                ) {
                                                    editor.putBoolean(
                                                        SharePreferenceKeys.TEAM_LEAD,
                                                        true
                                                    )
                                                } else if (user.isIs_to_exists) {
                                                    editor.putBoolean(
                                                        SharePreferenceKeys.TEAM_LEAD,
                                                        true
                                                    )
                                                } else {
                                                    editor.putBoolean(
                                                        SharePreferenceKeys.TEAM_LEAD,
                                                        false
                                                    )
                                                }
                                                if (apiresponce.has("is_full_access") && apiresponce.optString(
                                                        "is_full_access"
                                                    ) == "1"
                                                ) {
                                                    editor.putBoolean(
                                                        SharePreferenceKeys.FULL_ACCESS,
                                                        true
                                                    )
                                                } else {
                                                    editor.putBoolean(
                                                        SharePreferenceKeys.FULL_ACCESS,
                                                        false
                                                    )
                                                }
                                                if (apiresponce.has("user_info")) {
                                                    val userInfo = user.userInfo
                                                    if (userInfo != null) {
                                                        editor.putBoolean(
                                                            SharePreferenceKeys.attendance_all_tab,
                                                            userInfo.isAttendance_all_tab
                                                        )
                                                        editor.putBoolean(
                                                            SharePreferenceKeys.attendance_team_tab,
                                                            userInfo.isAttendance_team_tab
                                                        )
                                                        editor.putBoolean(
                                                            SharePreferenceKeys.request_all_tab,
                                                            userInfo.isAttendance_all_tab
                                                        )
                                                        editor.putBoolean(
                                                            SharePreferenceKeys.request_team_tab,
                                                            userInfo.isRequest_team_tab
                                                        )
                                                    }
                                                }
                                                editor.putInt("countforservice", count)
                                                editor.apply()
                                                if (HandlerHolder.applicationHandlers != null) {
                                                    HandlerHolder.applicationHandlers.obtainMessage(
                                                        Constants.LOGIN_SUCCESSFUL
                                                    ).sendToTarget()
                                                }

                                                /*MyApplication application=(MyApplication) getApplication();
                                application.initSocket();*/
                                                val sp1 = getSharedPreferences("userrecord", 0)
                                                val editor1 = sp1.edit()
                                                editor1.putBoolean("isLogin", true)
                                                editor1.apply()
                                                Navigation.getInstance()
                                                    .openHomePage(this@LoginActivity)
                                                finish()
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                    } else {
                                        Utilities.showSnackBar(
                                            bt_login,
                                            apiresponce.optString("message")
                                        )
                                        bt_login!!.visibility = View.VISIBLE
                                        tv_forget!!.visibility = View.VISIBLE
                                    }
                                } else {
                                    Utilities.showSnackBar(
                                        bt_login,
                                        resources.getString(R.string.somthing_went_wrong)
                                    )
                                    bt_login!!.visibility = View.VISIBLE
                                    tv_forget!!.visibility = View.VISIBLE
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<String?>, t: Throwable) {
                        closeProgress()
                        Log.e("error==> ", "" + t.message)
                        bt_login!!.visibility = View.VISIBLE
                        tv_forget!!.visibility = View.VISIBLE
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
                closeProgress()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun togglePasswordVisiblity() {
        try {
            if (et_password!!.text.toString().length > 0) {
                if (et_password!!.inputType == 129) {
                    et_password!!.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    password_visible!!.setImageResource(R.drawable.ic_hide)
                } else {
                    password_visible!!.setImageResource(R.drawable.ic_view)
                    et_password!!.inputType = 129
                }
                et_password!!.setSelection(et_password!!.text.toString().length)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }




}