package com.tvisha.trooptime.activity.activity.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class NotificationViewmodel: ViewModel() {

    private val _notificationUiModel: MutableLiveData<NotificationUiModel> = MutableLiveData()
    val notificationUiModel: LiveData<NotificationUiModel>  = _notificationUiModel
    init {

    }

}

data class NotificationUiModel(val calendar: Calendar, val type: Int)