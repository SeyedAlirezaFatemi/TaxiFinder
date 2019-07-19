package ir.sharif.taxifinder.webservice.webservices.login

import ir.sharif.taxifinder.webservice.MockServer
import java.io.IOException

import ir.sharif.taxifinder.webservice.base.BaseProcess
import ir.sharif.taxifinder.webservice.base.WebserviceException

class LoginProcess(val mssidn: String) : BaseProcess() {

    @Throws(IOException::class, WebserviceException::class)
    override fun process() = MockServer.login(mssidn)

//    override fun process() = send(MyRetrofit.webserviceUrls.login(mssidn))
}
