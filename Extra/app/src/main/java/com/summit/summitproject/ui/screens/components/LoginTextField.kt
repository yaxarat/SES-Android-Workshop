package com.summit.summitproject.ui.screens.components

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.summit.summitproject.ui.screens.login.LoginState

/**
 * A reusable UI component that shows a text field that can also be used as a password text field.
 * @param label a [String] value used as a field name.
 * @param maskTextInput a [Boolean] value that if true, masks password input, and not otherwise.
 * @param textFieldValue a [String] that represents the current state of the field input.
 * @param onTextFieldValueChange a function that takes [String]. Used to update [LoginState].
 * @param modifier a [Modifier] that can be passed in to modify the text field.
 */
@Composable
fun LoginTextField(
    label: String,
    textFieldValue: String,
    onTextFieldValueChange: (String) -> Unit,
    maskTextInput: Boolean,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { newUsernameString ->
            onTextFieldValueChange(newUsernameString)
        },
        label = {
            Text(text = label)
        },
        visualTransformation = if (maskTextInput) PasswordVisualTransformation() else VisualTransformation.None,
        singleLine = true,
        modifier = modifier,
    )
}