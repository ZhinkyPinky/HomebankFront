package com.example.homebankfront.feature.editTransactionRow.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowRoute

fun NavController.navigateToEditTransactionRow(
    transactionRowId : Long
) = navigate(
    route = "transactionRow/$transactionRowId/edit"
)

fun NavGraphBuilder.editTransactionRowScreen(
    onBackClick : () -> Unit
) {
    composable(
        route = "transactionRow/{transactionRowId}/edit",
        arguments = listOf(
            navArgument("transactionRowId") { type = NavType.LongType })
    ) {
        EditTransactionRowRoute(
            onBackClick = onBackClick
        )
    }
}