//가장 먼저 실행되는 클래스, 여기서 Activity == Screen
package com.example.cardify

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.cardify.navigation.AppNavigation
import com.example.cardify.ui.theme.CardifyTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    // Jetpack Compose에서 UI를 직접 코드로 그릴 수 있게 해주는 Jetpack Compose 전용 Activity
    override fun onCreate(savedInstanceState: Bundle?) { //앱에서 Activity가 생성될 때 가장 먼저 실행되는 함수
        super.onCreate(savedInstanceState)
        setContent { //Compose UI를 시작하는 시점
            CardifyTheme {
                Surface( //화면 전체의 배경 설정
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
    override fun attachBaseContext(newBase: Context) {
        val locale = Locale("ko")
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }
}
