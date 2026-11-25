package com.example.calculadoraimc

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResultImc(mensagem: String, resultado: String, classificacao: Classificacao) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = mensagem,
            color = Color(0xFFFF4757),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = resultado,
            color = Color(0xFFFF4757),
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = classificacao.texto,
            color = classificacao.cor,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, "Meu IMC hoje é: $resultado")
                }
                context.startActivity(Intent.createChooser(intent, null))
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1877F2)),
            shape = RoundedCornerShape(50),
            modifier = Modifier.padding(top = 30.dp)
        ) {
            Text("Compartilhar", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}