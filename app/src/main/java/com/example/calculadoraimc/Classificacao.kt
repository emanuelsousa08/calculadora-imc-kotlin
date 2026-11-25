package com.example.calculadoraimc

import androidx.compose.ui.graphics.Color

data class Classificacao(val texto: String, val cor: Color)

// Faixas da OMS para adultos
fun classificarImc(imc: Double): Classificacao = when {
    imc < 18.5 -> Classificacao("Abaixo do peso", Color(0xFF2E86DE))
    imc < 25.0 -> Classificacao("Peso normal", Color(0xFF10AC84))
    imc < 30.0 -> Classificacao("Sobrepeso", Color(0xFFFF9F43))
    imc < 35.0 -> Classificacao("Obesidade grau I", Color(0xFFEE5253))
    imc < 40.0 -> Classificacao("Obesidade grau II", Color(0xFFEA2027))
    else -> Classificacao("Obesidade grau III", Color(0xFFB71C1C))
}