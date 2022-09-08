package com.np.namasteyoga.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.np.namasteyoga.modules.SharedPreference
import com.np.namasteyoga.utils.Logger

import com.np.namasteyoga.utils.SharedPreferencesUtils
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.np.namasteyoga.R
import com.np.namasteyoga.ui.splash.SplashActivity
import com.np.namasteyoga.utils.UIUtils.capitalizeString
import com.np.namasteyoga.utils.UIUtils.getCapitalized


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Logger.Debug( "From: ${remoteMessage.from}",TAG)

        remoteMessage.notification?.let {
            showNotification(it.title?:getString(R.string.app_name),it.body?:"Push notification")
            Logger.Debug("Message Notification Body: ${it.body}",TAG)
            Logger.Debug("Message Notification Title: ${it.title}",TAG)
        }
        remoteMessage.data.run {
            Logger.Debug( "Message data payload: $this",TAG )
            if (isNotEmpty()){
                val title = get("title")?:getString(R.string.app_name)
                get("body")?.run {
                    showNotification(title.getCapitalized(),this)
                }
            }
        }

    }

    private var count = 45
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                getString(R.string.default_notification_channel_id), "FCM notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            serviceChannel.setShowBadge(false)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
    private fun showNotification(title:String,msg:String){
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, Intent(this, SplashActivity::class.java), 0
        )
        createNotificationChannel()
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notification = NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
            .setContentTitle(title.capitalizeString())
            .setContentText(msg.capitalizeString())
            .setSmallIcon(R.drawable.app_icon)
            .setContentIntent(pendingIntent)
            .setSound(soundUri)
            .setNumber(count)
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notification.color = ContextCompat.getColor(this,R.color.white)
        }
        //.build()
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, notification.build())
    }
    override fun onNewToken(data: String) {
        super.onNewToken(data)
        SharedPreferencesUtils.setPushToken(
            getSharedPreferences(
                SharedPreference.DataPrefs,
                Context.MODE_PRIVATE
            ), data
        )
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
        private const val NOTIFICATION_ID = 11
    }
}