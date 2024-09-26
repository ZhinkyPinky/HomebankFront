package com.example.homebankfront.feature.editTransactionHead.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadRoute

fun NavController.navigateToEditTransactionHead(
    customerId : Long,
    transactionHeadId : Long
) = navigate(
    route = "customer/$customerId/transactionHead/$transactionHeadId/edit"
)

fun NavGraphBuilder.editTransactionHeadScreen(
    onBackClick : () -> Unit
) {
    composable(
        route = "customer/{customerId}/transactionHead/{transactionHeadId}/edit",
        arguments = listOf(
            navArgument("customerId") { type = NavType.LongType },
            navArgument("transactionHeadId") { type = NavType.LongType })
    ) {
        EditTransactionHeadRoute(
            onBackClick = onBackClick
        )
    }
}