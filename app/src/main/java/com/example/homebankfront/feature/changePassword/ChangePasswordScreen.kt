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
import com.example.homebankfront.feature.changePassword.ChangePasswordState.*
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

    when (state) {
        PasswordChanged -> {
            //TODO: Figure out what to do.
            onChangedPassword()
        }

        is Input -> {
            val inputState = state as Input
            ChangePasswordScreenContent(
                oldPasswordField = inputState.oldPasswordField,
                newPasswordField = inputState.newPasswordField,
                confirmNewPasswordField = inputState.confirmNewPasswordField,
                onEvent = viewModel::onEvent
            )

            LoadingOverlay(isLoading = inputState.isLoading)
        }
    }
}

@Composable
fun ChangePasswordScreenContent(
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
