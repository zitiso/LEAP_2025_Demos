package com.example.notifydemoapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.notifydemoapp.ui.theme.NotifyDemoAppTheme
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the notification channel (Android 8.0+)
        myCreateNotificationChannel(
            context = this,
            channelId = CHANNEL_ID,
            channelName = "Channel 1",
            channelDescription = "Channel for My Notifications App"
        )

        setContent {
            NotifyDemoAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyNotifier()
                }
            }
        }
    }

    companion object {
        private const val CHANNEL_ID = "com.example.MyNotificationsApp"
    }
}

@Composable
fun MyNotifier() {
    val context = androidx.compose.ui.platform.LocalContext.current

    // Initial permission state (true below API 33)
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    // Launcher to request POST_NOTIFICATIONS on API 33+
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasNotificationPermission = granted
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Button 1: your existing "open activity" notification
        Button(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    !hasNotificationPermission
                ) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    postNotification(context)
                }
            }
        ) {
            Text(if (hasNotificationPermission) "Notify (open app)" else "Grant Permission")
        }
        // Button 2: post a notification whose content opens the browser to zitiso.com
        Button(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    !hasNotificationPermission
                ) {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    postBrowserNotification(context, "https://zitiso.com")
                }
            }
        ) {
            Text("Notify (open zitiso.com)")
        }

        Text(
            if (hasNotificationPermission)
                "WooHoo!! I can post for you!!"
            else
                "You need to grant permission."
        )
    }
}

// Simple incrementing id for demo notifications
private var notificationId = 0

private fun postNotification(context: Context) {
    // SAFETY CHECK (API 33+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) return
    }

    // Intent to reopen MainActivity when the notification is tapped
    val activityIntent = Intent(context, MainActivity::class.java)
    val activityPendingIntent = PendingIntent.getActivity(
        context,
        0,
        activityIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, "com.example.MyNotificationsApp")
        .setSmallIcon(R.drawable.icons8_notification_100) // ensure this exists; use android.R.drawable.ic_dialog_info if not
        .setContentTitle("Notifications App")
        .setContentText("${notificationId} : This is important!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(activityPendingIntent)
        .setOngoing(true)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context).notify(notificationId++, notification)
}

private fun postBrowserNotification(context: Context, url: String) {
    // SAFETY CHECK (API 33+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) return
    }

    // PendingIntent that opens the browser to the given URL
    val browserIntent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
        addCategory(Intent.CATEGORY_BROWSABLE)
        // NEW_TASK is fine here since we're potentially leaving the app
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    val browserPendingIntent = PendingIntent.getActivity(
        context,
        1, // different requestCode than the activity one
        browserIntent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val notification = NotificationCompat.Builder(context, "com.example.MyNotificationsApp")
        .setSmallIcon(R.drawable.icons8_notification_100) // or android.R.drawable.ic_dialog_info
        .setContentTitle("Open Zitiso")
        .setContentText("Tap to open zitiso.com")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(browserPendingIntent) // tapping notification opens browser
        .addAction(
            0, // optional small icon for action button; 0=no icon
            "Open",
            browserPendingIntent
        )
        .setOngoing(true)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context).notify(notificationId++, notification)
}

fun myCreateNotificationChannel(
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
        ).apply {
            description = channelDescription
        }
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(channel)
    }
}