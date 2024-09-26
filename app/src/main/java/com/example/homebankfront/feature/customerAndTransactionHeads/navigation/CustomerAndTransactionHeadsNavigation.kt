package com.example.homebankfront.feature.customerAndTransactionHeads.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.homebankfront.feature.customerAndTransactionHeads.CustomerAndTransactionHeadsRoute
fun NavController.navigateToCustomerAndTransactionHeads(
    customerId : Long,
) = navigate(
    route = "customer/$customerId"
)

fun NavGraphBuilder.customerAndTransactionHeadsScreen(
    onTransactionHeadClick : (Long, Long) -> Unit,
    onBackClick : () -> Unit
) {
    composable(
        route = "customer/{customerId}",
        arguments = listOf(navArgument("customerId") { type = NavType.LongType })
    ) {
        CustomerAndTransactionHeadsRoute(
            onTransactionHeadClick = onTransactionHeadClick,
            onBackClick = onBackClick
        )
    }
}