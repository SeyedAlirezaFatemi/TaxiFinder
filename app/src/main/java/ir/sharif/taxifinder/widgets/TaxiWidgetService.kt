package ir.sharif.taxifinder.widgets

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import ir.sharif.taxifinder.FormatHelper
import ir.sharif.taxifinder.R
import ir.sharif.taxifinder.webservice.WebserviceHelper
import ir.sharif.taxifinder.webservice.webservices.drivers.Driver
import java.lang.Exception
import kotlin.concurrent.thread


class TaxiWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return TaxiRemoteViewsFactory(this.applicationContext, intent)
    }
}

class TaxiRemoteViewsFactory(
    private val context: Context,
    intent: Intent
) : RemoteViewsService.RemoteViewsFactory {

    private lateinit var widgetItems: List<Driver>


    override fun onCreate() {
        thread(true) {
            try {
                val response = WebserviceHelper.getDrivers()
                if (response.code == 200) {
                    widgetItems = response.driver
                } else {
                }
            } catch (e: Exception) {

            }
        }
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onDataSetChanged() {
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getViewAt(position: Int): RemoteViews {
        val driver = widgetItems[position]
        return RemoteViews(context.packageName, R.layout.widget_item).apply {
            setTextViewText(R.id.widget_fullName, driver.firstName + " " + driver.lastName)
            setTextViewText(R.id.widget_phoneNumber, FormatHelper.toPersianNumber(driver.msisdn))
            setTextViewText(R.id.widget_brandName, driver.carBrand)
        }
    }

    override fun getCount(): Int {
        return widgetItems.size
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun onDestroy() {
    }
}
