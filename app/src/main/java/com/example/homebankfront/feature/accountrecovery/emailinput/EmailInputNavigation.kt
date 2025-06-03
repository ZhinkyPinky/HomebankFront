package com.example.homebankfront.feature.accountrecovery.emailinput

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.accountrecovery.onetimepasswordinput.navigationToOneTimePasswordInput
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data class AccountRecoveryEmailInput(val emailAddress: String?) : Route {
    override val enableNavDrawer: Boolean = false
    override val name: String = className
}

fun NavController.navigateToAccountRecoveryEmailInput(emailAddress: String? = null) =
    navigate(route = AccountRecoveryEmailInput(emailAddress))

fun NavGraphBuilder.accountRecoveryEmailInput(navController: NavController) {
    composable<AccountRecoveryEmailInput> {
        AccountRecoveryEmailInputScreen(onRecoveryInitiated = { emailAddress: String ->
            navController.navigationToOneTimePasswordInput(emailAddress)
        })
    }
}
