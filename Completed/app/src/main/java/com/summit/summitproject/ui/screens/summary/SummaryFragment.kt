package com.summit.summitproject.ui.screens.summary

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.summit.summitproject.prebuilt.model.Transaction

class SummaryFragment : Fragment() {

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
                Log.d("SummaryFragment", "accountHolderName: $accountHolderName\n" +
                    "accountLastFour: $accountLastFour\n" +
                    "accountTransactions: $accountTransactions")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent {
            /**
             * A layout composable that places its children in a vertical sequence.
             */
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Hello, ...",
                    style = MaterialTheme.typography.h5,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 32.dp)
                )

                Text(
                    text = "Your recent transactions for Card ...",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.padding(
                        top = 16.dp,
                        bottom = 24.dp
                    )
                )

                Divider()
            }
        }
    }
}