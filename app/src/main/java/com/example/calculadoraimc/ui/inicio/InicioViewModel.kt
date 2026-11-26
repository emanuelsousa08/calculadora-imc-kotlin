package com.example.calculadoraimc.ui.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculadoraimc.data.HistoricoRepository
import com.example.calculadoraimc.domain.CalculadoraImc
import com.example.calculadoraimc.domain.model.FaixaImc
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ResultadoImc(val imc: Double, val faixa: FaixaImc)

data class InicioUiState(
    val altura: String = "",
    val peso: String = "",
    val erroCampos: Boolean = false,
    /** Sobe a cada tentativa inválida; a tela usa para disparar a vibração. */
    val tentativasInvalidas: Int = 0,
    val resultado: ResultadoImc? = null
)

class InicioViewModel(
    private val repositorio: HistoricoRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(InicioUiState())
    val estado: StateFlow<InicioUiState> = _estado.asStateFlow()

    fun aoMudarAltura(valor: String) {
        _estado.update { it.copy(altura = valor) }
    }

    fun aoMudarPeso(valor: String) {
        _estado.update { it.copy(peso = valor) }
    }

    fun calcular() {
        val atual = _estado.value
        val altura = CalculadoraImc.lerNumero(atual.altura)
        val peso = CalculadoraImc.lerNumero(atual.peso)

        if (altura == null || peso == null) {
            _estado.update {
                it.copy(erroCampos = true, tentativasInvalidas = it.tentativasInvalidas + 1)
            }
            return
        }

        val imc = CalculadoraImc.calcular(peso, altura)
        _estado.value = InicioUiState(
            resultado = ResultadoImc(imc, CalculadoraImc.classificar(imc))
        )
        viewModelScope.launch { repositorio.salvar(peso, altura, imc) }
    }

    fun novoCalculo() {
        _estado.value = InicioUiState()
    }
}
