package com.codescafe.doctorappointment.services

import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.codescafe.doctorappointment.R
import com.codescafe.doctorappointment.SplashScreen
import com.codescafe.doctorappointment.utils.UserManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Random

class FireNotificationService : FirebaseMessagingService() {

    private var notificationManager: NotificationManager? = null
    private var notificationBuilder: NotificationCompat.Builder? = null
    var largeIcon: Bitmap? = null
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val NotificationType = remoteMessage.data["NotificationType"]
        Log.e("mytag", "onMessageReceived")
        if (NotificationType != null) {
            Log.e("mytag", "if")
            if (NotificationType == "AppointmentNotification") {
                Log.e("mytag", "AppointmentNotification")
                PostNotification(remoteMessage)
            }else if (NotificationType == "DecisionNotification"){
                Log.e("mytag", "DecisionNotification")
                NotifyUser(remoteMessage)
            }

        }
    }

    private fun NotifyUser(remoteMessage: RemoteMessage) {
        val msg = remoteMessage.data["msg"]
        val title = remoteMessage.data["title"]
        val receiverId = remoteMessage.data["receiverId"]
        largeIcon = BitmapFactory.decodeResource(resources, R.drawable.logo)
        val ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notificationchannel()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random().nextInt(3000)
        val intent = Intent(applicationContext, SplashScreen::class.java)
        val pendingIntent: PendingIntent
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.e("mytag", "SDK_INT")
            PendingIntent.getActivity(
                this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        notificationBuilder = NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.drawable.doctorpatient)
            .setLargeIcon(largeIcon)
            .setContentTitle(title)
            .setContentText(msg)
            .setSound(ringUri)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(msg)
            )
            .setAutoCancel(true)
        try {
            if (UserManager.getUserDetails(applicationContext).id.toString() == receiverId){
                notificationManager!!.notify(notificationId, notificationBuilder!!.build())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun PostNotification(remoteMessage: RemoteMessage) {
        largeIcon = BitmapFactory.decodeResource(resources, R.drawable.doctorpatient)
        val msg = remoteMessage.data["msg"]
        val title = remoteMessage.data["title"]
        val receiverId = remoteMessage.data["receiverId"]
        val ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val intent = Intent(applicationContext, SplashScreen::class.java)
        val pendingIntent: PendingIntent
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.e("mytag", "SDK_INT")
            PendingIntent.getActivity(
                this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        notificationchannel()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random().nextInt(3000)
        notificationBuilder = NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.drawable.doctorpatient)
            .setLargeIcon(largeIcon)
            .setContentTitle(title)
            .setContentText(msg)
            .setSound(ringUri)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(msg)
            )
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        try {
            if (UserManager.getUserDetails(applicationContext).id.toString() == receiverId){
                notificationManager!!.notify(notificationId, notificationBuilder!!.build())
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun isAppForground(context: Context): Boolean {
        val mActivityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val l = mActivityManager.runningAppProcesses
        for (info in l) {
            if (info.uid == context.applicationInfo.uid && info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }
        return false
    }

    private fun notificationDialog() {
        if (!isAppForground(applicationContext)) {
        }
    }

    fun notificationchannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "personal notificatiobn"
            val discription = "for All personal notificatiobn"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel("channel_id", name, importance)
            notificationChannel.description = discription
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(true)
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }

    override fun onNewToken(token: String) {
        Log.d("TAG", "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //  sendRegistrationToServer(token);
    }

}