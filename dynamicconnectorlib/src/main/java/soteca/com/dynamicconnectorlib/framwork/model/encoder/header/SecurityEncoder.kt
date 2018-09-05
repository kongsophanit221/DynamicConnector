package soteca.com.genisysandroid.framwork.model.encoder.header

import org.simpleframework.xml.*
import soteca.com.genisysandroid.framwork.helper.UtilsHelper

@Root(name = "Security")
@Order(elements = arrayOf("Timestamp", "EncryptedData", "UsernameToken")) // "wsse:BinarySecurityToken"
class SecurityEncoder() {
    @field:Attribute(name = "mustUnderstand")
    @field:Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
    private var mustUnderstand = "1"

    @field:Element(name = "Timestamp")
    @field:Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", prefix = "u")
    private var timestamp = Timestamp()

    @field:Element(name = "EncryptedData", required = false)
    @field:Namespace(reference = "http://www.w3.org/2001/04/xmlenc#")
    private var encryptedData: EncryptedData? = null

    @field:Element(name = "UsernameToken", required = false)
    @field:Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", prefix = "o")
    private var usernameToken: UsernameToken? = null

    // Execute Request
    constructor(encryptedData: EncryptedData) : this() {
        this.encryptedData = encryptedData
    }

    constructor(usernameToken: UsernameToken) : this() {
        this.usernameToken = usernameToken
    }

    @Root(name = "Timestamp")
    class Timestamp {
        val utils = UtilsHelper.getInstance()

        @field:Attribute(name = "Id")
        @field:Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", prefix = "u")
        private var id = "_0"

        @field:Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", prefix = "u")
        @field:Element(name = "Created")
        private var created = utils.now

        @field:Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", prefix = "u")
        @field:Element(name = "Expires")
        private var expires = utils.tomorrow
    }

    @Order(elements = arrayOf("EncryptionMethod", "KeyInfo", "CipherData"))
    @Namespace(reference = "http://www.w3.org/2001/04/xmlenc#")
    @Root(name = "EncryptedData")
    class EncryptedData(securityContent: Triple<String, String, String>) {
        @field:Attribute(name = "Id")
        private var id = "Assertion0"

        @field:Attribute(name = "Type")
        private var type = "http://www.w3.org/2001/04/xmlenc#Element"

        @field:Element(name = "EncryptionMethod")
        private var encryptionMethod = EncryptionMethod("http://www.w3.org/2001/04/xmlenc#tripledes-cbc")

        @field:Element(name = "KeyInfo")
        @field:Namespace(reference = "http://www.w3.org/2000/09/xmldsig#", prefix = "ds")
        private var keyInfo = KeyInfo(securityContent)

        @field:Path("CipherData")
        @field:Element(name = "CipherValue")
        private var cipherValue = securityContent.second // Second Token

        @Root(name = "EncryptionMethod")
        class EncryptionMethod(val value: String) {
            @field:Attribute(name = "Algorithm")
            private var algorithm = value

            @field:Text()
            private var text = ""
        }

        @Root(name = "KeyInfo")
        class KeyInfo(securityContent: Triple<String, String, String>) {

            @field:Element(name = "EncryptedKey")
            private var encryptedKey = EncryptedKey(securityContent)

            @Root(name = "EncryptedKey")
            @Order(elements = arrayOf("EncryptionMethod", "KeyInfo", "CipherData"))
            class EncryptedKey(securityContent: Triple<String, String, String>) {
                @field:Element(name = "EncryptionMethod")
                private var encryptionMethod = EncryptionMethod("http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p")

                @field:Element(name = "KeyInfo")
                @field:Namespace(reference = "http://www.w3.org/2000/09/xmldsig#", prefix = "ds")
                private var subKeyInfo = SubKeyInfo(securityContent.third) //Key Identifier

                @field:Path("CipherData")
                @field:Element(name = "CipherValue")
                private var cipherValue = securityContent.first //First Token

                @Root(name = "KeyInfo")
                class SubKeyInfo(key: String) {
                    @field:Attribute(name = "Id")
                    private var id = "keyinfo"

                    @field:Element(name = "wsse:SecurityTokenReference")
                    private var securityTokenReference = SecurityTokenReference(key)

                    @Root(name = "wsse:SecurityTokenReference")
                    class SecurityTokenReference(val key: String) {

                        @field:Attribute(name = "xmlns:wsse")
                        private var namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"

                        @field:Element(name = "wsse:KeyIdentifier")
                        private var keyIdentifier = KeyIdentifier(key)

                        @Root(name = "wsse:KeyIdentifier")
                        class KeyIdentifier(val key: String) {
                            @field:Attribute(name = "EncodingType")
                            private var encodingType = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary"

                            @field:Attribute(name = "ValueType")
                            private var valueType = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-x509-token-profile-1.0#X509SubjectKeyIdentifier"

                            @field:Text()
                            private var text = key
                        }
                    }
                }
            }
        }
    }

    @Root(name = "UsernameToken")
    @Order(elements = arrayOf("Username", "Password"))
    @Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", prefix = "o")
    class UsernameToken(id: String, username: String, password: Password) {

        @field:Attribute(name = "Id")
        @field:Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", prefix = "u")
        private var aId = id

        @field:Element(name = "Username")
        @field:Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", prefix = "o")
        private var aUsername = username

        @field:Element(name = "Password")
        @field:Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", prefix = "o")
        private var aPassword = password

        @Root(name = "Password")
        class Password(value: String) {
            @field:Attribute(name = "Type")
            private var type = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText"

            @field:Text()
            private var text = value
        }
    }

    @Root(name = "wsse:BinarySecurityToken")
    class BinarySecurityToken(deviceToken: String) {
        @field:Attribute(name = "xmlns:wsse")
        private var wsse = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"

        @field:Attribute(name = "ValueType")
        private var valueType = "urn:liveid:device"

        @field:Element(name = "EncryptedData")
        private var encryptedData = EncryptedData(deviceToken)


        @Root(name = "EncryptedData")
        @Order(elements = arrayOf("EncryptionMethod", "KeyInfo", "CipherData"))
        @Namespace(reference = "http://www.w3.org/2001/04/xmlenc#")
        class EncryptedData(deviceToken: String) {
            @field:Attribute(name = "Id")
            private var id = "BinaryDAToken0"

            @field:Attribute(name = "SoapExecuteTagType")
            private var type = "http://www.w3.org/2001/04/xmlenc#Element"

            @field:Element(name = "EncryptionMethod")
            private var encryptionMethod = SecurityEncoder.EncryptedData.EncryptionMethod("http://www.w3.org/2001/04/xmlenc#tripledes-cbc")

            @field:Element(name = "KeyInfo")
            private var keyInfo = KeyInfo()

            @field:Path("CipherData")
            @field:Element(name = "CipherValue")
            private var cipherValue = deviceToken
        }

        @Root(name = "KeyInfo")
        @Namespace(reference = "http://www.w3.org/2000/09/xmldsig#", prefix = "ds")
        class KeyInfo {

            @field:Element(name = "KeyName")
            @field:Namespace(reference = "http://www.w3.org/2000/09/xmldsig#", prefix = "ds")
            private var keyName = "http://Passport.NET/STS"

        }
    }
}