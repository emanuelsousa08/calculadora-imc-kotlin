package com.example.calculadoraimc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

private val Vermelho = Color(0xFFFF4757)

@Composable
fun Form(modifier: Modifier = Modifier) {
    var altura by remember { mutableStateOf("") }
    var peso by remember { mutableStateOf("") }
    var imc by remember { mutableStateOf<String?>(null) }
    var textoBotao by remember { mutableStateOf("Calcular") }
    var mensagemErro by remember { mutableStateOf<String?>(null) }
    val historico = remember { mutableStateListOf<String>() }
    var classificacao by remember { mutableStateOf<Classificacao?>(null) }

    val haptic = LocalHapticFeedback.current

    fun validar() {
        val a = altura.replace(",", ".").toDoubleOrNull()
        val p = peso.replace(",", ".").toDoubleOrNull()

        if (a != null && p != null && a > 0) {
            val total = String.format(Locale.US, "%.2f", p / (a * a))
            classificacao = classificarImc(total.toDouble())
            historico.add(0, total) // mais recente primeiro
            imc = total
            altura = ""
            peso = ""
            textoBotao = "Calcular Novamente"
            mensagemErro = null
        } else {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            mensagemErro = "* Campo obrigatório"
            imc = null
            textoBotao = "Calcular IMC"
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 30.dp)
            .background(Color.White, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .padding(top = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val resultado = imc
        val classe = classificacao
        if (resultado == null || classe == null) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Rotulo("Altura *")
                MensagemErro(mensagemErro)
                CampoNumerico(altura, { altura = it }, "Ex. 1.75")

                Rotulo("Peso *")
                MensagemErro(mensagemErro)
                CampoNumerico(peso, { peso = it }, "Ex. 90.450")
            }
            BotaoVermelho(textoBotao) { validar() }
        } else {
            ResultImc(mensagem = "Seu IMC é igual a: ", resultado = resultado, classificacao = classe)
            BotaoVermelho(textoBotao) {
                // volta para o formulário limpo
                imc = null
                mensagemErro = null
                textoBotao = "Calcular"
            }
        }

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            items(historico) { valor ->
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontSize = 18.sp, color = Color.Red)) {
                            append("Resultado IMC = ")
                        }
                        withStyle(SpanStyle(fontSize = 22.sp, color = Color.Red)) {
                            append(valor)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 20.dp)
                )
            }
        }
        LinkRepositorio()
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
    placeholder: String
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        BasicTextField(
            value = valor,
            onValueChange = aoMudar,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(0.9f)
                .height(40.dp)
                .background(Color(0xFFF1F2F6), RoundedCornerShape(50)),
            decorationBox = { campo ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 10.dp),
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
private fun BotaoVermelho(texto: String, aoClicar: () -> Unit) {
    Button(
        onClick = aoClicar,
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = Vermelho),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 20.dp)
    ) {
        Text(texto, color = Color.White, fontSize = 20.sp)
    }
}