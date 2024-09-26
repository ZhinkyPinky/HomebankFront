package com.example.homebankfront.feature.customerList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.dataAccess.bodies.Customer

@Composable
internal fun CustomerListRoute(
    viewModel : CustomerListViewModel = hiltViewModel(),
    onCustomerClick : (Long) -> Unit
) {
    val customerListUiState : CustomerListUiState by viewModel.customerListUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCustomers()
    }

    CustomerListScreen(
        customerListUiState = customerListUiState,
        onCustomerClick = onCustomerClick,
        viewModel::onEvent
    )
}

@Composable
fun CustomerListScreen(
    customerListUiState : CustomerListUiState,
    onCustomerClick : (Long) -> Unit,
    onEvent : () -> Unit
) {
    when (customerListUiState) {
        is CustomerListUiState.Loading -> {}
        is CustomerListUiState.Ready -> {
            CustomerListScreen(
                customers = customerListUiState.customers,
                onCustomerClick = onCustomerClick,
                onEvent = onEvent
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerListScreen(
    customers : List<Customer>,
    onCustomerClick : (Long) -> Unit,
    onEvent : () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Konton") },
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
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxSize()
        ) {
            customerList(
                customers = customers,
                onCustomerClick,
                onEvent = onEvent
            )
        }
    }
}

fun LazyListScope.customerList(
    customers : List<Customer>,
    onCustomerClick : (Long) -> Unit,
    onEvent : () -> Unit
) {
    itemsIndexed(
        items = customers
    ) { index, customer ->
        TextButton(
            onClick = { onCustomerClick(customer.id) },
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = MaterialTheme.colorScheme.onPrimary

            )
        ) {
            Text(text = customer.name)
        }
    }
}