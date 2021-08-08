package com.summit.summitproject.ui.screens.summary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class SummaryFragment : Fragment() {

    /**
     * Get a a property delegate to access [SummaryViewModel] by default scoped to this Fragment.
     */
    private val viewModel: SummaryViewModel by viewModels()

    // Called to do initial creation of a fragment.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // Called to have the fragment instantiate its user interface view.
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            // UI goes here!
        }
    }
}