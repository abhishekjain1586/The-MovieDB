package com.eros.moviesdb.service

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ServiceHelper {

    private val API_VERSION = 3
    private val BASE_URL = "https://api.themoviedb.org/" + API_VERSION + "/"
    private val logging = HttpLoggingInterceptor()

    fun getRetrofitClient() : APIClient {
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val certificatePinner = CertificatePinner.Builder()
            .add("api.themoviedb.org", "sha256/SDmBegTva5uU4C7qjcWMQf0oM7LUw7yoQQbXno8jI5M=")
            .build()

        var okHttpClient : OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.MINUTES)
                //.certificatePinner(certificatePinner)
                .addInterceptor(logging)
                .build()

        var retrofit : Retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

        return retrofit.create(APIClient::class.java)
    }
}