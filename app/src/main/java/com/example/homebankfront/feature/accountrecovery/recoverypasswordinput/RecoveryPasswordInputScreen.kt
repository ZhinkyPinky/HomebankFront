package com.example.homebankfront.feature.accountrecovery.recoverypasswordinput

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
import com.example.homebankfront.feature.accountrecovery.recoverypasswordinput.RecoveryPasswordInputEvent.Authenticate
import com.example.homebankfront.feature.accountrecovery.recoverypasswordinput.RecoveryPasswordInputEvent.UpdatePasswordField
import com.example.homebankfront.feature.accountrecovery.recoverypasswordinput.RecoveryPasswordInputState.Authenticated
import com.example.homebankfront.feature.accountrecovery.recoverypasswordinput.RecoveryPasswordInputState.Input
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.ui.components.LoadingOverlay
import com.example.homebankfront.ui.components.TextField
import com.example.homebankfront.ui.theme.HomeBankFrontTheme
import com.example.homebankfront.ui.theme.ThemePreviews

@Composable
fun RecoveryPasswordInputScreen(
    viewModel: RecoveryPasswordInputViewModel = hiltViewModel(),
    onAuthenticated: (String) -> Unit
) {
    val state: RecoveryPasswordInputState by viewModel.state.collectAsStateWithLifecycle()
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

    when (val localState = state) {
        is Input -> {
            RecoveryPasswordInputContent(
                passwordField = localState.recoveryPasswordField,
                updatePasswordField = {
                    viewModel.onEvent(
                        UpdatePasswordField(
                            localState.recoveryPasswordField.copy(
                                password = it,
                                error = null
                            )
                        )
                    )
                },
                authenticate = { viewModel.onEvent(Authenticate) },
                isLoading = localState.isLoading,
            )

            LoadingOverlay(isLoading = localState.isLoading)
        }

        is Authenticated -> onAuthenticated(localState.recoveryToken)
    }
}

@Composable
private fun RecoveryPasswordInputContent(
    passwordField: RecoveryPasswordField,
    updatePasswordField: (String) -> Unit,
    authenticate: () -> Unit,
    isLoading: Boolean,
) {
    Scaffold { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {
                TextField(
                    label = stringResource(R.string.password),
                    text = passwordField.password,
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

@ThemePreviews
@Composable
fun OneTimePasswordInputScreenPreview() {
    HomeBankFrontTheme {
        RecoveryPasswordInputContent(
            passwordField = RecoveryPasswordField(),
            updatePasswordField = {},
            authenticate = {},
            isLoading = false
        )
    }
}
