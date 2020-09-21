package projekt.quick.launcher

import android.app.Application
import android.content.Intent
import projekt.quick.launcher.helpers.Configurator.quickLauncherPrefs
import projekt.quick.launcher.notifications.NotificationService

class QuickLauncher : Application() {

    override fun onCreate() {
        super.onCreate()

        val intent = Intent(this, NotificationService::class.java)
        intent.action = NotificationService.ACTION_START_FOREGROUND_SERVICE
        startService(intent)

        quickLauncherPrefs = getSharedPreferences("quickLauncherPrefs", MODE_PRIVATE)
    }

}