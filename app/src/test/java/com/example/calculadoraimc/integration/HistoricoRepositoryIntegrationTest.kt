package com.example.calculadoraimc.integration

import androidx.room.Room
import com.example.calculadoraimc.data.HistoricoRepository
import com.example.calculadoraimc.data.local.AppDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/** Repositório + DAO + banco funcionando juntos. */
@RunWith(RobolectricTestRunner::class)
class HistoricoRepositoryIntegrationTest {

    private lateinit var banco: AppDatabase
    private lateinit var repositorio: HistoricoRepository

    @Before
    fun preparar() {
        banco = Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.getApplication(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        repositorio = HistoricoRepository(banco.registroDao())
    }

    @After
    fun encerrar() {
        banco.close()
    }

    @Test
    fun salvar_expoORegistroComoModeloDoDominio() {
        runBlocking {
            repositorio.salvar(peso = 70.0, altura = 1.75, imc = 22.86)

            val lista = repositorio.registros.first()

            assertEquals(1, lista.size)
            assertEquals(70.0, lista[0].peso, 0.0001)
            assertEquals(1.75, lista[0].altura, 0.0001)
            assertEquals(22.86, lista[0].imc, 0.0001)
            assertTrue(lista[0].dataHora > 0)
        }
    }

    @Test
    fun excluir_removeSomenteOItemEscolhido() {
        runBlocking {
            repositorio.salvar(peso = 55.0, altura = 1.75, imc = 17.96)
            repositorio.salvar(peso = 90.0, altura = 1.75, imc = 29.39)

            val antes = repositorio.registros.first()
            assertEquals(2, antes.size)
            val idParaExcluir = antes.first { it.imc == 17.96 }.id

            repositorio.excluir(idParaExcluir)

            val depois = withTimeout(5_000) {
                repositorio.registros.first { it.size == 1 }
            }
            assertEquals(29.39, depois[0].imc, 0.0001)
        }
    }
}
