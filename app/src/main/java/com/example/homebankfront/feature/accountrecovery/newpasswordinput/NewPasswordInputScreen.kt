import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.LocalSnackHostState
import com.example.homebankfront.feature.utility.Either.*

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
    state: ChangePasswordState,
    onEvent: (ChangePasswordEvent) -> Unit,
    onChangedPassword: () -> Unit
) {
    when (state) {
        ChangePasswordState.PasswordChanged -> {
            //TODO: Figure out what to do.
            onChangedPassword()
        }

        is ChangePasswordState.Ready -> {
            ChangePasswordScreen(
                oldPasswordField = state.oldPasswordField,
                newPasswordField = state.newPasswordField,
                confirmNewPasswordField = state.confirmNewPasswordField,
                onEvent = onEvent
            )

            LoadingOverlay(isLoading = state.isLoading)
        }
    }
}

@Composable
fun ChangePasswordScreen(
    oldPasswordField: ChangePasswordField.PasswordField,
    newPasswordField: ChangePasswordField.PasswordField,
    confirmNewPasswordField: ChangePasswordField.PasswordField,
    onEvent: (ChangePasswordEvent) -> Unit,
) {
    Scaffold { paddingValues ->
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.padding(paddingValues)) {
                SecurePasswordTextField(
                    label = stringResource(R.string.old_password),
                    text = oldPasswordField.password,
                    supportingText = oldPasswordField.error?.toStringResource(),
                    isError = oldPasswordField.error != null,
                    onValueChange = {
                        onEvent(
                            UpdateOldPassword(
                                oldPasswordField.copy(
                                    password = it,
                                    error = null
                                )
                            )
                        )
                    }
                )

                SecurePasswordTextField(
                    label = stringResource(R.string.new_password),
                    text = newPasswordField.password,
                    supportingText = newPasswordField.error?.toStringResource(),
                    isError = newPasswordField.error != null,
                    onValueChange = {
                        onEvent(
                            UpdateNewPassword(
                                newPasswordField.copy(
                                    password = it,
                                    error = null
                                )
                            )
                        )
                    }
                )

                SecurePasswordTextField(
                    label = stringResource(R.string.confirm_password),
                    text = confirmNewPasswordField.password,
                    supportingText = confirmNewPasswordField.error?.toStringResource(),
                    isError = confirmNewPasswordField.error != null,
                    onValueChange = {
                        onEvent(
                            UpdateConfirmNewPassword(
                                confirmNewPasswordField.copy(
                                    password = it,
                                    error = null
                                )
                            )
                        )
                    }
                )

                TextButton(onClick = { onEvent(ChangePassword) }) {
                    Text(text = stringResource(R.string.change_password))
                }
            }
        }
    }
}
