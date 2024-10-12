package com.example.homebankfront.feature.customerList.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.customerList.CustomerListRoute

const val CUSTOMER_LIST_ROUTE = "customers"

fun NavController.navigateToCustomerList() = navigate(
    route = CUSTOMER_LIST_ROUTE,
)

fun NavGraphBuilder.customerListScreen(
    onCustomerClick : (Long) -> Unit
) {
    composable(route = CUSTOMER_LIST_ROUTE) {
        CustomerListRoute(
            onCustomerClick = onCustomerClick
        )
    }
}