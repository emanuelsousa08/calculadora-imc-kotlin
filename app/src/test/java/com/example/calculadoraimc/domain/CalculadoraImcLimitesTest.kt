package com.example.calculadoraimc.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class CalculadoraImcLimitesTest {

    @Test
    fun lerAltura_aceitaValoresRealistas() {
        assertEquals(1.75, CalculadoraImc.lerAltura("1,75")!!, 0.0001)
        assertNotNull(CalculadoraImc.lerAltura("0,50"))
        assertNotNull(CalculadoraImc.lerAltura("2,75"))
    }

    @Test
    fun lerAltura_rejeitaForaDoIntervalo() {
        assertNull(CalculadoraImc.lerAltura("0,49"))
        assertNull(CalculadoraImc.lerAltura("2,76"))
    }

    @Test
    fun lerAltura_rejeitaDigitosSemVirgula() {
        // "175" sem máscara viraria 175 m e daria IMC 0,00
        assertNull(CalculadoraImc.lerAltura("175"))
    }

    @Test
    fun lerPeso_aceitaValoresRealistas() {
        assertEquals(70.0, CalculadoraImc.lerPeso("70")!!, 0.0001)
        assertNotNull(CalculadoraImc.lerPeso("2"))
        assertNotNull(CalculadoraImc.lerPeso("500"))
    }

    @Test
    fun lerPeso_rejeitaForaDoIntervalo() {
        assertNull(CalculadoraImc.lerPeso("1"))
        assertNull(CalculadoraImc.lerPeso("501"))
        assertNull(CalculadoraImc.lerPeso(""))
    }
}
