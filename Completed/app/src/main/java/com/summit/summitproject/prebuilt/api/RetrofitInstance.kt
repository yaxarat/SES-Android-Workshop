package com.summit.summitproject.prebuilt.api

import com.google.gson.GsonBuilder
import com.summit.summitproject.prebuilt.model.AccountInfo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Receives the raw response body String from the network call (or null if there was a problem).
 * Parses the JSON response for the user data. It looks something like:
 * {
 *    "name":"Yashar A.",
 *    "cardLastFour":"7890",
 *    "transactions":
 *    [
 *        {
 *          "merchant":"Starbucks",
 *          "amount":"$1.40"
 *        },
 *        {
 *          "merchant":"Macy's",
 *          "amount":"$35.00"
 *        },
 *
 *        // ...
 *
 *    ]
 * }
 *
 * The raw JSON response is then deserialized (converted) into [AccountInfo] by the [GsonConverterFactory].
 * Network logging is done by [OkHttpClient] for easier debugging.
 */
object RetrofitInstance {
    private const val baseRequestUrl = "http://www.mocky.io/"

    private val retrofit: Retrofit by lazy {

        val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client : OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()

        Retrofit.Builder()
            .baseUrl(baseRequestUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
            .build()
    }

    val loginApi: LoginApi by lazy {
        retrofit.create(LoginApi::class.java)
    }
}