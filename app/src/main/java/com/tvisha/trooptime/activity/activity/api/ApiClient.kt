package com.tvisha.trooptime.activity.activity.api

import com.google.gson.GsonBuilder
import com.tvisha.trooptime.activity.activity.helper.ServerUrls
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by tvisha on 8/6/18.
 */
class ApiClient {




    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(3000, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create()).client(httpClient)
            .baseUrl(ServerUrls.BaseUrl)
            .build()

    }

    //rename to service
    val instance: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }


}