package com.example.broadcastdemoapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

private const val CHANNEL_ID = "com.example.broadcastdemoapp.notifications"
private var notificationId = 1

/**
 * Manifest-registered BroadcastReceiver that listens for:
 *  - android.intent.action.AIRPLANE_MODE_CHANGED
 *  - android.net.wifi.WIFI_STATE_CHANGED
 *
 * Posts a notification describing the new state.
 */
class SystemChangeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Make sure the notification channel exists (safe on O+).
        createNotificationChannel(
            context = context,
            channelId = CHANNEL_ID,
            channelName = "Broadcast Demo",
            channelDescription = "Notifications for broadcast events"
        )

        when (intent.action) {
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                // System puts a boolean extra "state" == true when airplane mode is ON.
                val isOn = intent.getBooleanExtra("state", false)
                doNotify(context, "Airplane mode is ${if (isOn) "ON" else "OFF"}")
            }

            WifiManager.WIFI_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(
                    WifiManager.EXTRA_WIFI_STATE,
                    WifiManager.WIFI_STATE_UNKNOWN
                )
                val msg = when (state) {
                    WifiManager.WIFI_STATE_DISABLED -> "Wi-Fi DISABLED"
                    WifiManager.WIFI_STATE_DISABLING -> "Wi-Fi DISABLING"
                    WifiManager.WIFI_STATE_ENABLED -> "Wi-Fi ENABLED"
                    WifiManager.WIFI_STATE_ENABLING -> "Wi-Fi ENABLING"
                    else -> "Wi-Fi state UNKNOWN"
                }
                doNotify(context, msg)
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the channel once when the UI launches (safe on O+).
        createNotificationChannel(
            context = this,
            channelId = CHANNEL_ID,
            channelName = "Broadcast Demo",
            channelDescription = "Notifications for broadcast events"
        )

        // Optional: also register dynamically while the activity is in foreground.
        // Not strictly necessary since we register in the manifest, but harmless.
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        }
        registerReceiver(SystemChangeReceiver(), filter)

        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                NotificationPermissionAndDemo()
            }
        }
    }
}

/**
 * Simple UI that requests POST_NOTIFICATIONS on Android 13+ and lets you fire a test notification.
 */
@Composable
private fun NotificationPermissionAndDemo() {
    val context = LocalContext.current

    val needsRuntimePermission = remember { Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU }

    var hasPermission by remember {
        mutableStateOf(
            !needsRuntimePermission || ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
    }

    Column(Modifier.padding(16.dp)) {
        Text("Broadcast/Notifications demo", style = MaterialTheme.typography.titleLarge)

        if (needsRuntimePermission && !hasPermission) {
            Text(
                "This app needs notification permission on Android 13+.",
                modifier = Modifier.padding(top = 8.dp)
            )
            Button(
                onClick = { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Grant notification permission")
            }
        } else {
            Button(
                onClick = { doNotify(context, "Hello from the app UI!") },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Send test notification")
            }
        }
    }
}

/**
 * SAFE notify helper:
 * - Checks POST_NOTIFICATIONS at runtime on Android 13+
 * - Builds a PendingIntent to reopen MainActivity
 */
fun doNotify(context: Context, message: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) return
    }

    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent: PendingIntent = PendingIntent.getActivity(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_smiley_face_icon) // ensure this drawable exists
        .setContentTitle("Broadcast Received")
        .setContentText("${System.currentTimeMillis() % 100000}: $message")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context).notify(notificationId, notification)
    notificationId++
}

/**
 * Only create a NotificationChannel on O+.
 */
fun createNotificationChannel(
    context: Context,
    channelId: String,
    channelName: String,
    channelDescription: String
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply { description = channelDescription }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}


//package com.example.broadcastdemoapp
//
//import android.Manifest
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.compose.setContent
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import androidx.core.content.ContextCompat
//
//
//
//
//private const val CHANNEL_ID = "com.example.broadcastdemoapp.notifications"
//private var notificationId = 1
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Safe on O+ only
//        createNotificationChannel(
//            context = this,
//            channelId = CHANNEL_ID,
//            channelName = "Broadcast Demo",
//            channelDescription = "Notifications for broadcast events"
//        )
//
//        setContent {
//            Surface(
//                modifier = Modifier.fillMaxSize(),
//                color = MaterialTheme.colorScheme.background
//            ) {
//                NotificationPermissionAndDemo()
//            }
//        }
//    }
//}
//
///**
// * Simple UI: asks for POST_NOTIFICATIONS on Android 13+ and lets you send a test notification.
// */
//@Composable
//private fun NotificationPermissionAndDemo() {
//    val context = LocalContext.current
//
//    val needsRuntimePermission =
//        remember { Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU }
//
//    var hasPermission by remember {
//        mutableStateOf(
//            !needsRuntimePermission || ContextCompat.checkSelfPermission(
//                context, Manifest.permission.POST_NOTIFICATIONS
//            ) == PackageManager.PERMISSION_GRANTED
//        )
//    }
//
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { granted ->
//        hasPermission = granted
//    }
//
//    Column(Modifier.padding(16.dp)) {
//        Text("Broadcast/Notifications demo", style = MaterialTheme.typography.titleLarge)
//
//        if (needsRuntimePermission && !hasPermission) {
//            Text(
//                "This app needs notification permission on Android 13+.",
//                modifier = Modifier.padding(top = 8.dp)
//            )
//            Button(
//                onClick = { launcher.launch(Manifest.permission.POST_NOTIFICATIONS) },
//                modifier = Modifier.padding(top = 8.dp)
//            ) {
//                Text("Grant notification permission")
//            }
//        } else {
//            Button(
//                onClick = { doNotify(context, "Hello from the app UI!") },
//                modifier = Modifier.padding(top = 8.dp)
//            ) {
//                Text("Send test notification")
//            }
//        }
//    }
//}
//
///**
// * SAFE notify helper:
// * - Checks POST_NOTIFICATIONS at runtime on Android 13+
// * - Builds a PendingIntent to reopen MainActivity
// */
//fun doNotify(context: Context, message: String) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//        val granted = ContextCompat.checkSelfPermission(
//            context, Manifest.permission.POST_NOTIFICATIONS
//        ) == PackageManager.PERMISSION_GRANTED
//        if (!granted) {
//            // Permission not granted -> do nothing to avoid SecurityException
//            return
//        }
//    }
//
//    val intent = Intent(context, MainActivity::class.java)
//    val pendingIntent: PendingIntent = PendingIntent.getActivity(
//        context,
//        0,
//        intent,
//        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//    )
//
//    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
//        .setSmallIcon(R.drawable.ic_smiley_face_icon) // ensure this drawable exists
//        .setContentTitle("Broadcast Received")
//        .setContentText("$notificationId: $message")
//        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//        .setContentIntent(pendingIntent)
//        .setAutoCancel(true)
//        .build()
//
//    NotificationManagerCompat.from(context).notify(notificationId, notification)
//    notificationId++
//}
//
///**
// * Only create a NotificationChannel on O+.
// */
//fun createNotificationChannel(
//    context: Context,
//    channelId: String,
//    channelName: String,
//    channelDescription: String
//) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        val channel = NotificationChannel(
//            channelId,
//            channelName,
//            NotificationManager.IMPORTANCE_DEFAULT
//        ).apply { description = channelDescription }
//
//        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        manager.createNotificationChannel(channel)
//    }
//}
