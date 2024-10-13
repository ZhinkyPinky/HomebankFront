package com.example.homebankfront.navigation

import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.example.homebankfront.feature.transactionHeadsList.navigation.transactionHeadsListScreen
import com.example.homebankfront.feature.transactionHeadsList.navigation.navigateToTransactionHeadsList
import com.example.homebankfront.feature.customerList.navigation.CUSTOMER_LIST_ROUTE
import com.example.homebankfront.feature.customerList.navigation.customerListScreen
import com.example.homebankfront.feature.transactionHeadAndRows.navigation.transactionHeadAndRowsScreen
import com.example.homebankfront.feature.transactionHeadAndRows.navigation.navigateToTransactionHeadAndRows
import com.example.homebankfront.feature.editTransactionHead.navigation.editTransactionHeadScreen
import com.example.homebankfront.feature.editTransactionHead.navigation.navigateToEditTransactionHead
import com.example.homebankfront.feature.editTransactionRow.navigation.editTransactionRowScreen
import com.example.homebankfront.feature.editTransactionRow.navigation.navigateToEditTransactionRow
import com.example.homebankfront.ui.HomebankAppState

@Composable
fun HomeBankNavHost(
    appState: HomebankAppState
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = CUSTOMER_LIST_ROUTE,
        modifier = Modifier.imePadding()
    ) {
        customerListScreen(
            onCustomerClick = navController::navigateToTransactionHeadsList
        )

        transactionHeadsListScreen(
            onNewTransactionHeadClick = navController::navigateToEditTransactionHead,
            onTransactionHeadClick = navController::navigateToTransactionHeadAndRows,
            onBackClick = navController::navigateUp
        )

        transactionHeadAndRowsScreen(
            onBackClick = navController::navigateUp,
            onEditTransactionHeadClick = navController::navigateToEditTransactionHead,
            onEditTransactionRowClick = navController::navigateToEditTransactionRow
        )

        editTransactionHeadScreen(onBackClick = navController::navigateUp)

        editTransactionRowScreen(onBackClick = navController::navigateUp)
    }
}