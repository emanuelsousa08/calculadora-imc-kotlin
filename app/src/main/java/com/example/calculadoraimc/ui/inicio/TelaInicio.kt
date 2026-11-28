package com.example.calculadoraimc.ui.inicio

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculadoraimc.ui.AppViewModelProvider
import com.example.calculadoraimc.ui.components.AlturaVisualTransformation
import com.example.calculadoraimc.ui.components.SufixoVisualTransformation
import com.example.calculadoraimc.ui.components.Title
import kotlin.math.roundToInt

private val Vermelho = Color(0xFFFF4757)
private val MascaraAltura = AlturaVisualTransformation()
private val MascaraPeso = SufixoVisualTransformation(" kg")

@Composable
fun TelaInicio(
    modifier: Modifier = Modifier,
    viewModel: InicioViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current
    val tremor = remember { Animatable(0f) }

    // em caso de erro: vibra e balança os campos de um lado para o outro
    LaunchedEffect(estado.tentativasInvalidas) {
        if (estado.tentativasInvalidas > 0) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            tremor.snapTo(24f)
            tremor.animateTo(0f, spring(dampingRatio = 0.2f, stiffness = 500f))
        }
    }

    Column(modifier = modifier.padding(top = 80.dp)) {
        Title()

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 30.dp)
                .background(Color.White, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .padding(top = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(
                targetState = estado.resultado,
                transitionSpec = {
                    (fadeIn(tween(350)) + slideInVertically(tween(350)) { it / 12 }) togetherWith
                        fadeOut(tween(200))
                },
                label = "formulario-resultado"
            ) { resultado ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (resultado == null) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset { IntOffset(tremor.value.roundToInt(), 0) }
                        ) {
                            Rotulo("Altura *")
                            MensagemErro(estado.erroAltura)
                            CampoNumerico(
                                valor = estado.altura,
                                aoMudar = viewModel::aoMudarAltura,
                                placeholder = "Ex. 1,75",
                                transformacao = MascaraAltura,
                                habilitado = !estado.calculando
                            )

                            Rotulo("Peso *")
                            MensagemErro(estado.erroPeso)
                            CampoNumerico(
                                valor = estado.peso,
                                aoMudar = viewModel::aoMudarPeso,
                                placeholder = "Ex. 70,5",
                                transformacao = MascaraPeso,
                                habilitado = !estado.calculando
                            )
                        }
                        BotaoVermelho(
                            texto = if (estado.temErro) "Calcular IMC" else "Calcular",
                            carregando = estado.calculando,
                            aoClicar = viewModel::calcular
                        )
                    } else {
                        ResultImc(imc = resultado.imc, faixa = resultado.faixa)
                        BotaoVermelho(
                            texto = "Calcular Novamente",
                            aoClicar = viewModel::novoCalculo
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Rotulo(texto: String) {
    Text(
        text = texto,
        color = Color.Black,
        fontSize = 18.sp,
        modifier = Modifier.padding(start = 20.dp)
    )
}

@Composable
private fun MensagemErro(mensagem: String?) {
    if (mensagem != null) {
        Text(
            text = mensagem,
            color = Color(0xFFEA2027),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 20.dp)
        )
    }
}

@Composable
private fun CampoNumerico(
    valor: String,
    aoMudar: (String) -> Unit,
    placeholder: String,
    transformacao: VisualTransformation,
    habilitado: Boolean = true
) {
    // destaque do campo ativo: borda vermelha e fundo levemente rosado
    var focado by remember { mutableStateOf(false) }
    val corBorda by animateColorAsState(
        targetValue = if (focado) Vermelho else Color.Transparent,
        animationSpec = tween(200),
        label = "bordaDoCampo"
    )
    val corFundo by animateColorAsState(
        targetValue = if (focado) Color(0xFFFFF1F2) else Color(0xFFF1F2F6),
        animationSpec = tween(200),
        label = "fundoDoCampo"
    )

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        BasicTextField(
            value = valor,
            onValueChange = aoMudar,
            enabled = habilitado,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            visualTransformation = transformacao,
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            cursorBrush = SolidColor(Vermelho),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(0.9f)
                .height(40.dp)
                .onFocusChanged { focado = it.isFocused }
                .background(corFundo, RoundedCornerShape(50))
                .border(2.dp, corBorda, RoundedCornerShape(50)),
            decorationBox = { campo ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (valor.isEmpty()) {
                        Text(placeholder, color = Color.Gray, fontSize = 16.sp)
                    }
                    campo()
                }
            }
        )
    }
}

@Composable
private fun BotaoVermelho(
    texto: String,
    aoClicar: () -> Unit,
    carregando: Boolean = false
) {
    Button(
        onClick = aoClicar,
        enabled = !carregando,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(
            containerColor = Vermelho,
            disabledContainerColor = Vermelho
        ),
        contentPadding = PaddingValues(vertical = 14.dp),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 20.dp)
    ) {
        if (carregando) {
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 3.dp,
                modifier = Modifier.size(28.dp)
            )
        } else {
            Text(texto, color = Color.White, fontSize = 20.sp)
        }
    }
}
