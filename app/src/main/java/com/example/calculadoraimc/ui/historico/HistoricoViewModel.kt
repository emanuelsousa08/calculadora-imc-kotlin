package com.example.calculadoraimc.ui.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculadoraimc.data.HistoricoRepository
import com.example.calculadoraimc.domain.model.RegistroImc
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HistoricoUiState(
    val registros: List<RegistroImc> = emptyList(),
    val carregando: Boolean = true
)

class HistoricoViewModel(
    private val repositorio: HistoricoRepository
) : ViewModel() {

    val estado: StateFlow<HistoricoUiState> = repositorio.registros
        .map { HistoricoUiState(registros = it, carregando = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HistoricoUiState()
        )

    fun excluir(id: Long) {
        viewModelScope.launch { repositorio.excluir(id) }
    }
}
