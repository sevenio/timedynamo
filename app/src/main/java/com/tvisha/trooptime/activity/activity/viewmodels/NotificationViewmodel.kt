package com.tvisha.trooptime.activity.activity.viewmodels

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.util.*

class NotificationViewmodel : ViewModel() {
    private val _notificationCalendars: MutableLiveData<NotificationCalendars> = MutableLiveData()

    val notificationCalendars: LiveData<NotificationCalendars> = _notificationCalendars

    val _fragmentType: MutableLiveData<NotificationFragmentType> = MutableLiveData()

    val fragmentType: LiveData<NotificationFragmentType> = _fragmentType


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

}

data class NotificationCalendars(
    val selfCalendar: Calendar,
    val teamCalendar: Calendar,
)

enum class NotificationFragmentType {
    SELF, TEAM
}