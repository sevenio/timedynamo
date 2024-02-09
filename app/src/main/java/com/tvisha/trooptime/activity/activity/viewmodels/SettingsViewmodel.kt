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


    val isMuteSelfNotifications: MutableLiveData<Boolean> = MutableLiveData()
    val isMuteSelfCheckinNotifications: MutableLiveData<Boolean> = MutableLiveData()
    val isMuteSelfCheckoutNotifications: MutableLiveData<Boolean> = MutableLiveData()
    val isMuteSelfBreakNotifications: MutableLiveData<Boolean> = MutableLiveData()
    val isMuteLeaveApprovalNotifications: MutableLiveData<Boolean> = MutableLiveData()

    val isMuteTeamNotifications: MutableLiveData<Boolean> = MutableLiveData()
    val isMuteTeamCheckinNotifications: MutableLiveData<Boolean> = MutableLiveData()
    val isMuteTeamCheckoutNotifications: MutableLiveData<Boolean> = MutableLiveData()
    val isMuteTeamBreakNotifications: MutableLiveData<Boolean> = MutableLiveData()
    val isMuteTeamClientVisitNotifications: MutableLiveData<Boolean> = MutableLiveData()
    val isMuteTeamLeaveApprovalNotifications: MutableLiveData<Boolean> = MutableLiveData()

    private val apiService: ApiInterface by lazy {
        ApiClient.getClient().create<ApiInterface>(
            ApiInterface::class.java
        )
    }
    val showProgress: MutableLiveData<Boolean> = MutableLiveData(false)
    val responseSuccess: MutableLiveData<String> = MutableLiveData()
    val responceFailure: MutableLiveData<String> = MutableLiveData()

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


}


