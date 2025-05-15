package com.example.homebankfront.feature.accountrecovery.confirmationemailrequest

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.accountrecovery.onetimepasswordinput.navigationToOneTimePasswordInput
import com.example.homebankfront.feature.authentication.navigation.navigateToAuthentication
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object ConfirmationEmailRequest : Route {
    override val enableNavDrawer: Boolean = false
    override val name: String = className
}

fun NavController.navigateToConfirmationEmailRequest() = navigate(route = ConfirmationEmailRequest)

fun NavGraphBuilder.confirmationEmailRequest(navController: NavController) {
    composable<ConfirmationEmailRequest> {
        ConfirmationEmailRequestScreen(onRecoveryInitiated = {
            navController.navigationToOneTimePasswordInput()
        })
    }
}
