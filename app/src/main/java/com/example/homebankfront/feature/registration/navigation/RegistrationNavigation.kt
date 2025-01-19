package com.example.homebankfront.feature.registration.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.registration.RegistrationScreen
import kotlinx.serialization.Serializable

@Serializable
data object Registration

fun NavController.navigateToRegistration() = navigate(route = Registration)

fun NavGraphBuilder.registration(
    onRegistration: () -> Unit
) {
    composable<Registration> {
        RegistrationScreen(onRegistration = onRegistration)
    }
}
