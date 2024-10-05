package com.example.homebankfront

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.homebankfront.ui.theme.HomeBankApp
import com.example.homebankfront.ui.theme.HomeBankFrontTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState : Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            HomeBankFrontTheme {
                HomeBankApp()
            }
        }
    }
}