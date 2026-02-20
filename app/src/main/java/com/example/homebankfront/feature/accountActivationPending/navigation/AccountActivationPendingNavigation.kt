package com.example.homebankfront.feature.accountActivationPending.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.accountActivationPending.AccountActivationPendingScreen
import com.example.homebankfront.feature.authentication.navigation.navigateToAuthentication
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object AccountActivationPending : Route {
    override val enableNavDrawer: Boolean = false
    override val name: String = className
}

fun NavController.navigateToAccountActivationPending() =
    navigate(route = AccountActivationPending)

fun NavGraphBuilder.accountActivationPending(
    navController: NavController
) {
    composable<AccountActivationPending> {
        AccountActivationPendingScreen(
            onNewActivationEmailSent = { navController.navigateToAuthentication { popUpTo(0) } }
        )
    }
}