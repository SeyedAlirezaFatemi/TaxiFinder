package ir.sharif.taxifinder

import com.orhanobut.logger.Logger
import com.soywiz.klock.DateTime
import com.soywiz.klock.minutes
import ir.sharif.taxifinder.managers.ConnectionManager

object MessageController {
    private var last = 0
    private var lastDriversFetchTime: DateTime? = null

    fun fetchDrivers() {
        if (lastDriversFetchTime == null || (lastDriversFetchTime!! + 5.minutes > DateTime.now())) {
            Logger.i("Loading posts from server.")
            ConnectionManager.fetchDrivers()
            lastDriversFetchTime = DateTime.now()
        } else {
            Logger.i("Loading posts from storage.")
//            StorageManager.fetchDrivers()
        }
    }
}
