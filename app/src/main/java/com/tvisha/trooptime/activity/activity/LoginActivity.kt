package com.tvisha.trooptime.activity.activity


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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.tvisha.trooptime.R
import com.tvisha.trooptime.activity.activity.api.ApiClient
import com.tvisha.trooptime.activity.activity.api.ApiInterface
import com.tvisha.trooptime.activity.activity.apiPostModels.User
import com.tvisha.trooptime.activity.activity.dialog.CustomProgressBar
import com.tvisha.trooptime.activity.activity.handlers.HandlerHolder
import com.tvisha.trooptime.activity.activity.helper.*
import com.tvisha.trooptime.activity.activity.viewmodels.LoginViewModel
import com.tvisha.trooptime.databinding.LoginLayoutBinding
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var sharedPreferences: SharedPreferences

    var count = 1

    private lateinit var customProgressBar: CustomProgressBar
    var mLastClicked: Long = 0
    private lateinit var binding: LoginLayoutBinding
    private val viewModel: LoginViewModel by viewModels()

    fun openProgress() {
        lifecycleScope.launch {
            if (!customProgressBar.isShowing) {
                customProgressBar.show()
            }
        }
    }

    fun closeProgress() {
        lifecycleScope.launch {
            if (customProgressBar.isShowing) {
                customProgressBar.dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            customProgressBar = CustomProgressBar(this@LoginActivity)
            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE)
            clearTheData()
            sharedPreferences.edit().putString(
                SharePreferenceKeys.DEVICE_ID,
                Settings.System.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            ).apply()
            initializeWidgets()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        viewModel.progress.observe(this){
            if(it){
                openProgress()
            }else {
                closeProgress()
            }
        }

        viewModel.loginSuccess.observe(this){
            if(it) {
                Navigation.getInstance()
                    .openHomePage(this@LoginActivity)
                finish()
            }
        }
        viewModel.loginFail.observe(this){
            if(it.isNotBlank()) {
                Utilities.showSnackBar(
                    binding.btLogin,
                    it
                )
                binding.btLogin.visibility = View.VISIBLE
                binding.tvForget.visibility = View.VISIBLE
            }
        }
    }

    private fun clearTheData() {
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
            binding.btLogin.setOnClickListener(this)
            binding.tvForget.setOnClickListener(this)
            binding.passwordVisible.setOnClickListener(this)
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
        binding.tvPrivacyPolicy.run {
            text = ss
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = Color.TRANSPARENT
        }

    }

    private fun initializeWidgets() {
        try {
            setupPrivacyPolicy()

            binding.etPassword.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.toString().trim { it <= ' ' }.isNotEmpty()) {
                        binding.passwordVisible.visibility = View.VISIBLE
                    } else {
                        binding.passwordVisible.visibility = View.INVISIBLE
                    }
                }

                override fun afterTextChanged(s: Editable) {}
            })
            binding.etEmail.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    binding.etEmail.let {
                        val email = it.text.toString().trim { it <= ' ' }
                        if (email.isEmpty()) {
                            Utilities.showSnackBar(it, "Please enter Email/Mobile Number")
                            Helper.getInstance().openKeyboard(this@LoginActivity, it)
                            return@OnKeyListener true
                        } else if (!Utilities.validEmail(email) && Utilities.isNumeric(email) && email.length < 10) {
                            Utilities.showSnackBar(it, "Please enter valid Mobile Number")
                            Helper.getInstance().openKeyboard(this@LoginActivity, it)
                            return@OnKeyListener true
                        } else if (!Utilities.validEmail(email) && !Utilities.isNumeric(email)) {
                            Utilities.showSnackBar(it, "Please enter valid Email")
                            Helper.getInstance().openKeyboard(this@LoginActivity, it)
                            return@OnKeyListener true
                        }
                    }
                }
                false
            })
            binding.etPassword.setOnKeyListener { v, keyCode, event ->
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
                    binding.tvForget.isClickable = false
                    Navigation.getInstance().openForgotPasswordPage(this@LoginActivity)
                    binding.tvForget.isClickable = true
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
                val email = binding.etEmail.text.toString().trim { it <= ' ' }
                val password = binding.etPassword.text.toString().trim { it <= ' ' }

                if (email.isEmpty()) {
                    Utilities.showSnackBar(binding.etEmail, "Please enter Email/Mobile Number")
                    Helper.getInstance().openKeyboard(this@LoginActivity, binding.etEmail)
                } else if (!Utilities.validEmail(email) && Utilities.isNumeric(email) && email.length < 10) {
                    Utilities.showSnackBar(binding.etEmail, "Please enter valid Mobile Number")
                    Helper.getInstance().openKeyboard(this@LoginActivity, binding.etEmail)
                } else if (!Utilities.validEmail(email) && !Utilities.isNumeric(email)) {
                    Utilities.showSnackBar(binding.etEmail, "Please enter valid Email")
                    Helper.getInstance().openKeyboard(this@LoginActivity, binding.etEmail)
                } else if (password.trim { it <= ' ' }
                        .isEmpty() || password == "null" || password.isEmpty()) {
                    Utilities.showSnackBar(binding.etPassword, "Please enter  Password")
                    Helper.getInstance().openKeyboard(this@LoginActivity, binding.etPassword)
                } else {
                    closeKeyboard()
                    if (Utilities.getConnectivityStatus(this@LoginActivity) > 0) {
                        binding.btLogin.visibility = View.GONE
                        binding.tvForget.visibility = View.GONE
                        viewModel.callToLoginServer(email = email, password = password)
                    } else {
                        Utilities.showSnackBar(
                            binding.btLogin,
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

//    private fun callToLoginServer() {
//        val email = binding.etEmail.text.toString().trim { it <= ' ' }
//        val password = binding.etPassword.text.toString().trim { it <= ' ' }
//        try {
//            openProgress()
//
//            try {
//
//                val client = ApiClient().instance
//                val call = client?.getLoginDetails(email, password, Constants.TOKEN)
//                call?.enqueue(object : Callback<String?> {
//                    override fun onResponse(call: Call<String?>, response: Response<String?>) {
//                        if (response.code() == Constants.RESPONCE_SUCCESSFUL) {
//                            closeProgress()
//                            try {
//                                val apiresponce = JSONObject(response.body())
//                                if (apiresponce != null) {
//                                    if (apiresponce.optBoolean("success")) {
//
//                                        //Utilities.showSnackBar(bt_login, apiresponce.getMessage());
//                                        val user = Gson().fromJson(
//                                            apiresponce.optJSONObject("user").toString(),
//                                            User::class.java
//                                        ) // apiresponce.getUser();
//                                        Log.e(
//                                            "user==> ",
//                                            "" + Gson().toJson(apiresponce) + "   " + Gson().toJson(
//                                                response.body()
//                                            )
//                                        )
//                                        val editor = sharedPreferences.edit()
//                                        try {
//                                            if (user != null) {
//                                                editor.putString(
//                                                    SharePreferenceKeys.USER_AVATAR,
//                                                    if (user.userAvatar != null) user.userAvatar else ""
//                                                )
//                                                editor.putBoolean(
//                                                    SharePreferenceKeys.SP_LOGIN_STATUS,
//                                                    true
//                                                )
//                                                editor.putString(
//                                                    SharePreferenceKeys.USER_ID,
//                                                    user.userId
//                                                )
//                                                editor.putString(
//                                                    SharePreferenceKeys.USER_NAME,
//                                                    user.name
//                                                )
//                                                editor.putString(
//                                                    SharePreferenceKeys.USER_EMAIL,
//                                                    user.email
//                                                )
//                                                editor.putString(
//                                                    SharePreferenceKeys.USER_MOBILE,
//                                                    user.mobile
//                                                )
//                                                editor.putString(
//                                                    SharePreferenceKeys.COMPANY_ID,
//                                                    user.companyId
//                                                )
//                                                editor.putInt(
//                                                    SharePreferenceKeys.USER_ROLE,
//                                                    user.role.toInt()
//                                                )
//                                                editor.putString(
//                                                    SharePreferenceKeys.API_KEY,
//                                                    user.apiKey
//                                                )
//                                                editor.putString(
//                                                    SharePreferenceKeys.COMPANY_NAME,
//                                                    user.companyName
//                                                )
//                                                editor.putString(
//                                                    SharePreferenceKeys.USER_DEPARTMENT,
//                                                    user.department
//                                                )
//                                                editor.putString(
//                                                    SharePreferenceKeys.USER_DESIGNATION,
//                                                    user.designation
//                                                )
//                                                editor.putString(
//                                                    SharePreferenceKeys.REPORTING_BOSS_ID,
//                                                    user.reporting_boss_id
//                                                )
//                                                editor.putString(
//                                                    SharePreferenceKeys.COMPANY_LOCATION,
//                                                    user.comapanyBranch
//                                                )
//                                                editor.putBoolean(
//                                                    SharePreferenceKeys.SP_LOGOUT_STATUS,
//                                                    false
//                                                )
//                                                editor.putBoolean(
//                                                    SharePreferenceKeys.IS_CC_USER,
//                                                    user.isIs_cc_exist
//                                                )
//                                                if (apiresponce.has("reportingUsers") && (apiresponce.optJSONArray(
//                                                        "reportingUsers"
//                                                    )?.length() ?: 0) > 0
//                                                ) {
//                                                    editor.putBoolean(
//                                                        SharePreferenceKeys.TEAM_LEAD,
//                                                        true
//                                                    )
//                                                } else if (user.isIs_to_exists) {
//                                                    editor.putBoolean(
//                                                        SharePreferenceKeys.TEAM_LEAD,
//                                                        true
//                                                    )
//                                                } else {
//                                                    editor.putBoolean(
//                                                        SharePreferenceKeys.TEAM_LEAD,
//                                                        false
//                                                    )
//                                                }
//                                                if (apiresponce.has("is_full_access") && apiresponce.optString(
//                                                        "is_full_access"
//                                                    ) == "1"
//                                                ) {
//                                                    editor.putBoolean(
//                                                        SharePreferenceKeys.FULL_ACCESS,
//                                                        true
//                                                    )
//                                                } else {
//                                                    editor.putBoolean(
//                                                        SharePreferenceKeys.FULL_ACCESS,
//                                                        false
//                                                    )
//                                                }
//                                                if (apiresponce.has("user_info")) {
//                                                    val userInfo = user.userInfo
//                                                    if (userInfo != null) {
//                                                        editor.putBoolean(
//                                                            SharePreferenceKeys.attendance_all_tab,
//                                                            userInfo.isAttendance_all_tab
//                                                        )
//                                                        editor.putBoolean(
//                                                            SharePreferenceKeys.attendance_team_tab,
//                                                            userInfo.isAttendance_team_tab
//                                                        )
//                                                        editor.putBoolean(
//                                                            SharePreferenceKeys.request_all_tab,
//                                                            userInfo.isAttendance_all_tab
//                                                        )
//                                                        editor.putBoolean(
//                                                            SharePreferenceKeys.request_team_tab,
//                                                            userInfo.isRequest_team_tab
//                                                        )
//                                                    }
//                                                }
//                                                editor.putInt("countforservice", count)
//                                                editor.apply()
////                                                    if (HandlerHolder.applicationHandlers != null) {
////                                                        HandlerHolder.applicationHandlers.obtainMessage(
////                                                            Constants.LOGIN_SUCCESSFUL
////                                                        ).sendToTarget()
////                                                    }
//
//                                                /*MyApplication application=(MyApplication) getApplication();
//                            application.initSocket();*/
//                                                val sp1 = getSharedPreferences("userrecord", 0)
//                                                val editor1 = sp1.edit()
//                                                editor1.putBoolean("isLogin", true)
//                                                editor1.apply()
//                                                Navigation.getInstance()
//                                                    .openHomePage(this@LoginActivity)
//                                                finish()
//                                            }
//                                        } catch (e: Exception) {
//                                            e.printStackTrace()
//                                        }
//                                    } else {
//                                        Utilities.showSnackBar(
//                                            binding.btLogin,
//                                            apiresponce.optString("message")
//                                        )
//                                        binding.btLogin.visibility = View.VISIBLE
//                                        binding.tvForget.visibility = View.VISIBLE
//                                    }
//                                } else {
//                                    Utilities.showSnackBar(
//                                        binding.btLogin,
//                                        resources.getString(R.string.somthing_went_wrong)
//                                    )
//                                    binding.btLogin.visibility = View.VISIBLE
//                                    binding.tvForget.visibility = View.VISIBLE
//                                }
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                            }
//                        }
//                    }
//
//                    override fun onFailure(call: Call<String?>, t: Throwable) {
//                        closeProgress()
//                        Log.e("error==> ", "" + t.message)
//                        binding.btLogin.visibility = View.VISIBLE
//                        binding.tvForget.visibility = View.VISIBLE
//                    }
//                })
//            } catch (e: Exception) {
//                e.printStackTrace()
//                closeProgress()
//            }
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    private fun togglePasswordVisiblity() {
        try {
            if (binding.etPassword.text.toString().isNotEmpty()) {
                if (binding.etPassword.inputType == (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    binding.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
                    binding.passwordVisible.setImageResource(R.drawable.ic_hide)
                } else {
                    binding.passwordVisible.setImageResource(R.drawable.ic_view)
                    binding.etPassword.inputType =
                        (InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                }
                binding.etPassword.setSelection(binding.etPassword.text.toString().length)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}