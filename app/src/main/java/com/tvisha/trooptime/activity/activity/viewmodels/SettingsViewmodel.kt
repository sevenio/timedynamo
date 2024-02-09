package com.tvisha.trooptime.activity.activity.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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


}


