package projekt.quick.launcher.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences


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
        } catch (ignored: Exception) {
            false
        }
    }

    fun Context.isDefaultHomeApp(): Boolean {
        val intent = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
        val res = packageManager.resolveActivity(intent, 0)
        return res!!.activityInfo != null && packageName == res.activityInfo.packageName
    }

}