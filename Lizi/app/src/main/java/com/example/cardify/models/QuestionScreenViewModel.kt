package com.example.cardify.models

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.cardify.features.Question
import com.example.cardify.features.QuestionBank

@SuppressLint("MutableCollectionMutableState", "AutoboxingStateCreation")
class QuestionScreenViewModel : ViewModel() {
    private var _currentQuestionIndex by mutableStateOf(0)
    val currentQuestionIndex: Int get() = _currentQuestionIndex
    
    private var _selectedOptions by mutableStateOf(mutableMapOf<Int, Int>())
    val selectedOptions: Map<Int, Int> get() = _selectedOptions.toMap()
    
    val currentQuestion: Question
        get() = QuestionBank.questions[_currentQuestionIndex]
    
    val progress: Float
        get() = (_currentQuestionIndex + 1) / QuestionBank.questions.size.toFloat()
    
    fun selectOption(optionIndex: Int) {
        _selectedOptions[_currentQuestionIndex] = optionIndex
    }
    
    fun nextQuestion() {
        if (_currentQuestionIndex < QuestionBank.questions.size - 1) {
            _currentQuestionIndex++
        }
    }
    
    fun isLastQuestion(): Boolean = _currentQuestionIndex == QuestionBank.questions.size - 1
    
    fun getSelectedAnswers(): List<Int> {
        return (0 until QuestionBank.questions.size).map {
            _selectedOptions[it] ?: 0 
        }
    }
}
