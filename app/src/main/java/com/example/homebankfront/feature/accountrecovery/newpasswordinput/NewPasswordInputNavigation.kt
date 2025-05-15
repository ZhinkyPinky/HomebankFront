import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.homebankfront.feature.utility.className
import com.example.homebankfront.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object NewPasswordInput : Route {
    override val enableNavDrawer: Boolean = false
    override val name: String = className
}


fun NavController.navigationToNewPasswordInput() = navigate(route = NewPasswordInput)

fun NavGraphBuilder.newPasswordInput(navController: NavController) {
    composable<NewPasswordInput> {
        NewPasswordInputScreen()
    }
}
