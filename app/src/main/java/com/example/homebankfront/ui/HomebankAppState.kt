package com.example.homebankfront.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberHomebankAppState(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current
) = remember(
    navController,
    context
) {
    HomebankAppState(
        navController = navController,
        context = context
    )
}

class HomebankAppState(
    val navController: NavHostController,
    val context: Context
) {
    fun navigateBack() {
        when (!navController.popBackStack()) {
            true -> navController.popBackStack()
            false -> {
            }
        }
    }
}