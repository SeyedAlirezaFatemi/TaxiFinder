package ir.sharif.taxifinder.managers

import android.preference.PreferenceManager
import ir.sharif.taxifinder.ApplicationContext

object StorageManager {
    private val storage = DispatchQueue("Storage")
    private val prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.context)

}
