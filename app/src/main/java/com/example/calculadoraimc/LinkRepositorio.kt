package com.example.calculadoraimc

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val URL_REPOSITORIO = "https://github.com/emanuelsousa08/calculadora-imc-kotlin"

@Composable
fun LinkRepositorio() {
    val context = LocalContext.current

    TextButton(
        onClick = {
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(URL_REPOSITORIO)))
            } catch (e: ActivityNotFoundException) {
                // sem navegador instalado: não faz nada
            }
        },
        modifier = Modifier
            .navigationBarsPadding()
            .padding(bottom = 8.dp)
    ) {
        Text(
            text = "Repositório do projeto",
            color = Color(0xFF1877F2),
            fontSize = 16.sp,
            textDecoration = TextDecoration.Underline
        )
    }
}