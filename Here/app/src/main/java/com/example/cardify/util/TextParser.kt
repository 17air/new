package com.example.cardify.util

import com.example.cardify.models.BusinessCard

object TextParser {
    private val phoneRegex = Regex("[0-9]{2,3}[- ]?[0-9]{3,4}[- ]?[0-9]{4}")
    private val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")

    fun parse(textLines: List<String>): BusinessCard {
        var name = ""
        var company = ""
        var position = ""
        var phone = ""
        var email = ""
        val otherLines = mutableListOf<String>()

        for (line in textLines) {
            val trimmed = line.trim()
            if (email.isEmpty() && emailRegex.containsMatchIn(trimmed)) {
                email = emailRegex.find(trimmed)?.value ?: trimmed
            } else if (phone.isEmpty() && phoneRegex.containsMatchIn(trimmed)) {
                phone = phoneRegex.find(trimmed)?.value ?: trimmed
            } else {
                otherLines.add(trimmed)
            }
        }

        if (otherLines.isNotEmpty()) name = otherLines.getOrNull(0) ?: ""
        if (otherLines.size > 1) company = otherLines[1]
        if (otherLines.size > 2) position = otherLines[2]

        return BusinessCard(
            cardId = System.currentTimeMillis().toString(),
            name = name,
            company = company,
            position = position,
            phone = phone,
            email = email,
            sns = "",
            imageUrl = ""
        )
    }
}
