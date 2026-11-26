package com.example.calculadoraimc.ui.informacoes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculadoraimc.ui.components.LinkRepositorio
import com.example.calculadoraimc.ui.components.Title

@Composable
fun TelaInformacoes(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val versao = remember {
        runCatching {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName
        }.getOrNull() ?: "-"
    }

    Column(modifier = modifier.padding(top = 80.dp)) {
        Title("Informações")

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 30.dp)
                .background(Color.White, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .verticalScroll(rememberScrollState())
                .padding(start = 24.dp, end = 24.dp, top = 30.dp, bottom = 120.dp)
        ) {
            Secao(
                "Procure um especialista",
                "O IMC é apenas uma referência inicial. Ele não substitui a avaliação de um " +
                    "médico ou nutricionista, que consideram seu histórico de saúde, exames e " +
                    "estilo de vida. Em caso de dúvida sobre seu peso, procure um profissional de saúde."
            )
            Secao(
                "O que o IMC não mostra",
                "O cálculo usa apenas peso e altura. Ele não diferencia massa muscular de gordura " +
                    "nem mostra onde a gordura está distribuída, por isso pode classificar mal " +
                    "atletas e pessoas com muita massa muscular."
            )
            Secao(
                "Para quem as faixas valem",
                "As faixas do aplicativo são referências para adultos. Crianças, adolescentes, " +
                    "gestantes e idosos têm critérios próprios e devem ser avaliados por um profissional."
            )
            Secao(
                "Hábitos que ajudam",
                "Alimentação variada, atividade física regular, boas noites de sono e " +
                    "acompanhamento de saúde periódico contribuem para o bem-estar, " +
                    "independentemente do número do IMC."
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))

            Text(
                text = "Sobre",
                color = Color(0xFFFF4757),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Calculadora IMC - versão $versao",
                color = Color.DarkGray,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 6.dp)
            )
            LinkRepositorio()
        }
    }
}

@Composable
private fun Secao(titulo: String, texto: String) {
    Column(modifier = Modifier.padding(bottom = 20.dp)) {
        Text(
            text = titulo,
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = texto,
            color = Color.DarkGray,
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 6.dp)
        )
    }
}
