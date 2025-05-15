package com.example.homebankfront.feature.accountrecovery.confirmationemailrequest

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.LocalSnackHostState
import com.example.homebankfront.R
import com.example.homebankfront.feature.authentication.AuthenticationManager
import com.example.homebankfront.feature.accountrecovery.confirmationemailrequest.ConfirmationEmailRequestEvent.InitiateRecovery
import com.example.homebankfront.feature.accountrecovery.confirmationemailrequest.ConfirmationEmailRequestEvent.Update
import com.example.homebankfront.feature.accountrecovery.confirmationemailrequest.ConfirmationEmailRequestField.*
import com.example.homebankfront.feature.accountrecovery.confirmationemailrequest.ConfirmationEmailRequestState.*
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.ui.components.LoadingOverlay
import com.example.homebankfront.ui.components.TextField
import com.example.homebankfront.ui.theme.HomeBankFrontTheme
import com.example.homebankfront.ui.theme.ThemePreviews

@Composable
fun ConfirmationEmailRequestScreen(
    viewModel: ConfirmationEmailRequestViewModel = hiltViewModel(),
    onRecoveryInitiated: () -> Unit
) {
    val state: ConfirmationEmailRequestState by viewModel.state.collectAsStateWithLifecycle()
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

    ConfirmationEmailRequestScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onRecoveryInitiated = onRecoveryInitiated
    )
}

@Composable
fun ConfirmationEmailRequestScreen(
    state: ConfirmationEmailRequestState,
    onEvent: (ConfirmationEmailRequestEvent) -> Unit,
    onRecoveryInitiated: () -> Unit
) {
    when (state) {
        is Ready -> {
            ConfirmationEmailRequestScreen(
                emailField = state.emailField,
                isLoading = state.isLoading,
                onEvent = onEvent,
            )

            LoadingOverlay(isLoading = state.isLoading)
        }

        is RecoveryInitiated -> onRecoveryInitiated()
    }
}

@Composable
fun ConfirmationEmailRequestScreen(
    emailField: EmailField,
    isLoading: Boolean,
    onEvent: (ConfirmationEmailRequestEvent) -> Unit,
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
                    onValueChange = { onEvent(Update(emailField.copy(email = it, error = null))) }
                )

                TextButton(onClick = { onEvent(InitiateRecovery) }) {
                    Text(text = stringResource(R.string.recover_password))
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun ConfirmationEmailRequestScreenPreview() {
    HomeBankFrontTheme {
        ConfirmationEmailRequestScreen(
            emailField = EmailField(),
            isLoading = false,
            onEvent = {}
        )
    }
}