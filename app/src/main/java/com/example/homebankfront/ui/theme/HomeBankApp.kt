package com.example.homebankfront.ui.theme

import androidx.compose.runtime.Composable
import com.example.homebankfront.AppEvent
import com.example.homebankfront.navigation.NavigationDrawer
import com.example.homebankfront.navigation.HomeBankNavHost
import com.example.homebankfront.ui.HomebankAppState
import com.example.homebankfront.ui.components.HomeBankBackground
import com.example.homebankfront.ui.rememberHomebankAppState


@Composable
fun HomeBankApp(
    appState: HomebankAppState = rememberHomebankAppState(),
    onEvent: (AppEvent) -> Unit
) {
    HomeBankBackground {
        NavigationDrawer(
            appState = appState,
            onEvent = onEvent,
        ) {
            HomeBankNavHost(appState = appState)
        }
    }
}