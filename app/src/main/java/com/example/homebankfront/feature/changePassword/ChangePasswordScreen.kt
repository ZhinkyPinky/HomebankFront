package com.example.homebankfront.feature.changePassword

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
import com.example.homebankfront.feature.changePassword.ChangePasswordEvent.ChangePassword
import com.example.homebankfront.feature.changePassword.ChangePasswordEvent.UpdateConfirmNewPassword
import com.example.homebankfront.feature.changePassword.ChangePasswordEvent.UpdateNewPassword
import com.example.homebankfront.feature.changePassword.ChangePasswordEvent.UpdateOldPassword
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.ui.components.LoadingOverlay
import com.example.homebankfront.ui.components.SecurePasswordTextField

@Composable
fun ChangePasswordScreen(
    viewModel: ChangePasswordViewModel = hiltViewModel(),
    onChangedPassword: () -> Unit
) {
    val state: ChangePasswordState by viewModel.state.collectAsStateWithLifecycle()
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
                    showPassword = oldPasswordField.showPassword,
                    onToggleVisibility = {
                        onEvent(UpdateOldPassword(oldPasswordField.toggleVisibility()))
                    },
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
                    showPassword = newPasswordField.showPassword,
                    onToggleVisibility = {
                        onEvent(UpdateNewPassword(newPasswordField.toggleVisibility()))
                    },
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
                    showPassword = confirmNewPasswordField.showPassword,
                    onToggleVisibility = {
                        onEvent(UpdateConfirmNewPassword(confirmNewPasswordField.toggleVisibility()))
                    },
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
