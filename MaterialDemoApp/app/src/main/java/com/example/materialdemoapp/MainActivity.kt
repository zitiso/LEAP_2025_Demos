package com.example.materialdemoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
//import androidx.compose.ui.node.CanFocusChecker.end
import androidx.compose.ui.unit.dp
import com.example.materialdemoapp.ui.theme.MaterialDemoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialDemoAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyMaterial()
                }
            }
        }
    }
}

@Composable
fun MyMaterial() {

    val openDialog = remember { mutableStateOf(false) }
    val dialogTitle = remember { mutableStateOf("") }
    val dialogCrawl = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        val colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )

        val title = "Ep 4: A new Hope"
        val body = "It is a period of civil war ..."
        val end = "1977"

        ListItem(
            modifier = Modifier.clickable(enabled = true) {
                dialogTitle.value = title
                dialogCrawl.value = body
                openDialog.value = true  /* on click */
            },
            colors = colors,
            headlineContent = { Text(title) },
            supportingContent = { Text(body) },
            trailingContent = { Text(end) },
            leadingContent = {
                Icon(
                    Icons.Filled.ThumbUp,
                    contentDescription = "Localized description",
                )
            }
        )
    }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = true
            },
            title = {
                Text(text = dialogTitle.value)
            },
            text = {
                Text(text = dialogCrawl.value)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }

}
