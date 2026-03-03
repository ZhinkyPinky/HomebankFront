import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.accountActivationPending.navigation.navigateToAccountActivationPending
import com.example.homebankfront.feature.customerList.navigation.navigateToCustomerList
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data class NewPasswordInput(val recoveryToken: String) : Route {
    override val enableNavDrawer: Boolean = false
    override val name: String = className
}


fun NavController.navigationToNewPasswordInput(recoveryToken: String) =
    navigate(route = NewPasswordInput(recoveryToken))

fun NavGraphBuilder.newPasswordInput(navController: NavController) {
    composable<NewPasswordInput> {
        NewPasswordInputScreen(
            onNewPasswordSet = navController::navigateToCustomerList,
            onActivationPending = navController::navigateToAccountActivationPending,
        )
    }
}
