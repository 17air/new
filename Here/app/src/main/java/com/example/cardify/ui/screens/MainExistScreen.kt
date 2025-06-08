package com.example.cardify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardify.ui.components.BottomNavBar
import com.example.cardify.ui.components.BusinessCardItem
import com.example.cardify.models.BusinessCard
import com.example.cardify.requestresponse.MyCardListResponse

@Composable
fun MainExistScreen(
    cardList: List<MyCardListResponse>,
    onAddCard: () -> Unit,
    onCreateNewCard: () -> Unit,
    onCardClick: (BusinessCard) -> Unit,
    onNavigateToCardBook: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title and Add Button Row
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "내 명함",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterStart),
                    textAlign = TextAlign.Start
                )

                // Add button
                FloatingActionButton(
                    onClick = onAddCard,
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterEnd),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "기존 명함 추가하기"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Business Card List
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cardList) { cardResponse ->
                    val card = BusinessCard(
                        cardId = cardResponse.cardId.toString(),
                        name = cardResponse.name,
                        company = cardResponse.company,
                        position = cardResponse.position,
                        phone = "",
                        email = "",
                        sns = "",
                        imageUrl = cardResponse.cardImageUrl ?: ""
                    )

                    BusinessCardItem(
                        card = card,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        onClick = { onCardClick(card) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "명함을 탭해서 상세 내용을 열람, 편집할 수 있어요.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onCreateNewCard,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "AI와 함께 새로운 명함 만들기",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(64.dp))
        }

        BottomNavBar(
            currentRoute = "main",
            onNavigateToMain = {},
            onNavigateToCardBook = onNavigateToCardBook,
            onNavigateToSettings = onNavigateToSettings,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
