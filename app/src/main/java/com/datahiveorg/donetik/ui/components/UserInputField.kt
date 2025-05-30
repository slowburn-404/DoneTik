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