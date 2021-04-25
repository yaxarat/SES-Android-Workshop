package com.summit.summitproject.ui.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

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
                    modifier = Modifier.padding(vertical = 32.dp)
                )
            }

            OutlinedTextField(value = "k") {

            }
        }
    }
}