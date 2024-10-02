package com.example.homebankfront.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.example.homebankfront.feature.customerAndTransactionHeads.navigation.customerAndTransactionHeadsScreen
import com.example.homebankfront.feature.customerAndTransactionHeads.navigation.navigateToCustomerAndTransactionHeads
import com.example.homebankfront.feature.customerList.navigation.CUSTOMER_LIST_ROUTE
import com.example.homebankfront.feature.customerList.navigation.customerListScreen
import com.example.homebankfront.feature.customerTransactionHeadAndRows.navigation.customerTransactionHeadAndRowsScreen
import com.example.homebankfront.feature.customerTransactionHeadAndRows.navigation.navigateToCustomerTransactionHeadAndRows
import com.example.homebankfront.feature.editTransactionHead.navigation.editTransactionHeadScreen
import com.example.homebankfront.feature.editTransactionHead.navigation.navigateToEditTransactionHead
import com.example.homebankfront.feature.editTransactionRow.navigation.editTransactionRowScreen
import com.example.homebankfront.feature.editTransactionRow.navigation.navigateToEditTransactionRow
import com.example.homebankfront.ui.HomebankAppState

@Composable
fun HomeBankNavHost(appState: HomebankAppState) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = CUSTOMER_LIST_ROUTE
    ) {
        customerListScreen(
            onCustomerClick = navController::navigateToCustomerAndTransactionHeads
        )

        customerAndTransactionHeadsScreen(
            onNewTransactionHeadClick = navController::navigateToEditTransactionHead,
            onTransactionHeadClick = navController::navigateToCustomerTransactionHeadAndRows,
            onBackClick = navController::popBackStack
        )

        customerTransactionHeadAndRowsScreen(
            onBackClick = navController::popBackStack,
            onEditTransactionHeadClick = navController::navigateToEditTransactionHead,
            onEditTransactionRowClick = navController::navigateToEditTransactionRow
        )

        editTransactionHeadScreen(onBackClick = navController::popBackStack)

        editTransactionRowScreen(onBackClick = navController::popBackStack)
    }
}