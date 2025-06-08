// Create a file: app/src/main/java/com/example/cardify/ui/screens/CreateProgressScreen.kt
package com.example.cardify.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardify.models.BusinessCard
import com.example.cardify.models.CardCreationViewModel
import com.example.cardify.ui.theme.PrimaryTeal
import kotlinx.coroutines.delay

@Composable
fun CreateProgressScreen(
    cardInfo: BusinessCard,
    userAnswers: List<String>,
    onProgressComplete: () -> Unit,
    onCancelClick: () -> Unit,
    viewModel: CardCreationViewModel,
    token: String
) {
    LaunchedEffect(key1 = true) {
        // Call AI API with card info and user answers
        viewModel.createCardWithAI(cardInfo, userAnswers, token)
        
        // Wait for API response
        delay(3000) // 3 seconds delay for API processing
        onProgressComplete()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "AI 명함 생성 중",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryTeal,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "AI 디자이너가 명함을 생성하고 있어요.",
            fontSize = 16.sp,
            color = PrimaryTeal,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        CircularProgressIndicator(
            modifier = Modifier.size(48.dp),
            color = PrimaryTeal
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Loading indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(80.dp),
                color = PrimaryTeal,
                strokeWidth = 5.dp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Optional message
        Text(
            text = "이 작업은 약 2분의 시간이 소요됩니다.\n잠시 참아나 몇 잔 커피 마시고 오시는 건 어떨까요?",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "본 화면을 종료하면, 명함 생성이 늦어질 수 있어요.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Cancel button
        OutlinedButton(
            onClick = onCancelClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        ) {
            Text("명함 제작 취소하기")
        }
    }
}