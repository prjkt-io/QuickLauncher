package projekt.quick.launcher.helpers

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import projekt.quick.launcher.R
import projekt.quick.launcher.adapters.InstalledAppAdapter
import projekt.quick.launcher.adapters.InstalledAppItemModel
import projekt.quick.launcher.helpers.Configurator.killNotificationShade
import java.util.*

class AppPickerDialogActivity : Activity() {

    private var shownDialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        killNotificationShade(this)

        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customLayout: View = inflater.inflate(R.layout.app_picker_dialog, null)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_title))

        val installedAppItemModels: ArrayList<InstalledAppItemModel> = ArrayList<InstalledAppItemModel>()

        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        val resolveInfos: List<ResolveInfo> = packageManager.queryIntentActivities(mainIntent, 0)
        for (info in resolveInfos) {
            var applicationInfo: ApplicationInfo
            if (info.activityInfo.applicationInfo.also { applicationInfo = it } == null || !applicationInfo.enabled) {
                continue
            }
            val appName = InstalledAppAdapter.getAppName(this, applicationInfo.packageName)
            val packageName = applicationInfo.packageName

            if (packageName != getPackageName()) {
                val installedAppItemModel = InstalledAppItemModel(appName, packageName)
                installedAppItemModels.add(installedAppItemModel)
            }
        }

        val mMenuItems: RecyclerView
        val layoutManager = LinearLayoutManager(this)

        mMenuItems = customLayout.findViewById(R.id.app_list)
        mMenuItems.setHasFixedSize(true)
        mMenuItems.layoutManager = layoutManager
        mMenuItems.adapter = InstalledAppAdapter(this, installedAppItemModels)

        builder.setView(customLayout)

        // create and show the alert dialog
        shownDialog = builder.create()
        shownDialog?.setCancelable(false)
        shownDialog?.show()
        shownDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    override fun onDestroy() {
        shownDialog?.dismiss()
        super.onDestroy()
    }
}