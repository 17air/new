package com.example.cardify.models

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardify.api.CardifyApi
import com.example.cardify.api.RetrofitInstance
import com.example.cardify.api.SaveCardRequest
import com.example.cardify.requestresponse.CardEnrollRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

sealed class CardCreationState {
    object Loading : CardCreationState()
    object Success : CardCreationState()
    data class Error(val message: String) : CardCreationState()
}

data class CardCreationUiState(
    val cardImages: List<String> = emptyList(),
    val card: BusinessCard = BusinessCard(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false,
    val savedCardId: String? = null
)

class CardCreationViewModel(private val api: CardifyApi = RetrofitInstance.api) : ViewModel() {
    private val _cardCreationState = MutableStateFlow<CardCreationState>(CardCreationState.Loading)
    val cardCreationState: StateFlow<CardCreationState> = _cardCreationState.asStateFlow()

    private val _uiState = MutableStateFlow(CardCreationUiState())
    val uiState: StateFlow<CardCreationUiState> = _uiState.asStateFlow()

    private val _currentQuestion = MutableStateFlow(1)
    val currentQuestion: StateFlow<Int> = _currentQuestion.asStateFlow()

    private val _answers = MutableStateFlow(mutableMapOf<Int, String>())
    val answers: StateFlow<Map<Int, String>> = _answers.asStateFlow()

    fun analyzeCardImage(bitmap: Bitmap, token: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Convert bitmap to multipart
                val imageBytes = bitmapToByteArray(bitmap)
                val requestBody = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                val imagePart = MultipartBody.Part.createFormData(
                    "image",
                    "card_image.jpg",
                    requestBody
                )

                // Call AI analysis with the token
                val response = api.analyzeCardImage("Bearer $token", imagePart)

                if (response.isSuccessful) {
                    response.body()?.let { aiResponse ->
                        _uiState.value = _uiState.value.copy(
                            cardImages = aiResponse.cardImages,
                            card = aiResponse.cardInfo,
                            isLoading = false,
                            error = null
                        )
                    } ?: run {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Empty response from server"
                        )
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to analyze image: ${response.message()}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error analyzing image: ${e.message}"
                )
            }
        }
    }

    fun selectAndSaveCard(selectedImageIndex: String, base64Image: String) {
        viewModelScope.launch {
            try {
                _uiState.value = CardCreationUiState(isLoading = true)

                val currentState = _uiState.value
                // Convert the string index to Int safely
                val index = selectedImageIndex.toIntOrNull()
                    ?: throw IllegalArgumentException("Invalid image index")

                // Ensure the index is within bounds
                val selectedImage = currentState.cardImages.getOrNull(index)
                    ?: throw IndexOutOfBoundsException("Image index out of bounds")

                // Create selection request
                val request = SaveCardRequest(
                    selectedImage = selectedImage,
                    cardInfo = currentState.card
                )

                // Save card
                val response = api.saveCard(request)

                // Update UI
                _uiState.value = currentState.copy(
                    isLoading = false,
                    isSaved = true,
                    savedCardId = response.id,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun recordAnswer(questionNumber: Int, answer: String) {
        val currentAnswers = _answers.value.toMutableMap()
        currentAnswers[questionNumber] = answer
        _answers.value = currentAnswers

        // Move to next question if not at the last question
        if (questionNumber < 5) {
            _currentQuestion.value = questionNumber + 1
        }
    }

    fun recordAnswers(answers: List<String>) {
        // Store all answers with question numbers starting from 1
        val answerMap = answers.mapIndexed { idx, ans -> idx + 1 to ans }.toMap().toMutableMap()
        _answers.value = answerMap
    }

    fun resetCreation() {
        _uiState.value = CardCreationUiState()
        _currentQuestion.value = 1
        _answers.value = mutableMapOf()
        _currentQuestion.value = 1
    }

    fun enrollCard(token: String, base64Image: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                val card = _uiState.value.card
                val request = CardEnrollRequest(
                    name = card.name,
                    email = card.email,
                    company = card.company,
                    position = card.position,
                    phone = card.phone,
                    sns = card.sns ?: "",
                    base64Image = base64Image
                )

                val response = api.enrollCard("Bearer $token", request)

                if (response == 200) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSaved = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Failed to save card: ${response}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error saving card: ${e.message}"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun createCardWithAI(cardInfo: BusinessCard, answers: List<String>, token: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                // Convert answers to a map of question numbers to answers
                val answersMap = answers.mapIndexed { index, answer ->
                    (index + 1).toString() to answer
                }.toMap()
                
                // Create the request with card info and answers
                val request = SaveCardRequest(
                    selectedImage = cardInfo.imageUrl,
                    cardInfo = cardInfo
                )
                
                // Call the API to create the card with AI
                val response = api.saveCard(request)
                
                // Update the UI state with the created card
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isSaved = true,
                    savedCardId = response.id,
                    card = cardInfo.copy(cardId = response.id)
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to create card with AI: ${e.message}"
                )
            }
        }
    }
    
    fun updateCardInfo(cardInfo: BusinessCard) {
        _uiState.value = _uiState.value.copy(
            card = _uiState.value.card.copy(
                name = cardInfo.name,
                company = cardInfo.company,
                position = cardInfo.position,
                phone = cardInfo.phone,
                email = cardInfo.email,
                sns = cardInfo.sns,
                imageUrl = cardInfo.imageUrl
            )
        )
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        return stream.toByteArray()
    }
    
    fun selectCard(cardId: Int) {
        // In a real implementation, you would handle the card selection here
        // For now, we'll just store the selected card ID in the UI state
        _uiState.value = _uiState.value.copy(
            card = _uiState.value.card.copy(
                // Update any necessary card properties based on the selected card ID
                // For example:
                // templateId = cardId
            )
        )
    }
}
