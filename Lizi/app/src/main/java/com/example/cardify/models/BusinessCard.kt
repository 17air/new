package com.example.cardify.models
// (1)cardId (2)name (3)company (4)position (5)phone (6)email (7)sns (8) cardImageUrl

data class BusinessCard(
    val cardId: String = "",
    val name: String = "",
    val company: String = "",
    val position: String = "",
    val phone: String = "",
    val email: String = "",
    val sns: String = "",
    val imageUrl: String = "",
    val answers: List<String> = emptyList()
) {
    companion object {
        fun fromAIResult(result: Map<String, String>): BusinessCard {
            return BusinessCard(
                cardId = result["cardId"] ?: "",
                name = result["name"] ?: "",
                company = result["company"] ?: "",
                position = result["position"] ?: "",
                phone = result["phone"] ?: "",
                email = result["email"] ?: "",
                sns = result["sns"] ?: "",
                imageUrl = result["imageUrl"] ?: "",
                answers = result["answers"]?.split(",")?.toList() ?: emptyList()
            )
        }
    }
}


