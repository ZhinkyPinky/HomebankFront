package com.example.homebankfront.feature.editTransactionHead.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.editTransactionHead.EditTransactionHeadScreen
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data class EditTransactionHead(val transactionHeadId: Long) : Route {
    override val enableNavDrawer: Boolean = true
    override val name: String = className
}

fun NavController.navigateToEditTransactionHead(transactionHeadId: Long) =
    navigate(route = EditTransactionHead(transactionHeadId))

fun NavGraphBuilder.editTransactionHeadScreen(
    onBackClick: () -> Unit
) {
    composable<EditTransactionHead> {
        EditTransactionHeadScreen(onBackClick = onBackClick)
    }
}