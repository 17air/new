package com.example.cardify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cardify.auth.TokenManager
import com.example.cardify.models.LoginViewModel
import com.example.cardify.models.MainScreenViewModel
import com.example.cardify.ui.components.PrimaryButton
import com.example.cardify.ui.components.SimpleTextField

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onNavigateToMain: () -> Unit,
    loginViewModel: LoginViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val mainViewModel: MainScreenViewModel = viewModel()
    val cards by mainViewModel.cards.collectAsStateWithLifecycle()
    val hasCards by mainViewModel.hasCards.collectAsStateWithLifecycle()

    //API 응답 받아 성공/실패 처리
    val loginResult by loginViewModel.loginResult.collectAsState()
    LaunchedEffect(loginResult) {
        loginResult?.onSuccess { response ->
            tokenManager.saveToken(response.token)
            loginViewModel.clearResult()
            // Fetch user's cards after successful login
            mainViewModel.fetchCards(response.token)
            onNavigateToMain()
        }?.onFailure {
            showError = true
            errorMessage = "로그인에 실패했어요. 이메일과 비밀번호를 다시 확인해주세요."
            isLoading = false
        }
    }

    // Observe cards state changes
    LaunchedEffect(cards) {
        if (cards.isNotEmpty()) {
            // If cards are loaded and not empty, navigate to MainExist
            onNavigateToMain()
        } else if (loginResult != null && cards.isEmpty()) {
            // If cards are loaded but empty, navigate to MainEmpty
            onNavigateToMain()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Welcome Text
            Text(
                text = "안녕하세요!\n로그인 후 Cardify를 이용할 수 있어요.",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // email input
            SimpleTextField(
                value = email,
                onValueChange = { email = it },
                label = "이메일을 입력하세요.",
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
                isError = showError,
                errorMessage = if (showError) errorMessage else null,
            )

            // password input
            SimpleTextField(
                value = password,
                onValueChange = { password = it },
                label = "비밀번호를 입력하세요.",
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
                isError = showError,
                errorMessage = if (showError) errorMessage else null,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // login button
            PrimaryButton(
                text = "로그인",
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        showError = true
                        errorMessage = "이메일과 비밀번호를 모두 입력해주세요."
                    } else {
                        isLoading = true
                        loginViewModel.login(email, password)
                    }
                }, 
                isLoading = isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Register Text
            Text(
                text = "Cardify가 처음이신가요?",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(top = 8.dp)
            )

            // Register Button
            PrimaryButton(
                text = "회원가입",
                onClick = { onNavigateToRegister() }
            )
        }
    }
}
