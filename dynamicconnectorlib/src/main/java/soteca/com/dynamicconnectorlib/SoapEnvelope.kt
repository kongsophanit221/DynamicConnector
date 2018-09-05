package soteca.com.genisysandroid.framwork

import android.util.Log
import soteca.com.genisysandroid.framwork.connector.DynamicsConfiguration
import soteca.com.genisysandroid.framwork.helper.UtilsHelper
import java.util.*

/****
 *
 * Created by sophanit on 3/26/18.
 */
class SoapEnvelope() : SoapContent {

    private val TAG = "tEnvelope"

    enum class SoapEnvelopeType {
        execute, create, update, delete, securityToken, deviceToken,
        deviceRegistration
    }

    var type: SoapEnvelopeType? = null
    private var url: String = ""
    private var securityContent: Triple<String, String, String>? = null
    private var executeTag: SoapExecuteTag? = null
    private var deviceToken: String = ""
    private var configuration: DynamicsConfiguration? = null
    private var credential: HashMap<String, String>? = null
    private var host: String = ""
    private var utilsHelper = UtilsHelper.getInstance()

    constructor(url: String, securityContent: Triple<String, String, String>, executeTag: SoapExecuteTag, type: SoapEnvelopeType) : this() {
        this.url = url
        this.securityContent = securityContent
        this.executeTag = executeTag
        this.type = type
    }

    constructor(deviceToken: String, configuration: DynamicsConfiguration, type: SoapEnvelopeType) : this() {
        this.deviceToken = deviceToken
        this.configuration = configuration
        this.type = type
    }

    constructor(credential: HashMap<String, String>, host: String, type: SoapEnvelopeType) : this() {
        this.credential = credential
        this.host = host
        this.type = type
    }

    constructor(credential: HashMap<String, String>, type: SoapEnvelopeType) : this() {
        this.credential = credential
        this.type = type
    }

    override val content: String
        get() {
            var key = ""
            var first = ""
            var second = ""
            var contentTag = ""

            if (securityContent != null) {
                first = securityContent!!.first
                second = securityContent!!.second
                key = securityContent!!.third
            }
            if (executeTag != null) {
                contentTag = executeTag!!.content
            }

            when (type) {
                SoapEnvelopeType.execute -> {

                    Log.d(TAG, "contentTag: ${contentTag}")


                    return "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" " +
                            "xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">" +
                            "<s:Header><a:Action s:mustUnderstand=\"1\">http://schemas.microsoft.com/xrm/2011/Contracts/Services/IOrganizationService/Execute</a:Action>" +
                            "<a:MessageID>urn:uuid:${utilsHelper.uuid}</a:MessageID><a:ReplyTo><a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>" +
                            "</a:ReplyTo><VsDebuggerCausalityData xmlns=\"http://schemas.microsoft.com/vstudio/diagnostics/servicemodelsink\">uIDPozJEz+P/wJdOhoN2XNauvYcAAAAAK0Y6fOjvMEqbgs9ivCmFPaZlxcAnCJ1GiX+Rpi09nSYACQAA" +
                            "</VsDebuggerCausalityData><a:To s:mustUnderstand=\"1\">${url}</a:To><o:Security s:mustUnderstand=\"1\" " +
                            "xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
                            "<u:Timestamp u:Id=\"_0\"><u:Created>${utilsHelper.now}</u:Created><u:Expires>${utilsHelper.tomorrow}</u:Expires></u:Timestamp>" +
                            "<EncryptedData Id=\"Assertion0\" Type=\"http://www.w3.org/2001/04/xmlenc#Element\" " +
                            "xmlns=\"http://www.w3.org/2001/04/xmlenc#\"><EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#tripledes-cbc\">" +
                            "</EncryptionMethod><ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><EncryptedKey>" +
                            "<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p\"></EncryptionMethod><ds:KeyInfo Id=\"keyinfo\">" +
                            "<wsse:SecurityTokenReference xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
                            "<wsse:KeyIdentifier EncodingType=\"http://docs.oasis-open.org/wss/2004/01/" +
                            "oasis-200401-wss-soap-message-security-1.0#Base64Binary\" ValueType=\"http://docs.oasis-open.org/wss/2004/01/" +
                            "oasis-200401-wss-x509-token-profile-1.0#X509SubjectKeyIdentifier\">${key}</wsse:KeyIdentifier></wsse:SecurityTokenReference>" +
                            "</ds:KeyInfo><CipherData><CipherValue>${first}</CipherValue></CipherData></EncryptedKey></ds:KeyInfo><CipherData>" +
                            "<CipherValue>${second}</CipherValue></CipherData></EncryptedData></o:Security></s:Header>" +
                            "<s:Body>${contentTag}</s:Body></s:Envelope>"
                }

                SoapEnvelopeType.create ->
                    return "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" " +
                            "xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">" +
                            "<s:Header><a:Action s:mustUnderstand=\"1\">http://schemas.microsoft.com/xrm/2011/Contracts/Services/IOrganizationService/Create</a:Action>" +
                            "<a:MessageID>urn:uuid:${utilsHelper.uuid}</a:MessageID><a:ReplyTo>" +
                            "<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address></a:ReplyTo>" +
                            "<VsDebuggerCausalityData xmlns=\"http://schemas.microsoft.com/vstudio/diagnostics/servicemodelsink\">uIDPozJEz+P/wJdOhoN2XNauvYcAAAAAK0Y6fOjvMEqbgs9ivCmFPaZlxcAnCJ1GiX+Rpi09nSYACQAA</VsDebuggerCausalityData>" +
                            "<a:To s:mustUnderstand=\"1\">$url</a:To>" +
                            "<o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
                            "<u:Timestamp u:Id=\"_0\"><u:Created>${utilsHelper.now}</u:Created><u:Expires>${utilsHelper.tomorrow}</u:Expires></u:Timestamp>" +
                            "<EncryptedData Id=\"Assertion0\" SoapExecuteTagType=\"http://www.w3.org/2001/04/xmlenc#Element\" " +
                            "xmlns=\"http://www.w3.org/2001/04/xmlenc#\">" +
                            "<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#tripledes-cbc\"></EncryptionMethod>" +
                            "<ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">" +
                            "<EncryptedKey><EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p\"></EncryptionMethod>" +
                            "<ds:KeyInfo Id=\"keyinfo\">" +
                            "<wsse:SecurityTokenReference xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
                            "<wsse:KeyIdentifier EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" " +
                            "ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509SubjectKeyIdentifier\">$key</wsse:KeyIdentifier>" +
                            "</wsse:SecurityTokenReference></ds:KeyInfo>" +
                            "<CipherData><CipherValue>$first</CipherValue></CipherData></EncryptedKey></ds:KeyInfo>" +
                            "<CipherData><CipherValue>$second</CipherValue></CipherData></EncryptedData></o:Security></s:Header>" +
                            "<s:Body>$contentTag</s:Body></s:Envelope>"

                SoapEnvelopeType.update ->
                    return "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" " +
                            "xmlns:a=\"http://www.w3.org/2005/08/addressing\" " +
                            "xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">" +
                            "<s:Header><a:Action s:mustUnderstand=\"1\">" +
                            "http://schemas.microsoft.com/xrm/2011/Contracts/Services/IOrganizationService/Update</a:Action>" +
                            "<a:MessageID>urn:uuid:${utilsHelper.uuid}</a:MessageID>" +
                            "<a:ReplyTo><a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address></a:ReplyTo>" +
                            "<VsDebuggerCausalityData " +
                            "xmlns=\"http://schemas.microsoft.com/vstudio/diagnostics/servicemodelsink\">uIDPozJEz+P/wJdOhoN2XNauvYcAAAAAK0Y6fOjvMEqbgs9ivCmFPaZlxcAnCJ1GiX+Rpi09nSYACQAA</VsDebuggerCausalityData>" +
                            "<a:To s:mustUnderstand=\"1\">$url</a:To>" +
                            "<o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
                            "<u:Timestamp u:Id=\"_0\"><u:Created>${utilsHelper.now}</u:Created>" +
                            "<u:Expires>${utilsHelper.tomorrow}</u:Expires></u:Timestamp><EncryptedData Id=\"Assertion0\" SoapExecuteTagType=\"http://www.w3.org/2001/04/xmlenc#Element\" " +
                            "xmlns=\"http://www.w3.org/2001/04/xmlenc#\">" +
                            "<EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#tripledes-cbc\">" +
                            "</EncryptionMethod><ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">" +
                            "<EncryptedKey><EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p\"></EncryptionMethod>" +
                            "<ds:KeyInfo Id=\"keyinfo\"><wsse:SecurityTokenReference xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
                            "<wsse:KeyIdentifier EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" " +
                            "ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509SubjectKeyIdentifier\">$key</wsse:KeyIdentifier>" +
                            "</wsse:SecurityTokenReference></ds:KeyInfo><CipherData>" +
                            "<CipherValue>$first</CipherValue></CipherData></EncryptedKey></ds:KeyInfo>" +
                            "<CipherData><CipherValue>$second</CipherValue></CipherData></EncryptedData></o:Security></s:Header>" +
                            "<s:Body>$contentTag</s:Body></s:Envelope>"

                SoapEnvelopeType.delete ->
                    return "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" " +
                            "xmlns:a=\"http://www.w3.org/2005/08/addressing\" " +
                            "xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">" +
                            "<s:Header><a:Action s:mustUnderstand=\"1\">http://schemas.microsoft.com/xrm/2011/Contracts/Services/IOrganizationService/Delete</a:Action>" +
                            "<a:MessageID>urn:uuid:${utilsHelper.uuid}</a:MessageID><a:ReplyTo>" +
                            "<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address></a:ReplyTo>" +
                            "<VsDebuggerCausalityData xmlns=\"http://schemas.microsoft.com/vstudio/diagnostics/servicemodelsink\">uIDPozJEz+P/wJdOhoN2XNauvYcAAAAAK0Y6fOjvMEqbgs9ivCmFPaZlxcAnCJ1GiX+Rpi09nSYACQAA</VsDebuggerCausalityData>" +
                            "<a:To s:mustUnderstand=\"1\">url</a:To>" +
                            "<o:Security s:mustUnderstand=\"1\" " +
                            "xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
                            "<u:Timestamp u:Id=\"_0\"><u:Created>${utilsHelper.now}</u:Created><u:Expires>${utilsHelper.tomorrow}</u:Expires></u:Timestamp>" +
                            "<EncryptedData Id=\"Assertion0\" SoapExecuteTagType=\"http://www.w3.org/2001/04/xmlenc#Element\" " +
                            "xmlns=\"http://www.w3.org/2001/04/xmlenc#\"><EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#tripledes-cbc\">" +
                            "</EncryptionMethod><ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">" +
                            "<EncryptedKey><EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p\"></EncryptionMethod>" +
                            "<ds:KeyInfo Id=\"keyinfo\">" +
                            "<wsse:SecurityTokenReference xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
                            "<wsse:KeyIdentifier EncodingType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary\" " +
                            "ValueType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509SubjectKeyIdentifier\">key</wsse:KeyIdentifier>" +
                            "</wsse:SecurityTokenReference></ds:KeyInfo>" +
                            "<CipherData><CipherValue>$first</CipherValue></CipherData></EncryptedKey>" +
                            "</ds:KeyInfo><CipherData><CipherValue>$second</CipherValue></CipherData></EncryptedData></o:Security></s:Header>" +
                            "<s:Body>$contentTag</s:Body></s:Envelope>"

                SoapEnvelopeType.securityToken ->

                    return "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" " +
                            "xmlns:a=\"http://www.w3.org/2005/08/addressing\" " +
                            "xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">" +
                            "<s:Header><a:Action s:mustUnderstand=\"1\">http://schemas.xmlsoap.org/ws/2005/02/trust/RST/Issue</a:Action>" +
                            "<a:MessageID>urn:uuid:${utilsHelper.uuid}</a:MessageID><a:ReplyTo>" +
                            "<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address>" +
                            "</a:ReplyTo><VsDebuggerCausalityData xmlns=\"http://schemas.microsoft.com/vstudio/diagnostics/" +
                            "servicemodelsink\">uIDPozBEz+P/wJdOhoN2XNauvYcAAAAAK0Y6fOjvMEqbgs9ivCmFPaZlxcAnCJ1GiX+Rpi09nSYACQAA</VsDebuggerCausalityData>" +
                            "<a:To s:mustUnderstand=\"1\">https://login.${configuration!!.loginHost}/liveidSTS.srf</a:To>" +
                            "<o:Security s:mustUnderstand=\"1\" xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
                            "<u:Timestamp u:Id=\"_0\"><u:Created>${utilsHelper.now}</u:Created><u:Expires>${utilsHelper.tomorrow}</u:Expires></u:Timestamp>" +
                            "<o:UsernameToken u:Id=\"user\"><o:Username>${configuration!!.username}</o:Username>" +
                            "<o:Password SoapExecuteTagType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">" +
                            "${configuration!!.password}</o:Password></o:UsernameToken><wsse:BinarySecurityToken ValueType=\"urn:liveid:device\" " +
                            "xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
                            "<EncryptedData Id=\"BinaryDAToken0\" SoapExecuteTagType=\"http://www.w3.org/2001/04/xmlenc#Element\" " +
                            "xmlns=\"http://www.w3.org/2001/04/xmlenc#\"><EncryptionMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#tripledes-cbc\">" +
                            "</EncryptionMethod><ds:KeyInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">" +
                            "<ds:KeyName>http://Passport.NET/STS</ds:KeyName></ds:KeyInfo><CipherData><CipherValue>${deviceToken}\"</CipherValue>" +
                            "</CipherData></EncryptedData></wsse:BinarySecurityToken></o:Security></s:Header><s:Body>" +
                            "<t:RequestSecurityToken xmlns:t=\"http://schemas.xmlsoap.org/ws/2005/02/trust\">" +
                            "<wsp:AppliesTo xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\"><a:EndpointReference>" +
                            "<a:Address>${configuration!!.urnAddress}</a:Address></a:EndpointReference>" +
                            "</wsp:AppliesTo><wsp:PolicyReference URI=\"MBI_FED_SSL\" xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\" />" +
                            "<t:RequestType>http://schemas.xmlsoap.org/ws/2005/02/trust/Issue</t:RequestType></t:RequestSecurityToken>" +
                            "</s:Body></s:Envelope>"

                SoapEnvelopeType.deviceToken ->
                    return "<s:Envelope xmlns:s=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:a=\"http://www.w3.org/2005/08/addressing\" " +
                            "xmlns:u=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">" +
                            "<s:Header><a:Action s:mustUnderstand=\"1\">http://schemas.xmlsoap.org/ws/2005/02/trust/RST/Issue</a:Action>" +
                            "<a:MessageID>urn:uuid:${utilsHelper.uuid}</a:MessageID><a:ReplyTo>" +
                            "<a:Address>http://www.w3.org/2005/08/addressing/anonymous</a:Address></a:ReplyTo>" +
                            "<a:To s:mustUnderstand=\"1\">" +
                            "https://login.$host/liveidSTS.srf</a:To><o:Security s:mustUnderstand=\"1\" " +
                            "xmlns:o=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">" +
                            "<u:Timestamp u:Id=\"_0\">" +
                            "<u:Created>${utilsHelper.now}</u:Created><u:Expires>${utilsHelper.tomorrow}</u:Expires></u:Timestamp>" +
                            "<o:UsernameToken u:Id=\"devicesoftware\">" +
                            "<o:Username>${credential!!["deviceName"]}</o:Username>" +
                            "<o:Password SoapExecuteTagType=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">" +
                            "${credential!!["devicePassword"]}</o:Password></o:UsernameToken></o:Security></s:Header>" +
                            "<s:Body><t:RequestSecurityToken xmlns:t=\"http://schemas.xmlsoap.org/ws/2005/02/trust\">" +
                            "<wsp:AppliesTo xmlns:wsp=\"http://schemas.xmlsoap.org/ws/2004/09/policy\">" +
                            "<a:EndpointReference><a:Address>http://passport.net/tb</a:Address></a:EndpointReference></wsp:AppliesTo>" +
                            "<t:RequestType>http://schemas.xmlsoap.org/ws/2005/02/trust/Issue</t:RequestType></t:RequestSecurityToken></s:Body></s:Envelope>"

                SoapEnvelopeType.deviceRegistration ->
                    return "<DeviceAddRequestEncoder><ClientInfo name=\"${utilsHelper.uuid}\" " +
                            "version=\"1.0\"/><Authentication><Membername>${credential!!["deviceName"]}</Membername>" +
                            "<Password>${credential!!["devicePassword"]}</Password></Authentication></DeviceAddRequestEncoder>"
            }
            return ""
        }
}