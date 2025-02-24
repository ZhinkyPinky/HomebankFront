package com.example.homebankfront.feature.editTransactionHead.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadScreen
import kotlinx.serialization.Serializable

@Serializable
data class EditTransactionHead(val transactionHeadId: Long)

fun NavController.navigateToEditTransactionHead(transactionHeadId: Long) =
    navigate(route = EditTransactionHead(transactionHeadId))

fun NavGraphBuilder.editTransactionHeadScreen(
    onBackClick: () -> Unit
) {
    composable<EditTransactionHead> {
        EditTransactionHeadScreen(onBackClick = onBackClick)
    }
}