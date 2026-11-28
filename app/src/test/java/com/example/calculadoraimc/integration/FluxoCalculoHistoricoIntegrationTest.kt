package com.example.calculadoraimc.integration

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.example.calculadoraimc.data.HistoricoRepository
import com.example.calculadoraimc.data.local.AppDatabase
import com.example.calculadoraimc.domain.model.FaixaImc
import com.example.calculadoraimc.testutil.MainDispatcherRule
import com.example.calculadoraimc.ui.historico.HistoricoViewModel
import com.example.calculadoraimc.ui.inicio.InicioViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/** Fluxo completo: ViewModels -> repositório -> Room (SQLite em memória). */
@RunWith(RobolectricTestRunner::class)
class FluxoCalculoHistoricoIntegrationTest {

    @get:Rule
    val mainRule = MainDispatcherRule()

    private lateinit var banco: AppDatabase
    private lateinit var repositorio: HistoricoRepository
    private lateinit var inicio: InicioViewModel
    private lateinit var historico: HistoricoViewModel
    private val store = ViewModelStore()

    @Before
    fun preparar() {
        banco = Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.getApplication(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        repositorio = HistoricoRepository(banco.registroDao())

        val fabrica = viewModelFactory {
            initializer { InicioViewModel(repositorio, atrasoCalculoMs = 0) }
            initializer { HistoricoViewModel(repositorio) }
        }
        val provider = ViewModelProvider(store, fabrica)
        inicio = provider[InicioViewModel::class.java]
        historico = provider[HistoricoViewModel::class.java]
    }

    @After
    fun encerrar() {
        store.clear() // cancela o viewModelScope antes de fechar o banco
        banco.close()
    }

    @Test
    fun calcularComDadosValidos_mostraResultadoESalvaNoHistorico() {
        inicio.aoMudarAltura("1,75")
        inicio.aoMudarPeso("70")

        inicio.calcular()

        val resultado = inicio.estado.value.resultado
        assertNotNull(resultado)
        assertEquals(22.86, resultado!!.imc, 0.0001)
        assertEquals(FaixaImc.PesoNormal, resultado.faixa)

        val registros = runBlocking {
            withTimeout(5_000) {
                historico.estado.first { it.registros.isNotEmpty() }.registros
            }
        }
        assertEquals(1, registros.size)
        assertEquals(70.0, registros[0].peso, 0.0001)
        assertEquals(1.75, registros[0].altura, 0.0001)
        assertEquals(22.86, registros[0].imc, 0.0001)
    }

    @Test
    fun alturaDigitadaSemVirgula_eInterpretadaEmMetros() {
        // a máscara guarda só os dígitos: "175" equivale a 1,75 m
        inicio.aoMudarAltura("175")
        inicio.aoMudarPeso("70")

        inicio.calcular()

        assertEquals(22.86, inicio.estado.value.resultado!!.imc, 0.0001)

        // espera o salvamento terminar antes de o banco ser fechado
        runBlocking {
            withTimeout(5_000) { repositorio.registros.first { it.isNotEmpty() } }
        }
    }

    @Test
    fun alturaForaDoIntervalo_mostraErroENaoCalcula() {
        inicio.aoMudarAltura("75") // 7,5 m
        inicio.aoMudarPeso("70")

        inicio.calcular()

        val estado = inicio.estado.value
        assertNotNull(estado.erroAltura)
        assertNull(estado.erroPeso)
        assertNull(estado.resultado)
    }

    @Test
    fun calcularComCampoVazio_mostraErroENaoSalvaNada() {
        inicio.aoMudarAltura("1.75")

        inicio.calcular()

        val estado = inicio.estado.value
        assertTrue(estado.temErro)
        assertNull(estado.erroAltura)
        assertNotNull(estado.erroPeso)
        assertEquals(1, estado.tentativasInvalidas)
        assertNull(estado.resultado)

        val registros = runBlocking {
            withTimeout(5_000) { repositorio.registros.first() }
        }
        assertTrue(registros.isEmpty())
    }

    @Test
    fun corrigirUmCampo_limpaSomenteOErroDeleMesmo() {
        inicio.calcular() // os dois campos vazios
        assertNotNull(inicio.estado.value.erroAltura)
        assertNotNull(inicio.estado.value.erroPeso)

        inicio.aoMudarAltura("175")

        assertNull(inicio.estado.value.erroAltura)
        assertNotNull(inicio.estado.value.erroPeso)
    }

    @Test
    fun novoCalculo_limpaATelaMasMantemOHistorico() {
        inicio.aoMudarAltura("1.75")
        inicio.aoMudarPeso("90")
        inicio.calcular()

        inicio.novoCalculo()

        val estado = inicio.estado.value
        assertNull(estado.resultado)
        assertEquals("", estado.altura)
        assertEquals("", estado.peso)

        val registros = runBlocking {
            withTimeout(5_000) { repositorio.registros.first { it.isNotEmpty() } }
        }
        assertEquals(1, registros.size)
    }

    @Test
    fun excluirPeloHistorico_removeOItemDoBanco() {
        runBlocking {
            repositorio.salvar(peso = 70.0, altura = 1.75, imc = 22.86)
            val id = repositorio.registros.first().single().id

            historico.excluir(id)

            val lista = withTimeout(5_000) {
                repositorio.registros.first { it.isEmpty() }
            }
            assertTrue(lista.isEmpty())
        }
    }
}
