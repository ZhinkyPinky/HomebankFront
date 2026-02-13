package com.example.homebankfront.feature.registration

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.LocalSnackHostState
import com.example.homebankfront.R
import com.example.homebankfront.feature.authentication.AuthenticationManager
import com.example.homebankfront.feature.registration.RegistrationEvent.Register
import com.example.homebankfront.feature.registration.RegistrationField.EmailField
import com.example.homebankfront.feature.registration.RegistrationField.PasswordField
import com.example.homebankfront.feature.registration.RegistrationState.Registered
import com.example.homebankfront.feature.registration.RegistrationState.Input
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.ui.components.LoadingOverlay
import com.example.homebankfront.ui.components.SecurePasswordTextField
import com.example.homebankfront.ui.components.TextField
import kotlinx.coroutines.launch


@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    onRegistration: () -> Unit
) {
    val state: RegistrationState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val authenticationManager = remember { AuthenticationManager(context as ComponentActivity) }
    val snackHostState = LocalSnackHostState.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { error ->
            val errorMessage = when (error) {
                is Left -> error.value.getStringResourceFromContext(context)
                is Right -> error.value.getStringResourceFromContext(context)
            }

            snackHostState.showSnackbar(errorMessage)
        }
    }

    when (state) {
        is Input -> {
            val inputState = state as Input
            RegistrationScreenContent(
                emailField = inputState.emailField,
                passwordField = inputState.passwordField,
                isLoading = inputState.isLoading,
                onEvent = viewModel::onEvent,
            )

            LoadingOverlay(isLoading = inputState.isLoading)
        }

        is Registered -> LaunchedEffect(Unit) {
            val registeredState = state as Registered
            coroutineScope.launch {
                authenticationManager.registerCredentials(
                    registeredState.email,
                    registeredState.password
                )
            }.invokeOnCompletion {
                onRegistration()
            }
        }
    }

}

@Composable
fun RegistrationScreenContent(
    emailField: EmailField,
    passwordField: PasswordField,
    isLoading: Boolean,
    onEvent: (RegistrationEvent) -> Unit,
) {
    Scaffold { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {
                TextField(
                    label = stringResource(R.string.email),
                    text = emailField.email,
                    supportingText = emailField.error?.toStringResource(),
                    isError = emailField.error != null,
                    enabled = !isLoading,
                    onValueChange = { emailField.copy(email = it, error = null).update(onEvent) }
                )

                SecurePasswordTextField(
                    text = passwordField.password,
                    supportingText = passwordField.error?.toStringResource(),
                    isError = passwordField.error != null,
                    onValueChange = {
                        passwordField.copy(password = it, error = null).update(onEvent)
                    }
                )

                TextButton(onClick = { onEvent(Register) }) {
                    Text(text = stringResource(R.string.register))
                }
            }
        }
    }
}
