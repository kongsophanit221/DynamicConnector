package soteca.com.genisysandroid.framwork.networking

import android.os.AsyncTask
import android.util.Log
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import soteca.com.genisysandroid.framwork.model.decoder.*
import soteca.com.genisysandroid.framwork.model.encoder.body.Create
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection

/**
 * Created by SovannMeasna on 4/6/18.
 */
class RequestTask<T : Decoder>(val decoder: Decoder?, val done: ((result: T?, error: ResponseError?) -> Unit)? = null) : AsyncTask<T, T, Pair<T?, ResponseError?>>() {

    private val TAG = "tRequest"
    private var connection: HttpURLConnection? = null
    private var request: Request? = null

    override fun onPreExecute() {
        super.onPreExecute()
        request = decoder!!.request
    }

    override fun doInBackground(vararg params: T?): Pair<T?, ResponseError?> {
        var inputStream: InputStream? = null
        var isErrorResponse = false
        connection = request!!.requestUrl.openConnection() as HttpURLConnection
        connection!!.requestMethod = request!!.httpMethod.value
        connection!!.connectTimeout = request!!.timeout
        connection!!.doOutput = true
        connection!!.useCaches = false

        val postData: ByteArray = request!!.parameter!!
        val content = postData.toString(Charsets.UTF_8)
        for ((key, value) in request!!.header) {
            connection!!.setRequestProperty(key, value)
        }
        try {
            val outputStream = connection!!.outputStream
            outputStream.write(postData)
            outputStream.flush()

            if (connection!!.responseCode != HttpURLConnection.HTTP_OK && connection!!.responseCode != HttpURLConnection.HTTP_CREATED) {
                inputStream = connection!!.errorStream
                isErrorResponse = true
//                val errorText = streamToString(inputStream)!!
//                Log.d(TAG, errorText)
            } else {
                inputStream = connection!!.inputStream
                isErrorResponse = false
            }

        } catch (exception: Exception) {
            Log.d(TAG, "request: " + request!!.requestUrl)
            Log.e(TAG, "connection exception localizedMessage: " + exception.localizedMessage)
            Log.e(TAG, "connection exception message: " + exception.message)
        } finally {
            val serializer = Persister(AnnotationStrategy())

            if (inputStream == null) {
                return null to ResponseError.getInstance(DynamicsError.nilDataResponse)
            }

            if (isErrorResponse) {
                return null to (serializer.read(ErrorDecoder::class.java, inputStream).error)
            } else {
                when (decoder) {
                    is EntityMetadataListDecoder -> {
                        val entityMetadataListDecoder = serializer.read(EntityMetadataListDecoder::class.java, inputStream)
                        if (entityMetadataListDecoder.entityMetadatas == null) {
                            return null to ResponseError.getInstance(AuthenticationError.invalidSecurityToken)
                        }
                        return (entityMetadataListDecoder as T to null)
                    }
                    is EntityMetadataDecoder -> {
                        val entityMetadataDecoder = serializer.read(EntityMetadataDecoder::class.java, inputStream)
                        if (entityMetadataDecoder.entityMetadata == null) {
                            return null to ResponseError.getInstance(AuthenticationError.invalidSecurityToken)
                        }
                        return (entityMetadataDecoder as T to null)
                    }
                    is RolePrivilegeDecoder -> {
                        val rolePrivilegeDecoder = serializer.read(RolePrivilegeDecoder::class.java, inputStream)

                        if (rolePrivilegeDecoder.rolePrivileges == null) {
                            return null to ResponseError.getInstance(AuthenticationError.invalidSecurityToken)
                        }
                        return (rolePrivilegeDecoder as T to null)
                    }
                    is MyIdentifierDecoder -> {
                        val myIdentifierDecoder = serializer.read(MyIdentifierDecoder::class.java, inputStream)
                        closeConnection()
                        if (myIdentifierDecoder.myIdentifier == null) {
                            return null to ResponseError.getInstance(AuthenticationError.invalidSecurityToken)
                        }
                        return (myIdentifierDecoder as T to null)
                    }
                    is DeviceTokenDecoder -> {
                        val deviceTokenDecoder = serializer.read(DeviceTokenDecoder::class.java, inputStream)
                        if (deviceTokenDecoder.deviceToken == null) {
                            return null to ResponseError.getInstance(AuthenticationError.invalidCredential)
                        }
                        return (deviceTokenDecoder as T to null)
                    }
                    is TimeStampDecoder -> {

                        val timeStampDecoder = serializer.read(TimeStampDecoder::class.java, inputStream)
                        if (timeStampDecoder.timeStamp == null) {
                            return null to ResponseError.getInstance(AuthenticationError.invalidSecurityToken)
                        }
                        return (timeStampDecoder as T to null)
                    }
                    is TokenDecoder -> {
                        val tokenDecoder = serializer.read(TokenDecoder::class.java, inputStream)
                        if (tokenDecoder.token == null) {
                            return null to ResponseError.getInstance(AuthenticationError.invalidCredential)
                        }
                        return (tokenDecoder as T to null)
                    }
                    is RetrieveMultipleDecoder -> {
//                        val contentt = streamToString(inputStream)
                        val retrieveMultipleDecoder = serializer.read(RetrieveMultipleDecoder::class.java, inputStream)
                        closeConnection()
                        if (retrieveMultipleDecoder.results == null) {
                            return null to ResponseError.getInstance(AuthenticationError.invalidSecurityToken)
                        }
                        return (retrieveMultipleDecoder as T to null)
                    }
                    is CreateDecoder -> {
                        val createDecoder = serializer.read(CreateDecoder::class.java, inputStream)
                        if (createDecoder.createResult == null) {
                            return null to ResponseError.getInstance(AuthenticationError.invalidCredential)
                        }
                        return (createDecoder as T to null)
                    }
                    is ExecuteMultipleDecoder -> {
                        val executeMultipleDecoder = serializer.read(ExecuteMultipleDecoder::class.java, inputStream)
                        if (executeMultipleDecoder.responseItems == null) {
                            return null to ResponseError.getInstance(AuthenticationError.invalidCredential)
                        }
                        return (executeMultipleDecoder as T to null)
                    }
                    is StringDecoder -> { //Register Device
                        return (StringDecoder(null, streamToString(inputStream)) as T to null)
                    }
                }
            }

            return null to ResponseError.getInstance(DynamicsError.invalidDataResponse)
        }
    }

    override fun onPostExecute(result: Pair<T?, ResponseError?>) {
        super.onPostExecute(result)

        this.done?.let {
            it(result.first, result.second)
        }
    }

    private fun closeConnection() {
        connection?.disconnect()
    }

    private fun streamToString(inputStream: InputStream): String? {

        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line: String
        var result = ""

        try {
            do {
                line = bufferReader.readLine()
                if (line != null) {
                    result += line
                }
            } while (line != null)
            inputStream.close()
        } catch (ex: Exception) {
//            Log.e(TAG, "streamToString: " + ex.localizedMessage)
        }
        return result
    }
}