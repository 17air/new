package com.example.cardify.requestresponse

data class LoginResponse (
    val token: String
)

data class RegisterResponse(
    val message: String
)

data class EmailCheckResponse(
    val available: Boolean
)

data class MyCardListResponse (
    val cardId: String,
    val name: String,
    val company: String,
    val position: String,
    val phone: String,
    val email: String,
    val sns: String,
    val cardImageUrl: String
)

data class CreateCardResponse(
    val image: List<String>
)