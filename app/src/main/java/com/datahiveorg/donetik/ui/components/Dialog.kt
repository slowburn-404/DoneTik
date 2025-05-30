package com.datahiveorg.donetik.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputFieldDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    onSaveClick: (String) -> Unit,
    confirmButtonText: String = "Confirm",
    dismissButtonText: String = "Cancel",
    title: String,
    label: String? = null,
    showDialog: Boolean = false,
) {
    //maintains its own state
    var inputValue by remember { mutableStateOf("") }

    if (showDialog) {
        BasicAlertDialog(
            modifier = modifier,
            onDismissRequest = onDismiss,
        ) {
            Card(
                shape = shapes.extraLarge,
                colors = CardDefaults.cardColors(
                    containerColor = colorScheme.surfaceVariant,
                    contentColor = colorScheme.onSurfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    label?.let {
                        Text(
                            text = title,
                            style = typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    UserInputField(
                        modifier = Modifier.fillMaxWidth(),
                        label = null,
                        enterValue = {
                            inputValue = it
                        },
                        onTogglePasswordVisibility = {},
                        error = "",
                        keyboardOptions = KeyboardOptions(),
                        leadingIcon = null,
                        trailingIcon = null,
                        placeholder = "Enter Category",
                        value = inputValue,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                    ) {
                        SecondaryButton(
                            label = dismissButtonText,
                            onClick = onDismiss,
                            leadingIcon = null,
                            isLoading = false
                        )

                        PrimaryButton(
                            label = confirmButtonText,
                            onClick = {
                                onSaveClick(inputValue)
                                onDismiss()
                            },
                            isEnabled = inputValue.isNotEmpty(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}