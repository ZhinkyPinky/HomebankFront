package com.example.homebankfront.feature.changePassword.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.changePassword.ChangePasswordScreen
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object ChangePassword : Route {
    override val enableNavDrawer: Boolean = true
    override val name: String = className
}

fun NavController.navigateToChangePassword() = navigate(route = ChangePassword)

fun NavGraphBuilder.changePasswordScreen(onChangedPassword: () -> Unit) {
    composable<ChangePassword> {
        ChangePasswordScreen(onChangedPassword = onChangedPassword)
    }
}
