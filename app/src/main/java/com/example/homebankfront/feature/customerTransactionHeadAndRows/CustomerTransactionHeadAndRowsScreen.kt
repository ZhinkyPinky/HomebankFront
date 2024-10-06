package com.example.homebankfront.feature.customerTransactionHeadAndRows

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.dataAccess.bodies.TransactionRow
import com.example.homebankfront.designsystem.TextWithLabel
import com.example.homebankfront.ui.theme.HomeBankFrontTheme
import com.example.homebankfront.ui.theme.ThemePreviews
import java.time.LocalDate

@Composable
internal fun CustomerTransactionHeadAndRowsRoute(
    viewModel: CustomerTransactionHeadAndRowsViewModel = hiltViewModel(),
    onEditTransactionHeadClick: (Long, Long) -> Unit,
    onEditTransactionRowClick: (Long, Long) -> Unit,
    onBackClick: () -> Unit
) {
    val customerAndTransactionHeadAndRowsUiState: CustomerTransactionHeadAndRowsUiState by viewModel.customerTransactionHeadAndRowsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCustomerTransactionHeadAndRows()
    }

    CustomerTransactionHeadAndRowsScreen(
        customerTransactionHeadAndRowsUiState = customerAndTransactionHeadAndRowsUiState,
        onEditTransactionClick = onEditTransactionHeadClick,
        onEditTransactionRowClick = onEditTransactionRowClick,
        onBackClick = onBackClick,
        onEvent = viewModel::onEvent
    )
}


@Composable
fun CustomerTransactionHeadAndRowsScreen(
    customerTransactionHeadAndRowsUiState: CustomerTransactionHeadAndRowsUiState,
    onEditTransactionClick: (Long, Long) -> Unit,
    onEditTransactionRowClick: (Long, Long) -> Unit,
    onBackClick: () -> Unit,
    onEvent: (TransactionHeadAndRowsEvent) -> Unit
) {
    when (customerTransactionHeadAndRowsUiState) {
        is CustomerTransactionHeadAndRowsUiState.Loading -> {}
        is CustomerTransactionHeadAndRowsUiState.Ready -> {
            CustomerTransactionHeadAndRowsScreen(
                customer = customerTransactionHeadAndRowsUiState.customer,
                transactionHead = customerTransactionHeadAndRowsUiState.transactionHead,
                transactionRows = customerTransactionHeadAndRowsUiState.transactionRows,
                onEditTransactionHeadClick = onEditTransactionClick,
                onEditTransactionRowClick = onEditTransactionRowClick,
                onBackClick = onBackClick,
                onEvent = onEvent,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerTransactionHeadAndRowsScreen(
    customer: Customer,
    transactionHead: TransactionHead,
    transactionRows: List<TransactionRow>,
    onEditTransactionHeadClick: (Long, Long) -> Unit,
    onEditTransactionRowClick: (Long, Long) -> Unit,
    onBackClick: () -> Unit,
    onEvent: (TransactionHeadAndRowsEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = customer.name) },
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
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(bottom = 1.dp)
            )
        },
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            modifier = Modifier.padding(paddingValues)
        ) {
            TransactionHeadInfo(
                customer = customer,
                transactionHead = transactionHead,
                onEditTransactionHeadClick = onEditTransactionHeadClick
            )

            TransactionRowList(
                transactionHead = transactionHead,
                transactionRows = transactionRows,
                onEditTransactionRowClick = onEditTransactionRowClick,
                onEvent = onEvent
            )
        }
    }
}

@Composable
fun TransactionHeadInfo(
    customer: Customer,
    transactionHead: TransactionHead,
    onEditTransactionHeadClick: (Long, Long) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(start = 6.dp)
    ) {
        Text(text = "Transaktion")

        IconButton(onClick = {
            onEditTransactionHeadClick(
                customer.id,
                transactionHead.id
            )
        }) {
            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "",
                modifier = Modifier.size(16.dp)
            )
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Row {
            Column(modifier = Modifier.weight(1f)) {
                TextWithLabel(
                    label = "Titel",
                    text = transactionHead.transactionName ?: "",
                    modifier = Modifier.padding(
                        top = 6.dp,
                        bottom = 3.dp,
                        start = 6.dp,
                    )
                )

                Row {
                    TextWithLabel(
                        label = "Långivare",
                        text = transactionHead.lender ?: "",
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                top = 3.dp,
                                bottom = 3.dp,
                                start = 6.dp,
                            )
                    )

                    TextWithLabel(
                        label = "Låntagare",
                        text = transactionHead.borrower ?: "",
                        modifier = Modifier
                            .weight(1f)
                            .padding(
                                top = 3.dp,
                                bottom = 3.dp,
                            )
                    )
                }

                TextWithLabel(
                    label = "Saldo",
                    text = "2556",
                    modifier = Modifier.padding(
                        top = 3.dp,
                        bottom = 3.dp,
                        start = 6.dp,
                    )
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                TextWithLabel(
                    label = "Startdatum",
                    text = transactionHead.startDate.toString(),
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(
                        top = 6.dp,
                        bottom = 3.dp,
                        end = 6.dp
                    )
                )

                TextWithLabel(
                    label = "Prel. Slutdatum",
                    text = transactionHead.prelEndDate.toString(),
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(
                        top = 3.dp,
                        bottom = 3.dp,
                        end = 6.dp
                    )
                )

                TextWithLabel(
                    label = "Slutdatum",
                    text = transactionHead.endDate.toString(),
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(
                        top = 3.dp,
                        bottom = 3.dp,
                        end = 6.dp
                    )
                )
            }
        }

        TextWithLabel(
            label = "Beskrivning",
            text = transactionHead.description ?: "",
            textSoftWrap = true,
            modifier = Modifier.padding(
                top = 3.dp,
                bottom = 6.dp,
                start = 6.dp,
                end = 6.dp
            )
        )
    }
}

@Composable
fun TransactionRowList(
    transactionHead: TransactionHead,
    transactionRows: List<TransactionRow>,
    onEditTransactionRowClick: (Long, Long) -> Unit,
    onEvent: (TransactionHeadAndRowsEvent) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxWidth()
            .padding(start = 6.dp)
    ) {
        Text(text = "Rader")

        IconButton(onClick = {
            onEditTransactionRowClick(transactionHead.id, TransactionRow().id)
        }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "",
            )

        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(1.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        itemsIndexed(
            items = transactionRows,
            key = { _, transactionRow -> transactionRow.id }
        ) { _, transactionRow ->
            var expanded by rememberSaveable { mutableStateOf(false) }

            TransactionRowListItem(
                transactionHead = transactionHead,
                transactionRow = transactionRow,
                expanded = expanded,
                toggleExpanded = { expanded = !expanded },
                onEditTransactionRowClick = onEditTransactionRowClick,
                onEvent = onEvent
            )
        }
    }
}


@Composable
fun TransactionRowListItem(
    transactionHead: TransactionHead,
    transactionRow: TransactionRow,
    expanded: Boolean = false,
    toggleExpanded: () -> Unit,
    onEditTransactionRowClick: (Long, Long) -> Unit,
    onEvent: (TransactionHeadAndRowsEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .clickable { toggleExpanded() }
    ) {
        Text(
            text = "${transactionRow.transactionRowNo}",
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .align(Alignment.Top)
                .padding(12.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row {
                Column(
                    modifier = Modifier
                        .weight(2f)
                ) {
                    TextWithLabel(
                        label = "Titel",
                        text = transactionRow.name,
                        modifier = Modifier.padding(
                            top = 6.dp,
                            bottom = 3.dp,
                            start = 0.dp,
                            end = 0.dp
                        )
                    )

                    TextWithLabel(
                        label = "Typ",
                        text = transactionRow.typeOfTransactionCode?.let {
                            TransactionRow.Type.valueOf(
                                it
                            ).value
                        } ?: "",
                        modifier = Modifier.padding(
                            top = 3.dp,
                            bottom = 6.dp,
                            start = 0.dp,
                            end = 0.dp
                        )
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                ) {
                    TextWithLabel(
                        label = "Datum",
                        text = "${transactionRow.paymentDate}",
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(
                            top = 6.dp,
                            bottom = 3.dp,
                            start = 0.dp,
                            end = 6.dp
                        )
                    )

                    TextWithLabel(
                        label = "Belopp",
                        text = "${transactionRow.amount}",
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(
                            top = 3.dp,
                            bottom = 6.dp,
                            start = 0.dp,
                            end = 6.dp
                        )
                    )
                }


            }

            if (expanded) {
                TextWithLabel(
                    label = "Beskrivning",
                    text = transactionRow.description ?: "",
                    textSoftWrap = true,
                    modifier = Modifier.padding(
                        top = 0.dp,
                        bottom = 0.dp,
                        start = 0.dp,
                        end = 6.dp
                    )
                )

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = {
                        onEditTransactionRowClick(
                            transactionHead.id,
                            transactionRow.id
                        )
                    }) {
                        Text(text = "Redigera", color = MaterialTheme.colorScheme.onSurface)
                    }

                    TextButton(onClick = {
                        onEvent(TransactionHeadAndRowsEvent.DeleteRow(transactionRowId = transactionRow.id))
                    }) {
                        Text(text = "Ta bort", color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun TransactionHeadInfoPreview() {
    val customer = Customer()
    val transactionHead = TransactionHead(
        transactionName = "Test",
        lender = "Test",
        borrower = "Test",
        startDate = LocalDate.now(),
        prelEndDate = LocalDate.now(),
        endDate = LocalDate.now(),
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
    )

    HomeBankFrontTheme {
        TransactionHeadInfo(
            customer = customer,
            transactionHead = transactionHead,
            onEditTransactionHeadClick = { _, _ -> }
        )
    }
}

@ThemePreviews
@Composable
fun TransactionRowListItemPreview() {
    val transactionHead = TransactionHead()
    val transactionRow = TransactionRow(
        transactionRowNo = 1,
        name = "TestTestTestTestTestTestTest",
        typeOfTransactionCode = TransactionRow.Type.PAYBACK.name,
        amount = 555,
        paymentDate = LocalDate.now(),
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
    )

    var expanded by rememberSaveable { mutableStateOf(false) }

    HomeBankFrontTheme {
        TransactionRowListItem(
            transactionHead = transactionHead,
            transactionRow = transactionRow,
            expanded = expanded,
            toggleExpanded = { expanded = !expanded },
            onEditTransactionRowClick = { _, _ -> },
            onEvent = {}
        )
    }
}
