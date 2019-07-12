package ir.sharif.taxifinder.managers

import ir.sharif.taxifinder.Advertiser
import ir.sharif.taxifinder.models.Advertisement
import ir.sharif.taxifinder.models.AdvertisementType
import ir.sharif.taxifinder.webservice.WebserviceHelper
import java.lang.Exception

object ConnectionManager {
    private val cloud = DispatchQueue("Connection")

    fun fetchDrivers() {
        cloud.postRunnable {
            try {
                val response = WebserviceHelper.getDrivers()
                if (response.code == 200) {
                    Advertiser.advertise(Advertisement(AdvertisementType.FETCH_DRIVERS_SUCCESS, response.driver))
                } else {
                    Advertiser.advertise(Advertisement(AdvertisementType.FETCH_DRIVERS_ERROR, response.message))
                }
            } catch (e: Exception) {
                Advertiser.advertise(Advertisement(AdvertisementType.NO_INTERNET, null))
            }
        }
    }
}
