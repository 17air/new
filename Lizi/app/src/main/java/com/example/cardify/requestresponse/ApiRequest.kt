package com.example.cardify.requestresponse

import android.net.Uri
import com.example.cardify.models.BilingualData
import com.example.cardify.models.BusinessCard

data class LoginRequest (
    val email: String,
    val password: String
)

data class RegisterRequest (
    val name: String,
    val email: String,
    val password: String
)

data class EmailCheckRequest ( //이메일 중복 확인
    val email: String
)

//MainCardListRequest는 CardifyApi.kt에서 header에 JWT 담아 전달

data class CardEnrollRequest(
    val name: String,
    val email: String,
    val company: String,
    val position: String,
    val phone: String,
    val sns: String,
    val base64Image: String
)

data class CreateCardRequest(
    val name: BilingualData,
    val company: BilingualData,
    val position: BilingualData,
    val phone: String,
    val email: String,
    val sns: String,
    val answers: List<String>,
    val imageUrl: String = ""
)

data class AIImageAnalysisRequest(
    val imageUri: Uri,  // or Bitmap, 원석님과 합의 필요요
    // val analysisType: AnalysisType = AnalysisType.CARD
)

//Create에서 사용자가 선택한 명함을 DB에 저장
data class CardImageSelection( 
    val selectedImage: String, // base64-encoded string of selected image
    val cardInfo: BusinessCard
)
