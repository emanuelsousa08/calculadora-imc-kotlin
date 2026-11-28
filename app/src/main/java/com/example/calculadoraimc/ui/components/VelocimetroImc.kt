package com.example.calculadoraimc.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraimc.domain.CalculadoraImc
import com.example.calculadoraimc.domain.model.FaixaImc
import kotlin.math.cos
import kotlin.math.sin

private val LimitesDasFaixas = listOf(0.0, 18.5, 25.0, 30.0, 35.0, 40.0, 50.0)
private val CinzaDoArco = Color(0xFFE5E7EB)
private const val DURACAO_MS = 1600

/**
 * Posição (0 a 1) do valor no arco. Cada faixa ocupa 1/6 do arco, e dentro dela
 * o valor avança de forma proporcional. Assim todas as faixas ficam legíveis.
 */
internal fun posicaoNoArco(imc: Double): Float {
    val valor = imc.coerceIn(0.0, 50.0)
    val indice = LimitesDasFaixas.indexOfLast { valor >= it }.coerceIn(0, 5)
    val inicio = LimitesDasFaixas[indice]
    val fim = LimitesDasFaixas[indice + 1]
    val dentro = ((valor - inicio) / (fim - inicio)).coerceIn(0.0, 1.0)
    return ((indice + dentro) / 6.0).toFloat()
}

/**
 * Velocímetro do IMC: o arco e o número partem de zero e chegam ao resultado.
 * [aoConcluir] é chamado quando a animação termina.
 */
@Composable
fun VelocimetroImc(
    imc: Double,
    modifier: Modifier = Modifier,
    aoConcluir: () -> Unit = {}
) {
    val progresso = remember { Animatable(0f) }
    val aoConcluirAtual by rememberUpdatedState(aoConcluir)

    LaunchedEffect(imc) {
        progresso.snapTo(0f)
        progresso.animateTo(1f, tween(durationMillis = DURACAO_MS, easing = FastOutSlowInEasing))
        aoConcluirAtual()
    }

    val valorAtual = imc * progresso.value
    val posicao = posicaoNoArco(valorAtual)
    val faixaAtual = CalculadoraImc.classificar(valorAtual)
    val corAtual by animateColorAsState(
        targetValue = faixaAtual.cor(),
        animationSpec = tween(300),
        label = "corDoVelocimetro"
    )
    val descricao = "IMC ${CalculadoraImc.formatar(imc)}, ${CalculadoraImc.classificar(imc).rotulo}"

    Box(
        modifier = modifier
            .size(width = 280.dp, height = 156.dp)
            .semantics(mergeDescendants = true) { contentDescription = descricao },
        contentAlignment = Alignment.BottomCenter
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val espessura = 18.dp.toPx()
            val raio = (size.width - espessura) / 2f
            val centro = Offset(size.width / 2f, raio + espessura / 2f)
            val canto = Offset(centro.x - raio, centro.y - raio)
            val dimensao = Size(raio * 2f, raio * 2f)
            val estilo = Stroke(width = espessura, cap = StrokeCap.Round)

            val grausPorFaixa = 180f / FaixaImc.entries.size
            val grausDaPonta = Math.toDegrees((espessura / 2f / raio).toDouble()).toFloat()
            val folga = 3f
            val recuo = grausDaPonta + folga / 2f
            val varredura = grausPorFaixa - 2f * recuo
            val anguloAtual = posicao * 180f

            FaixaImc.entries.forEachIndexed { indice, faixa ->
                val inicioRelativo = indice * grausPorFaixa + recuo

                drawArc(
                    color = CinzaDoArco,
                    startAngle = 180f + inicioRelativo,
                    sweepAngle = varredura,
                    useCenter = false,
                    topLeft = canto,
                    size = dimensao,
                    style = estilo
                )

                val preenchido = (anguloAtual - inicioRelativo).coerceIn(0f, varredura)
                if (preenchido > 0.5f) {
                    drawArc(
                        color = faixa.cor(),
                        startAngle = 180f + inicioRelativo,
                        sweepAngle = preenchido,
                        useCenter = false,
                        topLeft = canto,
                        size = dimensao,
                        style = estilo
                    )
                }
            }

            // marcador que acompanha o valor ao longo do arco
            val anguloRad = Math.toRadians((180f + anguloAtual).toDouble())
            val marcador = Offset(
                x = centro.x + raio * cos(anguloRad).toFloat(),
                y = centro.y + raio * sin(anguloRad).toFloat()
            )
            drawCircle(color = Color.White, radius = espessura * 0.62f, center = marcador)
            drawCircle(
                color = corAtual,
                radius = espessura * 0.62f,
                center = marcador,
                style = Stroke(width = 3.dp.toPx())
            )
        }

        Column(
            modifier = Modifier.padding(bottom = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = CalculadoraImc.formatar(valorAtual),
                color = corAtual,
                fontSize = 44.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = "seu IMC", color = Color.Gray, fontSize = 14.sp)
        }
    }
}
