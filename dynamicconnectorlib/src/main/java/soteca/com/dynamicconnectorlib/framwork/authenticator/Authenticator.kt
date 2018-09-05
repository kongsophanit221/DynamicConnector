package soteca.com.genisysandroid.framwork.authenticator

import android.content.Context
import soteca.com.genisysandroid.framwork.connector.DynamicsConfiguration

open class Authenticator {
    open val crmUrl: String = ""
        get

    constructor()

    constructor(context: Context)

    open fun authenticate(): Triple<String, String, String> {
        return Triple("", "", "")
    }

    open fun setConfiguration(configuration: DynamicsConfiguration) {}
    open fun clearConfiguration() {}
    open fun clearSecurityToken() {}
}