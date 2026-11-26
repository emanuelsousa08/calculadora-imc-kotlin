package com.example.calculadoraimc.domain

import com.example.calculadoraimc.domain.model.FaixaImc
import java.util.Locale
import kotlin.math.roundToLong

/** Regras de negócio do IMC: Kotlin puro, fácil de testar. */
object CalculadoraImc {

    /** Converte o texto digitado em número positivo. Aceita vírgula ou ponto. */
    fun lerNumero(texto: String): Double? =
        texto.trim()
            .replace(',', '.')
            .toDoubleOrNull()
            ?.takeIf { it.isFinite() && it > 0 }

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
