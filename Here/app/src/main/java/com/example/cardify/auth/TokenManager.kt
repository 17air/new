// TokenManager.kt
package com.example.cardify.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("cardify_prefs", Context.MODE_PRIVATE)
    //SharedPreferences는 키-값 형태로 데이터를 저장하는 시스템 제공 저장소

    fun saveToken(token: String) {
        prefs.edit() { putString("jwt_token", token) }
    }

    fun getToken(): String? {
        return prefs.getString("jwt_token", null)
    }

    fun clearToken() {
        prefs.edit() { remove("jwt_token") }
    }
}
