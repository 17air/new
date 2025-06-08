// RegisterViewModel.kt
package com.example.cardify.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cardify.api.RetrofitInstance
import com.example.cardify.requestresponse.EmailCheckRequest
import com.example.cardify.requestresponse.RegisterResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _emailCheckResult = MutableStateFlow<Boolean?>(null)
    val emailCheckResult: StateFlow<Boolean?> = _emailCheckResult

    private val _registerResult = MutableStateFlow<Result<RegisterResponse>?>(null)
    val registerResult: StateFlow<Result<RegisterResponse>?> = _registerResult

    fun checkEmail(email: String) {
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.checkEmail(email)
                _emailCheckResult.value = result.available
            } catch (e: Exception) {
                _emailCheckResult.value = false
            }
        }
    }

    /* fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.register(
                    RegisterRequest(name, email, password)
                )
                _registerResult.value = Result.success(response)
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }

    fun clearRegisterResult() {
        _registerResult.value = null
    } */
}
