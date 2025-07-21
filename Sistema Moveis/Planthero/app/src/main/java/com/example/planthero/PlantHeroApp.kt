package com.example.planthero

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.planthero.ui.components.FinalScreen
import com.example.planthero.ui.components.HomeScreen
import com.example.planthero.ui.components.PlantGameScreen
import com.example.planthero.viewmodel.GameViewModel

@Composable
fun PlantHeroApp(viewModel: GameViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onStartClick = {
                navController.navigate("game")
            })
        }
        composable("game") {
            PlantGameScreen(viewModel, navController)
        }
        composable("final") {
            FinalScreen(
                pontuacao = viewModel.pontuacao,
                onRestartClick = {
                    viewModel.reiniciarJogo()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}
