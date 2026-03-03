package com.stockkeeper.app.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

// Response models
data class StockPriceResponse(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("currency")
    val currency: String = "USD",
    @SerializedName("timestamp")
    val timestamp: Long = System.currentTimeMillis()
)

data class StockQuoteResponse(
    @SerializedName("global_quote")
    val quote: QuoteData
)

data class QuoteData(
    @SerializedName("01. symbol")
    val symbol: String,
    @SerializedName("05. price")
    val price: String
)

// API Interface
interface StockApiService {
    @GET("query")
    suspend fun getStockPrice(
        @Query("symbol") symbol: String,
        @Query("function") function: String = "GLOBAL_QUOTE",
        @Query("apikey") apiKey: String = "demo" // Replace with real API key
    ): StockQuoteResponse

    @GET("quote")
    suspend fun getMultiplePrices(
        @Query("symbols") symbols: String,
        @Query("apikey") apiKey: String = "demo"
    ): List<StockPriceResponse>
}
