package com.example.homebankfront.feature.recoverUserAccount

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
import com.example.homebankfront.feature.recoverUserAccount.RecoverUserAccountEvent.InitiateRecovery
import com.example.homebankfront.feature.recoverUserAccount.RecoverUserAccountEvent.Update
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.ui.components.LoadingOverlay
import com.example.homebankfront.ui.components.TextField

@Composable
fun RecoverUserAccountScreen(
    viewModel: RecoverUserAccountViewModel = hiltViewModel(),
    onRecoveryInitiated: () -> Unit
) {
    val state: RecoverUserAccountState by viewModel.state.collectAsStateWithLifecycle()
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

    RecoverUserAccountScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onRecoveryInitiated = onRecoveryInitiated
    )
}

@Composable
fun RecoverUserAccountScreen(
    state: RecoverUserAccountState,
    onEvent: (RecoverUserAccountEvent) -> Unit,
    onRecoveryInitiated: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    when (state) {
        is RecoverUserAccountState.Ready -> {
            RecoverUserAccountScreen(
                emailField = state.emailField,
                isLoading = state.isLoading,
                onEvent = onEvent,
            )

            LoadingOverlay(isLoading = state.isLoading)
        }

        is RecoverUserAccountState.RecoveryInitiated ->
            onRecoveryInitiated()
    }
}

@Composable
fun RecoverUserAccountScreen(
    emailField: RecoverUserAccountField.EmailField,
    isLoading: Boolean,
    onEvent: (RecoverUserAccountEvent) -> Unit,
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
                    onValueChange = {
                        onEvent(Update(emailField.copy(email = it, error =  null)))
                    }
                )

                TextButton(onClick = { onEvent(InitiateRecovery) }) {
                    Text(text = stringResource(R.string.recover_password))
                }
            }
        }
    }
}
