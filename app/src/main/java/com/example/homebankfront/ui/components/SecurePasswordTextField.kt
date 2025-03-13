package com.example.homebankfront.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.homebankfront.R
import com.example.homebankfront.feature.authentication.AuthenticationEvent
import com.example.homebankfront.feature.authentication.AuthenticationEvent.UpdateField
import com.example.homebankfront.feature.authentication.toStringResource

@Composable
fun SecurePasswordTextField(
    modifier: Modifier = Modifier,
    text: String = "",
    supportingText: String? = null,
    isError: Boolean = false,
    showPassword: Boolean = false,
    onToggleVisibility: () -> Unit,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit
) {
    val passwordFieldState = rememberTextFieldState(text)
    LaunchedEffect(passwordFieldState.text) { onValueChange(passwordFieldState.text.toString()) }

    OutlinedSecureTextField(
        label = { Text(stringResource(R.string.password)) },
        state = passwordFieldState,
        supportingText = { supportingText?.let { Text(supportingText) } },
        isError = isError,
        enabled = enabled,
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    painter = painterResource(if (showPassword) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24),
                    contentDescription = stringResource(R.string.toggle_password_visibility)
                )
            }
        },
        textObfuscationMode = if (showPassword) TextObfuscationMode.Visible else TextObfuscationMode.RevealLastTyped,
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
        )
    )
}
