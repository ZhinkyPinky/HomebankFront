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
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputEvent
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputEvent.*
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputState
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputState.*
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputViewModel
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.PasswordField
import com.example.homebankfront.feature.utility.Either.*
import com.example.homebankfront.ui.components.LoadingOverlay
import com.example.homebankfront.ui.components.SecurePasswordTextField
import com.example.homebankfront.ui.theme.HomeBankFrontTheme
import com.example.homebankfront.ui.theme.ThemePreviews

@Composable
fun NewPasswordInputScreen(
    viewModel: NewPasswordInputViewModel = hiltViewModel(),
    onNewPasswordSet: () -> Unit,
    onActivationPending: () -> Unit,
) {
    val state: NewPasswordInputState by viewModel.state.collectAsStateWithLifecycle()
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
        NewPasswordSet -> LaunchedEffect(Unit) { onNewPasswordSet() }

        is Input -> {
            NewPasswordContent(
                newPasswordField = localState.newPasswordField,
                confirmNewPasswordField = localState.confirmNewPasswordField,
                onEvent = viewModel::onEvent
            )

            LoadingOverlay(isLoading = localState.isLoading)
        }

        AccountActivationPending -> LaunchedEffect(Unit) { onActivationPending() }
    }
}

@Composable
private fun NewPasswordContent(
    newPasswordField: PasswordField,
    confirmNewPasswordField: PasswordField,
    onEvent: (NewPasswordInputEvent) -> Unit,
) {
    Scaffold { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {
                SecurePasswordTextField(
                    label = stringResource(R.string.new_password),
                    text = newPasswordField.value,
                    supportingText = newPasswordField.error?.toStringResource(),
                    isError = newPasswordField.error != null,
                    onValueChange = {
                        val updatedNewPasswordField = newPasswordField.copy(
                            value = it,
                            error = null
                        )

                        onEvent(UpdateNewPasswordField(updatedNewPasswordField))
                    }
                )

                SecurePasswordTextField(
                    label = stringResource(R.string.confirm_password),
                    text = confirmNewPasswordField.value,
                    supportingText = confirmNewPasswordField.error?.toStringResource(),
                    isError = confirmNewPasswordField.error != null,
                    onValueChange = {
                        val updatedConfirmNewPasswordField = confirmNewPasswordField.copy(
                            value = it,
                            error = null
                        )

                        onEvent(UpdateConfirmNewPasswordField(updatedConfirmNewPasswordField))
                    }
                )

                TextButton(onClick = { onEvent(SetNewPassword) }) {
                    Text(text = stringResource(R.string.change_password))
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun NewPasswordInputScreenPreview() {
    HomeBankFrontTheme {
        NewPasswordContent(
            newPasswordField = PasswordField(),
            confirmNewPasswordField = PasswordField(),
            onEvent = {}
        )
    }
}