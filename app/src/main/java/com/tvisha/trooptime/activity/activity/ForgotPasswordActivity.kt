package com.tvisha.trooptime.activity.activity

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tvisha.trooptime.R
import com.tvisha.trooptime.activity.activity.api.ApiClient
import com.tvisha.trooptime.activity.activity.api.ApiInterface
import com.tvisha.trooptime.activity.activity.apiPostModels.ForgotPasswordResponce
import com.tvisha.trooptime.activity.activity.dialog.CustomProgressBar
import com.tvisha.trooptime.activity.activity.helper.Helper
import com.tvisha.trooptime.activity.activity.helper.Navigation
import com.tvisha.trooptime.activity.activity.helper.Utilities
import com.tvisha.trooptime.activity.activity.viewmodels.ForgotPasswordViewModel
import com.tvisha.trooptime.activity.activity.viewmodels.LoginViewModel
import com.tvisha.trooptime.databinding.ActivityForgotPasswordBinding
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {


    private val binding by lazy {
        ActivityForgotPasswordBinding.inflate(layoutInflater)
    }
    private val customProgressBar: CustomProgressBar by lazy {
        CustomProgressBar(this@ForgotPasswordActivity)
    }
    private val viewModel: ForgotPasswordViewModel by viewModels()

    var mLastClicked: Long = 0

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
        setContentView(binding.root)
        initListeners()
        binding.mobileNumber.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                Helper.getInstance().openKeyboard(this@ForgotPasswordActivity, binding.mobileNumber)
                binding.next.performClick()
                binding.next.isClickable = false
                true
            } else {
                false
            }
        }
        viewModel.progress.observe(this) {
            if (it) {
                openProgress()
            } else {
                closeProgress()
            }
        }
        viewModel.forgotSuccess.observe(this) {
            Log.d("ganga", "success")
            closeProgress()
            binding.next.isClickable = true

            Toast.makeText(
                this@ForgotPasswordActivity,
                it.message,
                Toast.LENGTH_LONG
            ).show()

            Navigation.getInstance().openOtpVerify(
                this@ForgotPasswordActivity,
                it.userId,
                binding.mobileNumber.text.toString().trim { it <= ' ' }
            )

        }

        viewModel.forgotFail.observe(this) {
            closeProgress()
            if (it.isNotBlank()) {
                binding.next.isClickable = true
                Utilities.ShowSnackbar(
                    this@ForgotPasswordActivity,
                    binding.mobileNumber,
                    it
                )
            }
        }
    }

    private fun initListeners() {

        binding.next.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)

    }


    override fun onClick(v: View) {

        when (v.id) {
            R.id.next -> {
                binding.next!!.isClickable = false
                validateForm()

            }
            R.id.tv_login -> {
                binding.tvLogin!!.isClickable = false
                Navigation.getInstance().openLoginPage(this@ForgotPasswordActivity)
            }
        }

    }

    private fun validateForm() {
        try {
            Helper.getInstance().closeKeyBoard(this@ForgotPasswordActivity, binding.mobileNumber)
            if (SystemClock.elapsedRealtime() - mLastClicked < 100) {
                return
            }
            mLastClicked = SystemClock.elapsedRealtime()
            val number = binding.mobileNumber!!.text.toString().trim { it <= ' ' }
            if (number.isEmpty()) {
                binding.next.isClickable = true
                Utilities.showSnackBar(binding.mobileNumber, "Please enter Email/Mobile Number")
                Helper.getInstance().openKeyboard(this@ForgotPasswordActivity, binding.mobileNumber)
            } else if (!Utilities.validEmail(number) && Utilities.isNumeric(number) && number.length < 10) {
                binding.next.isClickable = true
                Utilities.showSnackBar(binding.mobileNumber, "Please enter valid Mobile Number")
                Helper.getInstance().openKeyboard(this@ForgotPasswordActivity, binding.mobileNumber)
            } else if (!Utilities.validEmail(number) && !Utilities.isNumeric(number)) {
                binding.next.isClickable = true
                Utilities.showSnackBar(binding.mobileNumber, "Please enter valid Email")
                Helper.getInstance().openKeyboard(this@ForgotPasswordActivity, binding.mobileNumber)
            } else {
                if (Utilities.isNetworkAvailable(this@ForgotPasswordActivity)) {
                    viewModel.getOtp(binding.mobileNumber.text.toString().trim { it <= ' ' })
                } else {
                    binding.next.isClickable = true
                    Utilities.showSnackBar(
                        binding.mobileNumber,
                        resources.getString(R.string.no_internet_connection)
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}