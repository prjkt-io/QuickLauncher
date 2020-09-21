package projekt.quick.launcher

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import projekt.quick.launcher.helpers.AppPickerDialogActivity
import projekt.quick.launcher.helpers.Configurator.APP_LAUNCH_PREF
import projekt.quick.launcher.helpers.Configurator.quickLauncherPrefs

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launchApp()
        finish()
    }

    private fun launchApp() {
        val savedPackageName = quickLauncherPrefs?.getString(APP_LAUNCH_PREF, "")
        if (savedPackageName?.isEmpty()!!) {
            val dialogIntent = Intent(this, AppPickerDialogActivity::class.java)
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(dialogIntent)
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