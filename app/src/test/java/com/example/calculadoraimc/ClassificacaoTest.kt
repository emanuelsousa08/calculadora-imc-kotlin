package com.example.calculadoraimc

import org.junit.Assert.assertEquals
import org.junit.Test

class ClassificacaoTest {

    @Test
    fun abaixoDoPeso() {
        assertEquals("Abaixo do peso", classificarImc(18.49).texto)
    }

    @Test
    fun pesoNormal_nosLimites() {
        assertEquals("Peso normal", classificarImc(18.5).texto)
        assertEquals("Peso normal", classificarImc(24.99).texto)
    }

    @Test
    fun sobrepeso_nosLimites() {
        assertEquals("Sobrepeso", classificarImc(25.0).texto)
        assertEquals("Sobrepeso", classificarImc(29.99).texto)
    }

    @Test
    fun obesidadeGrauI() {
        assertEquals("Obesidade grau I", classificarImc(30.0).texto)
        assertEquals("Obesidade grau I", classificarImc(34.99).texto)
    }

    @Test
    fun obesidadeGrauII() {
        assertEquals("Obesidade grau II", classificarImc(35.0).texto)
        assertEquals("Obesidade grau II", classificarImc(39.99).texto)
    }

    @Test
    fun obesidadeGrauIII() {
        assertEquals("Obesidade grau III", classificarImc(40.0).texto)
        assertEquals("Obesidade grau III", classificarImc(55.0).texto)
    }
}