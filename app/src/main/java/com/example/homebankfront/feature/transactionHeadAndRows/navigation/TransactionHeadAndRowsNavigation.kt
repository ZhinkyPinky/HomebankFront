package com.example.homebankfront.feature.transactionHeadAndRows.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.homebankfront.feature.transactionHeadAndRows.TransactionHeadAndRowsRoute

fun NavController.navigateToTransactionHeadAndRows(
    customerId : Long,
    transactionHeadId : Long
) = navigate(
    route = "customers/$customerId/transactionHeads/$transactionHeadId"
)

fun NavGraphBuilder.transactionHeadAndRowsScreen(
    onBackClick : () -> Unit,
    onEditTransactionHeadClick : (Long, Long) -> Unit,
    onEditTransactionRowClick : (Long, Long) -> Unit
) {
    composable(
        route = "customers/{customerId}/transactionHeads/{transactionHeadId}",
        arguments = listOf(
            navArgument("customerId") { type = NavType.LongType },
            navArgument("transactionHeadId") { type = NavType.LongType })
    ) {
        TransactionHeadAndRowsRoute(
            onBackClick = onBackClick,
            onEditTransactionHeadClick = onEditTransactionHeadClick,
            onEditTransactionRowClick = onEditTransactionRowClick
        )
    }
}