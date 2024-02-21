package com.tvisha.trooptime.activity.activity.fcm

import android.content.SharedPreferences
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.tvisha.trooptime.activity.activity.api.ApiClient
import com.tvisha.trooptime.activity.activity.api.ApiInterface
import com.tvisha.trooptime.activity.activity.apiPostModels.CommonResponse
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys
import com.tvisha.trooptime.activity.activity.helper.Utilities
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FirebaseIDService : FirebaseMessagingService() {
    private val sharedPreferences by lazy {
        getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE)
    }
    private val apiService by lazy {
        ApiClient.getInstance()
    }



    override fun onNewToken(s: String) {
        super.onNewToken(s)
        callToserver(s)
    }



    private fun callToserver(refreshedToken: String?) {
        try {
            if (sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false)) {
                sendRegistrationToServer(refreshedToken)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendRegistrationToServer(refreshedToken: String?) {
        if (Utilities.isNetworkAvailable(
                applicationContext
            )
        ) {
            callSaveFcmApi(refreshedToken)
        }
    }

    fun callSaveFcmApi(refreshedToken: String?) {
        val userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "")
        val deviceId = sharedPreferences.getString(SharePreferenceKeys.DEVICE_ID, "")
        val token = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "")
        val call = apiService?.saveFcmToken(token, userId, refreshedToken, "1", deviceId)
        call?.enqueue(object : Callback<CommonResponse?> {
            override fun onResponse(
                call: Call<CommonResponse?>,
                response: Response<CommonResponse?>
            ) {
                val apiResponse = response.body()
                if (apiResponse != null) {
                }
            }

            override fun onFailure(call: Call<CommonResponse?>, t: Throwable) {}
        })
    }

}