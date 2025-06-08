package com.example.cardify.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.content.ContextCompat
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import kotlinx.coroutines.tasks.await
import com.example.cardify.models.BusinessCard
import com.example.cardify.models.CardBookViewModel
import com.example.cardify.util.TextParser
import kotlinx.coroutines.launch

@Composable
fun OcrNerScreen(
    viewModel: CardBookViewModel,
    onComplete: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = androidx.compose.ui.platform.LocalContext.current

    val selectedImage = remember { mutableStateOf<Uri?>(null) }
    val resultText = remember { mutableStateOf("") }
    var parsedCard by remember { mutableStateOf<BusinessCard?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        selectedImage.value = uri
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
        if (bitmap != null) {
            selectedImage.value = saveBitmapToCache(context, bitmap)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            cameraLauncher.launch(null)
        }
    }

    LaunchedEffect(selectedImage.value) {
        selectedImage.value?.let { uri ->
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            if (bitmap != null) {
                val recognizer = TextRecognition.getClient(
                    KoreanTextRecognizerOptions.Builder().build()
                )
                val image = InputImage.fromBitmap(bitmap, 0)
                val textResult = recognizer.process(image).await()
                val lines = textResult.text.split('\n').map { it.trim() }.filter { it.isNotEmpty() }
                parsedCard = TextParser.parse(lines)
                resultText.value = lines.joinToString("\n")
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (selectedImage.value == null) {
                Text(text = "명함 이미지를 선택해주세요", style = MaterialTheme.typography.bodyLarge)
            } else {
                selectedImage.value?.let { uri ->
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()
                    bitmap?.let { Image(it.asImageBitmap(), contentDescription = null) }
                }
                Text(resultText.value)
            }

            Button(onClick = { galleryLauncher.launch("image/*") }) {
                Text("사진 선택")
            }

            Spacer(modifier = Modifier.padding(4.dp))

            Button(onClick = {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(null)
                } else {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text("사진 찍기")
            }

            if (selectedImage.value != null && parsedCard != null) {
                Button(onClick = {
                    coroutineScope.launch {
                        val card = parsedCard!!.copy(imageUrl = selectedImage.value?.toString() ?: "")
                        viewModel.addCard(card)
                        onComplete()
                    }
                }, modifier = Modifier.padding(top = 16.dp)) {
                    Text("명함첩에 저장")
                }
            }
        }
    }
}

private fun saveBitmapToCache(context: android.content.Context, bitmap: Bitmap): Uri {
    val file = java.io.File.createTempFile("card_", ".jpg", context.cacheDir)
    file.outputStream().use { out ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
    }
    return Uri.fromFile(file)
}

