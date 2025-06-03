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
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputState
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.NewPasswordInputViewModel
import com.example.homebankfront.feature.accountrecovery.newpasswordinput.PasswordField
import com.example.homebankfront.feature.utility.Either.*
import com.example.homebankfront.ui.components.LoadingOverlay
import com.example.homebankfront.ui.components.SecurePasswordTextField

@Composable
fun NewPasswordInputScreen(
    viewModel: NewPasswordInputViewModel = hiltViewModel(),
    onChangedPassword: () -> Unit
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


    ChangePasswordScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onChangedPassword = onChangedPassword
    )
}

@Composable
fun ChangePasswordScreen(
    state: NewPasswordInputState,
    onEvent: (NewPasswordInputEvent) -> Unit,
    onChangedPassword: () -> Unit
) {
    when (state) {
       NewPasswordInputState.Changed  -> {
            //TODO: Figure out what to do.
            onChangedPassword()
        }

        is NewPasswordInputState.Input -> {
            ChangePasswordScreen(
                newPasswordField = state.newPasswordField,
                confirmNewPasswordField = state.confirmNewPasswordField,
                onEvent = onEvent
            )

            LoadingOverlay(isLoading = state.isLoading)
        }

        else -> {}
    }
}

@Composable
fun ChangePasswordScreen(
    newPasswordField: PasswordField,
    confirmNewPasswordField: PasswordField,
    onEvent: (NewPasswordInputEvent) -> Unit,
) {
    Scaffold { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {
                SecurePasswordTextField(
                    label = stringResource(R.string.old_password),
                    text = newPasswordField.value,
                    supportingText = newPasswordField.error?.toStringResource(),
                    isError = newPasswordField.error != null,
                    onValueChange = {
                        onEvent(
                            NewPasswordInputEvent.UpdateNewPasswordField(
                                newPasswordField.copy(
                                    value = it,
                                    error = null
                                )
                            )
                        )
                    }
                )

                SecurePasswordTextField(
                    label = stringResource(R.string.new_password),
                    text = confirmNewPasswordField.value,
                    supportingText = confirmNewPasswordField.error?.toStringResource(),
                    isError = confirmNewPasswordField.error != null,
                    onValueChange = {
                        onEvent(
                            NewPasswordInputEvent.UpdateConfirmNewPasswordField(
                                newPasswordField.copy(
                                    value = it,
                                    error = null
                                )
                            )
                        )
                    }
                )

                TextButton(onClick = { onEvent(NewPasswordInputEvent.Authenticate) }) {
                    Text(text = stringResource(R.string.change_password))
                }
            }
        }
    }
}
