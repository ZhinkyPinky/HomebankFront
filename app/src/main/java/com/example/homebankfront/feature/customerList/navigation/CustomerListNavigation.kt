package com.example.homebankfront.feature.customerList.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.customerList.CustomerListRoute
import kotlinx.serialization.Serializable

@Serializable
data object CustomerList


fun NavController.navigateToCustomerList() = navigate(route = CustomerList)

fun NavGraphBuilder.customerListScreen(
    onCustomerClick: (Long) -> Unit
) {
    composable<CustomerList> {
        CustomerListRoute(onCustomerClick = onCustomerClick)
    }
}