package com.example.homebankfront.ui.theme

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.homebankfront.designsystem.HomebankBackground
import com.example.homebankfront.navigation.HomeBankNavHost
import com.example.homebankfront.ui.HomebankAppState
import com.example.homebankfront.ui.rememberHomebankAppState

@Composable
fun HomebankApp(appState : HomebankAppState = rememberHomebankAppState()){
    HomebankBackground {
        Scaffold (containerColor = Color.Transparent

        ) { paddingValues ->
            HomeBankNavHost(appState = appState)
        }
    }
}