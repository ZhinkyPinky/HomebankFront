package com.example.homebankfront

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.example.homebankfront.ui.theme.HomeBankApp
import com.example.homebankfront.ui.theme.HomeBankFrontTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            HomeBankFrontTheme {
                HomeBankApp()
            }
        }
    }
}