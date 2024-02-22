package com.tvisha.trooptime.activity.activity.base

import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.tvisha.trooptime.activity.activity.api.ApiClient
import com.tvisha.trooptime.activity.activity.api.ResponseResult
import com.tvisha.trooptime.activity.activity.apiPostModels.GetAwsConfigResponse
import com.tvisha.trooptime.activity.activity.apiPostModels.User
import com.tvisha.trooptime.activity.activity.di.AppCompositionRoot
import com.tvisha.trooptime.activity.activity.helper.Constants
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.UnsupportedEncodingException
import java.net.SocketTimeoutException

open class BaseRepository(val appCompositionRoot: AppCompositionRoot) {

    private val apiService by lazy {
        ApiClient().instance
    }

    private val coroutineScope by lazy {
        appCompositionRoot.applicationCoroutineScope
    }

    private val sharedPreferences by lazy {
        appCompositionRoot.context.getSharedPreferences(
            SharePreferenceKeys.SP_NAME,
            AppCompatActivity.MODE_PRIVATE
        )
    }

    fun getAwsBaseUrl(): String {
        return sharedPreferences.getString(SharePreferenceKeys.AWS_BASE_URL, "")?:""
    }

    suspend fun login(email: String, password: String): ResponseResult<String> {
        return try {
            val loginResponse =
                apiService.getLoginDetails(
                    username = email,
                    password = password,
                    token = Constants.TOKEN
                )
            if (loginResponse.isSuccessful && loginResponse.body() != null) {
//                val awsConfigResult = callAwsKeys()
//                if (awsConfigResult is ResponseResult.Success) {
                    saveUserDetails(loginResponse.body()!!)
                    if (JSONObject(loginResponse.body()).optBoolean("success")) {
                        ResponseResult.Success(loginResponse.body()!!)
                    }else {
                        ResponseResult.Error(loginResponse.message())
                    }
//                } else {
//                    awsConfigResult as ResponseResult.Error
//                }
            } else {
                ResponseResult.Error(loginResponse.message())
            }
        } catch (e: Exception) {
            Log.e("error---> ", "" + e.message)
            ResponseResult.Error(getMessage(e))
        }
    }

//    suspend fun callAwsKeys(): ResponseResult<GetAwsConfigResponse> {
//        return try {
//
//            val awsConfigResponse = apiService.getAwsConfig(Constants.TOKEN)
//            if (awsConfigResponse.isSuccessful && awsConfigResponse.body() != null) {
//                decryptData(awsConfigResponse.body()!!)
//                ResponseResult.Success(awsConfigResponse.body()!!)
//            } else {
//                ResponseResult.Error(awsConfigResponse.message())
//            }
//
//        } catch (e: Exception) {
//            Log.e("error---> ", "" + e.message)
//            ResponseResult.Error(getMessage(e))
//        }
//
//    }

    private fun decryptData(apiResponse: GetAwsConfigResponse) {
        try {
            val base64 = apiResponse.data
            if (base64 != null) {
                val data = Base64.decode(base64, Base64.DEFAULT)
                try {
                    val text = String(data, Charsets.UTF_8)
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(text)
                        val AWS_KEY = jsonObject.getString("key")
                        val AWS_SECRET_KEY = jsonObject.getString("secret")
                        val AWS_BUCKET = jsonObject.getString("bucket")
                        val AWS_REGION = jsonObject.getString("region")
                        val AWS_BASE_URL = jsonObject.getString("base_url")
                        val editor = sharedPreferences.edit()


                        editor.putString(SharePreferenceKeys.AWS_KEY, AWS_KEY)
                        editor.putString(SharePreferenceKeys.AWS_BUCKET, AWS_BUCKET)

                        editor.putString(SharePreferenceKeys.AWS_REGION, AWS_REGION)

                        editor.putString(
                            SharePreferenceKeys.AWS_BASE_URL,
                            AWS_BASE_URL
                        )
                        editor.putString(
                            SharePreferenceKeys.AWS_SECRET_KEY,
                            AWS_SECRET_KEY
                        )
                        editor.apply()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun saveUserDetails(body: String) {

        val apiResponse = JSONObject(body)
        if (apiResponse.optBoolean("success")) {
            val user = Gson().fromJson(
                apiResponse.optJSONObject("user").toString(),
                User::class.java
            )
            if (user != null) {
                val editor = sharedPreferences.edit()
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

                if (apiResponse.has("reportingUsers") && (apiResponse.optJSONArray(
                        "reportingUsers"
                    )?.length() ?: 0) > 0
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

                if (apiResponse.has("is_full_access") && apiResponse.optString(
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

                if (apiResponse.has("user_info")) {
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
                editor.putInt("countforservice", 1)
                editor.apply()

            }
        }


    }


    fun getMessage(e: java.lang.Exception): String {
        Log.d("ganga", e.toString())
        return when (e) {
            is HttpException -> {
                when (e.code()) {
                    401 -> "Unauthorised"
                    404 -> "Resource not found"
                    else -> "Something went wrong"

                }

            }
            is SocketTimeoutException -> {
                "Timeout"
            }

            else -> "Something went wrong"
        }
    }
}