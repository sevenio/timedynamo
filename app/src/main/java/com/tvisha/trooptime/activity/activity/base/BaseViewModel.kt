package com.tvisha.trooptime.activity.activity.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tvisha.trooptime.activity.activity.app.MyApplication

open class BaseViewModel(application: Application) : AndroidViewModel(application = application) {
    val repository by lazy {
        (application as MyApplication).appCompositionRoot.repository
    }

    fun getAwsBaseUrl(): String{
        return repository.getAwsBaseUrl()
    }


}