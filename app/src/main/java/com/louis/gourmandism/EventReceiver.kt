package com.louis.gourmandism

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.louis.gourmandism.data.Shop

class EventReceiver : BroadcastReceiver() {

    private var channelId = "001010100"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {

        if(intent.action.equals("Event")){

            channelId = intent.extras?.get("EventId").toString()
            val shopName = intent.extras?.get("shopName").toString()
            val content = "$shopName  已到赴約時間!"

            createNotificationChannel(context)
            val intentObject = Intent().apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intentObject, 0)
            val builder = createBuilder(pendingIntent, content, context)
            if (builder != null) {
                NotificationManagerCompat.from(context).notify(0, builder.build())
            }

        }
    }

    private fun createBuilder(pendingIntent: PendingIntent, ContentText : String, context: Context): NotificationCompat.Builder? {

        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("揪團通知")
            .setContentText(ContentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(context: Context) {

        val name = "Event"
        val descriptionText = "Event"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
