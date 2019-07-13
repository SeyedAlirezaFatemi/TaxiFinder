package ir.sharif.taxifinder.managers

import android.preference.PreferenceManager
import ir.sharif.taxifinder.Advertiser
import ir.sharif.taxifinder.ApplicationContext
import ir.sharif.taxifinder.MyApplication
import ir.sharif.taxifinder.models.Advertisement
import ir.sharif.taxifinder.models.AdvertisementType
import ir.sharif.taxifinder.webservice.webservices.drivers.Driver

object StorageManager {
    private val storage = DispatchQueue("Storage")
    private val prefs = PreferenceManager.getDefaultSharedPreferences(ApplicationContext.context)

    fun fetchDrivers() {
        storage.postRunnable {
            val drivers = MyApplication.database.driverDao().getAll().map { driverBean ->
                with(driverBean) {
                    Driver(imageUrl, firstName, lastName, msisdn, carBrand, plate)
                }
            }
            Advertiser.advertise(Advertisement(AdvertisementType.FETCH_DRIVERS_SUCCESS, drivers))
        }
    }
}
