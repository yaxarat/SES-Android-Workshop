package com.summit.summitproject.ui.screens.summary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import com.summit.summitproject.prebuilt.model.Transaction
import com.summit.summitproject.ui.screens.components.TransactionCard

class SummaryFragment : Fragment() {

    /**
     * Get a a property delegate to access [SummaryViewModel] by default scoped to this Fragment.
     */
    private val viewModel: SummaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * Sets the [FragmentResultListener] for a given requestKey. Once this [Fragment] is at least in the STARTED state,
         * any results set by setFragmentResult using the same requestKey will be delivered to the [FragmentResultListener.onFragmentResult] callback.
         */
        setFragmentResultListener("requestKey") { requestKey, bundle ->
            // Any type that can be put in a Bundle is supported
            val accountHolderName: String? = bundle.getString("name")
            val accountLastFour: String? = bundle.getString("cardLastFour")
            val accountTransactions: List<Transaction>? = bundle.getSerializable("transactions") as? List<Transaction>

            if (
                accountHolderName != null &&
                accountLastFour != null &&
                accountTransactions != null
            ) {
                viewModel.updateAccountInfo(
                    accountHolderName = accountHolderName,
                    accountLastFour = accountLastFour,
                    accountTransactions = accountTransactions
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            val accountHolderName = viewModel.state.value.accountHolderName
            val accountLastFour = viewModel.state.value.accountLastFour
            val accountTransactions = viewModel.state.value.accountTransactions

            /**
             * A layout composable that places its children in a vertical sequence.
             */
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Hello, $accountHolderName",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 32.dp)
                )

                Text(
                    text = "Your recent transactions for Card $accountLastFour",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(
                        top = 16.dp,
                        bottom = 24.dp
                    )
                )

                Divider()

                LazyColumn(modifier = Modifier.fillMaxHeight()) {
                    itemsIndexed(items = accountTransactions) { index, transaction ->
                        TransactionCard(
                            transaction = transaction,
                            modifier = Modifier
                                .height(120.dp)
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable { viewModel.tapped(index) }
                        )
                    }
                }
            }
        }
    }
}