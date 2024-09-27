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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.dataAccess.bodies.TransactionRow
import com.example.homebankfront.designsystem.DatePicker
import com.example.homebankfront.designsystem.TextField
import java.time.Instant
import java.time.ZoneId

@Composable
fun EditTransactionRowRoute(
    viewModel : EditTransactionRowViewModel = hiltViewModel(),
    onBackClick : () -> Unit
) {
    val editTransactionRowUiState by viewModel.editTransactionRowUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (editTransactionRowUiState !is EditTransactionRowUiState.Saved) {
            viewModel.getTransactionRow()
        }
    }

    EditTransactionRowScreen(
        editTransactionRowUiState = editTransactionRowUiState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable fun EditTransactionRowScreen(
    editTransactionRowUiState : EditTransactionRowUiState,
    onEvent : (EditTransactionRowEvent) -> Unit,
    onBackClick : () -> Unit
) {
    when (editTransactionRowUiState) {
        is EditTransactionRowUiState.Loading -> {}
        is EditTransactionRowUiState.Ready -> {
            EditTransactionRowScreen(
                transactionRow = editTransactionRowUiState.transactionRow,
                onEvent = onEvent,
                onBackClick = onBackClick
            )
        }

        is EditTransactionRowUiState.Saved -> {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun EditTransactionRowScreen(
    transactionRow : TransactionRow,
    onEvent : (EditTransactionRowEvent) -> Unit,
    onBackClick : () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Redigera")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClick() },

                        ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(EditTransactionRowEvent.Save(transactionRow)) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = ""
                        )
                    }
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            TextField(
                label = "Titel",
                text = transactionRow.transactionName,
                onValueChange = { onEvent(EditTransactionRowEvent.Update(transactionRow = transactionRow.copy(transactionName = it))) }
            )

            TextField(
                label = "Belopp",
                text = transactionRow.amount.toString(),
                onValueChange = { onEvent(EditTransactionRowEvent.Update(transactionRow = transactionRow.copy(amount = it.toInt()))) }
            )

            DatePicker(
                label = "Datum",
                date = transactionRow.paymentDate,
                onDateSelected = {
                    it?.let {
                        onEvent(
                            EditTransactionRowEvent.Update(
                                transactionRow = transactionRow.copy(
                                    paymentDate = Instant
                                        .ofEpochMilli(it)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                )
                            )
                        )

                    }
                },
            )

            TextField(
                label = "Beskrivning",
                text = transactionRow.description,
                onValueChange = { onEvent(EditTransactionRowEvent.Update(transactionRow = transactionRow.copy(description = it))) }
            )
        }
    }
}
