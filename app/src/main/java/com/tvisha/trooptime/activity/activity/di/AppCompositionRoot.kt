package com.tvisha.trooptime.activity.activity.di


import android.content.Context
import android.content.SharedPreferences
import com.tvisha.trooptime.activity.activity.api.ApiClient

import com.tvisha.trooptime.activity.activity.app.MyApplication
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys
import kotlinx.coroutines.CoroutineScope

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

class AppCompositionRoot(
    private val application: MyApplication,
    private val coroutineScope: CoroutineScope
) {

//    val networkHandler by lazy {
//        NetworkHandler(application)
//    }

    val sharedPreferences: SharedPreferences by lazy {
        application.getSharedPreferences(
            SharePreferenceKeys.SP_NAME,
            Context.MODE_PRIVATE
        )
    }

    
    

    val applicationCoroutineScope by lazy {
        coroutineScope
    }

    val retrofitClient by lazy {
        ApiClient.instance
//        RetrofitClient(sharedPreferences, AuthorizationInterceptor(application))
    }
//    val sharePreferenceUtils by lazy {
//        SharedPreferenceUtils(sharedPreferences)
//    }


//    val quadgenAtDatabase by lazy {
//        QuadgenAtDatabase.DatabaseManager(application).createDatabase()
//    }
    
//    val repository by lazy {
//        BaseRepository(appCompositionRoot = this)
//    }


}