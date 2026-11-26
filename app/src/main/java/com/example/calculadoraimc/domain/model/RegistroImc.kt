package com.example.calculadoraimc.domain.model

data class RegistroImc(
    val id: Long,
    val peso: Double,
    val altura: Double,
    val imc: Double,
    val dataHora: Long
)
