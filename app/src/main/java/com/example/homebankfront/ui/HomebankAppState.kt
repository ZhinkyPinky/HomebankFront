package com.example.homebankfront.ui

import android.content.Context
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberHomebankAppState(
    navController: NavHostController = rememberNavController(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    context: Context = LocalContext.current
) = remember(
    navController,
    drawerState,
    context
) {
    HomebankAppState(
        navController = navController,
        drawerState = drawerState,
        context = context
    )
}

class HomebankAppState(
    val navController: NavHostController,
    val drawerState: DrawerState,
    val context: Context
)