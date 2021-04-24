package com.summit.summitproject.ui.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.summit.summitproject.prebuilt.model.AccountInfo
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