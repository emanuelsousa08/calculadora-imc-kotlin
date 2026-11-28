package com.example.calculadoraimc.domain

import org.junit.Assert.assertEquals
import org.junit.Test

class MascarasTest {

    @Test
    fun filtrarAltura_mantemSoDigitos() {
        assertEquals("175", Mascaras.filtrarAltura("1,75"))
        assertEquals("17", Mascaras.filtrarAltura("1.7"))
        assertEquals("1", Mascaras.filtrarAltura("abc1"))
        assertEquals("", Mascaras.filtrarAltura(""))
    }

    @Test
    fun filtrarAltura_limitaATresDigitos() {
        assertEquals("123", Mascaras.filtrarAltura("12345"))
    }

    @Test
    fun alturaComVirgula_inserePosDoPrimeiroDigito() {
        assertEquals("", Mascaras.alturaComVirgula(""))
        assertEquals("1", Mascaras.alturaComVirgula("1"))
        assertEquals("1,7", Mascaras.alturaComVirgula("17"))
        assertEquals("1,75", Mascaras.alturaComVirgula("175"))
    }

    @Test
    fun filtrarPeso_aceitaInteiroEDecimal() {
        assertEquals("70", Mascaras.filtrarPeso("70"))
        assertEquals("70,5", Mascaras.filtrarPeso("70,5"))
        assertEquals("70,", Mascaras.filtrarPeso("70,"))
    }

    @Test
    fun filtrarPeso_trocaPontoPorVirgula() {
        assertEquals("70,5", Mascaras.filtrarPeso("70.5"))
    }

    @Test
    fun filtrarPeso_limitaInteirosEDecimais() {
        assertEquals("705", Mascaras.filtrarPeso("7054"))
        assertEquals("70,56", Mascaras.filtrarPeso("70,567"))
    }

    @Test
    fun filtrarPeso_ignoraVirgulaSemInteiroEVirgulasRepetidas() {
        assertEquals("", Mascaras.filtrarPeso(",5"))
        assertEquals("1,23", Mascaras.filtrarPeso("1,2,3"))
        assertEquals("", Mascaras.filtrarPeso("abc"))
    }
}
