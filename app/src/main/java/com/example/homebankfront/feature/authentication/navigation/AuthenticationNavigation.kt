package com.example.homebankfront.feature.authentication.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.authentication.AuthenticationField
import com.example.homebankfront.feature.authentication.AuthenticationScreen
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object Authentication : Route {
    override val enableNavDrawer: Boolean = false
    override val name: String = className
}

fun NavController.navigateToAuthentication(builder: NavOptionsBuilder.() -> Unit) =
    navigate(route = Authentication, builder)

fun NavGraphBuilder.authentication(
    onAuthentication: () -> Unit,
    onActivationPending: () -> Unit,
    navigateToRegistration: () -> Unit,
    navigateToRecoverUserAccount: () -> Unit
) {
    composable<Authentication> {
        AuthenticationScreen(
            onAuthentication = onAuthentication,
            onActivationPending = onActivationPending,
            navigateToRegistration = navigateToRegistration,
            navigateToRecoverUserAccount = navigateToRecoverUserAccount
        )
    }
}
