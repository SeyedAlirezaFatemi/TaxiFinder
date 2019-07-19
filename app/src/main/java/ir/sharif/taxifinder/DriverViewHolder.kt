package ir.sharif.taxifinder

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.sharif.taxifinder.webservice.MockServer
import ir.sharif.taxifinder.webservice.webservices.drivers.Driver

class DriverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val fullNameTextView = itemView.findViewById<TextView>(R.id.fullName)
    private val phoneNumberTextView = itemView.findViewById<TextView>(R.id.phoneNumber)
    private val brandTextView = itemView.findViewById<TextView>(R.id.brandName)
    private val anotherPhoneNumberTextView = itemView.findViewById<TextView>(R.id.anotherPhoneNumber)
    private val profileImage = itemView.findViewById<ImageView>(R.id.profileImage)
    private val plateView = itemView.findViewById<PlateView>(R.id.plateView)
    private val bottomLayout = itemView.findViewById<RelativeLayout>(R.id.bottom_layout)
    private val arrowImage = itemView.findViewById<ImageView>(R.id.arrow)
    private val deleteImage = itemView.findViewById<ImageView>(R.id.delete_driver)
    private var isExpand = false

    fun bind(driver: Driver) {
        deleteImage.visibility = View.GONE
        plateView.refresh(normalizePlate(driver.plate))
        fullNameTextView.text = driver.firstName + " " + driver.lastName
        fullNameTextView.setTypeface(fullNameTextView.typeface, Typeface.BOLD)
        phoneNumberTextView.text = FormatHelper.toPersianNumber(driver.msisdn)
        phoneNumberTextView.bold()
        brandTextView.text = driver.carBrand
        brandTextView.bold()

        anotherPhoneNumberTextView.text = FormatHelper.toPersianNumber(driver.msisdn)
        phoneNumberTextView.setTypeface(phoneNumberTextView.typeface, Typeface.BOLD)
        anotherPhoneNumberTextView.setTypeface(anotherPhoneNumberTextView.typeface, Typeface.BOLD)
        Glide.with(itemView.context).load(driver.imageUrl).into(profileImage)
        itemView.findViewById<CardView>(R.id.card_view).setOnClickListener {
            isExpand = if (!isExpand) {
                expandView()
                true
            } else {
                collapseView()
                false
            }
        }
        deleteImage.setOnClickListener {
            val builder: AlertDialog.Builder? = it.context?.let {
                AlertDialog.Builder(it)
            }
            builder?.setMessage(R.string.sure_delete)
            builder?.setTitle(R.string.delete_driver)
            val alertDialog: AlertDialog? = it.context?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton(R.string.yes,
                        DialogInterface.OnClickListener { dialog, id ->
                            println("gooooodz")
                            MockServer.deleteDriver(driver.msisdn)
                            MessageController.fetchDrivers()
                        })
                    setNegativeButton(R.string.no,
                        DialogInterface.OnClickListener { dialog, id ->
                        })
                }
                builder.setTitle(R.string.delete_driver)
                builder.setMessage(R.string.sure_delete)
                // Create the AlertDialog
                builder.create()
            }
            alertDialog?.show()

        }

    }

    private fun collapseView() {
        bottomLayout.visibility = View.GONE
        phoneNumberTextView.visibility = View.VISIBLE
        brandTextView.visibility = View.GONE
        deleteImage.visibility = View.GONE
        arrowImage.rotation = 0F
    }

    private fun expandView() {
        bottomLayout.visibility = View.VISIBLE
        phoneNumberTextView.visibility = View.GONE
        brandTextView.visibility = View.VISIBLE
        arrowImage.rotation = 180F
        deleteImage.visibility = View.VISIBLE
    }

}