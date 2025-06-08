package com.example.cardify.ui.screens

import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.cardify.R
import com.example.cardify.ui.theme.PrimaryTeal
import com.example.cardify.utils.ImageUtils

@Composable
fun CreateDesignScreen(
    isFirst: Boolean = true,
    generatedCardImages: List<String> = emptyList(),
    onCardSelected: (String, String) -> Unit, 
    onCancelClick: () -> Unit
) {
    val designs = remember(generatedCardImages) {
        if (generatedCardImages.isNotEmpty()) {
            // Use generated card images if available
            generatedCardImages.mapIndexed { index, imageUrl ->
                CardDesign(
                    id = "generated_$index",
                    imageUrl = imageUrl
                )
            }
        } else {
            // Use default card designs
            listOf(
                CardDesign("card1", imageResId = R.drawable.card_one),
                CardDesign("card2", imageResId = R.drawable.card_two),
                CardDesign("card3", imageResId = R.drawable.card_three),
                CardDesign("card4", imageResId = R.drawable.card_four),
                CardDesign("card5", imageResId = R.drawable.card_five)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F3F0))
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "내 명함을 선택해주세요!",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryTeal,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "새로운 명함이 생성되었어요.\nAI가 분석한 당신의 취향을 확인해보세요.",
            color = PrimaryTeal,
            fontSize = 15.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(modifier = Modifier.height(32.dp))
        // Card grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(0.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(designs) { design ->
            CardDesignItem(
                design = design,
                onClick = {
                    val base64Image = design.imageUrl.takeIf { it.isNotBlank() } 
                        ?: design.imageResId?.let { resId ->
                            // Convert drawable to Base64
                            val bitmap = BitmapFactory.decodeResource(
                                LocalContext.current.resources,
                                resId
                            )
                            ImageUtils.bitmapToBase64(bitmap)
                        } ?: ""
                    onCardSelected(design.id, base64Image)
                }
            )
            }
        }
    }
}

@Composable
fun CardDesignItem(
    design: CardDesign,
    onClick: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .aspectRatio(0.63f) // Business card ratio
            .clip(RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick as () -> Unit)
    ) {
        if (design.imageUrl.isNotEmpty()) {
            // For network images
            AsyncImage(
                model = design.imageUrl,
                contentDescription = "Card design ${design.id}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else if (design.imageResId != null) {
                // For local resources
                Image(
                    painter = painterResource(id = design.imageResId),
                    contentDescription = "Card design ${design.id}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Fallback in case no image is provided
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", color = Color.Gray)
            }
        }
    }
}

data class CardDesign(
    val id: String,
    @DrawableRes val imageResId: Int? = null,
    val imageUrl: String = ""
)