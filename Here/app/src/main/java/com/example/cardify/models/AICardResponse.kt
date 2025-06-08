package com.example.cardify.models
//base-64

data class AICardResponse(
    val cardImages: List<String>, // List of base64-encoded image strings
    val cardInfo: BusinessCard
)

data class CardImageSelection(
    val selectedImage: String, // base64-encoded string of selected image
    val cardInfo: BusinessCard
)
