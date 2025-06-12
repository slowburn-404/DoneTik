package com.datahiveorg.donetik.feature.auth.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.core.ui.components.PrimaryButton
import com.datahiveorg.donetik.core.ui.components.UserInputField
import com.datahiveorg.donetik.feature.auth.presentation.AuthenticationIntent
import com.datahiveorg.donetik.feature.auth.presentation.AuthenticationUiState

@Composable
fun UsernameScreen(
    modifier: Modifier = Modifier,
    state: AuthenticationUiState,
    onIntent: (AuthenticationIntent) -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "Donetik",
            style = typography.displayMedium
        )

        Text(
            text = "Pick as username",
            style = typography.bodyLarge
        )


        UserInputField(
            modifier = Modifier.fillMaxWidth(),
            label = "Email",
            value = state.user.username,
            error = "",
            leadingIcon = painterResource(R.drawable.ic_profile),
            trailingIcon = null,
            placeholder = "Enter your username",
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            onTogglePasswordVisibility = {},
            enterValue = {
                onIntent(AuthenticationIntent.EnterUsername(it))
            }
        )

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Update username",
            onClick = { onIntent(AuthenticationIntent.UpdateUsername) },
            isEnabled = state.user.username.isNotEmpty(),
            isLoading = state.isLoading
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

    }

}