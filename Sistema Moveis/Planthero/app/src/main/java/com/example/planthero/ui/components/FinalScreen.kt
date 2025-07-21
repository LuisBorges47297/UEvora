package com.example.planthero.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.planthero.R

@Composable
fun FinalScreen(pontuacao: Int, onRestartClick: () -> Unit) {
    val imagemFinal = if (pontuacao > 3) R.drawable.planta_feliz else R.drawable.planta_triste

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Fim de Jogo!",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Pontuação Final: $pontuacao",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Opcional: imagem de celebração
        Image(
            painter = painterResource(id = imagemFinal), // adiciona esta imagem à pasta drawable
            contentDescription = "Imagem Final",
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onRestartClick) {
            Text(text = "Jogar Novamente")
        }
    }
}
