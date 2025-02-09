package ir.sharif.taxifinder

import ir.sharif.taxifinder.models.Advertisement
import ir.sharif.taxifinder.models.AdvertisementType


object Advertiser {
    interface AdvertiseListener<T> {
        fun receiveData(advertisement: Advertisement<T>)
    }

    private val subscribers =
        AdvertisementType.values().map { mutableSetOf<AdvertiseListener<Any>>() }

    fun <T> subscribe(subscriber: AdvertiseListener<T>, advertisementType: AdvertisementType) =
        selectSubscribers<T>(advertisementType).add(subscriber)

    fun <T> unSubscribe(subscriber: AdvertiseListener<T>, advertisementType: AdvertisementType) =
        selectSubscribers<T>(advertisementType).remove(subscriber)

    fun <T> advertise(advertisement: Advertisement<T>) =
        selectSubscribers<T>(advertisement.type).forEach { it.receiveData(advertisement) }

    private fun <T> selectSubscribers(advertisementType: AdvertisementType) =
        (subscribers[advertisementType.ordinal] as MutableSet<AdvertiseListener<T>>)
}
