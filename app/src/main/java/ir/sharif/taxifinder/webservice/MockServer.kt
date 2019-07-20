package ir.sharif.taxifinder.webservice

import ir.sharif.taxifinder.webservice.base.WebserviceException
import ir.sharif.taxifinder.webservice.webservices.driverCode.DriverCode
import ir.sharif.taxifinder.webservice.webservices.driverCode.DriverCodeResponse
import ir.sharif.taxifinder.webservice.webservices.driverDetail.DriverDetailResponse
import ir.sharif.taxifinder.webservice.webservices.drivers.Driver
import ir.sharif.taxifinder.webservice.webservices.drivers.DriversResponse
import ir.sharif.taxifinder.webservice.webservices.login.LoginResponse

object MockServer {

    val drivers = arrayListOf(
        Driver(
            "https://avatars2.githubusercontent.com/u/24273605?s=460&v=4",
            "مهدی",
            "حسن زاده",
            "09150773830",
            "206",
            "123456789"
        ),
        Driver(
            "https://avatars1.githubusercontent.com/u/39184114?s=460&v=4",
            "علیرضا",
            "فاطمی",
            "09171021072",
            "000",
            "123456788"
        )
    )

    fun login(phone: String) =
        if (phone == "09150773830") LoginResponse(200, "login successful") else throw WebserviceException(400, "error")

    fun addDriver(driver: Driver) {
        drivers.add(driver)
    }

    fun deleteDriver(msisdn : String){
        var index = -1;
        for (driver in drivers) {
            if (driver.msisdn == msisdn){
                index = drivers.indexOf(driver)
                break
            }
        }
        if (index != -1){
            drivers.removeAt(index)
        }
        println("deleteeddddddddddddddddddddddddddd")
    }

    fun getDriver() = DriversResponse(
        200, "successful",
        drivers
    )

    fun driverCode(uuid: String) = DriverCodeResponse(200, "", DriverCode("123456789"))

    fun getDriverDetail() = DriverDetailResponse()
}
