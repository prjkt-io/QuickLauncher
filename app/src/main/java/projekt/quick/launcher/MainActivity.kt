package projekt.quick.launcher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import projekt.quick.launcher.helpers.Configurator
import projekt.quick.launcher.helpers.Configurator.BROADCAST_DIALOG_OPEN

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        registerReceiver(notificationDialogChanger, IntentFilter(BROADCAST_DIALOG_OPEN))
    }

    private val notificationDialogChanger = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            Log.e("Nick", "Hey")
            when (intent?.action) {
                BROADCAST_DIALOG_OPEN -> Configurator.launchAppPickerDialog(this@MainActivity)
            }
        }
    }
}