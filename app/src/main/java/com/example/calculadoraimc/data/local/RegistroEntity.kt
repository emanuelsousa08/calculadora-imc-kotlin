package com.example.calculadoraimc.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/** Linha da tabela "registros" no banco SQLite. */
@Entity(tableName = "registros")
data class RegistroEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val peso: Double,
    val altura: Double,
    val imc: Double,
    @ColumnInfo(name = "data_hora") val dataHora: Long
)
