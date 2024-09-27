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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    viewModel : CustomerTransactionHeadAndRowsViewModel = hiltViewModel(),
    onEditTransactionHeadClick : (Long, Long) -> Unit,
    onEditTransactionRowClick : (Long) -> Unit,
    onBackClick : () -> Unit
) {
    val customerAndTransactionHeadAndRowsUiState : CustomerTransactionHeadAndRowsUiState by viewModel.customerTransactionHeadAndRowsUiState.collectAsStateWithLifecycle()

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
    customerTransactionHeadAndRowsUiState : CustomerTransactionHeadAndRowsUiState,
    onEditTransactionClick : (Long, Long) -> Unit,
    onEditTransactionRowClick : (Long) -> Unit,
    onTransactionRowClick : () -> Unit,
    onBackClick : () -> Unit
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
    customer : Customer,
    transactionHead : TransactionHead,
    transactionRows : List<TransactionRow>,
    onEditTransactionHeadClick : (Long, Long) -> Unit,
    onEditTransactionRowClick : (Long) -> Unit,
    onTransactionRowClick : () -> Unit,
    onBackClick : () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(customer.name) },
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
                modifier = Modifier.padding(bottom = 2.dp)
            )
        },
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.padding(paddingValues)
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

            Column(modifier = Modifier.background(MaterialTheme.colorScheme.primary)) {
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
                verticalArrangement = Arrangement.spacedBy(2.dp),
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


fun LazyListScope.transactionRowList(
    transactionRows : List<TransactionRow>,
    onTransactionRowClick : () -> Unit,
    onEditTransactionRowClick : (Long) -> Unit,
    onEvent : () -> Unit
) {
    itemsIndexed(
        items = transactionRows
    ) { index, transactionRow ->

        var expanded by rememberSaveable { mutableStateOf(false) }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = "Redigera") },
                onClick = { onEditTransactionRowClick(transactionRow.id) }
            )

            DropdownMenuItem(
                text = { Text(text = "Ta bort") },
                onClick = { }
            )
        }

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
                modifier = Modifier.weight(1F)
            )
            Text(
                text = transactionRow.transactionName,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1F)
            )
            Text(
                text = "${transactionRow.paymentDate}",
                modifier = Modifier.weight(1F)
            )
            Text(
                text = "${transactionRow.amount}",
                modifier = Modifier.weight(1F)
            )

            IconButton(onClick = {expanded = true}) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = ""
                )
            }
        }
    }
}