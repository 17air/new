package com.example.cardify.requestresponse

data class LoginRequest (
    val email: String,
    val password: String
)

data class RegisterRequest (
    val name: String,
    val email: String,
    val password: String
)

data class EmailCheckRequest (
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

data class BilingualTextData(
    val korean: String,
    val english: String,
    val isBilingual: Boolean
)

data class CreateCardRequest(
    val name: BilingualTextData,
    val company: BilingualTextData,
    val position: BilingualTextData,
    val phone: String,
    val email: String,
    val sns: String,
    val selectedCardId: String,
    val answers: List<Int>
) {
    
    // Constructor for backward compatibility, not sure about its use ....
    constructor(
        name: String,
        company: String,
        position: String,
        phone: String,
        email: String,
        sns: String,
        selectedCardId: String,
        answers: List<Int>
    ) : this(
        name = BilingualTextData(name, name, false),
        company = BilingualTextData(company, company, false),
        position = BilingualTextData(position, position, false),
        phone = phone,
        email = email,
        sns = sns,
        selectedCardId = selectedCardId,
        answers = answers
    )
}
