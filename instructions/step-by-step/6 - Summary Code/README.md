# Summary UI Logic<!-- {"fold":true} -->
## Update SummaryViewModel
In the same folder as `SummaryFragment`, go ahead and locate a kotlin class named `SummaryViewModel`.

<img src="assets/Screen Shot 2021-08-08 at 11.07.37 AM.png" width="700" />

Now, at the very bottom of this file, **outside** the `SummaryViewModel` class, define the state we would need for the `SummaryFragment`. This should do it:

```kotlin
class SummaryViewModel : ViewModel() { }

data class SummaryState(
    val accountHolderName: String = "",
    val accountLastFour: String = "",
    val accountTransactions: List<Transaction> = emptyList(),
)
```

We only need these 3 states since they are all we care about in our summary screen. Now let’s add observable variables so we can observe the state from our fragment:

```kotlin
class SummaryViewModel : ViewModel() {

    val state = mutableStateOf(SummaryState())

    private val currentState get() = state.value
}
```

These should be familiar to you since we used the exact same thing for the `LoginViewModel`.

Now add the function to update/change our state once we receive the actual results from our api call.

```kotlin
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
```

Now we can observe these new values inside our `setContent`

```kotlin
setContent {
    val accountHolderName = viewModel.state.value.accountHolderName
    val accountLastFour = viewModel.state.value.accountLastFour
    val accountTransactions = viewModel.state.value.accountTransactions
```

And pass them to appropriate components that needs them…
```kotlin
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
```

For `accountTransactions`, we can extract each transaction from the list of transactions and pass them into our `TransactionCard` to render the information there. For that, we would need to update `TransactionCard` to use passed in data rather than the placeholder:

```kotlin
@Composable
fun TransactionCard(
    transaction: Transaction, // <- Actual data!
    modifier: Modifier = Modifier
) {
    Card(...) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ...

            Row(...) {
                Text(
                    text = transaction.merchant, // <- Actual data!
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = transaction.amount, // <- Actual data!
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}
```

Now we need to pass in the individual data in `SummaryFragment`. To do so, we must first wrap `TransactionCard` inside `LazyColumn`. `LazyColumn` is a composable that allows large amount of composables to be stacked in a list while also keeping the whole process light on resource. We then have to wrap `TransactionCard` inside `itemsIndexed` to add the said list of items where the content of an item is aware of its index. List of items in this case is `accountTransactions`.

```kotlin
LazyColumn(modifier = Modifier.fillMaxHeight()) {
    itemsIndexed(items = accountTransactions) { index, transaction ->
        TransactionCard(
            transaction = transaction,
            modifier = Modifier
                .height(120.dp)
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}
```

Let’s run the app and see what we made.

<img src="assets/Kapture%202021-05-09%20at%2015.21.25.gif" width="400" />

As you can see, no data is being shown. This is expected, since we removed the place holder data for what is supposed to be the actual data. But, if you recall, we never used the `updateAccountInfo` function we created to update the actual data.

We will do this in the next step :)
