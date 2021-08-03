package com.summit.summitproject.ui.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.summit.summitproject.ui.screens.login.LoginState

/**
 * A reusable UI component that shows a check box followed by a label.
 * @param label a [String] value for the check box.
 * @param onCheck a function that takes [Boolean]. Used to update [LoginState].
 * @param isChecked a [Boolean] of the check box: checked or unchecked.
 * @param modifier a [Modifier] that can be passed in to modify [CheckBoxWithLabel] component.
 */
@Composable
fun CheckBoxWithLabel(
    label: String,
    onCheck: (Boolean) -> Unit,
    isChecked: Boolean,
    modifier: Modifier = Modifier
) {

    /**
     * A layout composable that places its children in a horizontal sequence.
     */
    Row(
        modifier = modifier.clickable {
            onCheck(!isChecked)
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { checked ->
                onCheck(checked)
            },
            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary)
        )

        Text(
            text = label,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}