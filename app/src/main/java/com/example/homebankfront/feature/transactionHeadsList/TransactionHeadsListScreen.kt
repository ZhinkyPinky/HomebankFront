package com.example.homebankfront.feature.transactionHeadsList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.TransactionHead
import com.example.homebankfront.designsystem.TextWithLabel


@Composable
internal fun TransactionHeadsListRoute(
    viewModel: TransactionHeadsListViewModel = hiltViewModel(),
    onNewTransactionHeadClick: (Long, Long) -> Unit,
    onTransactionHeadClick: (Long, Long) -> Unit,
    onBackClick: () -> Unit
) {
    val transactionHeadsListState: TransactionHeadsListState by viewModel.transactionHeadsListState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCustomerAndTransactionHeads()
    }

    TransactionHeadsListScreen(
        transactionHeadsListState = transactionHeadsListState,
        onNewTransactionHeadClick = onNewTransactionHeadClick,
        onTransactionHeadClick = onTransactionHeadClick,
        onBackClick = onBackClick
    )
}

@Composable
fun TransactionHeadsListScreen(
    transactionHeadsListState: TransactionHeadsListState,
    onNewTransactionHeadClick: (Long, Long) -> Unit,
    onTransactionHeadClick: (Long, Long) -> Unit,
    onBackClick: () -> Unit
) {
    when (transactionHeadsListState) {
        is TransactionHeadsListState.Loading -> {}

        is TransactionHeadsListState.Ready -> {
            TransactionHeadsListScreen(
                customer = transactionHeadsListState.customer,
                transactionHeads = transactionHeadsListState.transactionHeads,
                onNewTransactionHeadClick = onNewTransactionHeadClick,
                onTransactionHeadClick = onTransactionHeadClick,
                onBackClick = onBackClick
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHeadsListScreen(
    customer: Customer,
    transactionHeads: List<TransactionHead>,
    onNewTransactionHeadClick: (Long, Long) -> Unit,
    onTransactionHeadClick: (Long, Long) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = customer.name) },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onNewTransactionHeadClick(customer.id, -1L) }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
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
        }
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            contentPadding = PaddingValues(vertical = 1.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            transactionHeadList(
                customerId = customer.id,
                transactionHeads = transactionHeads,
                onTransactionHeadClick = onTransactionHeadClick,
            )
        }
    }
}

fun LazyListScope.transactionHeadList(
    customerId: Long,
    transactionHeads: List<TransactionHead>,
    onTransactionHeadClick: (Long, Long) -> Unit,
) {
    itemsIndexed(
        items = transactionHeads,
        key = { _, transactionHead -> transactionHead.id }
    ) { _, transactionHead ->
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.surface)
                .clickable {
                    onTransactionHeadClick(
                        customerId,
                        transactionHead.id
                    )
                }
        ) {

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                TextWithLabel(
                    label = if (customerId == transactionHead.lenderId) "Till" else "Från",
                    text = if (customerId == transactionHead.lenderId) {
                        transactionHead.borrower ?: ""
                    } else {
                        transactionHead.lender ?: ""
                    }
                )

                TextWithLabel(
                    label = "Titel",
                    text = transactionHead.transactionName ?: ""
                )

            }

            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
            ) {
                TextWithLabel(
                    label = "Startdatum",
                    text = transactionHead.startDate.toString(),
                    horizontalAlignment = Alignment.End
                )

                TextWithLabel(
                    label = "Saldo",
                    text = transactionHead.amount.toString(),
                    horizontalAlignment = Alignment.End
                )
            }
        }
    }
}