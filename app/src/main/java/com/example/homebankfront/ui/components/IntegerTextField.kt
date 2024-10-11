package com.example.homebankfront.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun IntegerTextField(
    modifier: Modifier = Modifier,
    label: String,
    text: String,
    maxLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
    onValueChange: (String) -> Unit,
) {
    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = text,
                selection = TextRange(text.length)
            )
        )
    }

    OutlinedTextField(
        value = textFieldValue,
        onValueChange = {
            val newText = it.text
            val bigIntegerValue = newText.toBigIntegerOrNull()

            textFieldValue = when {
                newText.isBlank() -> {
                    it.copy(text = "0", selection = TextRange(1))
                }

                newText.last().digitToIntOrNull() == null -> {
                    textFieldValue
                }

                bigIntegerValue == null -> {
                    it.copy(text = "0", selection = TextRange(1))
                }

                bigIntegerValue <= Int.MAX_VALUE.toBigInteger() -> {
                    val intValue = newText.toIntOrNull() ?: 0
                    it.copy(text = intValue.toString(), selection = TextRange(it.selection.end))
                }

                else -> {
                    textFieldValue
                }
            }

            onValueChange(textFieldValue.text)
        },
        label = { Text(text = label) },
        singleLine = maxLines == 1,
        maxLines = maxLines,
        keyboardOptions = keyboardOptions,
        modifier = modifier.then(
            Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 6.dp, bottom = 6.dp)
        )
    )
}