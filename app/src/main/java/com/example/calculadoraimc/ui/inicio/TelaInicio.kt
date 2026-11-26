package com.example.calculadoraimc.ui.inicio

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculadoraimc.domain.CalculadoraImc
import com.example.calculadoraimc.ui.AppViewModelProvider
import com.example.calculadoraimc.ui.components.Title

private val Vermelho = Color(0xFFFF4757)

@Composable
fun TelaInicio(
    modifier: Modifier = Modifier,
    viewModel: InicioViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(estado.tentativasInvalidas) {
        if (estado.tentativasInvalidas > 0) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
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
            val resultado = estado.resultado

            if (resultado == null) {
                val erro = if (estado.erroCampos) "* Campo obrigatório" else null

                Column(modifier = Modifier.fillMaxWidth()) {
                    Rotulo("Altura *")
                    MensagemErro(erro)
                    CampoNumerico(estado.altura, viewModel::aoMudarAltura, "Ex. 1.75")

                    Rotulo("Peso *")
                    MensagemErro(erro)
                    CampoNumerico(estado.peso, viewModel::aoMudarPeso, "Ex. 90.450")
                }
                BotaoVermelho(
                    texto = if (estado.erroCampos) "Calcular IMC" else "Calcular",
                    aoClicar = viewModel::calcular
                )
            } else {
                ResultImc(
                    mensagem = "Seu IMC é igual a: ",
                    resultado = CalculadoraImc.formatar(resultado.imc),
                    faixa = resultado.faixa
                )
                BotaoVermelho(
                    texto = "Calcular Novamente",
                    aoClicar = viewModel::novoCalculo
                )
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
        contentPadding = PaddingValues(vertical = 14.dp),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(top = 20.dp)
    ) {
        Text(texto, color = Color.White, fontSize = 20.sp)
    }
}
