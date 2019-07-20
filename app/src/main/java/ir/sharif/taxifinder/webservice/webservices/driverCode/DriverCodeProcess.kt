package ir.sharif.taxifinder.webservice.webservices.driverCode

import ir.sharif.taxifinder.webservice.MockServer
import java.io.IOException

import ir.sharif.taxifinder.webservice.base.BaseProcess
import ir.sharif.taxifinder.webservice.base.WebserviceException

class DriverCodeProcess(val uuid: String) : BaseProcess() {
    private val request: DriverCodeRequest = DriverCodeRequest(uuid)

//    @Throws(IOException::class, WebserviceException::class)
//    override fun process() = send(MyRetrofit.webserviceUrls.driverCode(uuid))

    @Throws(IOException::class, WebserviceException::class)
    override fun process() = MockServer.driverCode(uuid)
}
