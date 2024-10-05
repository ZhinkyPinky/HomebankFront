package com.example.homebankfront.feature.editTransactionRow

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.DpOffset
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
    viewModel: EditTransactionRowViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val editTransactionRowUiState by viewModel.editTransactionRowUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        if (editTransactionRowUiState !is EditTransactionRowUiState.Saved) {
            viewModel.getCustomersAndTransactionRow()
        }
    }

    EditTransactionRowScreen(
        editTransactionRowUiState = editTransactionRowUiState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick
    )
}

@Composable
fun EditTransactionRowScreen(
    editTransactionRowUiState: EditTransactionRowUiState,
    onEvent: (EditTransactionRowEvent) -> Unit,
    onBackClick: () -> Unit
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

        is EditTransactionRowUiState.Saved -> {
            onBackClick()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionRowScreen(
    transactionRow: TransactionRow,
    onEvent: (EditTransactionRowEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = "Redigera")
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
                IconButton(onClick = { onEvent(EditTransactionRowEvent.Save(transactionRow)) }) {
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
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            TextField(label = "Titel", text = transactionRow.name, onValueChange = {
                onEvent(
                    EditTransactionRowEvent.Update(
                        transactionRow = transactionRow.copy(
                            name = it
                        )
                    )
                )
            })

            TextField(label = "Belopp", text = transactionRow.amount.toString(), onValueChange = {
                onEvent(
                    EditTransactionRowEvent.Update(
                        transactionRow = transactionRow.copy(
                            amount = it.toInt()
                        )
                    )
                )
            })

            DatePicker(
                label = "Datum",
                date = transactionRow.paymentDate,
                onDateSelected = {
                    it?.let {
                        onEvent(
                            EditTransactionRowEvent.Update(
                                transactionRow = transactionRow.copy(
                                    paymentDate = Instant.ofEpochMilli(it)
                                        .atZone(ZoneId.systemDefault()).toLocalDate()
                                )
                            )
                        )

                    }
                },
            )

            TextField(
                label = "Beskrivning",
                text = transactionRow.description ?: "",
                onValueChange = {
                    onEvent(
                        EditTransactionRowEvent.Update(
                            transactionRow = transactionRow.copy(
                                description = it
                            )
                        )
                    )
                })

            Box {
                var transactionTypeDropDownExpanded by rememberSaveable { mutableStateOf(false) }

                DropdownMenu(
                    expanded = transactionTypeDropDownExpanded,
                    offset = DpOffset(x = 5.dp, y = 0.dp),
                    onDismissRequest = { transactionTypeDropDownExpanded = false },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    TransactionRow.Type.entries.forEach { transactionType ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = transactionType.value,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            },

                            leadingIcon = {
                                if (transactionRow.typeOfTransactionCode == transactionType.name) {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            },

                            onClick = {
                                onEvent(
                                    EditTransactionRowEvent.Update(
                                        transactionRow = transactionRow.copy(
                                            typeOfTransaction = transactionType.value,
                                            typeOfTransactionCode = transactionType.name
                                        )
                                    )
                                )

                                transactionTypeDropDownExpanded = false
                            }
                        )
                    }
                }

                TextField(
                    label = "Typ",
                    text = transactionRow.typeOfTransaction ?: "",
                    isSelected = transactionTypeDropDownExpanded,
                    enabled = false,
                    onValueChange = { },
                    modifier = Modifier.clickable {
                        transactionTypeDropDownExpanded = !transactionTypeDropDownExpanded
                    }
                )
            }
        }
    }
}
