package com.tvisha.trooptime.activity.activity.api;

/**
 * Created by tvisha on 8/6/18.
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tvisha.trooptime.activity.activity.Helper.ServerUrls;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = ServerUrls.BaseUrl;



  /* public static final String BASE_URL = "http://192.168.0.130/time_dynamo/public/api/attendance/";
   public static final String BASE_URL = "http://www.tvishasystems.com/webdemo/timedynamo_testing/public/api/attendance/";
   public static final String BASE_URL = "https://timedynamo.com/api/attendance/";*/


    private static Retrofit retrofit = null;

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(3000, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();

    public static Retrofit getClient() {
     /*   if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }*/

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
    public static ApiInterface getInstance(){
        if (retrofit!=null){
            return retrofit.create(ApiInterface.class);
        }else {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
            return retrofit.create(ApiInterface.class);
        }
    }


}
