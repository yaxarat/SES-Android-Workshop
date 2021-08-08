#  Fragment Transaction
Before we go any further, let’s see how we can move on from the login screen to what comes next (account summary screen).

### FragmentManager<!-- {"fold":true} -->
To initiate fragment transaction from one fragment to another, we have to use something call fragment manager. We can get it in a fragment by calling `parentFragmentManager`.

Since we want to start the transaction in `LoginFragment` and move to `SummaryFragment`, in `LoginFragment` we need to add the following logic below our `Column` wrapper code:

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

Run the app and log in again to see this in action!