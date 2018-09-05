package soteca.com.genisysandroid.framwork.connector

import soteca.com.genisysandroid.framwork.model.encoder.Encoder
import soteca.com.genisysandroid.framwork.networking.Request
import java.net.URL

/**
 * Created by SovannMeasna on 4/12/18.
 */
class DynamicConnectorRequest(private val urlString: String, private val envelope: Encoder?) : Request {

    override val requestUrl: URL
        get() = URL(urlString)

    override val path: String
        get() = ""

    override val httpMethod: Request.HTTPMethod
        get() = Request.HTTPMethod.post

    override val header: HashMap<String, String>
        get() = hashMapOf("Content-type" to "application/soap+xml; charset=UTF-8",
                "Host" to requestUrl.host,
                "Content-Length" to contentLenght.toString(),
                "Connection" to "Keep-Alive")

    override val parameter: ByteArray?
        get() {
            return envelope!!.toByteArray()
        }

    override val timeout: Int
        get() = 3000

}