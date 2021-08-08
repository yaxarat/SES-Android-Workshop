package com.summit.summitproject.ui.screens.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.summit.summitproject.R
import com.summit.summitproject.prebuilt.model.AccountInfo
import com.summit.summitproject.ui.screens.components.LoginTextField
import com.summit.summitproject.ui.screens.summary.SummaryFragment

class LoginFragment : Fragment() {

    /**
     * Get a an access [LoginViewModel] for this Fragment.
     */
    private val viewModel: LoginViewModel by viewModels()

    /**
     * Create a [View] that can host our Jetpack Compose UI content.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {

        val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

        /**
         * Set the Jetpack Compose UI content for this view.
         */
        setContent {
            val usernameTextFieldValue: String = viewModel.state.value.username
            val passwordTextFieldValue: String = viewModel.state.value.password
            val enabledSignIn: Boolean = viewModel.state.value.enableSignIn
            val handlingSignIn: Boolean = viewModel.state.value.handlingSignIn
            val accountInfo: AccountInfo? = viewModel.state.value.accountInfo

            /**
             * A layout composable that places its children in a vertical sequence.
             */
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Summit Bank",
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(vertical = 32.dp),
                )

                LoginTextField(
                    label = "Username",
                    textFieldValue = usernameTextFieldValue,
                    onTextFieldValueChange = { newUsername ->
                        viewModel.enterUsername(newUsername)
                    },
                    maskTextInput = false,
                    modifier = Modifier
                        .padding(
                            horizontal = 32.dp,
                            vertical = 32.dp
                        )
                        .fillMaxWidth()
                )

                LoginTextField(
                    label = "Password",
                    textFieldValue = passwordTextFieldValue,
                    onTextFieldValueChange = { newPassword ->
                        viewModel.enterPassword(newPassword)
                    },
                    maskTextInput = true,
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.fillMaxHeight(0.8f))

                if (handlingSignIn) {
                    CircularProgressIndicator(color = MaterialTheme.colors.primary)
                } else {
                    Button(
                        onClick = {
                            viewModel.signIn()
                        },
                        enabled = enabledSignIn,
                        modifier = Modifier
                            .padding(horizontal = 32.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Sign In",
                            style = MaterialTheme.typography.button
                        )
                    }
                }
            }

            /**
             * When sign in call succeeds and [AccountInfo] is provided, we transition to the next screen.
             */
            if (accountInfo != null) {

                viewModel.saveAccountInfo(sharedPreferences = sharedPreferences)

                /**
                 * Replace current fragment with the next.
                 * We use replace here sine we don't want users to be able to back navigate to login screen.
                 */
                parentFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container, SummaryFragment())
                    .commit()
            }
        }
    }
}