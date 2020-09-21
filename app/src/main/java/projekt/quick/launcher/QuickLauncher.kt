package projekt.quick.launcher

import android.app.Application
import android.content.Intent

class QuickLauncher : Application() {
    override fun onCreate() {
        super.onCreate()

        val intent = Intent(this, NotificationService::class.java)
        intent.action = NotificationService.ACTION_START_FOREGROUND_SERVICE
        startService(intent)
    }
}