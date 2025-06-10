package com.datahiveorg.donetik.feature.auth.presentation.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.datahiveorg.donetik.core.ui.components.PrimaryButton
import com.datahiveorg.donetik.core.ui.components.SecondaryButton
import com.datahiveorg.donetik.core.ui.components.UserInputField
import com.datahiveorg.donetik.util.GoogleSignHelper
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: AuthenticationUiState,
    onEvent: (AuthenticationUiEvent) -> Unit,
    onIntent: (AuthenticationIntent) -> Unit,
    googleSignHelper: GoogleSignHelper
) {

    val signUpText = buildAnnotatedString {
        append("Don't have an account? ")
        pushStringAnnotation("Sign Up", annotation = "Click Sign up")
        withStyle(style = SpanStyle(color = colorScheme.primary)) {
            append("Sign Up")
        }
    }

    var isPasswordVisible by remember { mutableStateOf(false) }

    val coroutineContext = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Spacer(
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "Donetik",
            style = typography.displayMedium,
        )

        Text(
            text = "Welcome back to Donetik",
            style = typography.bodyLarge
        )

        UserInputField(
            modifier = Modifier.fillMaxWidth(),
            label = "Email",
            enterValue = {
                onIntent(AuthenticationIntent.EnterEmail(it))
            },
            error = state.emailError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            visualTransformation = VisualTransformation.None,
            value = state.user.email,
            leadingIcon = painterResource(R.drawable.ic_mail),
            trailingIcon = null,
            placeholder = "Enter email",
            onTogglePasswordVisibility = {}

        )

        UserInputField(
            modifier = Modifier.fillMaxWidth(),
            label = "Password",
            enterValue = {
                onIntent(AuthenticationIntent.EnterPassword(it))
            },
            error = state.passwordError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else PasswordVisualTransformation(),
            value = state.user.password,
            leadingIcon = painterResource(R.drawable.ic_lock),
            trailingIcon = if (!isPasswordVisible) painterResource(R.drawable.ic_visibility_off) else painterResource(
                R.drawable.ic_visibility
            ),
            placeholder = "Enter password",
            onTogglePasswordVisibility = {
                isPasswordVisible = !isPasswordVisible
            }
        )

        TextButton(
            modifier = Modifier.align(Alignment.End),
            onClick = {
                //TODO(Navigate to forgot password screen)
            }
        ) {
            Text(
                text = "Forgot Password?",
                style = typography.bodyMedium,
                textAlign = TextAlign.End
            )
        }

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Login",
            onClick = {
                onIntent(AuthenticationIntent.Login)
            },
            isEnabled = state.isFormValid,
            isLoading = state.isLoading
        )

        SecondaryButton(
            modifier = Modifier.fillMaxWidth(),
            label = "Continue with Google",
            onClick = {
                coroutineContext.launch {
                    when (val result = googleSignHelper.getGoogleIdToken(true)) {
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
                        onEvent(AuthenticationUiEvent.Navigate.SignUp)
                    }
                },
            style = typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.weight(1f)
        )

    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun LoginScreenPreview() {
//    DoneTikTheme {
//        LoginScreen(
//            state = AuthenticationUiState(),
//            onEvent = {},
//            onIntent = {},
//        )
//    }
//}