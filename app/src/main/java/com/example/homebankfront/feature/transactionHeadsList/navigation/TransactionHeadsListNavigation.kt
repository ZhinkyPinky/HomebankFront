package com.example.homebankfront.feature.transactionHeadsList.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.transactionHeadsList.TransactionHeadsScreen
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data class TransactionHeadsList(val customerId: Long) : Route {
    override val enableNavDrawer: Boolean = true
    override val route: String = className
}

fun NavController.navigateToTransactionHeadsList(customerId: Long) =
    navigate(TransactionHeadsList(customerId))

fun NavGraphBuilder.transactionHeadsListScreen(
    onNewTransactionHeadClick: (Long) -> Unit,
    onTransactionHeadClick: (Long, Long) -> Unit,
    onBackClick: () -> Unit
) {
    composable<TransactionHeadsList> {
        TransactionHeadsScreen(
            onNewTransactionHeadClick = onNewTransactionHeadClick,
            onTransactionHeadClick = onTransactionHeadClick,
            onBackClick = onBackClick
        )
    }
}