package com.example.calculadoraimc.ui.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculadoraimc.data.HistoricoRepository
import com.example.calculadoraimc.domain.CalculadoraImc
import com.example.calculadoraimc.domain.Mascaras
import com.example.calculadoraimc.domain.model.FaixaImc
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val MSG_OBRIGATORIO = "* Campo obrigatório"
private const val MSG_ALTURA = "* Informe entre 0,50 m e 2,75 m"
private const val MSG_PESO = "* Informe entre 2 kg e 500 kg"

data class ResultadoImc(val imc: Double, val faixa: FaixaImc)

data class InicioUiState(
    /** Dígitos digitados: "175" representa 1,75 m. */
    val altura: String = "",
    /** Texto do peso já filtrado: "70,5". */
    val peso: String = "",
    val erroAltura: String? = null,
    val erroPeso: String? = null,
    /** Sobe a cada tentativa inválida; a tela usa para vibrar e tremer os campos. */
    val tentativasInvalidas: Int = 0,
    /** Verdadeiro durante a breve animação de carregamento do botão. */
    val calculando: Boolean = false,
    val resultado: ResultadoImc? = null
) {
    val temErro: Boolean get() = erroAltura != null || erroPeso != null
}

class InicioViewModel(
    private val repositorio: HistoricoRepository,
    /** Tempo do indicador de carregamento. Os testes usam 0. */
    private val atrasoCalculoMs: Long = 600L
) : ViewModel() {

    private val _estado = MutableStateFlow(InicioUiState())
    val estado: StateFlow<InicioUiState> = _estado.asStateFlow()

    fun aoMudarAltura(valor: String) {
        _estado.update { it.copy(altura = Mascaras.filtrarAltura(valor), erroAltura = null) }
    }

    fun aoMudarPeso(valor: String) {
        _estado.update { it.copy(peso = Mascaras.filtrarPeso(valor), erroPeso = null) }
    }

    fun calcular() {
        val atual = _estado.value
        if (atual.calculando) return
        val altura = CalculadoraImc.lerAltura(Mascaras.alturaComVirgula(atual.altura))
        val peso = CalculadoraImc.lerPeso(atual.peso)

        if (altura == null || peso == null) {
            _estado.update {
                it.copy(
                    erroAltura = when {
                        atual.altura.isEmpty() -> MSG_OBRIGATORIO
                        altura == null -> MSG_ALTURA
                        else -> null
                    },
                    erroPeso = when {
                        atual.peso.isEmpty() -> MSG_OBRIGATORIO
                        peso == null -> MSG_PESO
                        else -> null
                    },
                    tentativasInvalidas = it.tentativasInvalidas + 1
                )
            }
            return
        }

        _estado.update { it.copy(calculando = true, erroAltura = null, erroPeso = null) }

        viewModelScope.launch {
            delay(atrasoCalculoMs)
            val imc = CalculadoraImc.calcular(peso, altura)
            _estado.value = InicioUiState(
                resultado = ResultadoImc(imc, CalculadoraImc.classificar(imc))
            )
            repositorio.salvar(peso, altura, imc)
        }
    }

    fun novoCalculo() {
        _estado.value = InicioUiState()
    }
}
