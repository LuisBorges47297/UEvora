package com.example.planthero.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.planthero.data.PerguntasPlantas
import com.example.planthero.data.PlantasPerguntas


class GameViewModel: ViewModel() {
    private var perguntas = PerguntasPlantas.listaPerguntas.shuffled()

    var indexPerguntaAtual by mutableStateOf(0)
        private set

    var pontuacao by mutableStateOf(0)
        private set

    var respostaSelecionada by mutableStateOf<String?>(null)
        private set

    val perguntasAtual: PlantasPerguntas
        get() = perguntas[indexPerguntaAtual]

    fun selecionarResposta(resposta: String) {
        respostaSelecionada = resposta
        if (resposta == perguntasAtual.correctAnswer){
            pontuacao++
        }
    }

    fun proximaPergunta(){
        if (indexPerguntaAtual < perguntas.size){
            indexPerguntaAtual++
            respostaSelecionada = null
        }
    }

    fun reiniciarJogo(){
        perguntas = PerguntasPlantas.listaPerguntas.shuffled()
        indexPerguntaAtual = 0
        pontuacao = 0
        respostaSelecionada = null
    }

    fun jogoTerminado(): Boolean{
        return indexPerguntaAtual >= perguntas.size - 1 && respostaSelecionada != null
    }
}