package com.example.homebankfront.feature.registration

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import com.example.homebankfront.feature.registration.RegistrationState.Failure
import com.example.homebankfront.feature.registration.RegistrationState.InProgress
import com.example.homebankfront.feature.registration.RegistrationState.Success
import com.example.homebankfront.ui.components.TextField
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction2


@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    onRegistration: () -> Unit
) {
    val state: RegistrationState by viewModel.registrationState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val authenticationManager = remember { AuthenticationManager(context as ComponentActivity) }
    val snackbarHostState = LocalSnackHostState.current

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect {
            it.consume()?.let { message ->
                snackbarHostState.showSnackbar(message)
            }
        }
    }

    RegistrationScreen(
        state = state,
        onEvent = viewModel::onEvent,
        register = authenticationManager::register,
        onRegistration = onRegistration
    )
}

@Composable
fun RegistrationScreen(
    state: RegistrationState,
    onEvent: (RegistrationEvent) -> Unit,
    register: KSuspendFunction2<String, String, Unit>,
    onRegistration: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    when (state) {
        is Failure -> {}

        is InProgress -> RegistrationScreen(
            username = state.username,
            password = state.password,
            email = state.email,
            onEvent = onEvent,
        )

        is Success -> LaunchedEffect(Unit) {
            // coroutineScope.launch {
            //   register(state.username, state.password)
            //}.invokeOnCompletion {
            onRegistration()
            //}
        }
    }
}

@Composable
fun RegistrationScreen(
    username: String,
    password: String,
    email: String,
    onEvent: (RegistrationEvent) -> Unit,
) {
    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            TextField(
                label = stringResource(R.string.username),
                text = username,
                onValueChange = { RegistrationField.Username(it).update(onEvent) })

            TextField(
                label = stringResource(R.string.password),
                text = password,
                onValueChange = { RegistrationField.Password(it).update(onEvent) }
            )

            TextField(
                label = stringResource(R.string.email),
                text = email,
                onValueChange = { RegistrationField.Email(it).update(onEvent) }
            )

            TextButton(onClick = { onEvent(Register) }) {
                Text(text = stringResource(R.string.register))
            }
        }
    }
}
