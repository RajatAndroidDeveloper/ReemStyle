package com.reemastyle.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.reemastyle.MainActivity
import com.reemastyle.R
import com.reemastyle.preferences.Preferences
import com.reemastyle.preferences.saveValue
import org.json.JSONObject
import java.util.*

class AppFirebaseMessagingService : FirebaseMessagingService() {
    private val tag = AppFirebaseMessagingService::class.java.simpleName
    private var intent: Intent? = null
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e(tag, "Message data payload1111: " + remoteMessage.data.toString())
        if (remoteMessage?.data.isNotEmpty()) {
            Log.e(tag, "Message data payload: " + remoteMessage.data.toString() + "    " + remoteMessage.data.get("type"))
        }

        var messageBody = ""
        var notificationTitle = ""
        var notificationData : JSONObject
        try {
            if (remoteMessage.data != null) {
                notificationData = JSONObject(remoteMessage.data["data"])
                messageBody = notificationData.getString("message")
                notificationTitle = notificationData.getString("title")
            }
        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
        intent = Intent(applicationContext,MainActivity::class.java)
        sendNotification(messageBody, remoteMessage,notificationTitle)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("fcm_token", token)
        Preferences.prefs?.saveValue("fcm_token", token)
    }

    private fun sendNotification(messageBody: String, remoteMessage: RemoteMessage,notificationTitle: String) {
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0 /* Request code */, intent, PendingIntent.FLAG_IMMUTABLE)
        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId).setSmallIcon(R.drawable.splash_logo).setContentTitle(notificationTitle).setStyle(NotificationCompat.BigTextStyle().bigText(messageBody)).setContentText(messageBody).setAutoCancel(true).setPriority(Notification.PRIORITY_MAX).setDefaults(Notification.DEFAULT_ALL).setSound(defaultSoundUri).setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val channel = NotificationChannel(channelId, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
        val m = ((Date().time / 1000L) % Integer.MAX_VALUE).toInt()
        notificationManager.notify(m, notificationBuilder.build())
    }
}