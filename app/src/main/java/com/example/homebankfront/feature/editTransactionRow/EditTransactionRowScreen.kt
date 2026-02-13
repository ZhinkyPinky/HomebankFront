package com.example.homebankfront.feature.editTransactionRow

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.LocalSnackHostState
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.TransactionRow
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.AmountField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.DescriptionField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.NameField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.PaymentDateField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowField.TypeOfTransactionField
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowState.*
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowUiEvent.Save
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowUiEvent.UpdateField
import com.example.homebankfront.feature.utility.Either.Left
import com.example.homebankfront.feature.utility.Either.Right
import com.example.homebankfront.ui.components.DatePicker
import com.example.homebankfront.ui.components.IntegerTextField
import com.example.homebankfront.ui.components.LoadingOverlay
import com.example.homebankfront.ui.components.TextField
import com.example.homebankfront.ui.components.TextFieldWithDropdownMenu
import java.time.Instant
import java.time.ZoneId

@Composable
fun EditTransactionRowScreen(
    viewModel: EditTransactionRowViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
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
        is Loading -> LoadingOverlay()
        is Input -> {
            val inputState = state as Input
            EditTransactionRowScreenContent(
                transactionRowId = inputState.transactionRowId,
                nameField = inputState.nameField,
                amountField = inputState.amountField,
                paymentDateField = inputState.paymentDateField,
                typeOfTransactionField = inputState.typeOfTransactionField,
                descriptionField = inputState.descriptionField,
                snackHostState = snackHostState,
                onEvent = viewModel::onEvent,
                onBackClick = onBackClick
            )
        }

        is Saved -> onBackClick()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionRowScreenContent(
    transactionRowId: Long,
    nameField: NameField,
    amountField: AmountField,
    paymentDateField: PaymentDateField,
    typeOfTransactionField: TypeOfTransactionField,
    descriptionField: DescriptionField,
    snackHostState: SnackbarHostState,
    onEvent: (EditTransactionRowUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (transactionRowId) {
                            -1L -> ""
                            else -> stringResource(R.string.edit)
                        }
                    )
                }, navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                }, actions = {
                    IconButton(onClick = { onEvent(Save) }) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = stringResource(R.string.save)
                        )
                    }
                }, colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface,
                    subtitleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        snackbarHost = { SnackbarHost(snackHostState) }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TextField(
                label = stringResource(R.string.name),
                text = nameField.name,
                supportingText = nameField.error?.toStringResource(),
                isError = nameField.error != null,
                onValueChange = { onEvent(UpdateField(nameField.copy(name = it, error = null))) }
            )

            IntegerTextField(
                label = stringResource(R.string.amount),
                text = amountField.amount.toString(),
                onValueChange = {
                    onEvent(
                        UpdateField(
                            amountField.copy(
                                amount = it.toIntOrNull() ?: 0
                            )
                        )
                    )
                }
            )

            DatePicker(
                label = stringResource(R.string.date),
                date = paymentDateField.paymentDate,
                onDateSelected = {
                    it?.let {
                        onEvent(
                            UpdateField(
                                paymentDateField.copy(
                                    paymentDate = Instant.ofEpochMilli(it)
                                        .atZone(ZoneId.systemDefault()).toLocalDate()
                                )
                            )
                        )
                    }
                }
            )

            TextFieldWithDropdownMenu(
                label = stringResource(R.string.type),
                text = typeOfTransactionField.typeOfTransactionCode.value,
                selectedKey = typeOfTransactionField.typeOfTransactionCode.name,
                menuOptions = TransactionRow.Type.entries.associateBy({ it.name }, { it.value }),
                onClick = { typeOfTransactionCode, _ ->
                    onEvent(
                        UpdateField(
                            typeOfTransactionField.copy(
                                typeOfTransactionCode = TransactionRow.Type.valueOf(
                                    typeOfTransactionCode
                                )
                            )
                        )
                    )
                }
            )

            TextField(
                label = stringResource(R.string.description),
                text = descriptionField.description ?: "",
                maxLines = 10,
                onValueChange = { onEvent(UpdateField(descriptionField.copy(description = it))) }
            )
        }
    }
}

