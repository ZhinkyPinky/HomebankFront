package com.example.homebankfront.feature.accountrecovery.onetimepasswordinput

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable
import navigationToNewPasswordInput

@Serializable
data object OneTimePasswordInput : Route {
    override val enableNavDrawer: Boolean = false
    override val name: String = className
}


fun NavController.navigationToOneTimePasswordInput() = navigate(route = OneTimePasswordInput)

fun NavGraphBuilder.oneTimePasswordInput(navController: NavController) {
    composable<OneTimePasswordInput> {
        OneTimePasswordInputScreen(onAuthenticated = { navController.navigationToNewPasswordInput() })
    }
}