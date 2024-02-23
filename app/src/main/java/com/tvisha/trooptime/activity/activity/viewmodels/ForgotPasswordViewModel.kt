package com.tvisha.trooptime.activity.activity.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tvisha.trooptime.activity.activity.api.ResponseResult
import com.tvisha.trooptime.activity.activity.apiPostModels.ForgotPasswordResponce
import com.tvisha.trooptime.activity.activity.base.BaseViewModel
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(application: Application) : BaseViewModel(application = application) {
    private val _progress: MutableLiveData<Boolean> = MutableLiveData(false)
    val progress: LiveData<Boolean> = _progress

    private val _forgotSuccess: MutableLiveData<ForgotPasswordResponce> = MutableLiveData()
    val forgotSuccess: LiveData<ForgotPasswordResponce> = _forgotSuccess

    private val _forgotFail: MutableLiveData<String> = MutableLiveData("")
    val forgotFail: LiveData<String> = _forgotFail


    fun getOtp(number: String) {
        viewModelScope.launch {

            _progress.postValue(true)

            val result = repository.getOtp(number)
            _progress.postValue(false)

            if (result is ResponseResult.Success) {
                _forgotSuccess.postValue(result.data)
            } else {
                _forgotFail.postValue((result as ResponseResult.Error).message)
            }

        }
    }

}