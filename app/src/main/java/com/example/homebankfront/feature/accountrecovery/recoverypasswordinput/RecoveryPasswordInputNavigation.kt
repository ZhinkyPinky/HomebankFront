package com.example.homebankfront.feature.accountrecovery.recoverypasswordinput

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable
import navigationToNewPasswordInput

@Serializable
data class RecoveryPasswordInput(
    val emailAddress: String
) : Route {
    override val enableNavDrawer: Boolean = false
    override val name: String = className
}


fun NavController.navigationToRecoveryPasswordInput(emailAddress: String) =
    navigate(route = RecoveryPasswordInput(emailAddress))

fun NavGraphBuilder.recoveryPasswordInput(navController: NavController) {
    composable<RecoveryPasswordInput> {
        RecoveryPasswordInputScreen(onAuthenticated = navController::navigationToNewPasswordInput)
    }
}