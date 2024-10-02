package com.example.homebankfront.feature.customerTransactionHeadAndRows.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.homebankfront.feature.customerTransactionHeadAndRows.CustomerTransactionHeadAndRowsRoute

fun NavController.navigateToCustomerTransactionHeadAndRows(
    customerId : Long,
    transactionHeadId : Long
) = navigate(
    route = "customer/$customerId/transaction_head/$transactionHeadId"
)

fun NavGraphBuilder.customerTransactionHeadAndRowsScreen(
    onBackClick : () -> Unit,
    onEditTransactionHeadClick : (Long, Long) -> Unit,
    onEditTransactionRowClick : (Long, Long) -> Unit
) {
    composable(
        route = "customer/{customerId}/transaction_head/{transactionHeadId}",
        arguments = listOf(
            navArgument("customerId") { type = NavType.LongType },
            navArgument("transactionHeadId") { type = NavType.LongType })
    ) {
        CustomerTransactionHeadAndRowsRoute(
            onBackClick = onBackClick,
            onEditTransactionHeadClick = onEditTransactionHeadClick,
            onEditTransactionRowClick = onEditTransactionRowClick
        )
    }
}