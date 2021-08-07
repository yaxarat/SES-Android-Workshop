package com.summit.summitproject.ui.screens.summary

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.summit.summitproject.prebuilt.model.Transaction

/**
 * [ViewModel] for the [SummaryFragment]
 * This is where the business logic of the Login Fragment, and its screens live.
 */
class SummaryViewModel : ViewModel() {

    /**
     * Represents the current [SummaryState] of our UI. We launch the screen with this initial state.
     * Observed in [SummaryFragment] to update composable UIs accordingly.
     */
    val state = mutableStateOf(SummaryState())

    private val currentState get() = state.value

    fun updateAccountInfo(
        accountHolderName: String,
        accountLastFour: String,
        accountTransactions: List<Transaction>
    ) {
        state.value = currentState.copy(
            accountHolderName = accountHolderName,
            accountLastFour = accountLastFour,
            accountTransactions = accountTransactions
        )
    }

    fun tapped(transactionIndex: Int) {
        Log.d(
            this::class.java.simpleName,
            "Tapped: ${ currentState.accountTransactions[transactionIndex] }"
        )
    }
}

data class SummaryState(
    val accountHolderName: String = "",
    val accountLastFour: String = "",
    val accountTransactions: List<Transaction> = emptyList(),
)