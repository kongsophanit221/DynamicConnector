package soteca.com.genisysandroid.framwork.authenticator

import android.content.Context
import android.util.Log
import soteca.com.genisysandroid.framwork.connector.DynamicsConfiguration
import soteca.com.genisysandroid.framwork.helper.SharedPreferenceHelper
import soteca.com.genisysandroid.framwork.helper.crmFormatToDate
import soteca.com.genisysandroid.framwork.helper.crmFormatToString
import soteca.com.genisysandroid.framwork.model.decoder.DeviceTokenDecoder
import soteca.com.genisysandroid.framwork.model.decoder.StringDecoder
import soteca.com.genisysandroid.framwork.model.decoder.TokenDecoder
import soteca.com.genisysandroid.framwork.model.encoder.DeviceAddRequestEncoder
import soteca.com.genisysandroid.framwork.model.encoder.EnvelopeEncoder
import soteca.com.genisysandroid.framwork.model.encoder.body.BodyEncoder
import soteca.com.genisysandroid.framwork.model.encoder.body.RequestSecurityTokenEncoder
import soteca.com.genisysandroid.framwork.model.encoder.header.HeaderEncoder
import soteca.com.genisysandroid.framwork.networking.AuthenticationError
import soteca.com.genisysandroid.framwork.networking.RequestTask
import java.util.*


/**
 * Created by SovannMeasna on 3/27/18.
 */
class DynamicAuthenticator : Authenticator {

    private val TAG = "tAuth"

    private lateinit var context: Context
    private var _configuration: DynamicsConfiguration? = null //DynamicsConfiguration = DynamicsConfiguration(DynamicsConfiguration.DynamicsConnectionType.office365)
    private var token: Token? = null

    override val crmUrl: String
        get() {
            return _configuration!!.crmUrl
        }

    constructor() : super()

    constructor(context: Context) : super(context) {
        this.context = context

        val config = DynamicsConfiguration.initFromSave(context)

        if (config == null) {
            return
        }
        this._configuration = config
        this.token = Token.initFromSave(context)
    }

    override fun setConfiguration(configuration: DynamicsConfiguration) {

        val existConfig = DynamicsConfiguration.initFromSave(context)
        existConfig?.let {

            if (!it.isSameConfiguration(configuration)) {
                Token.removeFromStorage(context)
                token = null

                DynamicsConfiguration.removeFromStorage(context)
            }
        }
        _configuration = configuration
    }

    override fun clearConfiguration() {
        _configuration = null
        DynamicsConfiguration.removeFromStorage(context)
    }

    override fun clearSecurityToken() {
        token = null
        Token.removeFromStorage(context)
    }

    override fun authenticate(): Triple<String, String, String> {

        val content = token?.let {
            it.content
        }

        if (content != null) {
            return content
        }

        try {
            token = getSecurityDetail()
        } catch (e: Exception) {
            Log.d(TAG, "authenticate: " + e.localizedMessage)
            throw e
        }

        token!!.saveToStorage(context)
        _configuration!!.save(context)

        return token!!.content!!
    }

    private fun getSecurityDetail(): Token? {

        val accessTokenDetail: Token?
        val envelop = EnvelopeEncoder(HeaderEncoder.getSecurityToken("https://login.microsoftonline.com/liveidSTS.srf", _configuration!!), BodyEncoder(RequestSecurityTokenEncoder.securityToken(_configuration!!.urnAddress)))//crmemea
        val request = AuthenticatorRequest(_configuration!!.loginHost, envelop, AuthenticatorRequest.Type.securityTokenAcquisition)
        val tokenDecoder = TokenDecoder(request)

        val result = RequestTask<TokenDecoder>(tokenDecoder).execute().get()

        if (result.second != null) {
            throw result.second!!.error!!.error
        }

        accessTokenDetail = result.first!!.token
        Log.d(TAG, "accessTokenDetail: " + accessTokenDetail)

        return accessTokenDetail
    }

    class Token() {

        companion object {

            fun removeFromStorage(context: Context) {
                SharedPreferenceHelper.getInstance(context).deleteAccessTokenDetail()
            }

            fun initFromSave(context: Context): Token? {

                val tokenFromStorage = SharedPreferenceHelper.getInstance(context).getAccessTokenDetail()

                if (tokenFromStorage == null) {
                    return null
                }
                val expirationDate = tokenFromStorage["EXPIRATION_DATE"]!!.crmFormatToDate()

                if (expirationDate.time < Date().time) {
                    removeFromStorage(context)
                    return null
                }

                val a = tokenFromStorage["FIRST_TOKEN"]!!
                val b = tokenFromStorage["SECOND_TOKEN"]!!
                val c = tokenFromStorage["KEY_IDENTIFIER"]!!
                val d = expirationDate

                return Token(tokenFromStorage["FIRST_TOKEN"]!!, tokenFromStorage["SECOND_TOKEN"]!!, tokenFromStorage["KEY_IDENTIFIER"]!!, expirationDate)
            }
        }

        private val TAG = "tAccessTokenDetail"

        private var firstToken: String = ""
        private var secondToken: String = ""
        private var keyIdentifier: String = ""
        private var expirationDate: Date? = null

        val content: Triple<String, String, String>?
            get() {

                if (expirationDate == null) {
                    return null
                }
                Log.d("tExpire", "expirationDate: ${expirationDate!!}")
                Log.d("tExpire", "NOW: ${Date()}")

                if (expirationDate!!.time > Date().time) {
                    return Triple(firstToken, secondToken, keyIdentifier)
                }
                return null
            }

        constructor(firstToken: String, secondToken: String, keyIdentifier: String, expirationDate: Date) : this() {
            this.firstToken = firstToken
            this.secondToken = secondToken
            this.keyIdentifier = keyIdentifier
            this.expirationDate = expirationDate
        }

        override fun toString(): String {
            return "firstToken: $firstToken, secondToken: $secondToken, keyIdentifier: $keyIdentifier, expirationDate: $expirationDate"
        }

        fun saveToStorage(context: Context) {
            val accessTokenDetail: HashMap<String, String> = hashMapOf("FIRST_TOKEN" to firstToken,
                    "SECOND_TOKEN" to secondToken,
                    "KEY_IDENTIFIER" to keyIdentifier,
                    "EXPIRATION_DATE" to expirationDate!!.crmFormatToString())

            SharedPreferenceHelper.getInstance(context).setAccessTokenDetail(accessTokenDetail)
        }
    }
}
