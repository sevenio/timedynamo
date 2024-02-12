package com.tvisha.trooptime.activity.activity.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tvisha.trooptime.activity.activity.ApiPostModels.ForgotPasswordResponce
import com.tvisha.trooptime.activity.activity.api.ApiClient
import com.tvisha.trooptime.activity.activity.api.ApiInterface
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsViewmodel : ViewModel() {
    var selectedTab: Int = 0


    val isMuteSelfNotifications: MutableLiveData<Boolean> = MutableLiveData(true)
    val isMuteSelfCheckinNotifications: MutableLiveData<Boolean> = MutableLiveData(true)
    val isMuteSelfCheckoutNotifications: MutableLiveData<Boolean> = MutableLiveData(true)
    val isMuteSelfBreakNotifications: MutableLiveData<Boolean> = MutableLiveData(true)
    val isMuteLeaveApprovalNotifications: MutableLiveData<Boolean> = MutableLiveData(true)

    val isMuteTeamNotifications: MutableLiveData<Boolean> = MutableLiveData(true)
    val isMuteTeamCheckinNotifications: MutableLiveData<Boolean> = MutableLiveData(true)
    val isMuteTeamCheckoutNotifications: MutableLiveData<Boolean> = MutableLiveData(true)
    val isMuteTeamBreakNotifications: MutableLiveData<Boolean> = MutableLiveData(true)
    val isMuteTeamClientVisitNotifications: MutableLiveData<Boolean> = MutableLiveData(true)
    val isMuteTeamLeaveApprovalNotifications: MutableLiveData<Boolean> = MutableLiveData(true)

    private val apiService: ApiInterface by lazy {
        ApiClient.getClient().create<ApiInterface>(
            ApiInterface::class.java
        )
    }

    val showProgress: MutableLiveData<Boolean> = MutableLiveData(false)
    val responseSuccess: MutableLiveData<String> = MutableLiveData()
    val responceFailure: MutableLiveData<String> = MutableLiveData()

    val saveSettingsSuccess : MutableLiveData<String> = MutableLiveData()
    val saveSettingsFailure: MutableLiveData<String> = MutableLiveData()

    fun getSettings() {
        viewModelScope.launch {
            showProgress.postValue(true)

            val call: Call<ForgotPasswordResponce> = apiService.getOtp("number")
            call.enqueue(object : Callback<ForgotPasswordResponce> {
                override fun onResponse(
                    call: Call<ForgotPasswordResponce>,
                    response: Response<ForgotPasswordResponce>
                ) {
                    showProgress.postValue(false)

                    if (response.isSuccessful) {
                        responseSuccess.postValue("")
                    } else {
                        responceFailure.postValue("")
                    }
                }

                override fun onFailure(call: Call<ForgotPasswordResponce>, t: Throwable) {
                    showProgress.postValue(true)
                    responceFailure.postValue("")

                }
            })

        }
    }

    fun saveSettings() {
        viewModelScope.launch {
            showProgress.postValue(true)

            val call: Call<ForgotPasswordResponce> = apiService.getOtp("number")
            call.enqueue(object : Callback<ForgotPasswordResponce> {
                override fun onResponse(
                    call: Call<ForgotPasswordResponce>,
                    response: Response<ForgotPasswordResponce>
                ) {
                    showProgress.postValue(false)

                    if (response.isSuccessful) {
                        saveSettingsSuccess.postValue("")
                    } else {
                        saveSettingsFailure.postValue("")
                    }
                }

                override fun onFailure(call: Call<ForgotPasswordResponce>, t: Throwable) {
                    showProgress.postValue(true)
                    saveSettingsFailure.postValue("")

                }
            })

        }
    }


}


