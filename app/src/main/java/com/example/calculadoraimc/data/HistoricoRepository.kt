package com.example.calculadoraimc.data

import com.example.calculadoraimc.data.local.RegistroDao
import com.example.calculadoraimc.data.local.RegistroEntity
import com.example.calculadoraimc.domain.model.RegistroImc
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/** Única porta de acesso aos dados do histórico. As telas não conhecem o Room. */
class HistoricoRepository(private val dao: RegistroDao) {

    val registros: Flow<List<RegistroImc>> =
        dao.observarTodos().map { lista -> lista.map { it.paraModelo() } }

    suspend fun salvar(peso: Double, altura: Double, imc: Double) {
        dao.inserir(
            RegistroEntity(
                peso = peso,
                altura = altura,
                imc = imc,
                dataHora = System.currentTimeMillis()
            )
        )
    }

    suspend fun excluir(id: Long) = dao.excluir(id)
}

private fun RegistroEntity.paraModelo() = RegistroImc(
    id = id,
    peso = peso,
    altura = altura,
    imc = imc,
    dataHora = dataHora
)
