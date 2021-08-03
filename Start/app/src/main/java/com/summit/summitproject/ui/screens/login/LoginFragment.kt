package com.summit.summitproject.ui.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.material.Text
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

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

        /**
         * Set the Jetpack Compose UI content for this view.
         */
        setContent {
            Text(text = "Welcome to the Summit!")
        }
    }
}