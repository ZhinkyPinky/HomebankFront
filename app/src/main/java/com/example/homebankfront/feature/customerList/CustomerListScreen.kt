package com.example.homebankfront.feature.customerList

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homebankfront.R
import com.example.homebankfront.data.bodies.Customer
import com.example.homebankfront.ui.theme.HomeBankFrontTheme
import com.example.homebankfront.ui.theme.ThemePreviews

@Composable
internal fun CustomerListRoute(
    viewModel: CustomerListViewModel = hiltViewModel(), onCustomerClick: (Long) -> Unit
) {
    val customerListState: CustomerListState by viewModel.customerListState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCustomers()
    }

    CustomerListScreen(
        customerListState = customerListState,
        onCustomerClick = onCustomerClick,
    )
}

@Composable
fun CustomerListScreen(
    customerListState: CustomerListState,
    onCustomerClick: (Long) -> Unit,
) {
    when (customerListState) {
        is CustomerListState.Loading -> {}
        is CustomerListState.Ready -> {
            CustomerListScreen(
                customers = customerListState.customers,
                onCustomerClick = onCustomerClick,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerListScreen(
    customers: List<Customer>,
    onCustomerClick: (Long) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(stringResource(R.string.accounts))
            })
        },
    ) { paddingValues ->
        Surface {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(12.dp)
                    .fillMaxSize()
            ) {
                customerList(
                    customers = customers,
                    onCustomerClick = onCustomerClick,
                )
            }
        }
    }
}

fun LazyListScope.customerList(
    customers: List<Customer>,
    onCustomerClick: (Long) -> Unit,
) {
    itemsIndexed(
        items = customers
    ) { index, customer ->
        TextButton(onClick = { onCustomerClick(customer.id) }) {
            Text(
                text = customer.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}


@ThemePreviews
@Composable
fun CustomerListScreenPreview() {
    val customers = listOf(
        Customer(name = "Test"),
        Customer(name = "Test"),
        Customer(name = "Test"),
        Customer(name = "Test"),
        Customer(name = "Test"),
        Customer(name = "Test"),
        Customer(name = "Test"),

        )

    HomeBankFrontTheme {
        CustomerListScreen(
            customers = customers,
            onCustomerClick = { _ -> },
        )
    }
}