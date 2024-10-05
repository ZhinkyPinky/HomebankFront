package com.example.homebankfront.designsystem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.homebankfront.ui.theme.HomeBankFrontTheme
import com.example.homebankfront.ui.theme.ThemePreviews
import java.time.LocalDate

@Composable
fun DatePicker(
    label: String,
    date: LocalDate?,
    onDateSelected: (Long?) -> Unit
) {
    val showDialog = rememberSaveable { mutableStateOf(false) }

    if (showDialog.value) {
        DatePickerDialog(
            onDateSelected = {
                onDateSelected(it)
                showDialog.value = false
            },
            onDismiss = { showDialog.value = false }
        )
    }

    OutlinedTextField(
        value = date?.toString() ?: "",
        onValueChange = { },
        readOnly = true,
        enabled = false,
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
            unfocusedBorderColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledBorderColor = MaterialTheme.colorScheme.background,
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
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                showDialog.value = true
            }
            .padding(6.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()


    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                }
            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text(text = "Avbryt")
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.primary,

        )
    ) {
        DatePicker(
            state = datePickerState,
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,

                weekdayContentColor = MaterialTheme.colorScheme.onPrimary,

                subheadContentColor = MaterialTheme.colorScheme.onPrimary,
                navigationContentColor = MaterialTheme.colorScheme.onPrimary,
                headlineContentColor = MaterialTheme.colorScheme.onPrimary,

                yearContentColor = MaterialTheme.colorScheme.onSecondary,
                currentYearContentColor = MaterialTheme.colorScheme.onSecondary,
                selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,

                dayContentColor = MaterialTheme.colorScheme.onSecondary,
                selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,


                dateTextFieldColors = TextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    disabledTextColor = MaterialTheme.colorScheme.onSecondary,
                    errorTextColor = Color.Red,

                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                    errorContainerColor = MaterialTheme.colorScheme.primary,

                    cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    errorCursorColor = Color.Red,

                    focusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unfocusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    errorLabelColor = Color.Red,

                    focusedPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer,

                    focusedTrailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSecondaryContainer,


                    )
            )
        )
    }
}

@ThemePreviews
@Composable
fun DatePickerDialogPreview(){

    HomeBankFrontTheme {
        DatePickerDialog(
            onDateSelected = {_ -> },
            onDismiss = {}
        )
    }
}