package com.example.calculadoraimc.integration

import androidx.room.Room
import com.example.calculadoraimc.data.local.AppDatabase
import com.example.calculadoraimc.data.local.RegistroDao
import com.example.calculadoraimc.data.local.RegistroEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
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

/** Room + SQLite de verdade (banco em memória), sem emulador. */
@RunWith(RobolectricTestRunner::class)
class RegistroDaoIntegrationTest {

    private lateinit var banco: AppDatabase
    private lateinit var dao: RegistroDao

    @Before
    fun criarBanco() {
        banco = Room.inMemoryDatabaseBuilder(
            RuntimeEnvironment.getApplication(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = banco.registroDao()
    }

    @After
    fun fecharBanco() {
        banco.close()
    }

    private fun registro(imc: Double, dataHora: Long) = RegistroEntity(
        peso = 70.0,
        altura = 1.75,
        imc = imc,
        dataHora = dataHora
    )

    @Test
    fun bancoNovo_comecaVazio() {
        runBlocking {
            assertTrue(dao.observarTodos().first().isEmpty())
        }
    }

    @Test
    fun inserir_geraIdEMantemOsDados() {
        runBlocking {
            val id = dao.inserir(registro(imc = 22.86, dataHora = 1_000))

            val lista = dao.observarTodos().first()

            assertEquals(1, lista.size)
            assertTrue(id > 0)
            assertEquals(id, lista[0].id)
            assertEquals(70.0, lista[0].peso, 0.0001)
            assertEquals(1.75, lista[0].altura, 0.0001)
            assertEquals(22.86, lista[0].imc, 0.0001)
            assertEquals(1_000L, lista[0].dataHora)
        }
    }

    @Test
    fun lista_vemDoMaisRecenteParaOMaisAntigo() {
        runBlocking {
            dao.inserir(registro(imc = 20.0, dataHora = 1_000))
            dao.inserir(registro(imc = 30.0, dataHora = 3_000))
            dao.inserir(registro(imc = 25.0, dataHora = 2_000))

            val datas = dao.observarTodos().first().map { it.dataHora }

            assertEquals(listOf(3_000L, 2_000L, 1_000L), datas)
        }
    }

    @Test
    fun excluir_removeSomenteORegistroIndicado() {
        runBlocking {
            val primeiro = dao.inserir(registro(imc = 20.0, dataHora = 1_000))
            val segundo = dao.inserir(registro(imc = 30.0, dataHora = 2_000))

            dao.excluir(primeiro)

            val lista = dao.observarTodos().first()
            assertEquals(1, lista.size)
            assertEquals(segundo, lista[0].id)
        }
    }

    @Test
    fun fluxo_emiteNovaListaAposInserir() {
        runBlocking {
            val aguardando = async(Dispatchers.IO) {
                dao.observarTodos().first { it.size == 2 }
            }

            dao.inserir(registro(imc = 20.0, dataHora = 1_000))
            dao.inserir(registro(imc = 30.0, dataHora = 2_000))

            val lista = withTimeout(5_000) { aguardando.await() }
            assertEquals(2, lista.size)
        }
    }
}
