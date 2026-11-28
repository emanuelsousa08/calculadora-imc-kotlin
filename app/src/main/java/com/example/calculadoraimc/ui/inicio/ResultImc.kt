package com.example.calculadoraimc.ui.inicio

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraimc.domain.CalculadoraImc
import com.example.calculadoraimc.domain.model.FaixaImc
import com.example.calculadoraimc.ui.components.VelocimetroImc
import com.example.calculadoraimc.ui.components.cor

@Composable
fun ResultImc(imc: Double, faixa: FaixaImc) {
    val context = LocalContext.current
    // a classificação só aparece quando o velocímetro termina de animar
    var concluido by remember(imc) { mutableStateOf(false) }
    val aparicao by animateFloatAsState(
        targetValue = if (concluido) 1f else 0f,
        animationSpec = tween(400),
        label = "selo"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        VelocimetroImc(imc = imc, aoConcluir = { concluido = true })

        Box(
            modifier = Modifier
                .padding(top = 20.dp)
                .height(48.dp)
                .graphicsLayer {
                    alpha = aparicao
                    scaleX = 0.8f + 0.2f * aparicao
                    scaleY = 0.8f + 0.2f * aparicao
                }
                .clip(RoundedCornerShape(50))
                .background(faixa.cor())
                .padding(horizontal = 36.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = faixa.rotulo,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Meu IMC hoje é: ${CalculadoraImc.formatar(imc)}")
                }
                context.startActivity(Intent.createChooser(intent, null))
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Compartilhar", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}