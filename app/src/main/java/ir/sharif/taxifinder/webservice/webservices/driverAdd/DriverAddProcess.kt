package ir.sharif.taxifinder.webservice.webservices.driverAdd

import ir.sharif.taxifinder.webservice.MockServer
import ir.sharif.taxifinder.webservice.base.BaseProcess
import ir.sharif.taxifinder.webservice.base.MyRetrofit
import ir.sharif.taxifinder.webservice.base.WebserviceException
import ir.sharif.taxifinder.webservice.webservices.drivers.Driver
import java.io.IOException

class DriverAddProcess(driver: Driver) : BaseProcess() {

    private val driverr: Driver = driver

    @Throws(IOException::class, WebserviceException::class)
    override fun process() = MockServer.addDriver(driverr)
}
