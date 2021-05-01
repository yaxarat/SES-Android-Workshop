package com.summit.summitproject.ui.screens.components

import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

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