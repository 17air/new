package com.example.cardify.ml

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class OcrNerProcessor(context: Context) {
    private val interpreter: Interpreter

    init {
        interpreter = Interpreter(loadModelFile(context, MODEL_PATH))
    }

    private fun loadModelFile(context: Context, path: String): MappedByteBuffer {
        context.assets.openFd(path).use { fileDescriptor ->
            FileInputStream(fileDescriptor.fileDescriptor).use { inputStream ->
                val fileChannel = inputStream.channel
                return fileChannel.map(
                    FileChannel.MapMode.READ_ONLY,
                    fileDescriptor.startOffset,
                    fileDescriptor.declaredLength
                )
            }
        }
    }

    fun run(input: Any, output: Any) {
        interpreter.run(input, output)
    }

    fun close() {
        interpreter.close()
    }

    companion object {
        private const val MODEL_PATH = "ocr_ner_model.tflite"
    }
}
