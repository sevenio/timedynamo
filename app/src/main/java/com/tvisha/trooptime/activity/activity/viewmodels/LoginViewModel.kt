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
import com.tvisha.trooptime.activity.activity.api.ResponseResult
import com.tvisha.trooptime.activity.activity.apiPostModels.User
import com.tvisha.trooptime.activity.activity.base.BaseViewModel
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

class LoginViewModel(application: Application) : BaseViewModel(application = application) {


    private val _progress: MutableLiveData<Boolean> = MutableLiveData(false)
    val progress: LiveData<Boolean> = _progress

    private val _loginSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    private val _loginFail: MutableLiveData<String> = MutableLiveData("")
    val loginFail: LiveData<String> = _loginFail


    fun callToLoginServer(email: String, password: String) {
        viewModelScope.launch {

            _progress.postValue(true)

            val result = repository.login(email, password)
            _progress.postValue(false)

            if (result is ResponseResult.Success) {
                _loginSuccess.postValue(true)
            } else {
                _loginFail.postValue((result as ResponseResult.Error).message)
            }


        }
    }


}