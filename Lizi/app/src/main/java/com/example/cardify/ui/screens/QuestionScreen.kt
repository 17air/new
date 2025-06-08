package com.example.cardify.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cardify.models.QuestionScreenViewModel
import com.example.cardify.ui.theme.PrimaryTeal

@Composable
fun QuestionScreen(
    onNavigateToProgress: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: QuestionScreenViewModel = viewModel()
    val currentQuestion = viewModel.currentQuestion
    val progress = viewModel.progress
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Title
        Text(
            text = "나를 표현하는 명함",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryTeal
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Question Progress
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Current Question
        Text(
            text = currentQuestion.question,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Options
        currentQuestion.options.forEachIndexed { index, option ->
            Button(
                onClick = { viewModel.selectOption(index) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(option)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Navigation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onBackClick,
                modifier = Modifier.weight(1f)
            ) {
                Text("이전")
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Button(
                onClick = {
                    viewModel.nextQuestion()
                    if (viewModel.isLastQuestion()) {
                        onNavigateToProgress()
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (viewModel.isLastQuestion()) "완료" else "다음")
            }
        }
    }
}
