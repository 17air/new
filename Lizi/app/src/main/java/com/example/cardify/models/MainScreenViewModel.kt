package com.example.cardify.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardify.api.RetrofitInstance
import com.example.cardify.requestresponse.MyCardListResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel : ViewModel() {
    // StateFlow : 카드 목록 상태를 저장
    private val _cards = MutableStateFlow<List<MyCardListResponse>>(emptyList())
    val cards: StateFlow<List<MyCardListResponse>> = _cards

    //hasCards = whether user has cards or not
    private val _hasCards = MutableStateFlow<Boolean?>(null)
    val hasCards: StateFlow<Boolean?> = _hasCards

    // StateFlow : 오류 메시지 저저장
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchCards(token: String) {
        viewModelScope.launch {
            try {
                // "Bearer <token>" 형식으로 헤더 작성
                val bearerToken = "Bearer $token"
                // Retrofit을 사용하여 API 호출
                val result = RetrofitInstance.api.getUserCards(bearerToken, true)
                _cards.value = result
                _hasCards.value = result.isNotEmpty()
            } catch (e: Exception) {
                // 실패 시 오류 메시지 업데이트
                _error.value = e.message
                _hasCards.value = null
            }
        }
    }

    fun clear() {
        _cards.value = emptyList()
        _hasCards.value = null
        _error.value = null
    }
}