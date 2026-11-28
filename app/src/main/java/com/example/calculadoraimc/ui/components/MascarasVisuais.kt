package com.example.calculadoraimc.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.example.calculadoraimc.domain.Mascaras

/** Mostra os dígitos "175" como "1,75 m", sem alterar o que fica guardado. */
class AlturaVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val digitos = text.text
        if (digitos.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

        val exibido = Mascaras.alturaComVirgula(digitos) + " m"

        val mapeamento = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                if (offset <= 1) offset else offset + 1

            override fun transformedToOriginal(offset: Int): Int =
                if (offset <= 1) offset else minOf(offset - 1, digitos.length)
        }
        return TransformedText(AnnotatedString(exibido), mapeamento)
    }
}

/** Acrescenta uma unidade depois do texto ("70,5" vira "70,5 kg"). */
class SufixoVisualTransformation(private val sufixo: String) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val tamanho = text.text.length
        if (tamanho == 0) return TransformedText(text, OffsetMapping.Identity)

        val mapeamento = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = offset
            override fun transformedToOriginal(offset: Int): Int = minOf(offset, tamanho)
        }
        return TransformedText(AnnotatedString(text.text + sufixo), mapeamento)
    }
}
