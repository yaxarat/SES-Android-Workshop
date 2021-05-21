package com.summit.summitproject.prebuilt.api

import com.summit.summitproject.prebuilt.model.AccountInfo
import retrofit2.Response
import retrofit2.http.GET

/**
 * An interface to make a login call to the login endpoint.
 * We add our custom get value to the end of the base url to simulate a 2.5 seconds of network delay.
 * Base url can be found in [RetrofitInstance]
 */
interface LoginApi {
    @GET("v2/5b0074643100006f0076df40?mocky-delay=2500ms")
    suspend fun login(): Response<AccountInfo>
}