package com.example.calculadoraimc.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.calculadoraimc.CalculadoraApplication
import com.example.calculadoraimc.ui.historico.HistoricoViewModel
import com.example.calculadoraimc.ui.inicio.InicioViewModel

/** Ensina o Android a criar os ViewModels com o repositório do app. */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer { InicioViewModel(calculadoraApplication().historicoRepository) }
        initializer { HistoricoViewModel(calculadoraApplication().historicoRepository) }
    }
}

private fun CreationExtras.calculadoraApplication(): CalculadoraApplication =
    this[APPLICATION_KEY] as CalculadoraApplication
