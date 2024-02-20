package com.tvisha.trooptime.activity.activity.api_Helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tvisha.trooptime.activity.activity.helper.ServerUrls;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by tvisha on 12/12/17.
 */

public class ApiClient {

    private static final ApiClient ourInstance = new ApiClient();
    private OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build();
    private Gson gson = new GsonBuilder()
            .setLenient()
            .create();
    private Retrofit retrofit = null;

    public static ApiClient getInstance() {
        return ourInstance;
    }

    public static Retrofit getClient() {
        return getInstance().getRetrofit();
    }

    private Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ServerUrls.BaseUrl)
                    .client(httpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

        }
        return retrofit;
    }


}
