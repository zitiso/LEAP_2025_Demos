package com.example.senderdemoapp

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    // Change these to match the receiver app’s package and activity
    private val receiverPackage = "com.example.receiverdemoapp"
    private val receiverActivity = "com.example.receiverdemoapp.MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SenderScreen(
                        onSend = {
                            openReceiver(name = "Barbie") // put whatever extras you need
                        }
                    )
                }
            }
        }
    }

    private fun openReceiver(name: String) {
        // Create an explicit intent that targets the receiver app's activity directly
        val intent = Intent().apply {
            // Tell Android exactly which package and activity to open
            setClassName(
                "com.example.receiverdemoapp",            // Package name of the receiver app
                "com.example.receiverdemoapp.MainActivity" // Fully-qualified class name of the receiver's activity
            )

            // Pass data to the receiver app via extras
            putExtra("name", name)

            // IMPORTANT: Launch the receiver in its own separate task
            // Without this, the receiver opens in the SAME task (back stack) as the sender.
            // That causes Android to bring the receiver to the front when you re-launch the sender.
            // NEW_TASK ensures the receiver has its own task in Recents, so sender and receiver are independent.
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        // Check if there is actually something that can handle this intent
        val canHandle = intent.resolveActivity(packageManager) != null
        if (!canHandle) {
            Toast.makeText(this, "Receiver app not found.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // Launch the receiver activity
            startActivity(intent)
        } catch (_: ActivityNotFoundException) {
            // The specified activity does not exist in the target app
            Toast.makeText(this, "Receiver activity not available.", Toast.LENGTH_SHORT).show()
        } catch (_: SecurityException) {
            // The receiver activity is not exported (Android 12+ requirement for cross-app launch)
            Toast.makeText(this, "Receiver is not exported / not accessible.", Toast.LENGTH_LONG)
                .show()
        }
    }


    @Composable
    private fun SenderScreen(onSend: () -> Unit) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onSend) {
                Text("Open Receiver")
            }
            Text(
                text = "This launches the receiver's MainActivity with an explicit component.",
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}
