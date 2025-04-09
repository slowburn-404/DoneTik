package com.datahiveorg.donetik.feature.auth.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.datahiveorg.donetik.R
import com.datahiveorg.donetik.feature.auth.domain.DomainResponse
import com.datahiveorg.donetik.feature.auth.presentation.AuthenticationIntent
import com.datahiveorg.donetik.feature.auth.presentation.AuthenticationUiEvent
import com.datahiveorg.donetik.feature.auth.presentation.AuthenticationUiState
import com.datahiveorg.donetik.ui.components.PrimaryButton
import com.datahiveorg.donetik.ui.components.SecondaryButton
import com.datahiveorg.donetik.ui.components.UserInputField
import com.datahiveorg.donetik.util.GoogleSignHelper
import kotlinx.coroutines.launch

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    state: AuthenticationUiState,
    onEvent: (AuthenticationUiEvent) -> Unit,
    onIntent: (AuthenticationIntent) -> Unit,
    googleSignHelper: GoogleSignHelper
) {
    val loginText = buildAnnotatedString {
        append("Already have an account? ")
        pushStringAnnotation("Login", annotation = "Click Login")
        withStyle(style = SpanStyle(color = colorScheme.primary)) {
            append("Login")
        }
    }
    var isPasswordVisible by remember { mutableStateOf(false) }

    val coroutineContext = rememberCoroutineScope()
    val context = LocalContext.current

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
            leadingIcon = painterResource(R.drawable.ic_mail),
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
            leadingIcon = painterResource(R.drawable.ic_lock),
            trailingIcon = if (isPasswordVisible) painterResource(R.drawable.ic_visibility_off) else painterResource(
                R.drawable.ic_visibility
            ),
            placeholder = "Enter your password",
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else PasswordVisualTransformation(),
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

        PrimaryButton(
            label = "Create account",
            onClick = {
                onIntent(AuthenticationIntent.SignUp)
            },
            isFormValid = state.isFormValid,
            isLoading = state.isLoading
        )

        SecondaryButton(
            label = "Sign up with Google",
            onClick = {
                coroutineContext.launch {
                    when (val result = googleSignHelper.getGoogleIdToken(false)) {
                        is DomainResponse.Success -> {
                            onIntent(AuthenticationIntent.SignInWithGoogle(result.data))
                        }

                        is DomainResponse.Failure -> {
                            Toast.makeText(
                                context,
                                result.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            },
            leadingIcon = painterResource(R.drawable.ic_google),
            isLoading = state.isLoading
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
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun SignUpScreenPreview() {
//    DoneTikTheme {
//        SignUpScreen(
//            state = AuthenticationUiState(),
//            onIntent = {},
//            onEvent = {}
//        )
//    }
//}