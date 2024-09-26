package com.example.homebankfront.feature.editTransactionHead

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.designsystem.DatePicker
import com.example.homebankfront.designsystem.TextField
import java.time.Instant
import java.time.ZoneId

@Composable
fun EditTransactionHeadRoute(
    viewModel : EditTransactionHeadViewModel = hiltViewModel(),
    onBackClick : () -> Unit
) {
    val editTransactionHeadUiState : EditTransactionHeadUiState by viewModel.editTransactionHeadUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (editTransactionHeadUiState !is EditTransactionHeadUiState.Saved) {
            viewModel.getCustomerAndTransactionHead()
        }
    }

    EditTransactionHeadScreen(
        editTransactionHeadUiState = editTransactionHeadUiState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
fun EditTransactionHeadScreen(
    editTransactionHeadUiState : EditTransactionHeadUiState,
    onEvent : (EditTransactionHeadEvent) -> Unit,
    onBackClick : () -> Unit
) {
    when (editTransactionHeadUiState) {
        is EditTransactionHeadUiState.Loading -> {}
        is EditTransactionHeadUiState.Ready -> {
            EditTransactionHeadScreen(
                customer = editTransactionHeadUiState.customer,
                transactionHead = editTransactionHeadUiState.transactionHead,
                onEvent = onEvent,
                onBackClick = onBackClick
            )
        }

        is EditTransactionHeadUiState.Saved -> {
            onBackClick()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionHeadScreen(
    customer : Customer,
    transactionHead : TransactionHead,
    onEvent : (EditTransactionHeadEvent) -> Unit,
    onBackClick : () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = customer.name)
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
                        onClick = { onEvent(EditTransactionHeadEvent.SaveTransactionHead(transactionHead)) }
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
                text = transactionHead.transactionName,
                onValueChange = { onEvent(EditTransactionHeadEvent.UpdateTransactionHead(transactionHead = transactionHead.copy(transactionName = it))) }
            )

            TextField(
                label = "Långivare",
                text = transactionHead.lender,
                onValueChange = { onEvent(EditTransactionHeadEvent.UpdateTransactionHead(transactionHead = transactionHead.copy(borrower = it))) }
            )

            TextField(
                label = "Låntagare",
                text = transactionHead.borrower,
                onValueChange = { onEvent(EditTransactionHeadEvent.UpdateTransactionHead(transactionHead = transactionHead.copy(lender = it))) }
            )

            TextField(
                label = "Saldo",
                text = "456",
                onValueChange = {}
            )

            DatePicker(
                label = "Startdatum",
                date = transactionHead.startDate,
                onDateSelected = {
                    it?.let {
                        onEvent(
                            EditTransactionHeadEvent.UpdateTransactionHead(
                                transactionHead = transactionHead.copy(
                                    startDate = Instant
                                        .ofEpochMilli(it)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                )
                            )
                        )

                    }
                },
            )

            DatePicker(
                label = "Prel. Slutdatum",
                date = transactionHead.prelEndDate,
                onDateSelected = {
                    it?.let {
                        onEvent(
                            EditTransactionHeadEvent.UpdateTransactionHead(
                                transactionHead = transactionHead.copy(
                                    prelEndDate = Instant
                                        .ofEpochMilli(it)
                                        .atZone(ZoneId.systemDefault())
                                        .toLocalDate()
                                )
                            )
                        )
                    }
                },
            )

            DatePicker(
                label = "Slutdatum",
                date = transactionHead.endDate,
                onDateSelected = {
                    it?.let {
                        onEvent(
                            EditTransactionHeadEvent.UpdateTransactionHead(
                                transactionHead = transactionHead.copy(
                                    endDate = Instant
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
                text = transactionHead.description,
                onValueChange = { onEvent(EditTransactionHeadEvent.UpdateTransactionHead(transactionHead = transactionHead.copy(description = it))) }
            )
        }
    }
}