package com.github.hachimori.hachimori_01162020.network

import com.github.hachimori.hachimori_01162020.BuildConfig
import com.github.hachimori.hachimori_01162020.model.Videos
import com.github.hachimori.hachimori_01162020.util.Constants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface HachimoriService {


    @GET("videos.json")
    fun getVideoList(): Deferred<Videos>

    companion object {
        private val INSTANCE: HachimoriService

        init {
            val httpClientBuilder = OkHttpClient.Builder()

            // Display the result of Web API request on Logcat
            if (BuildConfig.DEBUG) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BASIC
                httpClientBuilder.addInterceptor(interceptor)
            }

            val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.HACHIMORI_API_ENDPOINT)
                    .client(httpClientBuilder.build())
                    .addCallAdapterFactory(CoroutineCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            INSTANCE = retrofit.create(HachimoriService::class.java)
        }

        fun getService() = INSTANCE
    }
}
