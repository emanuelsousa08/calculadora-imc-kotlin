package com.example.calculadoraimc.domain.model

/** Faixas de IMC da OMS para adultos. Não conhece cores nem Compose. */
enum class FaixaImc(val rotulo: String) {
    AbaixoDoPeso("Abaixo do peso"),
    PesoNormal("Peso normal"),
    Sobrepeso("Sobrepeso"),
    ObesidadeGrauI("Obesidade grau I"),
    ObesidadeGrauII("Obesidade grau II"),
    ObesidadeGrauIII("Obesidade grau III")
}
