package com.summit.summitproject.ui.screens.summary

import android.content.Context
import android.os.Bundle
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
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.summit.summitproject.prebuilt.model.Transaction
import com.summit.summitproject.ui.screens.components.TransactionCard
import com.summit.summitproject.ui.screens.login.PREF_CARD_LAST_FOUR
import com.summit.summitproject.ui.screens.login.PREF_NAME
import com.summit.summitproject.ui.screens.login.PREF_TRANSACTIONS
import com.summit.summitproject.ui.screens.login.SHARED_PREFERENCES_NAME
import java.lang.reflect.Type

class SummaryFragment : Fragment() {
    /**
     * Get a a property delegate to access [SummaryViewModel] by default scoped to this Fragment.
     */
    private val viewModel: SummaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = requireContext().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)


        val name = sharedPreferences.getString(PREF_NAME, "")
        val cardLastFour = sharedPreferences.getString(PREF_CARD_LAST_FOUR, "")
        val json: String? = sharedPreferences.getString(PREF_TRANSACTIONS, "")
        val type: Type = object : TypeToken<List<Transaction?>?>() {}.type
        val transactions: List<Transaction> = Gson().fromJson(json, type)

        viewModel.updateAccountInfo(
            accountHolderName = name!!,
            accountLastFour = cardLastFour!!,
            accountTransactions = transactions
        )
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