package com.example.homebankfront.navigation

import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.navigation
import com.example.homebankfront.feature.authentication.navigation.Authentication
import com.example.homebankfront.feature.authentication.navigation.authentication
import com.example.homebankfront.feature.changePassword.navigation.changePasswordScreen
import com.example.homebankfront.feature.customerList.navigation.CustomerList
import com.example.homebankfront.feature.customerList.navigation.customerListScreen
import com.example.homebankfront.feature.customerList.navigation.navigateToCustomerList
import com.example.homebankfront.feature.editTransactionHead.navigation.editTransactionHeadScreen
import com.example.homebankfront.feature.editTransactionHead.navigation.navigateToEditTransactionHead
import com.example.homebankfront.feature.editTransactionRow.navigation.editTransactionRowScreen
import com.example.homebankfront.feature.editTransactionRow.navigation.navigateToEditTransactionRow
import com.example.homebankfront.feature.accountrecovery.confirmationemailrequest.navigateToConfirmationEmailRequest
import com.example.homebankfront.feature.accountrecovery.confirmationemailrequest.confirmationEmailRequest
import com.example.homebankfront.feature.registration.navigation.navigateToRegistration
import com.example.homebankfront.feature.registration.navigation.registration
import com.example.homebankfront.feature.transactionHeadAndRows.navigation.navigateToTransactionHeadAndRows
import com.example.homebankfront.feature.transactionHeadAndRows.navigation.transactionHeadAndRowsScreen
import com.example.homebankfront.feature.transactionHeadsList.navigation.navigateToTransactionHeadsList
import com.example.homebankfront.feature.transactionHeadsList.navigation.transactionHeadsListScreen
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.ui.HomebankAppState
import kotlinx.serialization.Serializable

@Serializable
data object Unauthenticated : Route {
    override val enableNavDrawer: Boolean = false
    override val name: String = className
}

fun NavController.navigateToUnauthenticated(builder: NavOptionsBuilder.() -> Unit) =
    navigate(
        route = Unauthenticated,
        builder = builder
    )

@Serializable
data object Authenticated : Route {
    override val enableNavDrawer: Boolean = true
    override val name: String = className
}

fun NavController.navigateToAuthenticated(builder: NavOptionsBuilder.() -> Unit) =
    navigate(
        route = Authenticated,
        builder = builder
    )

@Composable
fun HomeBankNavHost(appState: HomebankAppState) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = Unauthenticated,
        modifier = Modifier.imePadding()
    ) {
        navigation<Unauthenticated>(startDestination = Authentication) {
            authentication(
                onAuthentication = navController::navigateToCustomerList,
                navigateToRegistration = navController::navigateToRegistration,
                navigateToRecoverUserAccount = navController::navigateToConfirmationEmailRequest
            )

            registration(navController)

            confirmationEmailRequest(navController = navController)
        }

        navigation<Authenticated>(startDestination = CustomerList) {
            customerListScreen(onCustomerClick = navController::navigateToTransactionHeadsList)

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

            changePasswordScreen(onChangedPassword = navController::navigateUp)
        }
    }
}