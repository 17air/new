// app/src/main/java/com/example/cardify/ui/components/Base64Image.kt
package com.example.cardify.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.asImageBitmap
import com.example.cardify.utils.ImageUtils

@Composable
fun Base64Image(
    base64: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val bitmap = remember(base64) { ImageUtils.base64ToBitmap(base64) }
    bitmap?.let {
        Image(
            bitmap = it.asImageBitmap(),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    } ?: Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Text("이미지 오류", color = Color.Gray)
    }
}