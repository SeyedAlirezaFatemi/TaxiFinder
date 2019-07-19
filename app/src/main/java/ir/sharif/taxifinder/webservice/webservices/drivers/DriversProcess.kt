package ir.sharif.taxifinder.webservice.webservices.drivers

import ir.sharif.taxifinder.webservice.MockServer
import java.io.IOException

import ir.sharif.taxifinder.webservice.base.BaseProcess
import ir.sharif.taxifinder.webservice.base.WebserviceException

class DriversProcess : BaseProcess() {
    @Throws(IOException::class, WebserviceException::class)
    override fun process() = MockServer.getDriver()

//    override fun process() = send(MyRetrofit.webserviceUrls.drivers())
}
