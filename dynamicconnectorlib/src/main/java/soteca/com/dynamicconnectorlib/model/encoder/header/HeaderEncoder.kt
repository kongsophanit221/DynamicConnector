package soteca.com.genisysandroid.framwork.model.encoder.header

import org.simpleframework.xml.*
import soteca.com.genisysandroid.framwork.connector.DynamicsConfiguration
import soteca.com.genisysandroid.framwork.helper.UtilsHelper
import java.util.*


@Root(name = "Header")
@Order(elements = arrayOf("Action", "MessageID", "ReplyTo", "VsDebuggerCausalityData", "To", "Security"))
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
class HeaderEncoder() {

    companion object {

        @Synchronized
        fun execute(urlString: String, contentSecurity: Triple<String, String, String>, isVsDebuggerCausalityData: Boolean = true): HeaderEncoder {
            return HeaderEncoder(urlString, contentSecurity, isVsDebuggerCausalityData)
        }

        fun getDeviceToken(urlString: String, credential: HashMap<String, String>?): HeaderEncoder {
            return HeaderEncoder(urlString, credential)
        }

        fun getSecurityToken(urlString: String, configuration: DynamicsConfiguration?): HeaderEncoder {
            return HeaderEncoder(urlString, configuration)
        }

        fun create(urlString: String, contentSecurity: Triple<String, String, String>, isVsDebuggerCausalityData: Boolean = false): HeaderEncoder {
            return HeaderEncoder(urlString, ActionUpload.CREATE, contentSecurity, isVsDebuggerCausalityData)
        }

        fun update(urlString: String, contentSecurity: Triple<String, String, String>, isVsDebuggerCausalityData: Boolean = false): HeaderEncoder {
            return HeaderEncoder(urlString, ActionUpload.UPDATE, contentSecurity, isVsDebuggerCausalityData)
        }

        fun delete(urlString: String, contentSecurity: Triple<String, String, String>, isVsDebuggerCausalityData: Boolean = false): HeaderEncoder {
            return HeaderEncoder(urlString, ActionUpload.DELETE, contentSecurity, isVsDebuggerCausalityData)
        }
    }


    @field:Element(name = "Action")
    @field:Namespace(reference = "http://www.w3.org/2005/08/addressing", prefix = "a")
    var action: Action? = null

    @field:Element(name = "Security")
    @field:Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", prefix = "o")
    var securityEncoder: SecurityEncoder? = null

    @field:Element(name = "MessageID")
    @field:Namespace(reference = "http://www.w3.org/2005/08/addressing", prefix = "a")
    private var messageId = "urn:uuid:${UtilsHelper.getInstance().uuid}"

    @field:Element(name = "ReplyTo")
    @field:Namespace(reference = "http://www.w3.org/2005/08/addressing", prefix = "a")
    private var replyTo = ReplyTo()

    @field:Element(name = "To")
    @field:Namespace(reference = "http://www.w3.org/2005/08/addressing", prefix = "a")
    var toAction: Action? = null

    @field:Element(name = "VsDebuggerCausalityData", required = false)
    @field:Namespace(reference = "http://schemas.microsoft.com/vstudio/diagnostics/servicemodelsink")
    private var vsDebuggerCausalityData: String? = null

    // Execute Header request
    private constructor(urlString: String, contentSecurity: Triple<String, String, String>, isVsDebuggerCausalityData: Boolean) : this() {

        if (isVsDebuggerCausalityData) {
            this.vsDebuggerCausalityData = "uIDPozJEz+P/wJdOhoN2XNauvYcAAAAAK0Y6fOjvMEqbgs9ivCmFPaZlxcAnCJ1GiX+Rpi09nSYACQAA"
        }
        this.action = Action("http://schemas.microsoft.com/xrm/2011/Contracts/Services/IOrganizationService/Execute")
        this.toAction = Action(urlString)

        val encryptedData = SecurityEncoder.EncryptedData(contentSecurity)
        this.securityEncoder = SecurityEncoder(encryptedData)
    }

    private constructor(urlString: String, credential: HashMap<String, String>?) : this() {
        this.action = Action("http://schemas.xmlsoap.org/ws/2005/02/trust/RST/Issue")
        this.toAction = Action(urlString)
        val usernameToken = SecurityEncoder.UsernameToken("devicesoftware", credential!!["deviceName"]!!, SecurityEncoder.UsernameToken.Password(credential!!["devicePassword"]!!))
        this.securityEncoder = SecurityEncoder(usernameToken)
    }

    // Get Security Token request
    private constructor(urlString: String, configuration: DynamicsConfiguration?) : this() {
        this.action = Action("http://schemas.xmlsoap.org/ws/2005/02/trust/RST/Issue")
        this.toAction = Action(urlString)

        val usernameToken = SecurityEncoder.UsernameToken("user", configuration!!.username, SecurityEncoder.UsernameToken.Password(configuration!!.password))
        this.securityEncoder = SecurityEncoder(usernameToken)
    }

    // Create Update Delete
    private constructor(urlString: String, action: ActionUpload, contentSecurity: Triple<String, String, String>, isVsDebuggerCausalityData: Boolean) : this() {

        if (isVsDebuggerCausalityData) {
            this.vsDebuggerCausalityData = "uIDPozJEz+P/wJdOhoN2XNauvYcAAAAAK0Y6fOjvMEqbgs9ivCmFPaZlxcAnCJ1GiX+Rpi09nSYACQAA"
        }
        this.action = Action("http://schemas.microsoft.com/xrm/2011/Contracts/Services/IOrganizationService/${action.value}")
        this.toAction = Action(urlString)

        val encryptedData = SecurityEncoder.EncryptedData(contentSecurity)
        this.securityEncoder = SecurityEncoder(encryptedData)
    }

    //For Element: Action, To
    class Action(val value: String) {
        @field:Text()
        private var text = value

        @field:Attribute(name = "mustUnderstand")
        @field:Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
        private var mustUnderstand = "1"
    }

    @Namespace(reference = "http://www.w3.org/2005/08/addressing", prefix = "a")
    @Root(name = "ReplyTo")
    class ReplyTo {
        @field:Namespace(reference = "http://www.w3.org/2005/08/addressing", prefix = "a")
        @field:Element(name = "Address")
        private var address = "http://www.w3.org/2005/08/addressing/anonymous"
    }

    private enum class ActionUpload(val value: String) {
        CREATE("Create"),
        UPDATE("Update"),
        DELETE("Delete"),
    }
}