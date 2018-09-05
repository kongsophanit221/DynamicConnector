package soteca.com.genisysandroid.framwork.networking

import org.simpleframework.xml.Element
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "Fault", strict = false)
class ResponseError {

    @field:Path("Code")
    @field:Element(name = "Value")
    var code: String? = null

    @field:Path("Code/Subcode")
    @field:Element(name = "Value", required = false)
    var subCode: String? = null

    @field:Path(value = "Reason")
    @field:Element(name = "Text")
    var reason: String? = null

    var e: Errors? = null

    companion object {

        @Synchronized
        fun getInstance(value: Errors?): ResponseError {
            var responseError = ResponseError()
            responseError.e = value
            return responseError
        }
    }

    val error: Errors?
        get() {
            if (e != null) {
                return e
            }
            if (code == null && reason == null) {
                return DynamicsError.invalidDataResponse
            } else if (reason != null && reason!!.contains("FailedAuthentication") && reason!!.contains("Authentication Failure")) {
                return AuthenticationError.invalidCredential
            } else if (subCode != null && subCode!!.contains("InvalidSecurity")) {
                return AuthenticationError.invalidSecurityToken
            } else {
                return DynamicsError.nilDataResponse
            }

            return null
        }

    override fun toString(): String {
        return "value: ${code} and reason: {$reason}"
    }
}