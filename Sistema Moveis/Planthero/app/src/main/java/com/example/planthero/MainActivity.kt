package com.example.planthero

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.planthero.ui.theme.PlantHeroTheme
import com.example.planthero.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel = GameViewModel()

        setContent {
            PlantHeroTheme {
                PlantHeroApp(viewModel)
            }
        }
    }
}

