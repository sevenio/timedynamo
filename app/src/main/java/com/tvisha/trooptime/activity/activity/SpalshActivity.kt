package com.tvisha.trooptime.activity.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tvisha.trooptime.activity.activity.Helper.Navigation
import com.tvisha.trooptime.activity.activity.Helper.SharePreferenceKeys
import com.tvisha.trooptime.databinding.ActivitySpalshBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SpalshActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySpalshBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpalshBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startTimer()
    }

    private fun startTimer() {
        lifecycleScope.launch {
            delay(1000)
            if (getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE).getBoolean(
                    SharePreferenceKeys.SP_LOGIN_STATUS,
                    false
                )
            ) {
                Navigation.getInstance().openHomePage(this@SpalshActivity)
                finish()
            } else {
                Navigation.getInstance().openLoginPage(this@SpalshActivity)
                finish()
            }
        }

    }


}