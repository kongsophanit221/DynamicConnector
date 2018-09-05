package soteca.com.genisysandroid.framwork.networking

import java.net.URL

/**
 * Created by SovannMeasna on 3/26/18.
 */
interface Request {

    enum class HTTPMethod(val value: String) {
        post("POST"), get("GET"), delete("DELETE"), put("PUT")
    }

    sealed class InterchangeDataType {
        class JSON(val data: String) : InterchangeDataType()
        class XML(val data: String) : InterchangeDataType()
    }

    val requestUrl: URL
        get() = URL("")
    val path: String
        get() = ""
    val httpMethod: HTTPMethod
        get() = HTTPMethod.post
    val header: HashMap<String, String>
        get() = hashMapOf()
    val parameter: ByteArray?
        get() = null
    val timeout: Int
        get() = 0
    val contentLenght: Int
        get() {
            return parameter!!.size
        }

}