package com.tvisha.trooptime.activity.activity.viewmodels

import android.content.Context
import androidx.lifecycle.*
import com.tvisha.trooptime.activity.activity.ApiPostModels.ForgotPasswordResponce
import com.tvisha.trooptime.activity.activity.Helper.SharePreferenceKeys
import com.tvisha.trooptime.activity.activity.api.ApiClient
import com.tvisha.trooptime.activity.activity.api.ApiInterface
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NotificationViewmodel : ViewModel() {
    private val _notificationCalendars: MutableLiveData<NotificationCalendars> = MutableLiveData()

    val notificationCalendars: LiveData<NotificationCalendars> = _notificationCalendars

    val _fragmentType: MutableLiveData<NotificationFragmentType> = MutableLiveData()

    val fragmentType: LiveData<NotificationFragmentType> = _fragmentType

    val showProgress: MutableLiveData<Boolean> = MutableLiveData(false)

    var selectedTab = 0


    var teamTotalCount = 0
    var teamOffset = 0
    var teamLimit = 10
    var teamIsLastPage = false
    var teamIsLoadingBool = true
    val teamIsLoading = MutableLiveData<Boolean>(false)


    var selfTotalCount = 0
    var selfOffset = 0
    var selfLimit = 10
    var selfIsLastPage = false
    var selfIsLoadingBool = true
    val selfIsLoading = MutableLiveData<Boolean>(false)

    private val apiService: ApiInterface by lazy {
        ApiClient.getClient().create<ApiInterface>(
            ApiInterface::class.java
        )
    }


    init {

        _notificationCalendars.postValue(
            NotificationCalendars(
                Calendar.getInstance(),
                Calendar.getInstance(),
            )
        )
        _fragmentType.postValue(NotificationFragmentType.SELF)
    }

    fun updateCalendar(calendar: Calendar) {
        when (_fragmentType.value) {
            NotificationFragmentType.SELF -> {
                _notificationCalendars.postValue(notificationCalendars.value?.copy(selfCalendar = calendar))
            }
            NotificationFragmentType.TEAM -> {
                _notificationCalendars.postValue(notificationCalendars.value?.copy(teamCalendar = calendar))

            }
            null -> {}
        }

    }

    fun updateType(type: NotificationFragmentType) {
        viewModelScope.launch {
            _fragmentType.postValue(type)
        }
    }

    fun getTeamNotifications(isLoading: Boolean) {
        viewModelScope.launch {
            showProgress.postValue(true)

            val call: Call<ForgotPasswordResponce> = apiService.getOtp("number")
            call.enqueue(object : Callback<ForgotPasswordResponce> {
                override fun onResponse(
                    call: Call<ForgotPasswordResponce>,
                    response: Response<ForgotPasswordResponce>
                ) {
                    showProgress.postValue(true)

                    if (response.isSuccessful) {
                        if (isLoading) {
                            teamIsLoading.postValue(false)
                        }

                    } else {

                    }
                }

                override fun onFailure(call: Call<ForgotPasswordResponce>, t: Throwable) {
                    showProgress.postValue(true)

                }
            })

        }
    }

}

data class NotificationCalendars(
    val selfCalendar: Calendar,
    val teamCalendar: Calendar,
)

enum class NotificationFragmentType {
    SELF, TEAM
}