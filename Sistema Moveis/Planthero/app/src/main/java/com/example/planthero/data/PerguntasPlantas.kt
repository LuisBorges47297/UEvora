package com.example.planthero.data

import com.example.planthero.R

object PerguntasPlantas {
    val listaPerguntas = listOf (
        PlantasPerguntas(
            imageResId = R.drawable.manjericao,
            correctAnswer = "Manjericao",
            options = listOf("Manjericao", "Hortelã", "Alecrim", "Salsa")
        ),
        PlantasPerguntas(
            imageResId = R.drawable.cacto,
            correctAnswer = "Cacto",
            options = listOf("Cacto", "Suculenta", "Babosa", "Samambaia")
        ),
        PlantasPerguntas(
            imageResId = R.drawable.lavanda,
            correctAnswer = "Lavanda",
            options = listOf("Manjericao", "Lavanda", "Alfazema", "Salsa")
        ),
        PlantasPerguntas(
            imageResId = R.drawable.alecrim,
            correctAnswer = "Alecrim",
            options = listOf("Alecrim", "Tomilho", "Manjerona", "Hortelã")
        ),
        PlantasPerguntas(
            imageResId = R.drawable.hortela,
            correctAnswer = "Hortelã",
            options = listOf("Manjericao", "Hortelã", "Cebolinho", "Salsa")
        ),

    )
}