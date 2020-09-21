package projekt.quick.launcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import projekt.quick.launcher.helpers.Configurator
import projekt.quick.launcher.helpers.Configurator.APP_LAUNCH_PREF
import projekt.quick.launcher.helpers.Configurator.BROADCAST_DIALOG_OPEN
import projekt.quick.launcher.helpers.Configurator.quickLauncherPrefs

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(notificationDialogChanger, IntentFilter(BROADCAST_DIALOG_OPEN))

        launchApp()
    }

    private val notificationDialogChanger = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            when (intent?.action) {
                BROADCAST_DIALOG_OPEN -> Configurator.launchAppPickerDialog(this@MainActivity)
            }
        }
    }

    private fun launchApp() {
        val savedPackageName = quickLauncherPrefs?.getString(APP_LAUNCH_PREF, "")
        if (savedPackageName?.isEmpty()!!) {
            Configurator.launchAppPickerDialog(this@MainActivity)
        } else {
            val launchIntent: Intent? = packageManager.getLaunchIntentForPackage(savedPackageName)
            try {
                launchIntent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(launchIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        launchApp()
    }
}