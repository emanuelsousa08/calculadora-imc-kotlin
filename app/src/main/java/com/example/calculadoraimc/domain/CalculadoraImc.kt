package com.example.calculadoraimc.domain

import com.example.calculadoraimc.domain.model.FaixaImc
import java.util.Locale
import kotlin.math.roundToLong

/** Regras de negócio do IMC: Kotlin puro, fácil de testar. */
object CalculadoraImc {

    const val ALTURA_MIN = 0.50
    const val ALTURA_MAX = 2.75
    const val PESO_MIN = 2.0
    const val PESO_MAX = 500.0

    /** Converte o texto digitado em número positivo. Aceita vírgula ou ponto. */
    fun lerNumero(texto: String): Double? =
        texto.trim()
            .replace(',', '.')
            .toDoubleOrNull()
            ?.takeIf { it.isFinite() && it > 0 }

    /** Altura em metros, entre 0,50 e 2,75. Evita casos como "175" virar 175 m. */
    fun lerAltura(texto: String): Double? =
        lerNumero(texto)?.takeIf { it in ALTURA_MIN..ALTURA_MAX }

    /** Peso em quilos, entre 2 e 500. */
    fun lerPeso(texto: String): Double? =
        lerNumero(texto)?.takeIf { it in PESO_MIN..PESO_MAX }

    /** IMC = peso / altura², arredondado para 2 casas decimais. */
    fun calcular(peso: Double, altura: Double): Double {
        val imc = peso / (altura * altura)
        return (imc * 100).roundToLong() / 100.0
    }

    fun classificar(imc: Double): FaixaImc = when {
        imc < 18.5 -> FaixaImc.AbaixoDoPeso
        imc < 25.0 -> FaixaImc.PesoNormal
        imc < 30.0 -> FaixaImc.Sobrepeso
        imc < 35.0 -> FaixaImc.ObesidadeGrauI
        imc < 40.0 -> FaixaImc.ObesidadeGrauII
        else -> FaixaImc.ObesidadeGrauIII
    }

    /** Texto com 2 casas decimais e ponto, como o app original. */
    fun formatar(valor: Double): String = String.format(Locale.US, "%.2f", valor)
}
