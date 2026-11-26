package com.example.calculadoraimc.ui

import androidx.annotation.DrawableRes
import com.example.calculadoraimc.R

enum class Tela(val rotulo: String, @param:DrawableRes val icone: Int) {
    Inicio("Início", R.drawable.ic_home),
    Historico("Histórico", R.drawable.ic_history),
    Informacoes("Informações", R.drawable.ic_info)
}
