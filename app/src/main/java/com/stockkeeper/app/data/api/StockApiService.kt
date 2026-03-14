package com.stockkeeper.app.data.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class YahooFinanceResponse(
    @SerializedName("chart") val chart: YahooChart
)

data class YahooChart(
    @SerializedName("result") val result: List<YahooResult>?,
    @SerializedName("error") val error: Any?
)

data class YahooResult(
    @SerializedName("meta") val meta: YahooMeta,
    @SerializedName("timestamp") val timestamp: List<Long>?,
    @SerializedName("indicators") val indicators: YahooIndicators?
)

data class YahooMeta(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("regularMarketPrice") val regularMarketPrice: Double = 0.0,
    @SerializedName("previousClose") val previousClose: Double = 0.0,
    @SerializedName("currency") val currency: String = "INR"
)

data class YahooIndicators(
    @SerializedName("quote") val quote: List<YahooQuote>?
)

data class YahooQuote(
    @SerializedName("close") val close: List<Double?>?
)

interface StockApiService {
    // interval: 1d for current price, 5m for intraday
    @GET("v8/finance/chart/{symbol}")
    suspend fun getQuote(
        @Path("symbol") symbol: String,
        @Query("interval") interval: String = "1d",
        @Query("range") range: String = "1d"
    ): YahooFinanceResponse
}
