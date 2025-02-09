package ir.sharif.taxifinder

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.google.android.gms.vision.barcode.Barcode
import com.notbytes.barcode_reader.BarcodeReaderActivity
import ir.sharif.taxifinder.models.Advertisement
import ir.sharif.taxifinder.models.AdvertisementType
import ir.sharif.taxifinder.webservice.WebserviceHelper
import ir.sharif.taxifinder.webservice.webservices.drivers.Driver
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import kotlin.concurrent.thread

class MainActivity : BaseActivity(), Advertiser.AdvertiseListener<Any>, SwipeRefreshLayout.OnRefreshListener {

    var drivers: List<Driver> = arrayListOf()

    lateinit var adapter: DriverAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Advertiser.subscribe(this, AdvertisementType.FETCH_DRIVERS_SUCCESS)
        Advertiser.subscribe(this, AdvertisementType.FETCH_DRIVERS_ERROR)
        Advertiser.subscribe(this, AdvertisementType.NO_INTERNET)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        ScreenValue.width = width
        ScreenValue.height = height


        // Create items
        val item1 = AHBottomNavigationItem(R.string.scan_barcode, R.drawable.qrcode, R.color.white)
        val item2 = AHBottomNavigationItem(R.string.list, R.drawable.list, R.color.white)
        val item3 = AHBottomNavigationItem(R.string.add, R.drawable.rounded_add_button, R.color.white)

        // Add items
        bottomNavigation.addItem(item1)
        bottomNavigation.addItem(item3)
        bottomNavigation.addItem(item2)

        bottomNavigation.defaultBackgroundColor = Color.parseColor("#2452C1")
        // Change colors
        bottomNavigation.accentColor = Color.WHITE
        bottomNavigation.inactiveColor = Color.WHITE

        bottomNavigation.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW

        bottomNavigation.setTitleTextSize(26F, 26F)

        // Set listeners
        bottomNavigation.setOnTabSelectedListener { position, wasSelected ->
            if (position == 0) { //barcode reader
                val launchIntent = BarcodeReaderActivity.getLaunchIntent(this, true, false)
                startActivityForResult(launchIntent, 100)
            }else if (position == 1) { //add driver
                val launchIntent =  Intent(this, AddDriverActivity::class.java)
                startActivityForResult(launchIntent, 100)
            }
            true
        }

        pullToRefresh.setOnRefreshListener(this)
        initList()
        MessageController.fetchDrivers()
    }

    override fun onRefresh() {
        MessageController.fetchDrivers()
    }

    override fun onResume() {
        super.onResume()
        Advertiser.subscribe(this, AdvertisementType.FETCH_DRIVERS_SUCCESS)
        Advertiser.subscribe(this, AdvertisementType.FETCH_DRIVERS_ERROR)
        Advertiser.subscribe(this, AdvertisementType.NO_INTERNET)
    }

    override fun onPause() {
        super.onPause()
        Advertiser.unSubscribe(this, AdvertisementType.FETCH_DRIVERS_SUCCESS)
        Advertiser.unSubscribe(this, AdvertisementType.FETCH_DRIVERS_ERROR)
        Advertiser.unSubscribe(this, AdvertisementType.NO_INTERNET)
    }

    override fun receiveData(advertisement: Advertisement<Any>) {
        pullToRefresh.isRefreshing = false
        when {
            advertisement.type == AdvertisementType.NO_INTERNET -> toastNoNetwork()
            advertisement.type == AdvertisementType.FETCH_DRIVERS_ERROR -> toast(advertisement.data as String)
            advertisement.type == AdvertisementType.FETCH_DRIVERS_SUCCESS -> {
                drivers = advertisement.data as List<Driver>
                updateList()
            }
        }
    }

    private fun updateList() {
        runOnUiThread {
            adapter.setDrivers(drivers)
            adapter.notifyDataSetChanged()
        }
    }

    private fun initList() {

        adapter = DriverAdapter(this, drivers)

        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
//            Toast.makeText(this, "error in  scanning", Toast.LENGTH_SHORT).show()
            return
        }

        if (requestCode == 100 && data != null) {
            val barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE) as Barcode
            callInquiryWebservice(barcode.rawValue)
        }
    }

    private fun callInquiryWebservice(uuid: String) {
        thread(true) {
            try {
                val response = WebserviceHelper.driverCode(uuid)
                if (response.code == 200) {
                    val intent = Intent(this, DriverInquiryActivity::class.java)
                    intent.putExtra("UUID", uuid)
                    intent.putExtra("DRIVER_CODE", response.driverCode.driverCode)
                    startActivity(intent)
                } else {
                    toast(response.message)
                }
            } catch (e: Exception) {
                toastNoNetwork()
            }
        }
    }
}
