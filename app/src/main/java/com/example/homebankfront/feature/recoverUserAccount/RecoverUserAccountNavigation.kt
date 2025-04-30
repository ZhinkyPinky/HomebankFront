package com.example.homebankfront.feature.recoverUserAccount

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.authentication.navigation.navigateToAuthentication
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object RecoverUserAccount : Route {
    override val enableNavDrawer: Boolean = false
    override val name: String = className
}

fun NavController.navigateToRecoverUserAccount() = navigate(route = RecoverUserAccount)

fun NavGraphBuilder.recoverUserAccount(
    navController: NavController
) {
    composable<RecoverUserAccount> {
        RecoverUserAccountScreen(onRecoveryInitiated = {
            navController.navigateToAuthentication { popUpTo(0) }
        })
    }
}
