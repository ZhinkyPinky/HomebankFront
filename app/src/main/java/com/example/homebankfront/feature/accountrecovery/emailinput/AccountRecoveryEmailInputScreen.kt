package com.example.homebankfront.feature.accountrecovery.emailinput

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
import com.example.homebankfront.feature.accountrecovery.emailinput.AccountRecoveryEmailInputEvent.RequestRecoveryPassword
import com.example.homebankfront.feature.accountrecovery.emailinput.AccountRecoveryEmailInputEvent.Update
import com.example.homebankfront.feature.accountrecovery.emailinput.AccountRecoveryEmailInputField.EmailField
import com.example.homebankfront.feature.accountrecovery.emailinput.AccountRecoveryEmailInputState.Input
import com.example.homebankfront.feature.accountrecovery.emailinput.AccountRecoveryEmailInputState.RecoveryPasswordSent
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.ui.components.LoadingOverlay
import com.example.homebankfront.ui.components.TextField
import com.example.homebankfront.ui.theme.HomeBankFrontTheme
import com.example.homebankfront.ui.theme.ThemePreviews

@Composable
fun AccountRecoveryEmailInputScreen(
    viewModel: AccountRecoveryEmailInputViewModel = hiltViewModel(),
    onRecoveryPasswordSent: (String) -> Unit
) {
    val state: AccountRecoveryEmailInputState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val snackHostState = LocalSnackHostState.current

    LaunchedEffect(Unit) {
        viewModel.errorFlow.collect { error ->
            val errorMessage = when (error) {
                is Left -> error.value.getStringResourceFromContext(context)
                is Right -> error.value.getStringResourceFromContext(context)
            }

            snackHostState.showSnackbar(errorMessage)
        }
    }

    when (val localState = state) {
        is Input -> {
            AccountRecoveryEmailInputContent(
                emailField = localState.emailField,
                isLoading = localState.isLoading,
                onEvent = viewModel::onEvent,
            )

            LoadingOverlay(isLoading = localState.isLoading)
        }

        is RecoveryPasswordSent -> onRecoveryPasswordSent(localState.email)
    }
}

@Composable
private fun AccountRecoveryEmailInputContent(
    emailField: EmailField,
    isLoading: Boolean,
    onEvent: (AccountRecoveryEmailInputEvent) -> Unit,
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

                TextButton(onClick = { onEvent(RequestRecoveryPassword) }) {
                    Text(text = stringResource(R.string.recover_password))
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun ConfirmationEmailRequestScreenPreview() {
    HomeBankFrontTheme {
        AccountRecoveryEmailInputContent(
            emailField = EmailField(),
            isLoading = false,
            onEvent = {}
        )
    }
}