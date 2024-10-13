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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.TransactionRow
import com.example.homebankfront.ui.components.DatePicker
import com.example.homebankfront.ui.components.IntegerTextField
import com.example.homebankfront.ui.components.TextField
import com.example.homebankfront.ui.components.TextFieldWithDropdownMenu

@Composable
fun EditTransactionRowRoute(
    viewModel: EditTransactionRowViewModel = hiltViewModel(), onBackClick: () -> Unit
) {
    val editTransactionRowUiState by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarFlow.collect { event ->
            event.consume()?.let { message ->
                snackbarHostState.showSnackbar(message = message)
            }
        }
    }

    EditTransactionRowScreen(
        editTransactionRowState = editTransactionRowUiState,
        snackbarHostState = snackbarHostState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
fun EditTransactionRowScreen(
    editTransactionRowState: EditTransactionRowState,
    snackbarHostState: SnackbarHostState,
    onEvent: (EditTransactionRowUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    when (editTransactionRowState) {
        is EditTransactionRowState.Loading -> {}
        is EditTransactionRowState.Ready -> {
            EditTransactionRowScreen(
                transactionRow = editTransactionRowState.transactionRow,
                snackbarHostState = snackbarHostState,
                onEvent = onEvent,
                onBackClick = onBackClick
            )
        }

        is EditTransactionRowState.Saved -> {
            onBackClick()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionRowScreen(
    transactionRow: TransactionRow,
    snackbarHostState: SnackbarHostState,
    onEvent: (EditTransactionRowUiEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = when(transactionRow.id) {
                    -1L -> ""
                    else -> stringResource(R.string.edit)
                })
            }, navigationIcon = {
                IconButton(
                    onClick = { onBackClick() },

                    ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = ""
                    )
                }
            }, actions = {
                IconButton(onClick = { onEvent(EditTransactionRowUiEvent.Save) }) {
                    Icon(
                        imageVector = Icons.Filled.Done, contentDescription = ""
                    )
                }
            }, colors = TopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.colorScheme.surface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurface
            )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TextField(
                label = stringResource(R.string.title),
                text = transactionRow.name,
                onValueChange = { changeName(onEvent, it) }
            )

            IntegerTextField (
                label = stringResource(R.string.amount),
                text = transactionRow.amount.toString(),
                onValueChange = { changeAmount(onEvent, it) }
            )

            DatePicker(
                label = stringResource(R.string.date),
                date = transactionRow.paymentDate,
                onDateSelected = { changePaymentDate(onEvent, it) }
            )

            TextFieldWithDropdownMenu(
                label = stringResource(R.string.type),
                text = transactionRow.typeOfTransactionCode?.value
                    ?: "",
                selectedKey = transactionRow.typeOfTransactionCode?.name ?: "",
                menuOptions = TransactionRow.Type.entries.associateBy({ it.name }, { it.value }),
                onClick = { typeOfTransactionCode, _ ->
                    changeTypeOfTransaction(onEvent, typeOfTransactionCode)
                }
            )

            TextField(
                label = stringResource(R.string.description),
                text = transactionRow.description ?: "",
                maxLines = 10,
                onValueChange = { changeDescription(onEvent, it) }
            )
        }
    }
}

