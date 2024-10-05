package com.example.homebankfront.ui.theme

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.homebankfront.designsystem.HomeBankBackground
import com.example.homebankfront.navigation.HomeBankNavHost
import com.example.homebankfront.ui.HomebankAppState
import com.example.homebankfront.ui.rememberHomebankAppState

@Composable
fun HomeBankApp(appState : HomebankAppState = rememberHomebankAppState()){
    HomeBankBackground {
        Scaffold (containerColor = Color.Transparent

        ) { paddingValues ->
            HomeBankNavHost(appState = appState)
        }
    }
}