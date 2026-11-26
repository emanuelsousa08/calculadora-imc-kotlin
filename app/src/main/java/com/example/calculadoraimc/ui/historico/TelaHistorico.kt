package com.example.calculadoraimc.ui.historico

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.calculadoraimc.domain.CalculadoraImc
import com.example.calculadoraimc.domain.model.RegistroImc
import com.example.calculadoraimc.ui.AppViewModelProvider
import com.example.calculadoraimc.ui.components.Title
import com.example.calculadoraimc.ui.components.cor
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Vermelho = Color(0xFFFF4757)

@Composable
fun TelaHistorico(
    modifier: Modifier = Modifier,
    viewModel: HistoricoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    var selecionado by remember { mutableStateOf<RegistroImc?>(null) }

    Column(modifier = modifier.padding(top = 80.dp)) {
        Title("Histórico")

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(top = 30.dp)
                .background(Color.White, RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
        ) {
            when {
                estado.carregando -> CircularProgressIndicator(
                    color = Vermelho,
                    strokeWidth = 3.dp,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 60.dp)
                        .size(32.dp)
                )

                estado.registros.isEmpty() -> Text(
                    text = "Nenhum cálculo realizado ainda.",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(30.dp)
                )

                else -> LazyColumn(
                    contentPadding = PaddingValues(
                        start = 20.dp, end = 20.dp, top = 30.dp, bottom = 120.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(estado.registros, key = { it.id }) { registro ->
                        ItemHistorico(registro, aoClicar = { selecionado = registro })
                    }
                }
            }
        }
    }

    selecionado?.let { registro ->
        DetalhesDialog(
            registro = registro,
            aoFechar = { selecionado = null },
            aoExcluir = {
                viewModel.excluir(registro.id)
                selecionado = null
            }
        )
    }
}

@Composable
private fun ItemHistorico(registro: RegistroImc, aoClicar: () -> Unit) {
    val faixa = CalculadoraImc.classificar(registro.imc)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF1F2F6))
            .clickable(onClick = aoClicar)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "IMC = ${CalculadoraImc.formatar(registro.imc)}",
                color = Vermelho,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = formatarData(registro.dataHora),
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
        Text(
            text = faixa.rotulo,
            color = faixa.cor(),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DetalhesDialog(
    registro: RegistroImc,
    aoFechar: () -> Unit,
    aoExcluir: () -> Unit
) {
    val faixa = CalculadoraImc.classificar(registro.imc)

    AlertDialog(
        onDismissRequest = aoFechar,
        title = { Text("Detalhes do cálculo") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Linha("IMC", CalculadoraImc.formatar(registro.imc))
                Linha("Classificação", faixa.rotulo)
                Linha("Peso", "${CalculadoraImc.formatar(registro.peso)} kg")
                Linha("Altura", "${CalculadoraImc.formatar(registro.altura)} m")
                Linha("Data", formatarData(registro.dataHora))
            }
        },
        confirmButton = {
            TextButton(onClick = aoFechar) { Text("Fechar") }
        },
        dismissButton = {
            TextButton(onClick = aoExcluir) {
                Text("Excluir", color = Color(0xFFEA2027))
            }
        }
    )
}

@Composable
private fun Linha(titulo: String, valor: String) {
    Row {
        Text(text = "$titulo: ", fontWeight = FontWeight.Bold)
        Text(text = valor)
    }
}

private fun formatarData(millis: Long): String =
    SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale.forLanguageTag("pt-BR"))
        .format(Date(millis))
