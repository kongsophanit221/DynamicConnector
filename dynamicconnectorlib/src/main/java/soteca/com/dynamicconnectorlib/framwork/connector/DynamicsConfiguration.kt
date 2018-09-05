package soteca.com.genisysandroid.framwork.connector

import android.content.Context
import soteca.com.genisysandroid.framwork.helper.SharedPreferenceHelper
import soteca.com.genisysandroid.framwork.model.EntityCollection

/**
 * Created by SovannMeasna on 3/26/18.
 */
class DynamicsConfiguration() {

    enum class DynamicsConnectionType(val value: String) {
        live("live.com"), office365("microsoftonline.com");

        companion object {
            fun from(findValue: String): DynamicsConnectionType = DynamicsConnectionType.values().first { it.value == findValue }
        }
    }

    var connectionType: DynamicsConnectionType? = null
    var crmUrl = ""
    var username = ""
    var password = ""

    val loginHost: String
        get() = connectionType!!.value

    companion object {
        val crmUrlToUrnAddress: HashMap<String, String> = hashMapOf("crm2.dynamics.com" to "urn:crmsam:dynamics.com", "crm4.dynamics.com" to "urn:crmemea:dynamics.com", "crm5.dynamics.com" to "urn:crmapac:dynamics.com")

        fun removeFromStorage(context: Context) {
            SharedPreferenceHelper.getInstance(context).deleteConfiguration()
        }

        fun initFromSave(context: Context): DynamicsConfiguration? {
            val configuration = SharedPreferenceHelper.getInstance(context).getConfiguration()

            if (configuration == null) {
                return null
            }
            return DynamicsConfiguration(DynamicsConnectionType.from(configuration["CONNECTION_TYPE"]!!), configuration["CRM_URL"]!!, configuration["USERNAME"]!!, configuration["PASSWORD"]!!)
        }
    }

    val urnAddress: String
        get() {
            for ((key, value) in crmUrlToUrnAddress) {
                if (crmUrl.contains(key)) {
                    return value
                }
            }
            return "urn:crmna:dynamics.com"
        }

//    constructor(context: Context) : this() {
//        val configuration = SharedPreferenceHelper.getInstance(context).getConfiguration()
//        if (configuration == null) {
//            return
//        }
//        this.crmUrl = configuration["CRM_URL"]!!
//        this.username = configuration["USERNAME"]!!
//        this.password = configuration["PASSWORD"]!!
//        this.connectionType = DynamicsConnectionType.valueOf(configuration["CONNECTION_TYPE"]!!)
//    }

//    constructor(type: DynamicsConnectionType) : this() {
//        this.connectionType = type
//    }

    constructor(connectionType: DynamicsConnectionType, crmUrl: String, username: String, password: String) : this() {
        this.connectionType = connectionType
        this.crmUrl = crmUrl
        this.username = username
        this.password = password
    }

    fun save(context: Context) {
        val configure: HashMap<String, String> = hashMapOf("CRM_URL" to crmUrl,
                "USERNAME" to username,
                "PASSWORD" to password,
                "CONNECTION_TYPE" to connectionType!!.value)
        SharedPreferenceHelper.getInstance(context).setConfiguration(configure)
    }

    fun isSameConfiguration(newConfig: DynamicsConfiguration): Boolean {
        return this.connectionType == newConfig.connectionType &&
                this.username == newConfig.username &&
                this.password == newConfig.password &&
                this.crmUrl == this.crmUrl
    }
}