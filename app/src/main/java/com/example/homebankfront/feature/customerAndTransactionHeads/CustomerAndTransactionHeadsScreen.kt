package com.example.homebankfront.feature.customerAndTransactionHeads

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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.dataAccess.bodies.Customer
import com.example.homebankfront.dataAccess.bodies.TransactionHead


@Composable
internal fun CustomerAndTransactionHeadsRoute(
    viewModel : CustomerAndTransactionHeadsViewModel = hiltViewModel(),
    onTransactionHeadClick : (Long, Long) -> Unit,
    onBackClick : () -> Unit
) {
    val customerAndTransactionHeadsUiState : CustomerAndTransactionHeadsUiState by viewModel.customerAndTransactionHeadsUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCustomerAndTransactionHeads()
    }

    CustomerAndTransactionHeadsScreen(
        customerAndTransactionHeadsUiState = customerAndTransactionHeadsUiState,
        onEvent = viewModel::onEvent,
        onTransactionHeadClick = onTransactionHeadClick,
        onBackClick = onBackClick
    )
}

@Composable
fun CustomerAndTransactionHeadsScreen(
    customerAndTransactionHeadsUiState : CustomerAndTransactionHeadsUiState,
    onEvent : () -> Unit,
    onTransactionHeadClick : (Long, Long) -> Unit,
    onBackClick : () -> Unit
) {
    when (customerAndTransactionHeadsUiState) {
        is CustomerAndTransactionHeadsUiState.Loading -> {}

        is CustomerAndTransactionHeadsUiState.Ready -> {
            CustomerAndTransactionHeadsScreen(
                customer = customerAndTransactionHeadsUiState.customer,
                transactionHeads = customerAndTransactionHeadsUiState.transactionHeads,
                onEvent = onEvent,
                onTransactionHeadClick = onTransactionHeadClick,
                onBackClick = onBackClick
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerAndTransactionHeadsScreen(
    customer : Customer,
    transactionHeads : List<TransactionHead>,
    onEvent : () -> Unit,
    onTransactionHeadClick : (Long, Long) -> Unit,
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
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary

                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            contentPadding = PaddingValues(vertical = 2.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            transactionHeadList(
                customerId = customer.id,
                transactionHeads = transactionHeads,
                onTransactionHeadClick = onTransactionHeadClick,
                onEvent = onEvent
            )
        }
    }
}

fun LazyListScope.transactionHeadList(
    customerId : Long,
    transactionHeads : List<TransactionHead>,
    onTransactionHeadClick : (Long, Long) -> Unit,
    onEvent : () -> Unit
) {
    itemsIndexed(
        items = transactionHeads
    ) { index, transactionHead ->
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    onTransactionHeadClick(
                        customerId,
                        transactionHead.id
                    )
                }
        ) {
            Column(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = if (customerId == transactionHead.lenderId) "Till" else "Från",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp
                )
                Text(
                    text = if (customerId == transactionHead.lenderId) transactionHead.borrower else transactionHead.lender,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = "Titel",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp,
                )
                Text(
                    text = transactionHead.transactionName,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(8.dp)
            ) {
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
                    text = "Saldo",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 12.sp,
                )
                Text(
                    text = "2254",
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
    }
}