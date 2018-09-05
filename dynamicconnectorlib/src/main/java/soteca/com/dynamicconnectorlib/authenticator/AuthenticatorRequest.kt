package soteca.com.genisysandroid.framwork.authenticator

import soteca.com.genisysandroid.framwork.model.encoder.Encoder
import soteca.com.genisysandroid.framwork.networking.Request
import java.net.URL

/**
 * Created by SovannMeasna on 3/26/18.
 */
class AuthenticatorRequest() : Request {

    enum class Type() {
        deviceRegistration, deviceTokenAcquisition, securityTokenAcquisition
    }

    private var type: Type? = null
    private var host: String = ""
    private var envelope: Encoder? = null

    constructor(host: String, envelope: Encoder?, type: Type) : this() {
        this.host = host
        this.envelope = envelope
        this.type = type
    }

    fun setEnvelope(envelope: Encoder?) {
        this.envelope = envelope
    }

    override val requestUrl: URL
        get() {
            when (type) {
                Type.deviceRegistration -> {
                    return URL("https://login.$host/ppsecure/DeviceAddCredential.srf")
                }
                Type.deviceTokenAcquisition -> {
                    return URL("https://login.$host/extSTS.srf")
                }
                Type.securityTokenAcquisition -> {
                    return URL("https://login.$host/extSTS.srf")
                }
            }
            return URL("")
        }

    override val path: String
        get() {
            return ""
        }

    override val httpMethod: Request.HTTPMethod
        get() {
            return Request.HTTPMethod.post
        }

    override val header: HashMap<String, String>
        get() {
            return hashMapOf("Content-type" to "application/soap+xml; charset=UTF-8",
                    "Host" to "login.$host",
                    "Content-Length" to contentLenght.toString(),
                    "Connection" to "Keep-Alive")
        }


    override val parameter: ByteArray?
        get() {
            return envelope!!.toByteArray()
        }

    override val timeout: Int
        get() = 3000
}