package com.example.homebankfront

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import com.example.homebankfront.ui.theme.HomeBankApp
import com.example.homebankfront.ui.theme.HomeBankFrontTheme
import dagger.hilt.android.AndroidEntryPoint

val LocalSnackHostState =
    compositionLocalOf<SnackbarHostState> { error("No snackbar host state found") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            HomeBankFrontTheme {
                val snackbarHostState = remember { SnackbarHostState() }

                CompositionLocalProvider(value = LocalSnackHostState provides snackbarHostState) {
                    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { _ ->
                        Surface {
                            HomeBankApp()
                        }
                    }
                }
            }
        }
    }
}