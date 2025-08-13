package com.example.notifydemoapp

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.notifydemoapp.ui.theme.NotifyDemoAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the notification channel (Android 8.0+)
        myCreateNotificationChannel(
            context = this,
            channelId = "com.example.MyNotificationsApp",
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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    !hasNotificationPermission
                ) {
                    // Ask the user for permission first
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    // Try to post (postNotification will re-check at call time)
                    postNotification(context)
                }
            }
        ) {
            Text(if (hasNotificationPermission) "Notify Now" else "Grant Permission")
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
    // SAFETY CHECK: Always verify permission *right before* posting (API 33+)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val granted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) {
            // No permission—bail out quietly or show a toast if you prefer
            return
        }
    }

    // Build an intent to reopen MainActivity when the notification is tapped
    val intent = Intent(context, MainActivity::class.java)
    val pendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
        addNextIntent(intent)
        getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    val notification = NotificationCompat.Builder(context, "com.example.MyNotificationsApp")
        .setSmallIcon(R.drawable.icons8_notification_100)
        .setContentTitle("Notifications App")
        .setContentText("${notificationId} : This is important!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)
        .build()

    with(NotificationManagerCompat.from(context)) {
        notify(notificationId, notification)
    }
    notificationId++
}

fun myCreateNotificationChannel(
    context: Context,
    channelId: String,
    channelName: String,
    channelDescription: String
) {
    // Notification channels are only required on Android 8.0+ (API 26)
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
