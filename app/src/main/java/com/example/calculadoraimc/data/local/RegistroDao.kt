package com.example.calculadoraimc.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroDao {

    @Query("SELECT * FROM registros ORDER BY data_hora DESC, id DESC")
    fun observarTodos(): Flow<List<RegistroEntity>>

    @Insert
    suspend fun inserir(registro: RegistroEntity): Long

    @Query("DELETE FROM registros WHERE id = :id")
    suspend fun excluir(id: Long)
}
