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
object ApiClient {
    const val BASE_URL = ServerUrls.BaseUrl
    private var retrofit: Retrofit? = null
    private var apiInterface: ApiInterface? = null
    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(3000, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
    private val client: Retrofit?
        private get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit
        }

    @JvmStatic
    @get:Synchronized
    val instance: ApiInterface?
        get() {
            if (apiInterface == null) {
                apiInterface = client!!.create(
                    ApiInterface::class.java
                )
            }
            return apiInterface
        }
}