package com.stockkeeper.app.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    // Yahoo Finance public endpoint — no API key required
    private const val BASE_URL = "https://query1.finance.yahoo.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            // Yahoo Finance requires a browser-like User-Agent
            val request = chain.request().newBuilder()
                .header("User-Agent", "Mozilla/5.0 (compatible; StockKeeper/1.0)")
                .header("Accept", "application/json")
                .build()
            chain.proceed(request)
        }
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    fun getStockApiService(): StockApiService = retrofit.create(StockApiService::class.java)
}
