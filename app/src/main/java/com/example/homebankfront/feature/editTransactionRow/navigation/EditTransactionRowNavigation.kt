package com.example.homebankfront.feature.editTransactionRow.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowRoute

fun NavController.navigateToEditTransactionRow(
    transactionHeadId: Long,
    transactionRowId: Long
) = navigate(
    route = "transactionHeads/$transactionHeadId/transactionRows/$transactionRowId/edit"
)

fun NavGraphBuilder.editTransactionRowScreen(
    onBackClick: () -> Unit
) {
    composable(
        route = "transactionHeads/{transactionHeadId}/transactionRows/{transactionRowId}/edit",
        arguments = listOf(
            navArgument("transactionHeadId") { type = NavType.LongType },
            navArgument("transactionRowId") { type = NavType.LongType }
        )
    ) {
        EditTransactionRowRoute(
            onBackClick = onBackClick
        )
    }
}