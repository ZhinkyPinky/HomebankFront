package com.example.homebankfront.feature.transactionHeadAndRows.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.transactionHeadAndRows.TransactionHeadAndRowsScreen
import kotlinx.serialization.Serializable

@Serializable
data class TransactionHeadAndRows(val customerId: Long, val transactionHeadId: Long)

fun NavController.navigateToTransactionHeadAndRows(
    customerId: Long,
    transactionHeadId: Long
) = navigate(TransactionHeadAndRows(customerId, transactionHeadId))

fun NavGraphBuilder.transactionHeadAndRowsScreen(
    onBackClick: () -> Unit,
    onEditTransactionHeadClick: (Long) -> Unit,
    onEditTransactionRowClick: (Long, Long) -> Unit
) {
    composable<TransactionHeadAndRows> {
        TransactionHeadAndRowsScreen(
            onBackClick = onBackClick,
            onEditTransactionHeadClick = onEditTransactionHeadClick,
            onEditTransactionRowClick = onEditTransactionRowClick
        )
    }
}