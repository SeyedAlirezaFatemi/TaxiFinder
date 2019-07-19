package ir.sharif.taxifinder

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.notbytes.barcode_reader.BarcodeReaderActivity
import ir.sharif.taxifinder.webservice.MockServer
import ir.sharif.taxifinder.webservice.WebserviceHelper
import ir.sharif.taxifinder.webservice.webservices.drivers.Driver
import kotlinx.android.synthetic.main.activity_add_driver.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.submitButton
import java.lang.Exception
import kotlin.concurrent.thread

class AddDriverActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_driver)


        submitButton.setOnClickListener {
            if (firstNameEditText.text.isBlank()) {
                firstNameEditText.error = getString(R.string.necessary_field)
            } else if (lastNameEditText.text.isBlank()) {
                lastNameEditText.error = getString(R.string.necessary_field)
            } else if (phoneNumberEditText.text.isBlank()) {
                phoneNumberEditText.error = getString(R.string.necessary_field)
            } else if (carBrandEditText.text.isBlank()) {
                carBrandEditText.error = getString(R.string.necessary_field)
            } else if (plateEditText.text.isBlank() || plateEditText.text.length < 7) {
                plateEditText.error = getString(R.string.necessary_field)
            } else {
                var driver: Driver = Driver(
                    generateImageUrl(avatarEditText.text.toString()),
                    firstNameEditText.text.toString(),
                    lastNameEditText.text.toString(),
                    phoneNumberEditText.text.toString(),
                    carBrandEditText.text.toString(),
                    plateEditText.text.toString()
                )
                MockServer.addDriver(driver)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    fun generateImageUrl(uname: String): String {
        var username = uname
        if (uname.isBlank()) {
            username = "question"
        }
        return "https://github.com/$username.png"
    }


}
