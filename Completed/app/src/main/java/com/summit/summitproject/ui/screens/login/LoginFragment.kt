package com.summit.summitproject.ui.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.summit.summitproject.ui.screens.components.LoginTextField

class LoginFragment : Fragment() {

    /**
     * Get a a property delegate to access [LoginViewModel] by default scoped to this Fragment.
     */
    private val viewModel: LoginViewModel by viewModels()

    /**
     * Create a [View] that can host Jetpack Compose UI content.
     * Use setContent to supply the content composable function for the view.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {

        /**
         * Set the Jetpack Compose UI content for this view.
         */
        setContent {
            val usernameTextFieldValue = remember { mutableStateOf("") }
            val passwordTextFieldValue = remember { mutableStateOf("") }

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
                    textFieldValue = usernameTextFieldValue.value,
                    onTextFieldValueChange = { newUsername ->
                        usernameTextFieldValue.value = newUsername
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
                    textFieldValue = passwordTextFieldValue.value,
                    onTextFieldValueChange = { newUsername ->
                        passwordTextFieldValue.value = newUsername
                    },
                    maskTextInput = true,
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                )

                Spacer(modifier = Modifier.fillMaxHeight(0.8f))

                Button(
                    onClick = {

                    },
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
    }
}