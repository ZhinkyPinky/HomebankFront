package com.example.homebankfront.designsystem

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    isSelected: Boolean = false,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        enabled = enabled,
        label = { Text(text = label) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledTextColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
            errorTextColor = MaterialTheme.colorScheme.onErrorContainer,

            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.primaryContainer,
            errorContainerColor = MaterialTheme.colorScheme.errorContainer,

            cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
            errorCursorColor = MaterialTheme.colorScheme.onErrorContainer,

            focusedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledBorderColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
            errorBorderColor = MaterialTheme.colorScheme.onErrorContainer,

            focusedLeadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledLeadingIconColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
            errorLeadingIconColor = MaterialTheme.colorScheme.onErrorContainer,

            focusedTrailingIconColor =  MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledTrailingIconColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
            errorTrailingIconColor = MaterialTheme.colorScheme.onErrorContainer,

            focusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledLabelColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onPrimaryContainer,
            errorLabelColor = MaterialTheme.colorScheme.onErrorContainer,

            focusedPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
            errorPlaceholderColor = MaterialTheme.colorScheme.onErrorContainer,


        ),
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(6.dp)
        )
    )
}