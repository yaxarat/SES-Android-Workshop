package com.summit.summitproject.prebuilt.service

import android.util.Log
import com.summit.summitproject.prebuilt.model.AccountInfo
import com.summit.summitproject.prebuilt.repository.LoginRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * Implementation of [LoginService].
 */
class LoginServiceImpl : LoginService {

    /**
     * Lazily initialize [LoginRepository] so we can make a login api call.
     */
    private val loginRepository: LoginRepository by lazy {
        LoginRepository()
    }

    /**
     * Handles making a single network call to a fake login API to retrieve user details (their
     * name, a single credit card number (the last 4), and a list of transactions).
     *
     * Since it's fake API call, we don't "really" send the username / password and it always
     * returns the same response, but it is "real" in the sense that a real network call is made.
     *
     * Networking is done in a background thread.
     * Result is delivered to [onResultReceived] callback lambda.
     *
     * @param username the username of this account holder.
     * @param password the password for this account.
     * @param onResultReceived a callback lambda to be invoked once result is received.
     */
    override fun loginWithCredentials(
        username: String,
        password: String,
        onResultReceived: (LoginResult) -> Unit
    ) {
        logLoginProcess(
            "Attempting login with following credentials:" +
                "\nUsername: $username" +
                "\nPassword: $password"
        )

        /**
         * Make login api call with coroutine.
         */
        GlobalScope.launch {
            val response: Response<AccountInfo> = loginRepository.login()

            if (response.isSuccessful) {
                response.body()?.let { accountInfo ->
                    logLoginProcess("Login succeeded! Account information: $accountInfo")

                    val result = LoginResult.Success(accountInfo = accountInfo)
                    onResultReceived(result)
                }
            } else {
                logLoginProcess("Login failed!")

                val result = LoginResult.Failure
                onResultReceived(result)
            }
        }
    }

    private fun logLoginProcess(message: String) {
        Log.d(
            this::class.java.simpleName,
            message
        )
    }
}