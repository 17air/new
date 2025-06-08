// app/src/main/java/com/example/cardify/features/AIImageAnalysis.kt
package com.example.cardify.features

import android.graphics.Bitmap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIImageAnalysis @Inject constructor() {
    private var analysisCallback: ((Bitmap) -> Unit)? = null

    fun setAnalysisCallback(callback: (Bitmap) -> Unit) {
        this.analysisCallback = callback
    }

    fun analyzeImage(bitmap: Bitmap) {
        analysisCallback?.invoke(bitmap)
    }

    fun clear() {
        analysisCallback = null
    }
}