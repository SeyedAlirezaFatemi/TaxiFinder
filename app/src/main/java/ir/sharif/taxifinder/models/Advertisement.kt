package ir.sharif.taxifinder.models

data class Advertisement<T>(val type: AdvertisementType, val data: T)

enum class AdvertisementType {
    FETCH_DRIVERS_SUCCESS,
    FETCH_DRIVERS_ERROR,
    NO_INTERNET,
}
