package com.example.homebankfront.designsystem

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = text,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
        )
    )
}