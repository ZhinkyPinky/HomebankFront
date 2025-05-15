package com.example.homebankfront.feature.accountrecovery.onetimepasswordinput

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.LocalSnackHostState
import com.example.homebankfront.R
import com.example.homebankfront.feature.accountrecovery.confirmationemailrequest.getStringResourceFromContext
import com.example.homebankfront.feature.accountrecovery.onetimepasswordinput.OneTimePasswordInputEvent.*
import com.example.homebankfront.feature.accountrecovery.onetimepasswordinput.OneTimePasswordInputState.*
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.ui.components.LoadingOverlay
import com.example.homebankfront.ui.components.TextField

@Composable
fun OneTimePasswordInputScreen(
    viewModel: OneTimePasswordInputViewModel = hiltViewModel(),
    onAuthenticated: () -> Unit
) {
    val state: OneTimePasswordInputState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
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

    OneTimePasswordScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onAuthenticated = onAuthenticated
    )
}

@Composable
fun OneTimePasswordScreen(
    state: OneTimePasswordInputState,
    onEvent: (OneTimePasswordInputEvent) -> Unit,
    onAuthenticated: () -> Unit
) {
    when (state) {
        is Default -> {
            OneTimePasswordScreen(
                passwordField = state.oneTimePasswordField,
                updatePasswordField = {
                    onEvent(
                        UpdatePasswordField(
                            state.oneTimePasswordField.copy(
                                value = it,
                                error = null
                            )
                        )
                    )
                },
                authenticate = { onEvent(Authenticate) },
                isLoading = state.isLoading,
            )

            LoadingOverlay(isLoading = state.isLoading)
        }

        is Authenticated -> onAuthenticated()
    }
}

@Composable
fun OneTimePasswordScreen(
    passwordField: OneTimePasswordField,
    updatePasswordField: (String) -> Unit,
    authenticate: () -> Unit,
    isLoading: Boolean,
) {
    Scaffold { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {
                TextField(
                    label = stringResource(R.string.password),
                    text = passwordField.value,
                    supportingText = passwordField.error?.toStringResource(),
                    isError = passwordField.error != null,
                    enabled = !isLoading,
                    onValueChange = updatePasswordField
                )

                TextButton(onClick = authenticate) {
                    Text(text = stringResource(R.string.recover_password))
                }
            }
        }
    }
}
