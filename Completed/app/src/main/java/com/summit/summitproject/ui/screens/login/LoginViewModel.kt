package com.summit.summitproject.ui.screens.login

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.summit.summitproject.prebuilt.model.AccountInfo
import com.summit.summitproject.prebuilt.model.ConversionHelper.encodeTransactionsToJson
import com.summit.summitproject.prebuilt.service.LoginResult
import com.summit.summitproject.prebuilt.service.LoginService
import com.summit.summitproject.prebuilt.service.LoginServiceImpl

/**
 * [ViewModel] for the [LoginFragment]
 * This is where the business logic of the Login Fragment, and its screens live.
 */
class LoginViewModel: ViewModel() {
    /**
     * Initialize [LoginService] that provides methods to initiate a login call and listen for its result.
     */
    private val loginService: LoginService = LoginServiceImpl()

    /**
     * Represents the current [LoginState] of our UI. We launch the screen with this initial state.
     * Observed in [LoginFragment] to update composable UIs accordingly.
     */
    val state = mutableStateOf(LoginState())

    /**
     * Grabs the current snapshot of the [LoginState].
     */
    private val currentState get() = state.value

    fun enterUsername(username: String) {
        state.value = currentState.copy(username = username)
        shouldEnableSignInButton()
    }

    fun enterPassword(password: String) {
        state.value = currentState.copy(password = password)
        shouldEnableSignInButton()
    }

    private fun shouldEnableSignInButton() {
        val usernameFilled = currentState.username.isNotEmpty()
        val passwordFilled = currentState.password.isNotEmpty()
        val enableSignIn = (usernameFilled && passwordFilled)

        state.value = currentState.copy(enableSignIn = enableSignIn)
    }

    fun signIn() {
        performSignIn(
            username = currentState.username,
            password = currentState.password
        )

        state.value = currentState.copy(
            enableSignIn = false,
            handlingSignIn = true
        )
    }

    private fun performSignIn(
        username: String,
        password: String
    ) {
        loginService.loginWithCredentials(
            username = username,
            password = password,
            onResultReceived = { result ->
                loginResultReceived(result)
            }
        )
    }

    private fun loginResultReceived(result: LoginResult) {
        if (result is LoginResult.Success) {
            state.value = currentState.copy(accountInfo = result.accountInfo)
        } else {
            state.value = currentState.copy(
                enableSignIn = true,
                handlingSignIn = false
            )
        }
    }

    fun saveAccountInfo(sharedPreferences: SharedPreferences) {
        val accountInfo = currentState.accountInfo ?: return
        val editor = sharedPreferences.edit()

        editor
            .putString(PREF_NAME, accountInfo.name)
            .putString(PREF_CARD_LAST_FOUR, accountInfo.cardLastFour)
            .putString(PREF_TRANSACTIONS, encodeTransactionsToJson(accountInfo.transactions))
            .apply()
    }
}

/**
 * Represents the state that can be changed by the user in [LoginFragment] UI.
 * We start with these initial property values.
 */
data class LoginState(
    val username: String = "",
    val password: String = "",
    val enableSignIn: Boolean = false,
    val handlingSignIn: Boolean = false,
    val accountInfo: AccountInfo? = null
)