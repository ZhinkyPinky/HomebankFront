package com.example.homebankfront.feature.customerList.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.customerList.CustomerListRoute
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object CustomerList : Route {
    override val enableNavDrawer: Boolean = true
    override val name: String = className
}

fun NavController.navigateToCustomerList() = navigate(route = CustomerList)

fun NavGraphBuilder.customerListScreen(
    onCustomerClick: (Long) -> Unit
) {
    composable<CustomerList> {
        CustomerListRoute(onCustomerClick = onCustomerClick)
    }
}