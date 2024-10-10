package com.example.homebankfront.feature.customerList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
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
import com.example.homebankfront.data.bodies.Customer
import com.example.homebankfront.ui.theme.HomeBankFrontTheme
import com.example.homebankfront.ui.theme.ThemePreviews

@Composable
internal fun CustomerListRoute(
    viewModel: CustomerListViewModel = hiltViewModel(),
    onCustomerClick: (Long) -> Unit
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
            TopAppBar(
                title = { Text("Konton") }, colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
        ) {
            customerList(
                customers = customers,
                onCustomerClick = onCustomerClick,
            )
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
        TextButton(
            onClick = { onCustomerClick(customer.id) },
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            )
        ) {
            Text(text = customer.name)
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