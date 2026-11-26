package com.example.calculadoraimc.domain

import com.example.calculadoraimc.domain.model.FaixaImc
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CalculadoraImcTest {

    @Test
    fun calcular_arredondaParaDuasCasas() {
        assertEquals(22.86, CalculadoraImc.calcular(70.0, 1.75), 0.0001)
        assertEquals(29.39, CalculadoraImc.calcular(90.0, 1.75), 0.0001)
        assertEquals(17.96, CalculadoraImc.calcular(55.0, 1.75), 0.0001)
    }

    @Test
    fun classificar_abaixoDoPeso() {
        assertEquals(FaixaImc.AbaixoDoPeso, CalculadoraImc.classificar(18.49))
    }

    @Test
    fun classificar_pesoNormal_nosLimites() {
        assertEquals(FaixaImc.PesoNormal, CalculadoraImc.classificar(18.5))
        assertEquals(FaixaImc.PesoNormal, CalculadoraImc.classificar(24.99))
    }

    @Test
    fun classificar_sobrepeso_nosLimites() {
        assertEquals(FaixaImc.Sobrepeso, CalculadoraImc.classificar(25.0))
        assertEquals(FaixaImc.Sobrepeso, CalculadoraImc.classificar(29.99))
    }

    @Test
    fun classificar_obesidade() {
        assertEquals(FaixaImc.ObesidadeGrauI, CalculadoraImc.classificar(30.0))
        assertEquals(FaixaImc.ObesidadeGrauI, CalculadoraImc.classificar(34.99))
        assertEquals(FaixaImc.ObesidadeGrauII, CalculadoraImc.classificar(35.0))
        assertEquals(FaixaImc.ObesidadeGrauII, CalculadoraImc.classificar(39.99))
        assertEquals(FaixaImc.ObesidadeGrauIII, CalculadoraImc.classificar(40.0))
        assertEquals(FaixaImc.ObesidadeGrauIII, CalculadoraImc.classificar(55.0))
    }

    @Test
    fun lerNumero_aceitaVirgulaEPonto() {
        assertEquals(1.75, CalculadoraImc.lerNumero("1,75")!!, 0.0001)
        assertEquals(1.75, CalculadoraImc.lerNumero("1.75")!!, 0.0001)
    }

    @Test
    fun lerNumero_rejeitaVazioZeroENegativo() {
        assertNull(CalculadoraImc.lerNumero(""))
        assertNull(CalculadoraImc.lerNumero("0"))
        assertNull(CalculadoraImc.lerNumero("-3"))
        assertNull(CalculadoraImc.lerNumero("abc"))
    }

    @Test
    fun formatar_usaDuasCasasComPonto() {
        assertEquals("22.80", CalculadoraImc.formatar(22.8))
    }
}
