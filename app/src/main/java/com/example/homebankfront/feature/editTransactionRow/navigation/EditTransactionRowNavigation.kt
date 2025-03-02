package com.example.homebankfront.feature.editTransactionRow.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.editTransactionRow.EditTransactionRowRoute
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable


@Serializable
data class EditTransactionRow(
    val transactionHeadId: Long,
    val transactionRowId: Long
) : Route {
    override val enableNavDrawer: Boolean = true
    override val route: String = className
}

fun NavController.navigateToEditTransactionRow(transactionHeadId: Long, transactionRowId: Long) =
    navigate(route = EditTransactionRow(transactionHeadId, transactionRowId))

fun NavGraphBuilder.editTransactionRowScreen(onBackClick: () -> Unit) {
    composable<EditTransactionRow> {
        EditTransactionRowRoute(
            onBackClick = onBackClick
        )
    }
}