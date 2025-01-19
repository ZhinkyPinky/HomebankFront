package com.example.homebankfront.feature.authentication.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.authentication.AuthenticationScreen
import kotlinx.serialization.Serializable

@Serializable
data object Authentication

fun NavGraphBuilder.authentication(
    onAuthentication: () -> Unit,
    navigateToRegistration: () -> Unit
) {
    composable<Authentication> {
        AuthenticationScreen (
            onAuthentication = onAuthentication,
            navigateToRegistration = navigateToRegistration
        )
    }
}
