package com.example.homebankfront.feature.authentication

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
import com.example.homebankfront.feature.authentication.AuthenticationEvent.Authenticate
import com.example.homebankfront.feature.authentication.AuthenticationEvent.ToggleAutoAuthentication
import com.example.homebankfront.feature.authentication.AuthenticationEvent.UpdateField
import com.example.homebankfront.feature.authentication.AuthenticationField.PasswordField
import com.example.homebankfront.feature.authentication.AuthenticationField.UsernameField
import com.example.homebankfront.feature.authentication.AuthenticationState.Authenticated
import com.example.homebankfront.feature.authentication.AuthenticationState.Authenticating
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.ui.components.LoadingOverlay
import com.example.homebankfront.ui.components.SecurePasswordTextField
import com.example.homebankfront.ui.components.TextField
import kotlinx.coroutines.launch

@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel = hiltViewModel(),
    onAuthentication: () -> Unit,
    navigateToRegistration: () -> Unit,
    navigateToRecoverUserAccount: () -> Unit
) {
    val state: AuthenticationState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val authenticationManager = remember { AuthenticationManager(context as ComponentActivity) }
    val snackbarHostState = LocalSnackHostState.current

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { error ->
            val errorMessage = when (error) {
                is Left -> error.value.getStringResourceFromContext(context)
                is Right -> error.value.getStringResourceFromContext(context)
            }

            snackbarHostState.showSnackbar(errorMessage)
        }
    }

    AuthenticationScreen(
        state = state,
        authenticationManager = authenticationManager,
        onEvent = viewModel::onEvent,
        onAuthentication = onAuthentication,
        navigateToRegistration = navigateToRegistration,
        navigateToRecoverUserAccount = navigateToRecoverUserAccount
    )
}

@Composable
fun AuthenticationScreen(
    state: AuthenticationState,
    authenticationManager: AuthenticationManager,
    onEvent: (AuthenticationEvent) -> Unit,
    onAuthentication: () -> Unit,
    navigateToRegistration: () -> Unit,
    navigateToRecoverUserAccount: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    when (state) {
        is Authenticating -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    authenticationManager.signIn()?.let {
                        onEvent(UpdateField(state.usernameField.copy(username = it.username)))
                        onEvent(UpdateField(state.passwordField.copy(password = it.password)))
                        onEvent(ToggleAutoAuthentication)
                        onEvent(Authenticate)
                    }
                }
            }

            AuthenticationScreen(
                usernameField = state.usernameField,
                passwordField = state.passwordField,
                isLoading = state.isLoading,
                onEvent = onEvent,
                navigateToRegistration = navigateToRegistration,
                navigateToRecoverUserAccount = navigateToRecoverUserAccount
            )

            LoadingOverlay(isLoading = state.isLoading)
        }

        is Authenticated -> LaunchedEffect(Unit) {
            coroutineScope.launch {
                if (state.registerCredentials) {
                    authenticationManager.registerCredentials(
                        username = state.username,
                        password = state.password
                    )
                }
            }.invokeOnCompletion {
                onAuthentication()
            }
        }
    }
}

@Composable
fun AuthenticationScreen(
    usernameField: UsernameField,
    passwordField: PasswordField,
    isLoading: Boolean,
    onEvent: (AuthenticationEvent) -> Unit,
    navigateToRegistration: () -> Unit,
    navigateToRecoverUserAccount: () -> Unit
) {
    Scaffold { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {
                TextField(
                    label = stringResource(R.string.username),
                    text = usernameField.username,
                    supportingText = usernameField.error?.toStringResource(),
                    isError = usernameField.error != null,
                    enabled = !isLoading,
                    onValueChange = {
                        onEvent(UpdateField(usernameField.copy(username = it, error = null)))
                    }
                )

                SecurePasswordTextField(
                    text = passwordField.password,
                    supportingText = passwordField.error?.toStringResource(),
                    isError = passwordField.error != null,
                    onValueChange = { onEvent(UpdateField(passwordField.copy(password = it))) }
                )

                TextButton(onClick = { onEvent(Authenticate) }) {
                    Text(text = stringResource(R.string.sign_in))
                }

                TextButton(onClick = navigateToRecoverUserAccount) {
                    Text(text = stringResource(R.string.forgot_password))
                }

                TextButton(onClick = navigateToRegistration) {
                    Text(text = stringResource(R.string.registration))
                }
            }
        }
    }
}
