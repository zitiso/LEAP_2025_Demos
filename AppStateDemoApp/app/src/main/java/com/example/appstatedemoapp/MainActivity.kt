package com.example.appstatedemoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.appstatedemoapp.ui.theme.AppStateDemoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppStateDemoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StateApp()
                }
            }
        }
    }
}

private const val TAG = "StateApp"

@Composable
fun StateApp() {
    Text("Application State")

    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val latestEvent = remember { mutableStateOf(Lifecycle.Event.ON_ANY) }

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            latestEvent.value = event
            when (event) {
                Lifecycle.Event.ON_START   -> Log.wtf(TAG, event.name)
                Lifecycle.Event.ON_CREATE  -> Log.wtf(TAG, event.name)
                Lifecycle.Event.ON_RESUME  -> Log.wtf(TAG, event.name)
                Lifecycle.Event.ON_PAUSE   -> Log.wtf(TAG, event.name)
                Lifecycle.Event.ON_STOP    -> Log.wtf(TAG, event.name)
                Lifecycle.Event.ON_DESTROY -> Log.wtf(TAG, event.name)
                Lifecycle.Event.ON_ANY     -> Log.wtf(TAG, event.name)
            }
        }

        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
}
