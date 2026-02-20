package com.example.homebankfront.feature.accountActivationPending

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.LocalSnackHostState
import com.example.homebankfront.R
import com.example.homebankfront.feature.accountActivationPending.AccountActivationPendingEvent.*
import com.example.homebankfront.feature.accountActivationPending.AccountActivationPendingState.*
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.ui.components.LoadingOverlay

@Composable
fun AccountActivationPendingScreen(
    viewModel: AccountActivationPendingViewModel = hiltViewModel(),
    onNewActivationEmailSent: () -> Unit,
) {

    val state: AccountActivationPendingState by viewModel.state.collectAsStateWithLifecycle()
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

    when (state) {
        is ActivationPending -> {
            val activationPendingState = state as ActivationPending

            AccountActivationPendingScreenContent(onEvent = viewModel::onEvent)
            LoadingOverlay(isLoading = activationPendingState.isLoading)
        }

        is NewActivationEmailSent -> LaunchedEffect(Unit) {
            onNewActivationEmailSent()
        }
    }


}

@Composable
fun AccountActivationPendingScreenContent(
    onEvent: (AccountActivationPendingEvent) -> Unit,
) {
    Scaffold { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {
                Text(
                    text = stringResource(R.string.account_not_activated),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                TextButton(onClick = { onEvent(ResendActivationEmail) }) {
                    Text(text = stringResource(R.string.resend_activation_email))
                }
            }
        }
    }
}
