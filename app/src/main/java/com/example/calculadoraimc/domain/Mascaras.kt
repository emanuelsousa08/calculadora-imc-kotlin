package com.example.calculadoraimc.domain

/**
 * Máscaras de digitação do peso e da altura (Kotlin puro, fácil de testar).
 *
 * Altura: o estado guarda só os dígitos ("175") e a tela exibe "1,75 m".
 * Peso: o estado guarda o texto filtrado ("70,5") e a tela exibe "70,5 kg".
 */
object Mascaras {

    private const val MAX_DIGITOS_ALTURA = 3
    private const val MAX_INTEIROS_PESO = 3
    private const val MAX_DECIMAIS_PESO = 2

    /** Mantém apenas os dígitos da altura (até 3). "1,75" e "175" viram "175". */
    fun filtrarAltura(entrada: String): String =
        entrada.filter { it.isDigit() }.take(MAX_DIGITOS_ALTURA)

    /** Insere a vírgula depois do primeiro dígito: "175" -> "1,75". */
    fun alturaComVirgula(digitos: String): String =
        if (digitos.length <= 1) digitos else "${digitos.first()},${digitos.drop(1)}"

    /** Peso: dígitos e uma vírgula, até 3 inteiros e 2 decimais. O ponto vira vírgula. */
    fun filtrarPeso(entrada: String): String {
        val texto = entrada.replace('.', ',')
        val posicaoVirgula = texto.indexOf(',')

        val parteInteira = (if (posicaoVirgula >= 0) texto.substring(0, posicaoVirgula) else texto)
            .filter { it.isDigit() }
            .take(MAX_INTEIROS_PESO)

        if (posicaoVirgula < 0 || parteInteira.isEmpty()) return parteInteira

        val decimais = texto.substring(posicaoVirgula + 1)
            .filter { it.isDigit() }
            .take(MAX_DECIMAIS_PESO)

        return "$parteInteira,$decimais"
    }
}
