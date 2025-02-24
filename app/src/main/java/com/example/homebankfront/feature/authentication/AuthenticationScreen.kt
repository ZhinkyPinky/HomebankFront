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
import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.feature.authentication.AuthenticationEvent.*
import com.example.homebankfront.feature.authentication.AuthenticationField.*
import com.example.homebankfront.feature.authentication.AuthenticationState.*
import com.example.homebankfront.feature.utility.Either.*
import com.example.homebankfront.feature.utility.getStringResourceFromContext
import com.example.homebankfront.ui.components.LoadingOverlay
import com.example.homebankfront.ui.components.TextField
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel = hiltViewModel(),
    onAuthentication: () -> Unit,
    navigateToRegistration: () -> Unit
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
        signIn = authenticationManager::signIn,
        onEvent = viewModel::onEvent,
        onAuthenticated = onAuthentication,
        navigateToRegistration = navigateToRegistration
    )
}

@Composable
fun AuthenticationScreen(
    state: AuthenticationState,
    signIn: KSuspendFunction0<AuthenticationRequest>,
    onEvent: (AuthenticationEvent) -> Unit,
    onAuthenticated: () -> Unit,
    navigateToRegistration: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    when (state) {
        is Authenticating -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    signIn()
                }
            }

            AuthenticationScreen(
                usernameField = state.usernameField,
                passwordField = state.passwordField,
                isLoading = state.isLoading,
                onEvent = onEvent,
                navigateToRegistration = navigateToRegistration
            )

            LoadingOverlay(isLoading = state.isLoading)
        }

        is Authenticated -> onAuthenticated()
    }
}

@Composable
fun AuthenticationScreen(
    usernameField: UsernameField,
    passwordField: PasswordField,
    isLoading: Boolean,
    onEvent: (AuthenticationEvent) -> Unit,
    navigateToRegistration: () -> Unit
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

                TextField(
                    label = stringResource(R.string.password),
                    text = passwordField.password,
                    supportingText = passwordField.error?.toStringResource(),
                    isError = passwordField.error != null,
                    enabled = !isLoading,
                    onValueChange = {
                        onEvent(UpdateField(passwordField.copy(password = it, error = null)))
                    }
                )

                TextButton(onClick = { onEvent(Authenticate) }) {
                    Text(text = stringResource(R.string.sign_in))
                }

                TextButton(onClick = navigateToRegistration) {
                    Text(text = stringResource(R.string.registration))
                }
            }
        }
    }
}