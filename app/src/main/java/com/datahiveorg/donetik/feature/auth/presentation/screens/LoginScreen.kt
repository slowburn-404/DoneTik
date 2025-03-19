package com.datahiveorg.donetik.feature.auth.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.datahiveorg.donetik.ui.components.PrimaryButton
import com.datahiveorg.donetik.ui.components.SecondaryButton
import com.datahiveorg.donetik.ui.components.UserInputField
import com.datahiveorg.donetik.ui.theme.DoneTikTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    val signUpText = buildAnnotatedString {
        append("Don't have an account? ")
        pushStringAnnotation("Sign Up", annotation = "Click Sign up")
        withStyle(style = SpanStyle(color = colorScheme.primary)) {
            append("Sign Up")
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = "Login to your account",
            style = typography.bodyLarge
        )

        UserInputField(
            label = "Email",
            enterValue = {},
            error = "",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            visualTransformation = VisualTransformation.None,
            value = "",
            leadingIcon = Icons.Filled.Email,
            trailingIcon = null,
            placeholder = "Enter email",
            onTogglePasswordVisibility = {}

        )

        UserInputField(
            label = "Password",
            enterValue = {},
            error = "",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else PasswordVisualTransformation(),
            value = "",
            leadingIcon = Icons.Filled.Email,
            trailingIcon = if (isPasswordVisible) Icons.Rounded.Build else Icons.Rounded.Lock,
            placeholder = "Enter password",
            onTogglePasswordVisibility = {
                isPasswordVisible = !isPasswordVisible
            }

        )

        TextButton(
            modifier = Modifier.align(Alignment.End),
            onClick = {}
        ) {
            Text(
                text = "Forgot Password?",
                style = typography.bodyMedium,
                textAlign = TextAlign.End
            )
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        PrimaryButton(
            label = "Login",
            onClick = {},
            isEnabled = true
        )

        SecondaryButton(
            label = "Continue with Google",
            onClick = {},
            leadingIcon = Icons.Rounded.Share
        )


        Text(
            text = signUpText,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .clickable {
                    val annotatedString = signUpText
                        .getStringAnnotations(
                            tag = "Sign Up",
                            start = 0,
                            end = signUpText.length
                        ).firstOrNull()

                    annotatedString?.let {
                        //TODO(Navigate to sign up screen)
                    }
                },
            style = typography.bodyMedium,
            textAlign = TextAlign.Center
        )


    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    DoneTikTheme {
        LoginScreen()
    }
}