package com.example.implicitintentdemoapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.webkit.URLUtil
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { DemoScreen() }
    }
}

@OptIn(ExperimentalMaterial3Api::class) // <- add this or upgrade Material3
@Composable
fun DemoScreen() {
    val ctx = LocalContext.current

    var url by remember { mutableStateOf("") }
    var urlError by remember { mutableStateOf<String?>(null) }

    var phone by remember { mutableStateOf("") }
    var phoneError by remember { mutableStateOf<String?>(null) }

    fun normalizeUrl(input: String): String {
        val trimmed = input.trim()
        return if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://")) {
            "https://$trimmed"
        } else trimmed
    }

    fun isValidUrl(input: String): Boolean {
        val candidate = normalizeUrl(input)
        return URLUtil.isValidUrl(candidate) && Patterns.WEB_URL.matcher(candidate).matches()
    }

    fun isValidPhone(inputRaw: String): Boolean {
        val input = inputRaw.trim()
        if (!Patterns.PHONE.matcher(input).matches()) return false
        val digits = input.replace("""[^\d+]""".toRegex(), "")
        val noPlus = digits.removePrefix("+")
        return noPlus.length in 7..15
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Implicit Intent Demo") }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                // --- Browser section ---
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Open a web page", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = url,
                        onValueChange = {
                            url = it
                            urlError = null
                        },
                        label = { Text("Website URL (e.g., example.com or https://example.com)") },
                        isError = urlError != null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                        modifier = Modifier.fillMaxWidth()
                    )
                    urlError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    Button(
                        onClick = {
                            if (!isValidUrl(url)) {
                                urlError = "Please enter a valid URL."
                                return@Button
                            }
                            val normalized = normalizeUrl(url)
//                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(normalized))
                            val intent = Intent(Intent.ACTION_VIEW, normalized.toUri())
                            val chooser = Intent.createChooser(intent, "Open with")

                            ctx.startActivity(chooser)
                        },
                        enabled = url.isNotBlank(),
                        modifier = Modifier.align(Alignment.End)
                    ) { Text("Open in Browser") }
                }



                // --- Dialer section ---
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Open dialer with number", style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = phone,
                        onValueChange = {
                            phone = it
                            phoneError = null
                        },
                        label = { Text("Phone Number (digits, spaces, +, - allowed)") },
                        isError = phoneError != null,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth()
                    )
                    phoneError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    Button(
                        onClick = {
                            if (!isValidPhone(phone)) {
                                phoneError = "Please enter a valid phone number."
                                return@Button
                            }
                            val uri = "tel:${phone.trim()}".toUri()
                            val intent = Intent(Intent.ACTION_DIAL, uri)
                            ctx.startActivity(intent)
                        },
                        enabled = phone.isNotBlank(),
                        modifier = Modifier.align(Alignment.End)
                    ) { Text("Open Dialer") }
                }
            }
        }
    }
}
