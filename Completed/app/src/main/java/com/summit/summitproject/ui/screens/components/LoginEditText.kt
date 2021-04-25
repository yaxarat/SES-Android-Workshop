package com.summit.summitproject.ui.screens.components

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.summit.summitproject.ui.screens.login.LoginState

/**
 * A reusable UI component that shows a text field that can also be used as a password text field.
 * @param label a [String] value used as a field name.
 * @param isPassword a [Boolean] value that if true, masks password input, and not otherwise.
 * @param textFieldValue a [String] that represents the current state of the field input.
 * @param onTextFieldValueChanged a function that takes [String]. Used to update [LoginState].
 * @param modifier a [Modifier] that can be passed in to modify the text field.
 */
@Composable
fun LoginEditText(
    label: String,
    isPassword: Boolean,
    textFieldValue: String,
    onTextFieldValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = {
            onTextFieldValueChanged(it)
        },
        label = {
            Text(text = label)
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        )
    )
}

/**
 * A preview for this UI.
 */
@Preview
@Composable
fun LoginEditTextPreview() {
    val textFieldValue = remember { mutableStateOf("") }

    LoginEditText(
        label = "Username",
        isPassword = false,
        textFieldValue = textFieldValue.value,
        onTextFieldValueChanged = {
            textFieldValue.value = it
        }
    )
}