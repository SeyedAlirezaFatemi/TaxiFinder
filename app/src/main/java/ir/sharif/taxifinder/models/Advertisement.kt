package ir.sharif.taxifinder.models

data class Advertisement<T>(val type: AdvertisementType, val data: T)

enum class AdvertisementType {
    DRIVERS_LOADED,
}
