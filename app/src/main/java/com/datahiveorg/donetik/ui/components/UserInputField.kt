package com.datahiveorg.donetik.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * A composable function that creates a user input field with a label, placeholder, leading and trailing icons,
 * error handling, and password visibility toggle.
 *
 * @param modifier The modifier to be applied to the input field.
 * @param label The label to be displayed above the input field.
 * @param enterValue A callback function that is invoked when the user enters text into the input field.
 * @param onTogglePasswordVisibility A callback function that is invoked when the user clicks the trailing icon
 * to toggle password visibility.
 * @param error The error message to be displayed below the input field if there is an error.
 * @param keyboardOptions The keyboard options to be used for the input field.
 * @param leadingIcon The painter for the leading icon to be displayed in the input field.
 * @param trailingIcon The painter for the trailing icon to be displayed in the input field.
 * @param placeholder The placeholder text to be displayed in the input field when it is empty.
 * @param value The current value of the input field.
 * @param visualTransformation The visual transformation to be applied to the input field, such as password masking.
 * @param isReadOnly A boolean value indicating whether the input field is read-only.
 * @param onClick A callback function that is invoked when the user clicks the input field.
 */
@Composable
fun UserInputField(
    modifier: Modifier = Modifier,
    label: String?,
    enterValue: (input: String) -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    error: String,
    keyboardOptions: KeyboardOptions,
    leadingIcon: Painter?,
    trailingIcon: Painter?,
    placeholder: String,
    value: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isReadOnly: Boolean = false,
    onClick: () -> Unit = {}

) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        label?.let {
            Text(
                text = it,
                style = typography.labelLarge
            )
        }

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = { inputValue ->
                enterValue(inputValue)
            },
            singleLine = true,
            keyboardOptions = keyboardOptions,
            placeholder = {
                Text(
                    text = placeholder,
                    style = typography.labelMedium,
                    color = Color.LightGray
                )
            },
            leadingIcon = if (leadingIcon !== null) {
                {
                    Icon(
                        painter = leadingIcon,
                        contentDescription = null
                    )
                }
            } else {
                null
            },
            trailingIcon = if (trailingIcon !== null) {
                {
                    IconButton(
                        onClick = onTogglePasswordVisibility
                    ) {
                        Icon(
                            painter = trailingIcon,
                            contentDescription = null
                        )
                    }
                }
            } else {
                null
            },
            shape = RoundedCornerShape(14.dp),
            isError = error.isNotEmpty(),
            supportingText = {
                if (error.isNotEmpty()) {
                    Text(
                        text = error,
                        color = colorScheme.error,
                        style = typography.labelMedium
                    )
                }
            },
            visualTransformation = visualTransformation,
            readOnly = isReadOnly

        )
    }

}