package com.example.calculadoraimc

import android.app.Application
import com.example.calculadoraimc.data.HistoricoRepository
import com.example.calculadoraimc.data.local.AppDatabase

/**
 * Cria as dependências compartilhadas do app (injeção manual, sem biblioteca).
 */
class CalculadoraApplication : Application() {

    val historicoRepository: HistoricoRepository by lazy {
        HistoricoRepository(AppDatabase.criar(this).registroDao())
    }
}
