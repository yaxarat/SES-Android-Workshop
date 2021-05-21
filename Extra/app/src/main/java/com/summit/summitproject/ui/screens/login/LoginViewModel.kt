package com.summit.summitproject.ui.screens.login

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.summit.summitproject.prebuilt.model.AccountInfo
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

    fun rememberMeChecked(isChecked: Boolean) {
        state.value = currentState.copy(rememberMe = isChecked)
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

    /**
     * Save the username and preference to local storage.
     */
    fun savePreference(sharedPreferences: SharedPreferences) {
        val editor = sharedPreferences.edit()
        val usernameToSave = if (currentState.rememberMe) currentState.username else ""

        editor.putString(PREF_USERNAME, usernameToSave)
        editor.putBoolean(PREF_REMEMBER_ME, currentState.rememberMe)
        editor.apply()
    }

    fun restoreUserCredentials(sharedPreferences: SharedPreferences) {
        if (sharedPreferences.getBoolean(PREF_REMEMBER_ME, false)) {
            state.value = currentState.copy(
                username = sharedPreferences.getString(PREF_USERNAME, "")!!,
                rememberMe = true
            )
        }
    }
}

/**
 * Represents the state that can be changed by the user in [LoginFragment] UI.
 * We start with these initial property values.
 */
data class LoginState(
    val username: String = "",
    val password: String = "",
    val rememberMe: Boolean = false,
    val enableSignIn: Boolean = false,
    val handlingSignIn: Boolean = false,
    val accountInfo: AccountInfo? = null
)