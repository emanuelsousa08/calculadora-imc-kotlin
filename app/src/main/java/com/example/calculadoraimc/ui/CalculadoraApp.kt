package com.example.calculadoraimc.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.calculadoraimc.ui.abertura.TelaAbertura
import com.example.calculadoraimc.ui.components.BarraFlutuante
import com.example.calculadoraimc.ui.historico.TelaHistorico
import com.example.calculadoraimc.ui.informacoes.TelaInformacoes
import com.example.calculadoraimc.ui.inicio.TelaInicio
import kotlinx.coroutines.delay

private const val DURACAO_ABERTURA_MS = 1500L

@Composable
fun Raiz() {
    // rememberSaveable evita repetir a abertura ao girar a tela
    var carregando by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (carregando) {
            delay(DURACAO_ABERTURA_MS)
            carregando = false
        }
    }

    Crossfade(targetState = carregando, label = "abertura") { emAbertura ->
        if (emAbertura) TelaAbertura() else CalculadoraApp()
    }
}

@Composable
fun CalculadoraApp() {
    var tela by rememberSaveable { mutableStateOf(Tela.Inicio) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0))
    ) {
        Crossfade(targetState = tela, animationSpec = tween(250), label = "tela") { atual ->
            when (atual) {
                Tela.Inicio -> TelaInicio(modifier = Modifier.fillMaxSize())
                Tela.Historico -> TelaHistorico(modifier = Modifier.fillMaxSize())
                Tela.Informacoes -> TelaInformacoes(modifier = Modifier.fillMaxSize())
            }
        }

        BarraFlutuante(
            telaAtual = tela,
            aoSelecionar = { tela = it },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
