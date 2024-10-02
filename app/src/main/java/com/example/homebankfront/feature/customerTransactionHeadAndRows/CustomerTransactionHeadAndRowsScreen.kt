package com.example.homebankfront.feature.customerTransactionHeadAndRows

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
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
import androidx.compose.runtime.ComposableTarget
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.dataAccess.bodies.TransactionRow

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
        onTransactionRowClick = { },
        onBackClick = onBackClick
    )
}

@Composable
fun CustomerTransactionHeadAndRowsScreen(
    customerTransactionHeadAndRowsUiState: CustomerTransactionHeadAndRowsUiState,
    onEditTransactionClick: (Long, Long) -> Unit,
    onEditTransactionRowClick: (Long, Long) -> Unit,
    onTransactionRowClick: () -> Unit,
    onBackClick: () -> Unit
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
                onTransactionRowClick = onTransactionRowClick,
                onBackClick = onBackClick
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
    onTransactionRowClick: () -> Unit,
    onBackClick: () -> Unit
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
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary

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

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
            ) {
                Text(text = "Rader")

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "",

                        )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(1.dp),
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .fillMaxSize()
            ) {
                transactionRowList(
                    transactionRows = transactionRows,
                    onTransactionRowClick = onTransactionRowClick,
                    onEvent = {},
                    onEditTransactionRowClick = onEditTransactionRowClick
                )
            }
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
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
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
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Row {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Titel",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp
                )
                Text(
                    text = transactionHead.transactionName,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Row {
                    Column(modifier = Modifier.weight(1F)) {
                        Text(
                            text = "Långivare",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = transactionHead.lender,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Column(modifier = Modifier.weight(2F)) {
                        Text(
                            text = "Låntagare",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = transactionHead.borrower,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }

                Column {
                    Text(
                        text = "Saldo",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "245",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            Column {
                Text(
                    text = "Startdatum",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp
                )
                Text(
                    text = transactionHead.startDate.toString(),
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = "Prel. Slutdatum",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp
                )
                Text(
                    text = transactionHead.prelEndDate.toString(),
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = "Slutdatum",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp
                )
                Text(
                    text = transactionHead.endDate.toString(),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Text(
            text = "Beskrivning",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 12.sp
        )
        Text(
            text = transactionHead.description,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

fun LazyListScope.transactionRowList(
    transactionRows: List<TransactionRow>,
    onTransactionRowClick: () -> Unit,
    onEditTransactionRowClick: (Long, Long) -> Unit,
    onEvent: () -> Unit
) {
    itemsIndexed(
        items = transactionRows
    ) { _, transactionRow ->
        TransactionRowListItem(
            transactionRow = transactionRow,
            onEditTransactionRowClick = onEditTransactionRowClick,
            onEvent = onEvent
        )
    }
}

@Composable
fun TransactionRowListItem(
    transactionRow: TransactionRow,
    onEditTransactionRowClick: (Long, Long) -> Unit,
    onEvent: () -> Unit
) {
    var dropDownMenuExpanded by rememberSaveable { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.primary)
            .clickable {

            }
    ) {

        Text(
            text = "${transactionRow.transactionRowNo}",
            modifier = Modifier.align(Alignment.CenterVertically).weight(0.5f).padding(8.dp)
        )
        Column(modifier = Modifier.weight(3f)) {
            Text(
                text = "Titel",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 12.sp
            )

            Text(
                text = transactionRow.name,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Column(modifier = Modifier.weight(2f)) {
            Text(
                text = "Datum",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 12.sp
            )

            Text(
                text = "${transactionRow.paymentDate}",
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Belopp",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 12.sp
            )

            Text(
                text = "${transactionRow.amount}",
            )
        }

        Box {
            DropdownMenu(
                expanded = dropDownMenuExpanded,
                onDismissRequest = { dropDownMenuExpanded = false },
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Redigera",
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    },
                    onClick = {
                        onEditTransactionRowClick(
                            transactionRow.transactionHeadId,
                            transactionRow.id
                        )
                    }
                )

                DropdownMenuItem(
                    text = {
                        Text(
                            text = "Ta bort",
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    },
                    onClick = { onEvent() }
                )
            }

            IconButton(onClick = { dropDownMenuExpanded = !dropDownMenuExpanded }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = ""
                )
            }
        }
    }
}