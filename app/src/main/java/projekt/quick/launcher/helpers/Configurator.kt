package projekt.quick.launcher.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object Configurator {

    var APP_LAUNCH_PREF: String = "launch_app"
    var quickLauncherPrefs: SharedPreferences? = null

    fun killNotificationShade(context: Context): Boolean {
        @SuppressLint("WrongConstant") val service = context.getSystemService("statusbar")
        val statusBarManager: Class<*>
        return try {
            statusBarManager = Class.forName("android.app.StatusBarManager")
            val collapse = statusBarManager.getMethod("collapsePanels")
            collapse.invoke(service)
            true
        } catch (e: Exception) {
            Log.e("QuickLauncher", "Exception raised in collapsePanels() -> " + e.message)
            false
        }
    }

}