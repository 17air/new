// Create a file: app/src/main/java/com/example/cardify/ui/screens/CreateQuestionScreen.kt
package com.example.cardify.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardify.ui.theme.PrimaryTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateQuestionScreen(
    questionNumber: Int,
    onAnswerSelected: (String) -> Unit,
    onCancelClick: () -> Unit
) {
    val questions = listOf(
        "나를 가장 잘 표현하는 말은?",
        "두 번째 질문입니다.",
        "세 번째 질문입니다.",
        "네 번째 질문입니다.",
        "다섯 번째 질문입니다."
    )

    val options = when (questionNumber) {
        1 -> listOf(
            "깔끔, 단정.\n커리어우먼(맨)의\n성실",
            "열정영업정신!\n모든 일에 진심인\n활발한 열정파",
            "내 깊은 사람은\n자유로워,\n통통 튀는 개성파",
            "내가 불어넣\n열여 나의지\n엔지니어 부처"
        )
        else -> listOf(
            "질문에 대한\n답변\n선택지 1",
            "질문에 대한\n답변\n선택지 2",
            "질문에 대한\n답변\n선택지 3",
            "질문에 대한\n답변\n선택지 4"
        )
    }

    var selectedOption by remember { mutableStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Title
        Text(
            text = "새로운 명함 생성하기",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryTeal
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(16.dp)
        )

        // AI Description
        Text(
            text = "AI와 함께 내 명함을 디자인해봐요!",
            fontSize = 16.sp,
            color = PrimaryTeal
        )

        Text(
            text = "여러의 질문에 답하면, AI 디자이너가 내 스타일을 파악해요.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(32.dp)
        )

        // Question
        if (questionNumber <= questions.size) {
            Text(
                text = questions[questionNumber - 1],
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(24.dp)
        )

        // Options
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OptionCard(
                text = options[0],
                isSelected = selectedOption == 0,
                onClick = {
                    selectedOption = 0
                    onAnswerSelected("0")
                },
                modifier = Modifier.weight(1f)
            )

            OptionCard(
                text = options[1],
                isSelected = selectedOption == 1,
                onClick = {
                    selectedOption = 1
                    onAnswerSelected("1")
                },
                modifier = Modifier.weight(1f)
            )
        }

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OptionCard(
                text = options[2],
                isSelected = selectedOption == 2,
                onClick = {
                    selectedOption = 2
                    onAnswerSelected("2")
                },
                modifier = Modifier.weight(1f)
            )

            OptionCard(
                text = options[3],
                isSelected = selectedOption == 3,
                onClick = {
                    selectedOption = 3
                    onAnswerSelected("3")
                },
                modifier = Modifier.weight(1f)
            )
        }

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(32.dp)
        )

        // Progress Bar
        LinearProgressIndicator(
            progress = questionNumber / 10f,
            modifier = Modifier.fillMaxWidth(),
            color = PrimaryTeal
        )

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(32.dp)
        )

        // Cancel Button
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionCard(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(140.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) PrimaryTeal else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PrimaryTeal else MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                lineHeight = 20.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun Spacer(height: Int) {
    Spacer(modifier = Modifier.height(height.dp))
}