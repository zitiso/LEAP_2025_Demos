package com.example.informuserdemoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.informuserdemoapp.ui.theme.InformUserDemoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InformUserDemoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Inform()
                }
            }
        }
    }
}

@Composable
fun Inform() {

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        val (visible, setVisible) = remember { mutableStateOf(false) }
        var show by remember { mutableStateOf(false) }

        Button(onClick = { show = show.not() }) { Text("Show Alert Dialog") }

        if (show) {
            AlertDialog(
                onDismissRequest = { show = false },
                title = { Text("WOPR") },
                text = { Text("How about a nice game of chess?") },
                confirmButton = {
                    TextButton(onClick = {
                        show = false
                        setVisible(true)
                    }) {
                        Text("Yes".uppercase())
                    }
                },
                dismissButton = {
                    TextButton(onClick = { show = false }) { Text("CANCEL") }

                },
            )
        }
        if (visible) {
            Snackbar(
                modifier = Modifier.padding(8.dp),
                action = {
                    Button(onClick = { setVisible(false) }) { Text("Dismiss") }
                }
            ) {
                Text(text = "This is a snackbar!")
            }
        }
    }
}