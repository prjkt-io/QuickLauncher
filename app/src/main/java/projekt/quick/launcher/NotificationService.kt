package projekt.quick.launcher

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import projekt.quick.launcher.helpers.Configurator.BROADCAST_DIALOG_OPEN


class NotificationService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        // TODO: Return the communication channel to the service.
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ACTION_START_FOREGROUND_SERVICE -> {
                startForegroundService()
            }
            ACTION_STOP_FOREGROUND_SERVICE -> {
                stopForegroundService()
            }
            ACTION_SETTINGS -> {
                val broadcastIntent = Intent()
                broadcastIntent.action = BROADCAST_DIALOG_OPEN
                applicationContext.sendBroadcast(broadcastIntent)
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    /* Used to build and start foreground service. */
    private fun startForegroundService() {
        Log.d(TAG, "Starting foreground service...")

        val channelId = "quickLauncher_settings"
        val name = getString(R.string.notification_channel)
        val description = getString(R.string.notification_channel_desc)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance)

        channel.importance = NotificationManager.IMPORTANCE_LOW
        channel.description = description
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)

        // Create notification default intent.
        val intent = Intent()
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, channelId)

        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle(getString(R.string.notification_title))
        bigTextStyle.bigText(getString(R.string.notification_summary))
        builder.setStyle(bigTextStyle)
        builder.setWhen(System.currentTimeMillis())
        builder.setSmallIcon(R.drawable.ic_notifications_icon)

        val largeIconBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_notifications_icon)
        builder.setLargeIcon(largeIconBitmap)

        builder.priority = NotificationManager.IMPORTANCE_LOW
        builder.setFullScreenIntent(pendingIntent, true)

        // Add Settings button intent in notification.
        val settingsIntent = Intent(this, NotificationService::class.java)
        settingsIntent.action = ACTION_SETTINGS
        val pendingSettingsIntent = PendingIntent.getService(this, 0, settingsIntent, 0)
        val settingsAction = NotificationCompat.Action(android.R.drawable.ic_menu_preferences, getString(R.string.notification_button), pendingSettingsIntent)
        builder.addAction(settingsAction)

        // Build the notification.
        val notification = builder.build()

        // Start foreground service.
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun stopForegroundService() {
        Log.d(TAG, "Stopping foreground service...")

        // Stop foreground service and remove the notification.
        stopForeground(true)

        // Stop the foreground service.
        stopSelf()
    }

    companion object {
        private const val TAG = "QuickLaunch"
        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
        const val ACTION_SETTINGS = "ACTION_PLAY"
        const val NOTIFICATION_ID = 10871150
    }
}