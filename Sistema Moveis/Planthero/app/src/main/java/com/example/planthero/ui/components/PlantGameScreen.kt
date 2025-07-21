package com.example.planthero.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.planthero.viewmodel.GameViewModel
import androidx.compose.material3.MaterialTheme


@Composable
fun PlantGameScreen(viewModel: GameViewModel,  navController: NavController) {
    val pergunta = viewModel.perguntasAtual
    val respostaSelecionada = viewModel.respostaSelecionada
    val jogoTerminado = viewModel.jogoTerminado()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Pontuacao: ${viewModel.pontuacao}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = pergunta.imageResId),
            contentDescription = "Imagen da Planta",
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        pergunta.options.forEach { opcao ->
            Button(
                onClick = { viewModel.selecionarResposta(opcao) },
                enabled = respostaSelecionada == null,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(text = opcao)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (respostaSelecionada != null) {
            val correta = respostaSelecionada == pergunta.correctAnswer
            val mensagem = if (correta) "Correto!" else "Errado! Era: ${pergunta.correctAnswer}"

            Text(
                text = mensagem,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (jogoTerminado) navController.navigate("final")
                    else viewModel.proximaPergunta()
                },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = if (jogoTerminado) "Reiniciar Jogo" else "Proxima Pergunta")
            }
        }
    }
}

