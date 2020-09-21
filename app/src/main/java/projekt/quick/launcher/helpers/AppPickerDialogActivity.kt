package projekt.quick.launcher.helpers

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import projekt.quick.launcher.R
import projekt.quick.launcher.adapters.InstalledAppAdapter
import projekt.quick.launcher.adapters.InstalledAppItemModel
import java.util.ArrayList

class AppPickerDialogActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val customLayout: View = inflater.inflate(R.layout.app_picker_dialog, null)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick an app")

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

            val installedAppItemModel = InstalledAppItemModel(appName, packageName)
            installedAppItemModels.add(installedAppItemModel)
        }

        val mMenuItems: RecyclerView
        val layoutManager = LinearLayoutManager(this)

        mMenuItems = customLayout.findViewById(R.id.app_list)
        mMenuItems.setHasFixedSize(true)
        mMenuItems.layoutManager = layoutManager
        mMenuItems.adapter = InstalledAppAdapter(this, installedAppItemModels)

        builder.setView(customLayout)

        // create and show the alert dialog
        val shownDialog = builder.create()
        shownDialog?.setCancelable(true)
        shownDialog?.show()
    }
}