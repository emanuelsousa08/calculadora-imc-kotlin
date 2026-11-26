package com.example.calculadoraimc.ui.components

import androidx.compose.ui.graphics.Color
import com.example.calculadoraimc.domain.model.FaixaImc

/** A cor é decisão da interface; o domínio só conhece a faixa. */
fun FaixaImc.cor(): Color = when (this) {
    FaixaImc.AbaixoDoPeso -> Color(0xFF2E86DE)
    FaixaImc.PesoNormal -> Color(0xFF10AC84)
    FaixaImc.Sobrepeso -> Color(0xFFFF9F43)
    FaixaImc.ObesidadeGrauI -> Color(0xFFEE5253)
    FaixaImc.ObesidadeGrauII -> Color(0xFFEA2027)
    FaixaImc.ObesidadeGrauIII -> Color(0xFFB71C1C)
}
