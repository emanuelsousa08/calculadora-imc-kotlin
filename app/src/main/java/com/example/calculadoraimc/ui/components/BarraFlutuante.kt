package com.example.calculadoraimc.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraimc.ui.Tela

private val Vermelho = Color(0xFFFF4757)
private val Cinza = Color(0xFF8395A7)

@Composable
fun BarraFlutuante(
    telaAtual: Tela,
    aoSelecionar: (Tela) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Tela.entries.forEach { tela ->
                ItemBarra(
                    tela = tela,
                    selecionado = tela == telaAtual,
                    aoClicar = { aoSelecionar(tela) }
                )
            }
        }
    }
}

@Composable
private fun ItemBarra(tela: Tela, selecionado: Boolean, aoClicar: () -> Unit) {
    val cor = if (selecionado) Vermelho else Cinza

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = aoClicar)
            .widthIn(min = 76.dp)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(tela.icone),
            contentDescription = null,
            tint = cor,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = tela.rotulo,
            color = cor,
            fontSize = 11.sp,
            fontWeight = if (selecionado) FontWeight.Bold else FontWeight.Normal
        )
    }
}
