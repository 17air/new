package com.example.cardify.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.cardify.auth.TokenManager
import com.example.cardify.models.CardCreationViewModel
import com.example.cardify.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAutoClassifyScreen(
    navController: NavController,
    viewModel: CardCreationViewModel,
    capturedImage: Bitmap
) {
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    val tokenManager = TokenManager(LocalContext.current)
    val token = tokenManager.getToken() ?: ""

    LaunchedEffect(Unit) {
        viewModel.analyzeCardImage(capturedImage, token)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("명함 분석 결과") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로 가기"
                        )
                    }
                }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator()
                        Text("명함 정보를 분석 중입니다...")
                    }
                } else if (uiState.error != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("분석 중 오류가 발생했습니다")
                        Text(uiState.error!!)
                        Button(
                            onClick = {
                                scope.launch { viewModel.analyzeCardImage(capturedImage, token) }
                            }
                        ) {
                            Text("다시 시도하기")
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Display card information
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "분석된 명함 정보",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                DetailItem("이름", uiState.card.name)
                                DetailItem("회사", uiState.card.company)
                                DetailItem("직책", uiState.card.position)
                                DetailItem("전화번호", uiState.card.phone)
                                DetailItem("이메일", uiState.card.email)
                                DetailItem("SNS", uiState.card.sns)
                            }
                        }

                        // Display image options
                        Text(
                            text = "명함 이미지 선택",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            items(uiState.cardImages.size) { index ->
                                Card(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(150.dp)
                                        .clickable {
                                            viewModel.selectAndSaveCard(
                                                index.toString(),
                                                ""
                                            )
                                            navController.navigate(Screen.AddClassified.route)
                                        }
                                ) {
                                    Image(
                                        painter = rememberAsyncImagePainter(
                                            "data:image/jpeg;base64,${uiState.cardImages[index]}"
                                        ),
                                        contentDescription = "명함 이미지 $index",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun DetailItem(label: String, value: String) {
    if (value.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}