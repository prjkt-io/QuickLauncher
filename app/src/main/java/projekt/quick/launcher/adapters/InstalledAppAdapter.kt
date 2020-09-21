package projekt.quick.launcher.adapters

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import projekt.quick.launcher.R
import projekt.quick.launcher.helpers.Configurator
import projekt.quick.launcher.helpers.Configurator.APP_LAUNCH_PREF
import java.util.*

class InstalledAppAdapter(private val mActivity: Activity, private val installedAppItemModels: ArrayList<InstalledAppItemModel>) : RecyclerView.Adapter<InstalledAppAdapter.ViewHolder>() {
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.installed_app_item, parent, false)
        return ViewHolder(v)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (appName, packageName) = installedAppItemModels[position]
        holder.titleView.text = appName
        holder.mainIcon.setImageDrawable(getAppIcon(mActivity, packageName))
        holder.cardView.setOnClickListener {
            run {
                Configurator.quickLauncherPrefs?.edit()?.putString(APP_LAUNCH_PREF, packageName)?.apply()
                mActivity.finish()
            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return installedAppItemModels.size
    }

    class CustomComparator : Comparator<InstalledAppItemModel> {
        override fun compare(o1: InstalledAppItemModel, o2: InstalledAppItemModel): Int {
            return o1.appName.compareTo(o2.appName)
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var mainIcon: ImageView
        var titleView: TextView
        var cardView: CardView

        init {
            val iv = v.findViewById<ImageView>(R.id.app_icon)
            val tv = v.findViewById<TextView>(R.id.app_label)
            val cv: CardView = v.findViewById(R.id.installed_app_card)
            mainIcon = iv
            titleView = tv
            cardView = cv
        }
    }

    companion object {
        private const val TAG = "QuickLauncher"
        fun getAppName(context: Context, packageName: String): String {
            try {
                return context.packageManager.getApplicationLabel(context.packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)) as String
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get app name for \"$packageName\", falling back to default")
            }
            return packageName
        }

        fun getAppIcon(context: Context,
                       packageName: String): Drawable? {
            try {
                return context.packageManager.getApplicationIcon(packageName)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to get app icon for \"$packageName\", falling back to default")
            }
            return ContextCompat.getDrawable(context, R.mipmap.ic_launcher)
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    init {
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
        Collections.sort(installedAppItemModels, CustomComparator())
    }
}