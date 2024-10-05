package com.example.homebankfront.feature.editTransactionHead

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
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
    viewModel: EditTransactionHeadViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val editTransactionHeadUiState: EditTransactionHeadUiState by viewModel.editTransactionHeadUiState.collectAsStateWithLifecycle()

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
    editTransactionHeadUiState: EditTransactionHeadUiState,
    onEvent: (EditTransactionHeadEvent) -> Unit,
    onBackClick: () -> Unit
) {
    when (editTransactionHeadUiState) {
        is EditTransactionHeadUiState.Loading -> {}
        is EditTransactionHeadUiState.Ready -> {
            EditTransactionHeadScreen(
                transactionHead = editTransactionHeadUiState.transactionHead,
                customers = editTransactionHeadUiState.customers,
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
    transactionHead: TransactionHead,
    customers: List<Customer>,
    onEvent: (EditTransactionHeadEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (transactionHead.id == -1L) "Lägg till" else "Redigera")
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
                        onClick = {
                            onEvent(
                                EditTransactionHeadEvent.SaveTransactionHead(
                                    transactionHead
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = ""
                        )
                    }
                },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TextField(
                label = "Titel",
                text = transactionHead.transactionName ?: "",
                onValueChange = {
                    onEvent(
                        EditTransactionHeadEvent.UpdateTransactionHead(
                            transactionHead = transactionHead.copy(transactionName = it)
                        )
                    )
                }
            )

            Box {
                var lenderChoiceDropdownMenuExpanded by rememberSaveable { mutableStateOf(false) }

                DropdownMenu(
                    expanded = lenderChoiceDropdownMenuExpanded,
                    offset = DpOffset(x = 5.dp, y = 0.dp),
                    onDismissRequest = { lenderChoiceDropdownMenuExpanded = false },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    customers.forEach { customer ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = customer.name,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            },
                            leadingIcon = {
                                if (transactionHead.lenderId == customer.id) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = ""
                                    )
                                }
                            },
                            onClick = {
                                onEvent(
                                    EditTransactionHeadEvent.UpdateTransactionHead(
                                        transactionHead = transactionHead.copy(
                                            lender = customer.name,
                                            lenderId = customer.id
                                        )
                                    )
                                )

                                lenderChoiceDropdownMenuExpanded = false
                            }
                        )
                    }
                }

                TextField(
                    label = "Långivare",
                    text = transactionHead.lender ?: "",
                    isSelected = lenderChoiceDropdownMenuExpanded,
                    enabled = false,
                    onValueChange = { },
                    modifier = Modifier.clickable {
                        lenderChoiceDropdownMenuExpanded = !lenderChoiceDropdownMenuExpanded
                    }
                )
            }

            Box {
                var borrowerChoiceDropdownMenuExpanded by rememberSaveable { mutableStateOf(false) }

                DropdownMenu(
                    expanded = borrowerChoiceDropdownMenuExpanded,
                    offset = DpOffset(x = 5.dp, y = 0.dp),
                    onDismissRequest = { borrowerChoiceDropdownMenuExpanded = false },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                ) {
                    customers.forEach { customer ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = customer.name,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            },
                            leadingIcon = {
                                if (transactionHead.borrowerId == customer.id) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = ""
                                    )
                                }
                            },
                            onClick = {
                                onEvent(
                                    EditTransactionHeadEvent.UpdateTransactionHead(
                                        transactionHead = transactionHead.copy(
                                            borrower = customer.name,
                                            borrowerId = customer.id
                                        )
                                    )
                                )

                                borrowerChoiceDropdownMenuExpanded = false
                            }
                        )
                    }
                }

                TextField(
                    label = "Låntagare",
                    text = transactionHead.borrower ?: "",
                    isSelected = borrowerChoiceDropdownMenuExpanded,
                    enabled = false,
                    onValueChange = { },
                    modifier = Modifier.clickable {
                        borrowerChoiceDropdownMenuExpanded = !borrowerChoiceDropdownMenuExpanded
                    }
                )
            }

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
                text = transactionHead.description ?: "",
                onValueChange = {
                    onEvent(
                        EditTransactionHeadEvent.UpdateTransactionHead(
                            transactionHead = transactionHead.copy(description = it)
                        )
                    )
                }
            )
        }
    }
}