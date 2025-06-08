package com.example.cardify.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cardify.models.CardCreationViewModel
import com.example.cardify.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddClassifiedScreen(
    navController: NavController,
    viewModel: CardCreationViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("명함 저장 완료") },
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.isSaved) {
                    // Display success message
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "완료",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "명함이 성공적으로 저장되었습니다",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "카드 ID: ${uiState.savedCardId}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        
                        // Action buttons
                        Button(
                            onClick = { 
                                navController.navigate(Screen.CardBook.route)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("명함 목록으로")
                        }
                        
                        TextButton(
                            onClick = { 
                                navController.navigateUp()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("다시 촬영하기")
                        }
                    }
                } else {
                    // Loading state while saving
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CircularProgressIndicator()
                        Text("명함을 저장 중입니다...")
                    }
                }
            }
        }
    )
}
