package com.summit.summitproject.prebuilt.service

import com.summit.summitproject.prebuilt.model.AccountInfo
import kotlinx.coroutines.CoroutineScope

/**
 * LoginService interface that provides methods to initiate a login call and listen for its result.
 * Implemented in [LoginServiceImpl].
 */
interface LoginService {

    /**
     * A method that performs login network call with the provided credentials.
     *
     * @param username the username of this account holder.
     * @param password the password for this account.
     * @param scope the [CoroutineScope] which an async login call will be scoped to.
     */
    fun loginWithCredentials(
        username: String,
        password: String,
        onResultReceived: (LoginResult) -> Unit
    )
}

/**
 * A sealed class that represents response from the login network call.
 */
sealed class LoginResult {

    /**
     * Returned on login success, containing all necessary data for the account.
     * @see AccountInfo
     */
    data class Success(val accountInfo: AccountInfo) : LoginResult()

    /**
     * Returned on login failure.
     */
    object Failure : LoginResult()
}