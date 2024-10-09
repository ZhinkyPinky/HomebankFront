package com.example.homebankfront.feature.transactionHeadsList.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.homebankfront.feature.transactionHeadsList.TransactionHeadsListRoute

fun NavController.navigateToTransactionHeadsList(
    customerId: Long,
) = navigate(
    route = "customers/$customerId/transactionHeads"
)

fun NavGraphBuilder.transactionHeadsListScreen(
    onNewTransactionHeadClick: (Long, Long) -> Unit,
    onTransactionHeadClick: (Long, Long) -> Unit,
    onBackClick: () -> Unit
) {
    composable(
        route = "customers/{customerId}/transactionHeads",
        arguments = listOf(navArgument("customerId") { type = NavType.LongType })
    ) {
        TransactionHeadsListRoute(
            onNewTransactionHeadClick = onNewTransactionHeadClick,
            onTransactionHeadClick = onTransactionHeadClick,
            onBackClick = onBackClick
        )
    }
}