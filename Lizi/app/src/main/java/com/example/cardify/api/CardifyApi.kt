package com.example.cardify.api

import com.example.cardify.models.AICardResponse
import com.example.cardify.models.BusinessCard
import com.example.cardify.requestresponse.CardEnrollRequest
import com.example.cardify.requestresponse.CreateCardRequest
import com.example.cardify.requestresponse.CreateCardResponse
import com.example.cardify.requestresponse.EmailCheckRequest
import com.example.cardify.requestresponse.EmailCheckResponse
import com.example.cardify.requestresponse.LoginRequest
import com.example.cardify.requestresponse.LoginResponse
import com.example.cardify.requestresponse.MyCardListResponse
import com.example.cardify.requestresponse.RegisterRequest
import com.example.cardify.requestresponse.RegisterResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface CardifyApi {
    //로그인
    @POST("api/users/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse
    //suspend : 비동기 네트워크 가능하게 만드는 함수 / LoginRequest를 Body로 하여 request를 보냄 -> LoginResponse의 형태로 DB의 응답을 받음

    //회원가입
    @POST("api/users/signup")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    //이메일 중복 확인
    @GET("api/users/email")
    suspend fun checkEmail(
        @Body request: EmailCheckRequest
    ): EmailCheckResponse

    @GET("api/cards")
    suspend fun getUserCards(
        @Header("Authorization") token: String,
        @Query("owner") owner: Boolean
    ): List<MyCardListResponse>

    @POST("api/cards/enroll")
    suspend fun enrollCard(
        @Header("Authorization") token: String,
        @Body request: CardEnrollRequest
    ): Int

    @POST("api/my-cards/generate")
    suspend fun createCard(
        @Header("Authorization") token: String,
        @Body request: CreateCardRequest
    ): retrofit2.Response<CreateCardResponse>

    @Multipart
    @POST("api/cards/analyze")
    suspend fun analyzeCardImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part
    ): retrofit2.Response<AICardResponse>

    @POST("api/card/save")
    suspend fun saveCard(
        @Body request: SaveCardRequest
    ): SaveCardResponse

    @GET("api/card/{id}")
    suspend fun getCard(@Path("id") id: String): BusinessCard

    @GET("api/card")
    suspend fun getCards(): List<BusinessCard>

    @DELETE("api/card/{id}")
    suspend fun deleteCard(@Path("id") id: String): Boolean
}

data class AICardResponse(
    val cardImages: List<String>, // List of base64-encoded image strings
    val cardInfo: BusinessCard
)

data class SaveCardRequest(
    val selectedImage: String, // base64-encoded string of selected image
    val cardInfo: BusinessCard
)

data class SaveCardResponse(
    val id: String,
    val imageUrl: String,
    val createdAt: String
)

