package com.datahiveorg.donetik.feature.auth.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.datahiveorg.donetik.feature.auth.presentation.AuthenticationIntent
import com.datahiveorg.donetik.feature.auth.presentation.AuthenticationUiEvent
import com.datahiveorg.donetik.feature.auth.presentation.AuthenticationUiState
import com.datahiveorg.donetik.ui.components.PrimaryButton
import com.datahiveorg.donetik.ui.components.SecondaryButton
import com.datahiveorg.donetik.ui.components.UserInputField
import com.datahiveorg.donetik.ui.theme.DoneTikTheme

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    state: AuthenticationUiState,
    onEvent: (AuthenticationUiEvent) -> Unit,
    onIntent: (AuthenticationIntent) -> Unit
) {
    val loginText = buildAnnotatedString {
        append("Already have an account? ")
        pushStringAnnotation("Login", annotation = "Click Login")
        withStyle(style = SpanStyle(color = colorScheme.primary)) {
            append("Sign Up")
        }
    }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = "Create your account",
            style = typography.bodyLarge
        )

        UserInputField(
            label = "Email",
            value = state.user.email,
            error = state.emailError,
            leadingIcon = Icons.Rounded.Email,
            trailingIcon = null,
            placeholder = "Enter your email",
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            onTogglePasswordVisibility = {},
            enterValue = {
                onIntent(AuthenticationIntent.EnterEmail(it))
            }

        )

        UserInputField(
            label = "Password",
            value = state.user.password,
            error = state.passwordError,
            leadingIcon = Icons.Rounded.Create,
            trailingIcon = null,
            placeholder = "Enter your password",
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            onTogglePasswordVisibility = {
                isPasswordVisible = !isPasswordVisible
            },
            enterValue = {
                onIntent(AuthenticationIntent.EnterPassword(it))
            }

        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        PrimaryButton(
            label = "Create account",
            onClick = {
                onIntent(AuthenticationIntent.SignUp)
            },
            isEnabled = state.isFormValid
        )

        SecondaryButton(
            label = "Sign up with Google",
            onClick = {
                //TODO(Sign up with google)
            },
            leadingIcon = Icons.Rounded.Share
        )

        Text(
            text = loginText,
            style = typography.bodyLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    val annotatedString = loginText.getStringAnnotations(
                        tag = "Login",
                        start = 0,
                        end = loginText.length
                    ).firstOrNull()

                    annotatedString?.let {
                        onEvent(AuthenticationUiEvent.Navigate.Login)
                    }
                },
            textAlign = TextAlign.Center
        )


    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    DoneTikTheme {
        SignUpScreen(
            state = AuthenticationUiState(),
            onIntent = {},
            onEvent = {}
        )
    }
}