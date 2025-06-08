// app/src/main/java/com/example/cardify/models/AIImageAnalysisModel.kt
package com.example.cardify.models

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardify.features.AIImageAnalysis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AIImageAnalysisModel @Inject constructor(
    private val imageAnalysisService: AIImageAnalysis
) : ViewModel() {
    
    private val _analysisResult = MutableStateFlow<AnalysisResult?>(null)
    val analysisResult = _analysisResult.asStateFlow()

    init {
        setupAnalysisCallback()
    }


    private fun setupAnalysisCallback() {
        imageAnalysisService.setAnalysisCallback { bitmap ->
            viewModelScope.launch {
                // Process the image and update state
                val result = processImage(bitmap)
                _analysisResult.value = result
            }
        }
    }

    private suspend fun processImage(bitmap: Bitmap): AnalysisResult { TODO()
        // Your image processing logic here
        // This runs in a background thread
        // return AnalysisResult.Success(/* processed data */)
    }

    override fun onCleared() {
        super.onCleared()
        imageAnalysisService.clear()
    }
}

sealed class AnalysisResult {
    data class Success(val data: Any) : AnalysisResult()
    data class Error(val message: String) : AnalysisResult()
    object Loading : AnalysisResult()
}