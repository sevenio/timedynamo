package com.tvisha.trooptime.activity.activity.viewmodels

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tvisha.trooptime.R
import com.tvisha.trooptime.activity.activity.api.ApiClient
import com.tvisha.trooptime.activity.activity.apiPostModels.User
import com.tvisha.trooptime.activity.activity.handlers.HandlerHolder
import com.tvisha.trooptime.activity.activity.helper.Constants
import com.tvisha.trooptime.activity.activity.helper.Navigation
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys
import com.tvisha.trooptime.activity.activity.helper.Utilities
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application = application) {


    private val _progress: MutableLiveData<Boolean> = MutableLiveData(false)
    private val progress: LiveData<Boolean> = _progress

    private fun callToLoginServer(email: String, password: String) {
        viewModelScope.launch {
            try {
                _progress.postValue(true)

                try {

                    val client = ApiClient().instance
                    val call = client.getLoginDetails(email, password, Constants.TOKEN)
//                    call?.enqueue(object : Callback<String?> {
//                        override fun onResponse(call: Call<String?>, response: Response<String?>) {
//                            if (response.code() == Constants.RESPONCE_SUCCESSFUL) {
//                                _progress.postValue(false)
//
//                                try {
//                                    val apiresponce = JSONObject(response.body())
//                                    if (apiresponce != null) {
//                                        if (apiresponce.optBoolean("success")) {
//
//                                            //Utilities.showSnackBar(bt_login, apiresponce.getMessage());
//                                            val user = Gson().fromJson(
//                                                apiresponce.optJSONObject("user").toString(),
//                                                User::class.java
//                                            ) // apiresponce.getUser();
//                                            Log.e(
//                                                "user==> ",
//                                                "" + Gson().toJson(apiresponce) + "   " + Gson().toJson(
//                                                    response.body()
//                                                )
//                                            )
//                                            val editor = sharedPreferences.edit()
//                                            try {
//                                                if (user != null) {
//                                                    editor.putString(
//                                                        SharePreferenceKeys.USER_AVATAR,
//                                                        if (user.userAvatar != null) user.userAvatar else ""
//                                                    )
//                                                    editor.putBoolean(
//                                                        SharePreferenceKeys.SP_LOGIN_STATUS,
//                                                        true
//                                                    )
//                                                    editor.putString(
//                                                        SharePreferenceKeys.USER_ID,
//                                                        user.userId
//                                                    )
//                                                    editor.putString(
//                                                        SharePreferenceKeys.USER_NAME,
//                                                        user.name
//                                                    )
//                                                    editor.putString(
//                                                        SharePreferenceKeys.USER_EMAIL,
//                                                        user.email
//                                                    )
//                                                    editor.putString(
//                                                        SharePreferenceKeys.USER_MOBILE,
//                                                        user.mobile
//                                                    )
//                                                    editor.putString(
//                                                        SharePreferenceKeys.COMPANY_ID,
//                                                        user.companyId
//                                                    )
//                                                    editor.putInt(
//                                                        SharePreferenceKeys.USER_ROLE,
//                                                        user.role.toInt()
//                                                    )
//                                                    editor.putString(
//                                                        SharePreferenceKeys.API_KEY,
//                                                        user.apiKey
//                                                    )
//                                                    editor.putString(
//                                                        SharePreferenceKeys.COMPANY_NAME,
//                                                        user.companyName
//                                                    )
//                                                    editor.putString(
//                                                        SharePreferenceKeys.USER_DEPARTMENT,
//                                                        user.department
//                                                    )
//                                                    editor.putString(
//                                                        SharePreferenceKeys.USER_DESIGNATION,
//                                                        user.designation
//                                                    )
//                                                    editor.putString(
//                                                        SharePreferenceKeys.REPORTING_BOSS_ID,
//                                                        user.reporting_boss_id
//                                                    )
//                                                    editor.putString(
//                                                        SharePreferenceKeys.COMPANY_LOCATION,
//                                                        user.comapanyBranch
//                                                    )
//                                                    editor.putBoolean(
//                                                        SharePreferenceKeys.SP_LOGOUT_STATUS,
//                                                        false
//                                                    )
//                                                    editor.putBoolean(
//                                                        SharePreferenceKeys.IS_CC_USER,
//                                                        user.isIs_cc_exist
//                                                    )
//                                                    if (apiresponce.has("reportingUsers") && (apiresponce.optJSONArray(
//                                                            "reportingUsers"
//                                                        )?.length() ?: 0) > 0
//                                                    ) {
//                                                        editor.putBoolean(
//                                                            SharePreferenceKeys.TEAM_LEAD,
//                                                            true
//                                                        )
//                                                    } else if (user.isIs_to_exists) {
//                                                        editor.putBoolean(
//                                                            SharePreferenceKeys.TEAM_LEAD,
//                                                            true
//                                                        )
//                                                    } else {
//                                                        editor.putBoolean(
//                                                            SharePreferenceKeys.TEAM_LEAD,
//                                                            false
//                                                        )
//                                                    }
//                                                    if (apiresponce.has("is_full_access") && apiresponce.optString(
//                                                            "is_full_access"
//                                                        ) == "1"
//                                                    ) {
//                                                        editor.putBoolean(
//                                                            SharePreferenceKeys.FULL_ACCESS,
//                                                            true
//                                                        )
//                                                    } else {
//                                                        editor.putBoolean(
//                                                            SharePreferenceKeys.FULL_ACCESS,
//                                                            false
//                                                        )
//                                                    }
//                                                    if (apiresponce.has("user_info")) {
//                                                        val userInfo = user.userInfo
//                                                        if (userInfo != null) {
//                                                            editor.putBoolean(
//                                                                SharePreferenceKeys.attendance_all_tab,
//                                                                userInfo.isAttendance_all_tab
//                                                            )
//                                                            editor.putBoolean(
//                                                                SharePreferenceKeys.attendance_team_tab,
//                                                                userInfo.isAttendance_team_tab
//                                                            )
//                                                            editor.putBoolean(
//                                                                SharePreferenceKeys.request_all_tab,
//                                                                userInfo.isAttendance_all_tab
//                                                            )
//                                                            editor.putBoolean(
//                                                                SharePreferenceKeys.request_team_tab,
//                                                                userInfo.isRequest_team_tab
//                                                            )
//                                                        }
//                                                    }
//                                                    editor.putInt("countforservice", count)
//                                                    editor.apply()
//                                                    if (HandlerHolder.applicationHandlers != null) {
//                                                        HandlerHolder.applicationHandlers.obtainMessage(
//                                                            Constants.LOGIN_SUCCESSFUL
//                                                        ).sendToTarget()
//                                                    }
//
//                                                    /*MyApplication application=(MyApplication) getApplication();
//                            application.initSocket();*/
//                                                    val sp1 = getSharedPreferences("userrecord", 0)
//                                                    val editor1 = sp1.edit()
//                                                    editor1.putBoolean("isLogin", true)
//                                                    editor1.apply()
//                                                    Navigation.getInstance()
//                                                        .openHomePage(this@LoginActivity)
//                                                    finish()
//                                                }
//                                            } catch (e: Exception) {
//                                                e.printStackTrace()
//                                            }
//                                        } else {
////                                    Utilities.showSnackBar(
////                                        binding.btLogin,
////                                        apiresponce.optString("message")
////                                    )
////                                    binding.btLogin.visibility = View.VISIBLE
////                                    binding.tvForget.visibility = View.VISIBLE
//                                        }
//                                    } else {
////                                Utilities.showSnackBar(
////                                    binding.btLogin,
////                                    resources.getString(R.string.somthing_went_wrong)
////                                )
////                                binding.btLogin.visibility = View.VISIBLE
////                                binding.tvForget.visibility = View.VISIBLE
//                                    }
//                                } catch (e: Exception) {
//                                    e.printStackTrace()
//                                }
//                            }
//                        }
//
//                        override fun onFailure(call: Call<String?>, t: Throwable) {
//                            _progress.postValue(false)
//                            Log.e("error==> ", "" + t.message)
////                    binding.btLogin.visibility = View.VISIBLE
////                    binding.tvForget.visibility = View.VISIBLE
//                        }
//                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                    _progress.postValue(false)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}