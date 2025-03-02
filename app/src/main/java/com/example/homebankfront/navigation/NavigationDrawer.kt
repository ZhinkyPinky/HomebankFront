package com.example.homebankfront.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.homebankfront.AppEvent
import com.example.homebankfront.AppEvent.SignOut
import com.example.homebankfront.R
import com.example.homebankfront.ui.HomebankAppState
import kotlinx.coroutines.launch

fun NavDestination.routeSubstring(): String? =
    route?.substringAfterLast(".")?.substringBefore("/")?.substringBefore("?")

@Composable
fun NavigationDrawer(
    appState: HomebankAppState,
    onEvent: (AppEvent) -> Unit,
    content: @Composable () -> Unit
) {
    val navController = appState.navController
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val routeSubstring = currentDestination?.routeSubstring()
    val route = Route.fromString(routeSubstring)

    if (route?.enableNavDrawer == true) {
        val coroutineScope = rememberCoroutineScope()

        BackHandler(appState.drawerState.isOpen) {
            coroutineScope.launch { appState.drawerState.close() }
        }

        ModalNavigationDrawer(
            drawerState = appState.drawerState,
            gesturesEnabled = route.enableNavDrawer,
            drawerContent = {
                DrawerContent(
                    navController = navController,
                    onEvent = onEvent,
                    closeDrawer = { coroutineScope.launch { appState.drawerState.close() } },
                    snapDrawerClosed = {
                        coroutineScope.launch {
                            appState.drawerState.snapTo(
                                DrawerValue.Closed
                            )
                        }
                    }
                )
            },
            content = { content() }
        )
    } else {
        content()
    }
}

@Composable
fun DrawerContent(
    navController: NavController,
    onEvent: (AppEvent) -> Unit,
    closeDrawer: () -> Unit,
    snapDrawerClosed: () -> Unit
) {
    ModalDrawerSheet(drawerShape = MaterialTheme.shapes.large.copy(CornerSize(0))) {
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.customers)) },
            selected = false,
            onClick = {
                navController.navigateToAuthenticated {
                    popUpTo(0)
                    launchSingleTop = true
                }

                closeDrawer()
            }
        )

        NavigationDrawerItem(
            label = { Text(stringResource(R.string.sign_out)) },
            selected = false,
            onClick = {
                onEvent(SignOut)

                navController.navigateToUnauthenticated {
                    popUpTo(0)
                    launchSingleTop = true
                }

                snapDrawerClosed()
            }
        )
    }
}