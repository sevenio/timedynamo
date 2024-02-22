package com.tvisha.trooptime.activity.activity.app

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.content.SharedPreferences
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Base64
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.tvisha.trooptime.activity.activity.api.ApiClient
import com.tvisha.trooptime.activity.activity.apiPostModels.GetAwsConfigResponse
import com.tvisha.trooptime.activity.activity.apiPostModels.HomePageResponse
import com.tvisha.trooptime.activity.activity.apiPostModels.SelfAttendenceApiResponce
import com.tvisha.trooptime.activity.activity.apiPostModels.UserRequestListResponse
import com.tvisha.trooptime.activity.activity.di.AppCompositionRoot
import com.tvisha.trooptime.activity.activity.handlers.HandlerHolder
import com.tvisha.trooptime.activity.activity.helper.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.UnsupportedEncodingException

class MyApplication : Application(), ActivityLifecycleCallbacks {

    private val applicationScope = CoroutineScope(SupervisorJob())

    val appCompositionRoot by lazy {
        AppCompositionRoot(this, applicationScope)
    }

    @SuppressLint("HandlerLeak")
    var socketHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                SocketConstants.SOCKET_CONNECT -> {
                    Utilities.syncAttendance(this@MyApplication)
                    val socketIo = SocketIo.getInstance()
                    val socket = socketIo.socket
                    socket.emit(SocketConstants.EVENT_SYNC_DATA, JSONObject())
                }
                SocketConstants.SOCKET_DISCONNECT -> {}
                SocketConstants.SOCKET_SYNC_DATA -> try {
                    val syncDataObject = msg.obj as JSONObject
                    Thread {
                        if (syncDataObject.has("users")) {
                            try {
                                val update_employee = Utilities.addEmployee(
                                    this@MyApplication,
                                    syncDataObject.getJSONArray("users")
                                )
                                if (update_employee) {
                                    if (HandlerHolder.userdetectHandler != null) {
                                        HandlerHolder.userdetectHandler.obtainMessage(Constants.EMPLOYESSD_ADDED)
                                            .sendToTarget()
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        Utilities.syncData(this@MyApplication, syncDataObject)
                    }.start()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                SocketConstants.SOCKET_SYNC_USER -> try {
                    val jsonObject = msg.obj as JSONObject
                    if (jsonObject != null) {
                        Utilities.addEmployee(this@MyApplication, jsonObject)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                SocketConstants.SOCKET_SYNC_ATTENDANCE -> try {
                    val jsonArray = msg.obj as JSONArray
                    if (jsonArray.length() > 0) {
                        Utilities.updateAttendance(this@MyApplication, jsonArray)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                SocketConstants.SOCKET_SYNC_BREAK -> try {
                    val jsonArray = msg.obj as JSONArray
                    Utilities.updateBreak(this@MyApplication, jsonArray)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    var activity: Activity? = null
    var mManager: UsbManager? = null
    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        this.activity = activity
    }

    fun remoteConfig() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val defaultValue: MutableMap<String, Any> = HashMap()
        defaultValue[UpdateHelper.KEY_UPDATE_ENABLE] = true
        defaultValue[UpdateHelper.KEY_UPDATE_VERSION] = "2.0"
        defaultValue[UpdateHelper.KEY_UPDATE_URL] =
            "https://play.google.com/store/apps/details?id=com.tvisha.trooptime"
        remoteConfig.setDefaultsAsync(defaultValue)
        remoteConfig.fetch(5)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    remoteConfig.fetchAndActivate()
                }
            }
    }

    fun initSocket() {
        HandlerHolder.socketHandler = socketHandler
        val socketIo = SocketIo.getInstance()
        socketIo.setContext(applicationContext)
        socketIo.setHandlers(socketHandler)
        socketIo.connectSocket()
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        //initSocket();
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
    override fun onCreate() {
        /*    if(Utilities.getLoginStatus(this)){
            initSocket();
        }
        registerActivityLifecycleCallbacks(this);
*/
        sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE)
        if (sharedPreferences?.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false) == true) {
            AWS_KEY = sharedPreferences?.getString(SharePreferenceKeys.AWS_KEY, "")
            AWS_BASE_URL = sharedPreferences?.getString(SharePreferenceKeys.AWS_BASE_URL, "")
            AWS_BUCKET = sharedPreferences?.getString(SharePreferenceKeys.AWS_BUCKET, "")
            AWS_SECRET_KEY = sharedPreferences?.getString(SharePreferenceKeys.AWS_SECRET_KEY, "")
            AWS_REGION = sharedPreferences?.getString(SharePreferenceKeys.AWS_REGION, "")
            HandlerHolder.applicationHandlers = uiHandler
            callAwsKeys()
        }
        super.onCreate()
    }

    /*public void ls () {
        try {
            Process process = Runtime.getRuntime().exec("ping 192.168.2.2");
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = "";
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    Log.e("reader--> ",""+line);
                    output += line + "\n";
                }
            }catch (Exception e){
                Log.e("reader--> ",""+e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            Log.e("reader--> "," 1 "+e.getMessage());
            e.printStackTrace();
        }
    }*/
    @SuppressLint("HandlerLeak")
    var uiHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                Constants.LOGIN_SUCCESSFUL -> callAwsKeys()
            }
        }
    }

    fun callAwsKeys() {
        if (Utilities.isNetworkAvailable(
                applicationContext
            )
        ) {
            Thread { callGetAwsConfig() }.start()
        }
    }

    private fun callGetAwsConfig() {
        try {
            val call = ApiClient().instance.getAwsConfig(Constants.TOKEN)
            call!!.enqueue(object : Callback<GetAwsConfigResponse?> {
                override fun onResponse(
                    call: Call<GetAwsConfigResponse?>,
                    response: Response<GetAwsConfigResponse?>
                ) {
                    val apiResponse = response.body()
                    if (apiResponse != null) {
                        if (apiResponse.isSuccess) {
                            decryptData(apiResponse)
                        }
                    }
                }

                override fun onFailure(call: Call<GetAwsConfigResponse?>, t: Throwable) {}
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun decryptData(apiResponse: GetAwsConfigResponse) {
        try {
            val base64 = apiResponse.data
            if (base64 != null) {
                val data = Base64.decode(base64, Base64.DEFAULT)
                try {
                    val text = String(data, Charsets.UTF_8)
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(text)
                        if (jsonObject != null) {
                            AWS_KEY = jsonObject.getString("key")
                            AWS_SECRET_KEY = jsonObject.getString("secret")
                            AWS_BUCKET = jsonObject.getString("bucket")
                            AWS_REGION = jsonObject.getString("region")
                            AWS_BASE_URL = jsonObject.getString("base_url")
                            sharedPreferences!!.edit()
                                .putString(SharePreferenceKeys.AWS_KEY, AWS_KEY).apply()
                            sharedPreferences!!.edit()
                                .putString(SharePreferenceKeys.AWS_BUCKET, AWS_BUCKET).apply()
                            sharedPreferences!!.edit()
                                .putString(SharePreferenceKeys.AWS_REGION, AWS_REGION).apply()
                            sharedPreferences!!.edit()
                                .putString(SharePreferenceKeys.AWS_BASE_URL, AWS_BASE_URL).apply()
                            sharedPreferences!!.edit()
                                .putString(SharePreferenceKeys.AWS_SECRET_KEY, AWS_SECRET_KEY)
                                .apply()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onTerminate() {
        unregisterActivityLifecycleCallbacks(this)
        super.onTerminate()
    }

    fun getRunningActivityName(mContext: Context): String {
        var activityName = ""
        val am = mContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val tasks = am.getRunningTasks(1)
        if (!tasks.isEmpty()) {
            activityName = tasks[0].topActivity!!.className
        }
        return activityName
    }

    companion object {
        @JvmField
        var homePageResponse: HomePageResponse? = null
        @JvmField
        var UserRequestListResponse: UserRequestListResponse? = null
        @JvmField
        var selfAttendenceApiResponce: SelfAttendenceApiResponce? = null
        @JvmField
        var AWS_KEY: String? = ""
        @JvmField
        var AWS_SECRET_KEY: String? = ""
        @JvmField
        var AWS_BUCKET: String? = ""
        var AWS_REGION: String? = ""
        @JvmField
        var AWS_BASE_URL: String? = ""
        @JvmField
        var sharedPreferences: SharedPreferences? = null
    }
}