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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.cardify.navigation.AppNavigation
import com.example.cardify.ui.theme.CardifyTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                val appContext = LocalContext.current.applicationContext
            CardifyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent()
                }
            }
        }
    }

    override fun attachBaseContext(newBase: Context) {
        val locale = Locale("ko")  // Force Korean locale
        val config = Configuration(newBase.resources.configuration).apply {
            setLocale(locale)
            setLayoutDirection(locale)
        }
        val context = newBase.createConfigurationContext(config)
        super.attachBaseContext(context)
    }

    @Composable
    private fun AppContent() {
        val lifecycleOwner = LocalLifecycleOwner.current
        
        // Handle activity lifecycle events if needed
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> {
                        // Handle app coming to foreground
                    }
                    Lifecycle.Event.ON_STOP -> {
                        // Handle app going to background
                    }
                    else -> {}
                }
            }
            
            lifecycleOwner.lifecycle.addObserver(observer)
            
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
        AppNavigation()
    }
}