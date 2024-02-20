package com.tvisha.trooptime.activity.activity.fcm;

import android.content.SharedPreferences;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.tvisha.trooptime.activity.activity.apiPostModels.CommonResponse;
import com.tvisha.trooptime.activity.activity.helper.SharePreferenceKeys;
import com.tvisha.trooptime.activity.activity.helper.Utilities;
import com.tvisha.trooptime.activity.activity.api.ApiClient;
import com.tvisha.trooptime.activity.activity.api.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FirebaseIDService extends FirebaseMessagingService {


    public static String token = "", userId = "", fcmToken = "", deviceId = "";
    boolean isLogin = false;
    SharedPreferences sharedPreferences;
    ApiInterface apiService;

    public String getRefreshToken() {
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()){
                    return;
                }
                fcmToken = task.getResult();
            }
        });
        return fcmToken;
        //return FirebaseInstanceId.getInstance().getToken();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        fcmToken = getRefreshToken();
        callToserver(fcmToken);
    }

    public void onTokenRefresh() {
        // Get updated InstanceID token.
        try {

            String refreshedToken = getRefreshToken();
            callToserver(refreshedToken);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    public void callToserver(String refreshedToken){
        try {
            fcmToken = refreshedToken;

            sharedPreferences = getSharedPreferences(SharePreferenceKeys.SP_NAME, MODE_PRIVATE);
            isLogin = sharedPreferences.getBoolean(SharePreferenceKeys.SP_LOGIN_STATUS, false);
            userId = sharedPreferences.getString(SharePreferenceKeys.USER_ID, "");
            deviceId = sharedPreferences.getString(SharePreferenceKeys.DEVICE_ID, "");
            token = sharedPreferences.getString(SharePreferenceKeys.API_KEY, "");
            apiService = ApiClient.getClient().create(ApiInterface.class);
            if (isLogin) {
                sendRegistrationToServer(refreshedToken);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendRegistrationToServer(String refreshedToken) {


        if (Utilities.isNetworkAvailable(getApplicationContext())) {
            callSaveFcmApi(refreshedToken);
        }
    }

    public void callSaveFcmApi(String refreshedToken) {

        Call<CommonResponse> call = apiService.saveFcmToken(token, userId, refreshedToken, "1", deviceId);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse apiResponse = response.body();

                if (apiResponse != null) {

                }

            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {


            }

        });
    }

}
