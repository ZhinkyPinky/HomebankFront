package com.example.homebankfront.feature.registration.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.authentication.navigation.navigateToAuthentication
import com.example.homebankfront.feature.registration.RegistrationScreen
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object Registration : Route {
    override val enableNavDrawer: Boolean = false
    override val name: String = className
}

fun NavController.navigateToRegistration() = navigate(route = Registration)

fun NavGraphBuilder.registration(
    navController: NavController
) {
    composable<Registration> {
        RegistrationScreen(onRegistration = {
            navController.navigateToAuthentication { popUpTo(0) }
        })
    }
}
