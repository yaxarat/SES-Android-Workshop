package com.summit.summitproject.prebuilt.repository

import com.summit.summitproject.prebuilt.api.RetrofitInstance
import com.summit.summitproject.prebuilt.model.AccountInfo
import retrofit2.Response

/**
 * Receives the deserialized [AccountInfo] from the raw network response.
 * The serialization and deserialization of objects, as well as network logging is handled in [RetrofitInstance]
 * by
 *
 *
 * retrofit2: https://square.github.io/retrofit/
 * okhttp3: http://square.github.io/okhttp/
 */
class LoginRepository {
    suspend fun login(): Response<AccountInfo> = RetrofitInstance.loginApi.login()
}