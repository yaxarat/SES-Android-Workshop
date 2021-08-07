# Login UI Logic<!-- {"fold":true} -->
This section covers adding behaviors to our login screen by writing Kotlin business logic.
Here are the business logic we will be implementing:

* Disable the "Sign In" button unless a username & password are entered.
* Show loading dialog when the login api call is being made.

## ViewModel<!-- {"fold":true} -->
Our business logic for the login screen will be living inside a `viewModel` class that we will explore shortly. But before we go on, let’s review what view model is all about.

You can read all about viewModel here in the [official docs](https://developer.android.com/topic/libraries/architecture/viewmodel), but I way layout the gist here. 

The [Android framework manages the lifecycles of UI](https://developer.android.com/guide/components/activities/activity-lifecycle), such as our `LoginFragment`. The framework may decide to destroy or re-create a UI in response to certain user actions or device events that are completely out of your control. One such example is device screen rotation (a.k.a configuration change).

If the system destroys or re-creates a UI, any transient UI-related data you store in them is lost. For example, your app may include a list of user data (transaction details in our case). When the UI is re-created for a configuration change, the new UI has to re-fetch the data all over again. And if the data can’t be fetched, like user entered password/username, it will be lost forever.

Therefore, it's easier and more efficient to separate out view data ownership from UI logic to something else. This is where `viewModel` comes in.

In Jetpack Compose, you can use the `ViewModel` to expose the state in an observable form. What this means in practice is basically to move our states one more level up, from `LoginFragment` into `LoginViewModel`.

So these
```kotlin
val usernameTextFieldValue = remember { mutableStateOf("") }
val passwordTextFieldValue = remember { mutableStateOf("") }
```
Will soon be moving into a `viewModel` so we can observe it directly from there.

## Create LoginViewModel
In the same folder as our `LoginFragment`, locate and open `LoginViewModel` class. This is a pre-built `viewModel` class that we will use to explore this idea further. Later in the session, we will create another `viewModel` from scratch.

![](assets/Kapture%202021-05-01%20at%2017.02.49.gif)

Once you open the file, you should see some pre-written codes with some explanatory comments on it.

Let’s review them one by one from bottom up:

### State
```kotlin
data class LoginState(
    val username: String = "",
    val password: String = "",
    val enableSignIn: Boolean = false,
    val handlingSignIn: Boolean = false,
    val accountInfo: AccountInfo? = null
)
```

Any variable which represent how the UI should appear is a `state` . For example, username entered, password entered, login button enabled, user logged in, etc. Altogether, these constitute state of any screen. You can think of this as a snapshot of current screen and all of its variables.

Here is the explanation for each state values and what we use them for:
* `username` the currently entered username
* `password` the currently entered password
* `enableSignIn` if the sign in button should be enabled
* `handlingSignIn` if the login api being actively called/listened for (used for loading indicator)
* `accountInfo` the result we get back form login api. We pass this to the details screen.

### ViewModel
```kotlin
class LoginViewModel: ViewModel() {

    private val loginService: LoginService = LoginServiceImpl()

    val state = mutableStateOf(LoginState())

    private val currentState get() = state.value
}
```

This is the `viewModel` class that will keep all of the business logic for our login screen. Here are the quick explanation of above three values so you will understand why we use which throughout this section:

* `loginService`: This is the pre-built networking service that will be handling our login API call. Here we are initializing it to be used down the road.
* `state` this is a reference to an instance of `LoginState` we covered earlier. Here, we are creating a new initial state (all values are default as defined in the `LoginState` data class), and wrapping it in a `mutableStateOf` just as we did in the composable functions. This is to allow our composable functions to observe changes made to the state inside the `viewModel` and recompose the UI accordingly.
* `currentState` this value re-computes the current state every time it is called. This allows us to get a snapshot instance of the current state value so we can use that as a base for us to modify certain state values.

These may sound a bit confusing right now, but that is okay. It will become more clear as we progress through this section.

### State hoisting (again!)
Let’s start by moving our state another level up, this time into the `LoginViewModel` from the `LoginFragment`.

So change these two state values in `LoginFragment` from

```kotlin
val usernameTextFieldValue = remember { mutableStateOf("") }
val passwordTextFieldValue = remember { mutableStateOf("") }
```

to

```kotlin
val usernameTextFieldValue: String = viewModel.state.value.username
val passwordTextFieldValue: String = viewModel.state.value.password
```

Let’s re-cap what just happened: Here, instead of having the current **state** of our UI directly inside the `fragment`, we are now referencing it from the `viewModel`. Notice how we are observing the state object within the `viewModel`. Since our state object is `mutableStateOf` , it allows our UI to automatically recompose to the latest state changes to reflect the correct UI.

However, there are more modification we need to make to the rest of our fragment code to make this new change work.

Notice our `textField` composable are throwing an error since the new argument we provide does not match what they are expecting. Our `textFieldValue` and `onTextFieldValueChange` are expecting a `mutableStateOf` `string`, but our `usernameTextFieldValue` and `passwordTextFieldValue` is now just a straight up `string`.

One fix is easy: since `textFieldValue` is a `String`, just remove `.value` after our text field values for both `LoginTextField` like so:

```kotlin
LoginTextField(
    label = "Username",
    textFieldValue = usernameTextFieldValue, // used to be ...TextFieldValue.value
    onTextFieldValueChange = { newUsername ->
        usernameTextFieldValue.value = newUsername
    },
    ...
```

Next up is `onTextFieldValueChange`. If you remember, this is a function that takes in the new username/password string and assigns it to the `mutableStateOf` those two fields. Which means, we will now need to pass a function here that allows us to do the same, except this time the states for those two fields are located inside the `viewModel`.

Head to `LoginViewModel` and add following functions inside the `viewModel` class under the `currentState` variable:

```kotlin
fun enterUsername(username: String) {
    state.value = currentState.copy(username = username)
}

fun enterPassword(password: String) {
    state.value = currentState.copy(password = password)
}
```

**Take a time to understand what is happening here since this will be the foundational idea of how we will manipulate the composable UI by modifying the state.**

1. We accept new state value from the user, here it is `username` and `password` parameters.
2. We take a snapshot of current state by using `currentState`
3. We modify only the state values that actually changed, while keeping everything else the same. `copy` operator of the `LoginState` data class does exactly this by moving over everything while only modifying what we specify: `currentState.copy(username = username)`
4. We assign the updated state object to our `mutableStateOf(LoginState())` so its changed can be observed by our UI: `state.value = currentState.copy(username = username)`

This is how we accept new intention to change the state by the user, and modify our state accordingly.

Since these function are exactly the type expected by `onTextFieldValueChange` parameter for our text fields, let go and update them to uses these:

```kotlin
LoginTextField(
    label = "Username",
    textFieldValue = usernameTextFieldValue,
    onTextFieldValueChange = { newUsername ->
        viewModel.enterUsername(newUsername)
    },
    ...
)

LoginTextField(
    label = "Password",
    textFieldValue = passwordTextFieldValue,
    onTextFieldValueChange = { newPassword ->
        viewModel.enterPassword(newPassword)
    },
    ...
)
```

Run the app one more time and check everything is working as usual.

### Enable Sign In Button
Remember our login button composable?

```kotlin
Button(
    onClick = {

    },
    modifier = Modifier
        .padding(horizontal = 32.dp)
        .fillMaxWidth()
) {
    Text(
        text = "Sign In",
        style = MaterialTheme.typography.button
    )
}
```

Let's enable it when all fields are filled so a user can login to their account.

We want to make sure the sign in button is enabled (default is disabled) once password and username is entered, and disabled if either of those two field is empty.

We can do so by adding this check to run very time those two fields are updated:

```kotlin
fun enterUsername(username: String) {
    state.value = currentState.copy(username = username)
    shouldEnableSignInButton()
}

fun enterPassword(password: String) {
    state.value = currentState.copy(password = password)
    shouldEnableSignInButton()
}

private fun shouldEnableSignInButton() {
    val usernameFilled = currentState.username.isNotEmpty()
    val passwordFilled = currentState.password.isNotEmpty()
    val enableSignIn = (usernameFilled && passwordFilled)

    state.value = currentState.copy(enableSignIn = enableSignIn)
}
```

### Calling login API
Now is the time to provide its `onClick` function for the freshly enabled sign in button. We will follow the same pattern as how we updated the username and password fields earlier.

In `LoginViewModel` add following:

```kotlin
fun signIn() {
    performSignIn(
        username = currentState.username,
        password = currentState.password
    )

    state.value = currentState.copy(
        enableSignIn = false,
        handlingSignIn = true
    )
}
```

> `performSignIn` does not exist yet, but it is useful to have it here now to see the flow. 

Now we want to *perform sign in* with the current username and password, and also disable sign in button and show loading indicator while the sign in is being performed.

Now, add the `performSignIn` function below like so:

```kotlin
private fun performSignIn(
    username: String,
    password: String
) {
    loginService.loginWithCredentials(
        username = username,
        password = password,
        onResultReceived = { result ->
            loginResultReceived(result)
        }
    )
}
```

> `loginResultReceived` does not exist yet, but it is useful to have it here now to see the flow. 

`LoginService` has a function `loginWithCredentials` that takes `username`, `password`, and a `onResultReceived` callback function to be executed with result once the result is received from the server.

We have both username and password, let’s now create the callback function we want executed once the result is back. Add following under the last function:

```kotlin
private fun loginResultReceived(result: LoginResult) {
    if (result is LoginResult.Success) {
        state.value = currentState.copy(accountInfo = result.accountInfo)
    } else {
        state.value = currentState.copy(
            enableSignIn = true,
            handlingSignIn = false
        )
    }
}
```

Here we are saying that if the login result is success, we want to extract `accountInfo` from the result. Otherwise, we want to re-enable sign in button and hide the loading indicator so user can try logging in again.

Now that we have proper state manipulating code in place, let’s use these in our fragment.

Add the new sign in function to our button:
```kotlin
Button(
    onClick = {
        viewModel.signIn() // <-- New!
    },
    modifier = Modifier
        .padding(horizontal = 32.dp)
        .fillMaxWidth()
) {
    Text(
        text = "Sign In",
        style = MaterialTheme.typography.button
    )
}
```

We also want to disable and show loading indicator while signing in, so modify our fragment code like so:

Add two new observable values at the beginning of `setContent` along with username/password.

```kotlin
val enabledSignIn: Boolean = viewModel.state.value.enableSignIn
val handlingSignIn: Boolean = viewModel.state.value.handlingSignIn
```

And modify the sign in button to change it’s behavior depending on these values:

```kotlin
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
```

Here we are replacing our button with one of the compose’s default loading indicator if we are currently in the middle of sign in. We also added `enabled = enabledSignIn` to handle enabling and disabling of the button.

Run the app and see the change!

<img src="assets/Kapture%202021-05-09%20at%2010.20.07.gif" width="400" />

Everything is working! Notice how the loading indicator does not go away. But that’s okay! This is because our sign in succeeded and we didn’t put in any logic to take away the indicator for that case. Why? Because we would be transferred to the details screen anyways!

To double check sign in has succeeded, you can open the Logcat to see the logs from our loginService:
![](assets/Kapture%202021-05-09%20at%2010.23.52.gif)

At this point your `Fragment` code should look like this:

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
        }
    }
}
```

And your `ViewModel`:

```kotlin
imports...

/**
 * [ViewModel] for the [LoginFragment]
 * This is where the business logic of the Login Fragment, and its screens live.
 */
class LoginViewModel: ViewModel() {
    /**
     * Initialize [LoginService] that provides methods to initiate a login call and listen for its result.
     */
    private val loginService: LoginService = LoginServiceImpl()

    /**
     * Represents the current [LoginState] of our UI. We launch the screen with this initial state.
     * Observed in [LoginFragment] to update composable UIs accordingly.
     */
    val state = mutableStateOf(LoginState())

    /**
     * Grabs the current snapshot of the [LoginState].
     */
    private val currentState get() = state.value

    fun enterUsername(username: String) {
        state.value = currentState.copy(username = username)
        shouldEnableSignInButton()
    }

    fun enterPassword(password: String) {
        state.value = currentState.copy(password = password)
        shouldEnableSignInButton()
    }

    private fun shouldEnableSignInButton() {
        val usernameFilled = currentState.username.isNotEmpty()
        val passwordFilled = currentState.password.isNotEmpty()
        val enableSignIn = (usernameFilled && passwordFilled)

        state.value = currentState.copy(enableSignIn = enableSignIn)
    }

    fun signIn() {
        performSignIn(
            username = currentState.username,
            password = currentState.password
        )

        state.value = currentState.copy(
            enableSignIn = false,
            handlingSignIn = true
        )
    }

    private fun performSignIn(
        username: String,
        password: String
    ) {
        loginService.loginWithCredentials(
            username = username,
            password = password,
            onResultReceived = { result ->
                loginResultReceived(result)
            }
        )
    }

    private fun loginResultReceived(result: LoginResult) {
        if (result is LoginResult.Success) {
            state.value = currentState.copy(accountInfo = result.accountInfo)
        } else {
            state.value = currentState.copy(
                enableSignIn = true,
                handlingSignIn = false
            )
        }
    }
}

/**
 * Represents the state that can be changed by the user in [LoginFragment] UI.
 * We start with these initial property values.
 */
data class LoginState(
    val username: String = "",
    val password: String = "",
    val enableSignIn: Boolean = false,
    val handlingSignIn: Boolean = false,
    val accountInfo: AccountInfo? = null
)
```
