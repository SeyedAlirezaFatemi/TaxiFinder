package ir.sharif.taxifinder.webservice

import ir.sharif.taxifinder.webservice.base.WebserviceException
import ir.sharif.taxifinder.webservice.webservices.login.LoginResponse

object MockServer {
    fun login(phone: String) = if (phone == "09150773830") LoginResponse(200, "login successful") else throw WebserviceException(400, "error")
}
