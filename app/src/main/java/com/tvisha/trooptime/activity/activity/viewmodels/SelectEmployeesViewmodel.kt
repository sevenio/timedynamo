package com.tvisha.trooptime.activity.activity.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tvisha.trooptime.activity.activity.db.UsersModel
import com.tvisha.trooptime.activity.activity.db.UsersTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SelectEmployeesViewmodel(application: Application): AndroidViewModel(application) {

    val userList: MutableLiveData<List<UsersModel>> = MutableLiveData()

    fun getUsers(){
        viewModelScope.launch {
            val list = withContext(Dispatchers.IO) {
                val userTable = UsersTable(getApplication())
               userTable.users
            }
            userList.postValue(list)
        }
    }
}