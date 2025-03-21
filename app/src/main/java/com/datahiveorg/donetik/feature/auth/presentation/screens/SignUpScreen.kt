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
fun SignUpScreen(
    modifier: Modifier = Modifier,
) {
    val loginText = buildAnnotatedString {
        append("Already have an account? ")
        pushStringAnnotation("Login", annotation = "Click Login")
        withStyle(style = SpanStyle(color = colorScheme.primary)) {
            append("Sign Up")
        }
    }

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
            value = "",
            error = "",
            leadingIcon = Icons.Rounded.Email,
            trailingIcon = null,
            placeholder = "Enter your email",
            visualTransformation = VisualTransformation.None,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            onTogglePasswordVisibility = {},
            enterValue = {}

        )

        UserInputField(
            label = "Password",
            value = "",
            error = "",
            leadingIcon = Icons.Rounded.Create,
            trailingIcon = null,
            placeholder = "Enter your password",
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            onTogglePasswordVisibility = {},
            enterValue = {}

        )

        UserInputField(
            label = "Confirm Password",
            value = "",
            error = "",
            leadingIcon = Icons.Rounded.Lock,
            trailingIcon = Icons.Rounded.Create,
            placeholder = "Confirm your password",
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            onTogglePasswordVisibility = {},
            enterValue = {}

        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

        PrimaryButton(
            label = "Create account",
            onClick = {},
            isEnabled = true
        )

        SecondaryButton(
            label = "Sign up with Google",
            onClick = {},
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
                        //TODO(Navigate to log in screen)
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
        SignUpScreen()
    }
}