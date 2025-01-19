package com.example.homebankfront.feature.authentication

import android.util.Log
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
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.AuthenticationRequest
import com.example.homebankfront.feature.authentication.AuthenticationState.*
import com.example.homebankfront.ui.components.TextField
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction0

@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel = hiltViewModel(),
    onAuthentication: () -> Unit,
    navigateToRegistration: () -> Unit
) {
    val state: AuthenticationState by viewModel.authenticationState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val authenticationManager = remember { AuthenticationManager(context as ComponentActivity) }

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
        is NotSignedIn -> {
            LaunchedEffect(Unit) {
                coroutineScope.launch {
                    signIn()
                }
            }

            AuthenticationScreen(
                username = state.username,
                password = state.password,
                onEvent = onEvent,
                navigateToRegistration = navigateToRegistration
            )
        }

        is Authenticated -> onAuthenticated()
    }
}

@Composable
fun AuthenticationScreen(
    username: String,
    password: String,
    onEvent: (AuthenticationEvent) -> Unit,
    navigateToRegistration: () -> Unit
) {
    Scaffold { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {
                TextField(
                    label = stringResource(R.string.username),
                    text = username,
                    onValueChange = { onEvent(AuthenticationEvent.UpdateUsername(it)) }
                )

                TextField(
                    label = stringResource(R.string.password),
                    text = password,
                    onValueChange = { onEvent(AuthenticationEvent.UpdatePassword(it)) }
                )

                TextButton(onClick = { onEvent(AuthenticationEvent.Authenticate) }) {
                    Text(text = stringResource(R.string.sign_in))
                }

                TextButton(onClick = navigateToRegistration) {
                    Text(text = stringResource(R.string.registration))
                }

            }
        }
    }
}