package com.example.receiverdemoapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {

    private var nameState by mutableStateOf("Android")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateFromIntent(intent)     // initial

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Greeting(name = nameState)
            }
        }
    }

    // NOTE: Intent is NON-NULL here
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)            // keep Activity.intent in sync
        updateFromIntent(intent)
    }

    private fun updateFromIntent(intent: Intent?) {
        nameState = intent?.getStringExtra("name") ?: "Android"
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}
