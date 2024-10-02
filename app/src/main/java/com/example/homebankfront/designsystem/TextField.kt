package com.example.homebankfront.designsystem

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
    Log.d(label, isSelected.toString())

    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        enabled = enabled,
        label = { Text(text = label) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            errorTextColor = Color.Red,

            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            errorContainerColor = MaterialTheme.colorScheme.primaryContainer,

            cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
            errorCursorColor = Color.Red,

            focusedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedBorderColor = MaterialTheme.colorScheme.background,
            disabledBorderColor = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.background,
            errorBorderColor = Color.Red,

            focusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
            errorLabelColor = Color.Red,

            focusedPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,

            focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(6.dp)
        )
    )
}