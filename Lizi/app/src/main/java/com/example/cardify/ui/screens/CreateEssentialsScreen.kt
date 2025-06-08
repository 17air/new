package com.example.cardify.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cardify.R
import com.example.cardify.models.BusinessCard
import com.example.cardify.models.CardCreationViewModel
import com.example.cardify.ui.components.CardifyButton
import com.example.cardify.ui.components.SimpleTextField
import com.example.cardify.ui.theme.PrimaryTeal
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEssentialsScreen(
    cardInfo: BusinessCard,
    onCardInfoChange: (BusinessCard) -> Unit,
    onNextClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: CardCreationViewModel,
    token: String,
    onNavigateToCreateQuestion: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    
    // Observe UI state
    val uiState by viewModel.uiState.collectAsState()
    
    // Show error message if any
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = error,
                    actionLabel = context.getString(R.string.ok)
                )
                viewModel.clearError()
            }
        }
    }
    
    // Navigate to next screen when card is created
    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            onNextClick()
        }
    }
    
    var nameEnabled by remember { mutableStateOf(true) }
    var companyEnabled by remember { mutableStateOf(false) }
    var positionEnabled by remember { mutableStateOf(false) }
    var phoneEnabled by remember { mutableStateOf(false) }
    var emailEnabled by remember { mutableStateOf(false) }
    var snsEnabled by remember { mutableStateOf(false) }
    
    // Field validations
    val isNameValid by remember { derivedStateOf { cardInfo.name.isNotBlank() } }
    val isFormValid = isNameValid && nameEnabled
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Title
            Text(
                text = "새로운 명함 생성하기",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryTeal,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Subtitle
            Text(
                text = "명함에 넣을 정보를 입력해주세요.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            Text(
                text = "보다 상세한 내용은 명함 생성 이후 별도로 추가할 수 있습니다.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Name Field
            FieldWithSwitch(
                label = "이름(필수항목)",
                value = cardInfo.name,
                onValueChange = { onCardInfoChange(cardInfo.copy(name = it)) },
                isEnabled = true,
                onEnabledChange = {},
                isRequired = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Company Field
            FieldWithSwitch(
                label = "회사명",
                value = cardInfo.company,
                onValueChange = {
                    if (!companyEnabled) companyEnabled = true
                    onCardInfoChange(cardInfo.copy(company = it))
                },
                isEnabled = companyEnabled,
                onEnabledChange = { companyEnabled = it }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Position Field
            FieldWithSwitch(
                label = "직함",
                value = cardInfo.position,
                onValueChange = {
                    if (!positionEnabled) positionEnabled = true
                    onCardInfoChange(cardInfo.copy(position = it))
                },
                isEnabled = positionEnabled,
                onEnabledChange = { positionEnabled = it }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Phone Field
            FieldWithSwitch(
                label = "전화번호",
                value = cardInfo.phone,
                onValueChange = {
                    if (!phoneEnabled) phoneEnabled = true
                    onCardInfoChange(cardInfo.copy(phone = it))
                },
                isEnabled = phoneEnabled,
                onEnabledChange = { phoneEnabled = it }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Email Field
            FieldWithSwitch(
                label = "이메일",
                value = cardInfo.email,
                onValueChange = {
                    if (!emailEnabled) emailEnabled = true
                    onCardInfoChange(cardInfo.copy(email = it))
                },
                isEnabled = emailEnabled,
                onEnabledChange = { emailEnabled = it }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // SNS Field
            FieldWithSwitch(
                label = "SNS",
                value = cardInfo.sns,
                onValueChange = {
                    if (!snsEnabled) snsEnabled = true
                    onCardInfoChange(cardInfo.copy(sns = it))
                },
                isEnabled = snsEnabled,
                onEnabledChange = { snsEnabled = it }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Next Button
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryTeal)
                }
            } else {
                CardifyButton(
                    text = "AI와 함께 명함 디자인하기",
                    onClick = {
                            viewModel.updateCardInfo(cardInfo)
                            onNavigateToCreateQuestion()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    isPrimary = isFormValid
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Back Button
            CardifyButton(
                text = "내명함으로 돌아가기",
                onClick = onBackClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                isPrimary = false
            )
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FieldWithSwitch(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEnabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    isRequired: Boolean = false
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isRequired) "$label *" else label,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isRequired || isEnabled) PrimaryTeal else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Switch(
                checked = if (isRequired) true else isEnabled,
                onCheckedChange = if (isRequired) null else onEnabledChange,
                enabled = !isRequired,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = PrimaryTeal,
                    checkedTrackColor = PrimaryTeal.copy(alpha = 0.5f),
                    uncheckedThumbColor = com.example.cardify.ui.theme.OffWhite,
                    uncheckedTrackColor = com.example.cardify.ui.theme.OffWhite.copy(alpha = 0.5f)
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (isRequired || isEnabled) 1f else 0.6f)
        ) {
            SimpleTextField(
                value = value,
                onValueChange = { if (isRequired || isEnabled) onValueChange(it) },
                label = label,
                modifier = Modifier.fillMaxWidth(),
                onFocusChanged = {
                    if (!isRequired && !isEnabled) {
                        onEnabledChange(true)
                    }
                }
            )
        }
    }
}