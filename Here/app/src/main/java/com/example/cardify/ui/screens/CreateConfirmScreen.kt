// Create a file: app/src/main/java/com/example/cardify/ui/screens/CreateConfirmScreen.kt
package com.example.cardify.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardify.R
import com.example.cardify.ui.components.Base64Image
import com.example.cardify.ui.theme.PrimaryTeal

@Composable
fun CreateConfirmScreen(
    isComplete: Boolean = false,
    selectedCardId: String,
    selectedCardImage: String? = null,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit,
    onAddDetailsClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onSaveClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Title
        Text(
            text = if (isComplete) "새로운 명함이 생겼어요!" else "이 명함이 가장 마음에 드시나요?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryTeal
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Subtitle
        Text(
            text = if (isComplete)
                "이 명함이 내 명함 목록에 추가됐어요.\n이미지로 공유하거나, 상세 정보를 추가할 수 있어요."
            else
                "새로운 명함이 생성되었어요.\nAI가 분석한 당신의 취향을 확인해보세요.",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Card display
        if (isComplete) {
            // Card with border frame in complete screen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.6f)
                        .border(
                            width = 2.dp,
                            color = PrimaryTeal,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(8.dp))
                    ) {
                        CardImage(cardId = selectedCardId, imageUrl = selectedCardImage)
                    }
                }
            }
        } else {
            // Just the card in confirm screen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.6f)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    CardImage(selectedCardId)
                }
            }
        }

        Spacer(modifier = Modifier.height(if (isComplete) 32.dp else 24.dp))

        // Action buttons based on screen state
        if (isComplete) {
            // Complete screen buttons
            Button(
                onClick = onAddDetailsClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = PrimaryTeal
                ),
                border = BorderStroke(1.dp, PrimaryTeal)
            ) {
                Text("명함에 상세정보 추가하기")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onShareClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = PrimaryTeal
                ),
                border = BorderStroke(1.dp, PrimaryTeal)
            ) {
                Text("내 명함 이미지로 공유하기")
            }

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onHomeClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryTeal,
                    contentColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Text("메인화면으로 돌아가기")
            }
        } else {
            // Confirm screen buttons
            Text(
                text = "다시 선택 버튼을 눌러 다른 네 개의 명함을 다시 볼 수 있어요.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedButton(
                onClick = onBackClick,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                border = BorderStroke(1.dp, PrimaryTeal),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = PrimaryTeal)
            ) {
                Text("다시 선택할게요.", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onConfirmClick,
                modifier = Modifier.fillMaxWidth().height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal, contentColor = Color.White)
            ) {
                Text("이 명함으로 결정할게요.", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            OutlinedButton(
                onClick = { /* Cancel creation */ },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
            ) {
                Text("명함 제작 취소하기", fontWeight = FontWeight.Normal, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun CardImage(
    cardId: String,
    imageUrl: String? = null
) {
    if (!imageUrl.isNullOrEmpty()) {
        if (imageUrl.length > 200) { // crude base64 check
            Base64Image(base64 = imageUrl, modifier = Modifier.fillMaxSize(), contentDescription = "Business Card Design")
        } else {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Business Card Design",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    } else {
        // For local drawable resources (fallback)
        val cardResource = when (cardId) {
            "1" -> R.drawable.card_one
            "2" -> R.drawable.card_two
            "3" -> R.drawable.card_three
            "4" -> R.drawable.card_four
            "5" -> R.drawable.card_five
            else -> R.drawable.card_placeholder
        }

        Image(
            painter = painterResource(id = cardResource),
            contentDescription = "Business Card Design",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}