package projekt.quick.launcher.helpers

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ApplicationInfo
import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import projekt.quick.launcher.R
import projekt.quick.launcher.adapters.InstalledAppAdapter
import projekt.quick.launcher.adapters.InstalledAppItemModel
import java.util.*


object Configurator {

    var BROADCAST_DIALOG_OPEN : String = "projekt.quick.launcher.notification.DIALOG_OPEN"
    var APP_LAUNCH_PREF : String = "launch_app"
    var quickLauncherPrefs: SharedPreferences? = null
    var shownDialog: AlertDialog? = null

    fun launchAppPickerDialog(activity: Activity) {
        val inflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customLayout: View = inflater.inflate(R.layout.app_picker_dialog, null)

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Pick an app")

        val installedAppItemModels: ArrayList<InstalledAppItemModel> = ArrayList<InstalledAppItemModel>()

        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfos: List<ResolveInfo> = activity.packageManager.queryIntentActivities(mainIntent, 0)
        for (info in resolveInfos) {
            var applicationInfo: ApplicationInfo
            if (info.activityInfo.applicationInfo.also { applicationInfo = it } == null || !applicationInfo.enabled) {
                continue
            }
            val appName = InstalledAppAdapter.getAppName(activity, applicationInfo.packageName)
            val packageName = applicationInfo.packageName

            val installedAppItemModel = InstalledAppItemModel(appName, packageName)
            installedAppItemModels.add(installedAppItemModel)
        }

        val mMenuItems: RecyclerView
        val layoutManager = LinearLayoutManager(activity)

        mMenuItems = customLayout.findViewById(R.id.app_list)
        mMenuItems.setHasFixedSize(true)
        mMenuItems.layoutManager = layoutManager
        mMenuItems.adapter = InstalledAppAdapter(activity, installedAppItemModels)

        builder.setView(customLayout)

        // create and show the alert dialog
        shownDialog = builder.create()
        shownDialog?.setCancelable(true)
        shownDialog?.show()
    }

}