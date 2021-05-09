# Summary UI<!-- {"fold":true} -->
## Create SummaryFragment<!-- {"fold":true} -->
Inside the screens folder, create summary folder and `SummaryFragment.kt` class within it.

![](assets/Kapture%202021-05-09%20at%2010.43.02.gif)

From there, follow what we have in `LoginFragment` and set up the `onCreateView`:

```kotlin
class SummaryFragment : Fragment() {
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setContent { 
            
        }
    }
}
```

### Divide and conquer
Now that we have the foundation set up, let’s see what we want our finished product to look like:

<img src="assets/summary_screen.png" width="400" />

1. A simple text component that we have already created before
2. Same thing, a smaller text component!
3. A divider line component
4. A transaction card component with picture and texts representing that transaction

### Text components
Let’s start by kicking out the simpler text components 1 and 2. Since we know we want all components to be stacked vertically, we will wrap them in a `Column` composable as before:

```kotlin
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
}
```

We leave the dynamic portion of our text, the username and the card number, which we will receive from the sign in as `...` for now. We will observe them from the view model as we have done earlier.

### Divider component
This divider is actually a super simple built in composable. We can use it by just dropping `Divider()` at a place where you want the divider to show up. So in our case, right after the second text component:

```kotlin
Text(
    text = "Your recent transactions for Card ...",
    style = MaterialTheme.typography.body1,
    modifier = Modifier.padding(
        top = 16.dp,
        bottom = 24.dp
    )
)

Divider()
```

## Fragment transaction<!-- {"fold":true} -->
Before we go any further, let’s check that what we have looks good… wait, how do we get to this screen in the first place?

### FragmentManager<!-- {"fold":true} -->
To initiate fragment transaction from one fragment to another, we have to use something call fragment manager. We can get it in a fragment by calling `parentFragmentManager`.

Since we want to start the transaction in `LoginFragment` and move to `SummaryFragment`, we need to go back to the `LoginFragment` and add the following logic below our `Column` wrapper code:

```kotlin
if (accountInfo != null) {
    /**
     * Replace current fragment with the next.
     * We use replace here sine we don't want users to be able to back navigate to login screen.
     */
    parentFragmentManager
        .beginTransaction()
        .replace(R.id.main_container, SummaryFragment())
        .commit()
}
```

And don’t forget to add the `accountInfo` observable at the top along with others:

```kotlin
val accountInfo: AccountInfo? = viewModel.state.value.accountInfo
```

What we are doing here is:
1. Observe account info and once the sign in is successful (when accountInfo is no longer null)
2. Get `parentFragmentManager` and initiate the transaction process `.beginTransaction()`
3. We tell the manager to **replace** current fragment inside the `R.id.main_container` with a new fragment `SummaryFragment()`
4. Finally, we tell the manager to process this request by`.commit()` our transaction request

We don’t have to do anything inside `SummaryFragment` since it will be automatically be created and brought up by the `parentFragmentManager`.

Run the app and see this in action!

<img src="assets/Kapture%202021-05-09%20at%2011.28.03.gif" width="400" />

### FragmentResult<!-- {"fold":true} -->
This is cool, but how do we pass the said `accountInfo` to the summary screen? After all, that is where we want to show those information.

For that, we will be using [FragmentResult API](https://developer.android.com/guide/fragments/communicate#fragment-result). You can read more about it by clicking on the link.  But the main idea is to pass a one-time value between two fragments by setting `setFragmentResultListener` in the result receiving fragment (in our case `SummaryFragment`) and setting `setFragmentResult` in the result producing fragment (`LoginFragment`).

![](assets/fragment-a-to-b.png)

### setFragmentResult<!-- {"fold":true} -->
Let’s start by setting result inside our `LoginFragment`. We want to do this right before we transition to the `SummaryFragment` since that is the point in time which we know that the account information exists.

```kotlin
/**
 * When sign in call succeeds and [AccountInfo] is provided, we transition to the next screen.
 */
if (accountInfo != null) {

    /**
     * Sets the given result for the requestKey. This result will be delivered to a [FragmentResultListener]
     * that is called with the same requestKey.
     */
    setFragmentResult(
        requestKey = "requestKey",
        result = bundleOf(
            "name" to accountInfo.name,
            "cardLastFour" to accountInfo.cardLastFour,
            "transactions" to accountInfo.transactions
        )
    )

    /**
     * Replace current fragment with the next.
     * We use replace here sine we don't want users to be able to back navigate to login screen.
     */
    parentFragmentManager
        .beginTransaction()
        .replace(R.id.main_container, SummaryFragment())
        .commit()
}
```

Let us dig deeper into what is going on here in the fragment result:

```kotlin
setFragmentResult(
    requestKey = "requestKey",
    result = bundleOf(
        "name" to accountInfo.name,
        "cardLastFour" to accountInfo.cardLastFour,
        "transactions" to accountInfo.transactions
    )
)
```

* `requestKey` is a unique key with which the `SummaryFragment` will use to request for the `result`. So you can think of `FragmentResult` as a `key-value` pair where for each value, there exist a unique key that can be used to retrieve it.
* `result` has the same idea. For each element inside a result (which is of type `bundleOf`), there is a `key-value` association with which the receiving fragment can use to query the `result`.

### setFragmentResultListener<!-- {"fold":true} -->
Now is the time to set our listener inside the `SummaryFragment`.

We want to immediately start listing to the results when the fragment is created, so we will put the listener code inside a `onCreate` function like below. Make sure this is done outside of `onCreateView` function since this needs to happen before the view (UI) is created.

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setFragmentResultListener("requestKey") { requestKey, bundle ->
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

...
override fun onCreateView(...) {...}
```

Here we are listening to a result with key `"requestKey"` and once the result is in, we are getting each element from the result by using their individual keys:
```kotlin
bundle.getString("name")
bundle.getString("cardLastFour")
bundle.getSerializable("transactions") as? List<Transaction> // type casting
```

We would want to pass these values into the `viewModel` to be put into state and observed, but since we don’t have a `viewModel` for `SummaryFragment` for now, we are just logging them so we can make sure that this is working.

```kotlin
Log.d("SummaryFragment", "accountHolderName: $accountHolderName\n" +
    "accountLastFour: $accountLastFour\n" +
    "accountTransactions: $accountTransactions")
```

Note how these keys match exactly with what we set in the `LoginFragment` and that is necessary for this result sharing to happen:
```kotlin
setFragmentResult(
    requestKey = "requestKey",
    result = bundleOf(
        "name" to accountInfo.name,
        "cardLastFour" to accountInfo.cardLastFour,
        "transactions" to accountInfo.transactions
    )
)
```

Let’s run the app and see what get’s logged. If you sign in and get to the summary page, you should see following in your logcat:
```
D/SummaryFragment: accountHolderName: Summiteers
    accountLastFour: 7890
    accountTransactions: [Merchant: Starbucks, Amount: $1.40, Merchant: Macy's, Amount: $35.00, Merchant: Giant, Amount: $64.37, Merchant: Amazon, Amount: $11.99, Merchant: Petsmart, Amount: $29.12, Merchant: Cava, Amount: $10.34, Merchant: AMC Theaters, Amount: $41.00, Merchant: Peet's Coffee, Amount: $6.49, Merchant: Best Buy, Amount: $674.11]
```

See how the tag `D/SummaryFragment` corresponds to our `SummaryFragment`? That means this is working!

So far, your `LoginFragment` code should looks like this:

```kotlin
imports...

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

                /**
                 * Sets the given result for the requestKey. This result will be delivered to a [FragmentResultListener]
                 * that is called with the same requestKey.
                 */
                setFragmentResult(
                    requestKey = "requestKey",
                    result = bundleOf(
                        "name" to accountInfo.name,
                        "cardLastFour" to accountInfo.cardLastFour,
                        "transactions" to accountInfo.transactions
                    )
                )

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
```

And your `SummaryFragment`:

```kotlin
imports ...

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
```

## TransactionCard component<!-- {"fold":true} -->
![](assets/Screen%20Shot%202021-05-09%20at%2012.22.05%20PM.png)

Now is the time to make a custom composable function to create the above UI. It might look complicated at the beginning, but if we take a step back and see what exactly they are made from, it will look a lot less daunting.

![](assets/PNG%20image-9B8A3CE070BB-1.png)

As you can see above, this is just a combination of few nesting composables that are already included within the Jetpack compose library. All we need to do is to arrange and nest them in the correct order!

Since we want to re-use this composable for each of the transactions, let’s make this it’s own component by creating a new `TransactionCard` composable inside our components folder.

![](assets/Kapture%202021-05-09%20at%2012.32.34.gif)

Referring back to our beautiful hand drawn diagram above, we know that the whole thing must be wrapped in a `Card` composable. This comes standard in the library and we can use it like so:

```kotlin
@Composable
fun TransactionCard() {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        
    }
}
```

Now that we have card configured, let’s put in the `Row` that will hold our other composables. We use `verticalAlignment = Alignment.CenterVertically` here to center vertically all of the composable UI inside the row.

```kotlin
@Composable
fun TransactionCard() {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

        }
    }
}
```

Compose provides a default composable function that loads and modifies an image located inside the project. We already have a pre-imported image `credit_card`, so let’s go ahead and load it using `Image` composable:

```kotlin
Row(verticalAlignment = Alignment.CenterVertically) {
    Image(
        painter = painterResource(id = R.drawable.credit_card),
        contentDescription = "",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .fillMaxWidth(0.3f)
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            )
    )
}
```

`.fillMaxWidth(0.3f)` here works in the same fashion as we saw when we implemented the `Spacer()` composable. It tells the composable to take up 30% of the available space. In this case, 30% of the available width.

Now all that’s left is just two text composable inside another `Row`. This `Row` is used to keep two text composable equidistant from each other while keeping same distance from either edge. For the actual values of the text, we will just put some placeholders for now:

```kotlin
Row(
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
) {
    Text(
        text = "Merchant",
        style = MaterialTheme.typography.body1,
        fontWeight = FontWeight.SemiBold
    )
    Text(
        text = "$0.00",
        style = MaterialTheme.typography.body1
    )
}
```

All in all, you should have this right now:
```kotlin
@Composable
fun TransactionCard() {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.credit_card),
                contentDescription = "",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .padding(
                        vertical = 8.dp,
                        horizontal = 16.dp
                    )
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Merchant",
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "$0.00",
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}
```

Now let’s actually see this in action. Go back to the `SummaryFragment` and add the following code after `Divider()`:

```kotlin
repeat(5) {
    TransactionCard()
}
```

This just shows our created component 5 times. Now let’s run the app and see what we have.

<img src="assets/Screenshot_1620585656.png" width="400" />

Not terrible, but not great. We need to adjust it’s modifiers to give each card some breathing room. Make the `TransactionCard` accept modifier parameter, and apply the said modifier to the `Card` composable. We can then pass in custom modifier from the `SummaryFragment`:

```kotlin
@Composable
fun TransactionCard(
    modifier: Modifier
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = 8.dp,
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier
    ) { ...
```

```kotlin
repeat(5) {
    TransactionCard(
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .padding(16.dp)
    )
}
```

Now let’s run it.

<img src="assets/Screenshot_1620586002.png" width="400" />

Love it!
