package com.example.homebankfront.feature.transactionHeadsList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.Customer
import com.example.homebankfront.data.bodies.TransactionHead
import com.example.homebankfront.ui.components.LoadingOverlay
import com.example.homebankfront.ui.components.TextWithLabel


@Composable
internal fun TransactionHeadsScreen(
    viewModel: TransactionHeadsListViewModel = hiltViewModel(),
    onNewTransactionHeadClick: (Long) -> Unit,
    onTransactionHeadClick: (Long, Long) -> Unit,
    onBackClick: () -> Unit
) {
    val transactionHeadsListState: TransactionHeadsListState by viewModel.transactionHeadsListState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCustomerAndTransactionHeads()
    }

    TransactionHeadsScreen(
        transactionHeadsListState = transactionHeadsListState,
        onNewTransactionHeadClick = onNewTransactionHeadClick,
        onTransactionHeadClick = onTransactionHeadClick,
        onBackClick = onBackClick
    )
}

@Composable
fun TransactionHeadsScreen(
    transactionHeadsListState: TransactionHeadsListState,
    onNewTransactionHeadClick: (Long) -> Unit,
    onTransactionHeadClick: (Long, Long) -> Unit,
    onBackClick: () -> Unit
) {
    when (transactionHeadsListState) {
        is TransactionHeadsListState.Loading -> LoadingOverlay()

        is TransactionHeadsListState.Ready -> {
            TransactionHeadsScreen(
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
fun TransactionHeadsScreen(
    customer: Customer,
    transactionHeads: List<TransactionHead>,
    onNewTransactionHeadClick: (Long) -> Unit,
    onTransactionHeadClick: (Long, Long) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Row { Text(text = "${customer.name} (${customer.customerAmount} kr)") } },
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.go_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onNewTransactionHeadClick(-1L) }) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.new_transaction_head)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(1.dp),
            contentPadding = PaddingValues(vertical = 1.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            itemsIndexed(
                items = transactionHeads,
                key = { _, transactionHead -> transactionHead.id }
            ) { _, transactionHead ->
                TransactionHeadItem(
                    customerId = customer.id,
                    transactionHead = transactionHead,
                    onTransactionHeadClick = onTransactionHeadClick
                )
            }
        }
    }
}

@Composable
fun TransactionHeadItem(
    customerId: Long,
    transactionHead: TransactionHead,
    onTransactionHeadClick: (Long, Long) -> Unit
) {
    Surface(
        modifier = Modifier.clickable {
            onTransactionHeadClick(
                customerId,
                transactionHead.id
            )
        }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                TextWithLabel(
                    label = stringResource(R.string.title),
                    text = transactionHead.transactionName
                )

                Spacer(modifier = Modifier.height(6.dp))

                TextWithLabel(
                    label = when (customerId) {
                        transactionHead.lenderId -> stringResource(R.string.to)
                        transactionHead.borrowerId -> stringResource(R.string.from)
                        else -> stringResource(R.string.error_wrong_id)
                    },
                    text = when (customerId) {
                        transactionHead.lenderId -> transactionHead.borrower
                        transactionHead.borrowerId -> transactionHead.lender
                        else -> stringResource(R.string.error_wrong_id)
                    }
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                TextWithLabel(
                    label = stringResource(R.string.start_date),
                    text = transactionHead.startDate.toString(),
                    horizontalAlignment = Alignment.End
                )

                Spacer(modifier = Modifier.height(6.dp))

                TextWithLabel(
                    label = stringResource(R.string.balance),
                    text = transactionHead.amount.toString(),
                    horizontalAlignment = Alignment.End
                )
            }
        }
    }
}