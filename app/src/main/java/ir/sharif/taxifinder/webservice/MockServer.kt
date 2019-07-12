package ir.sharif.taxifinder.webservice

import ir.sharif.taxifinder.webservice.base.WebserviceException
import ir.sharif.taxifinder.webservice.webservices.drivers.Driver
import ir.sharif.taxifinder.webservice.webservices.drivers.DriversResponse
import ir.sharif.taxifinder.webservice.webservices.login.LoginResponse

object MockServer {
    fun login(phone: String) = if (phone == "09150773830") LoginResponse(200, "login successful") else throw WebserviceException(400, "error")

    fun getDriver() = DriversResponse(200, "successful", arrayListOf(Driver("https://avatars2.githubusercontent.com/u/24273605?s=460&v=4", "مهدی", "حسن زاده", "09150773830", "206", "123456789")))
}
