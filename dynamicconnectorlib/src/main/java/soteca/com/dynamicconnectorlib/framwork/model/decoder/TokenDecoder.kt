package soteca.com.genisysandroid.framwork.model.decoder

import org.simpleframework.xml.*
import soteca.com.genisysandroid.framwork.authenticator.DynamicAuthenticator
import soteca.com.genisysandroid.framwork.helper.crmFormatToDate
import soteca.com.genisysandroid.framwork.networking.Request

@Root(name = "Envelope", strict = false)
@NamespaceList(
        Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "S"),
        Namespace(reference = "http://schemas.xmlsoap.org/ws/2005/02/trust", prefix = "wst"),
        Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", prefix = "wsse"),
        Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", prefix = "wsu")
)
data class TokenDecoder(
        private var req: Request? = null,

        @field:Path(value = "Body/RequestSecurityTokenResponse/Lifetime")
        @field:Element(name = "Expires", required = false)
        private var expirationDate: String? = null,

        @field:Path(value = "Body/RequestSecurityTokenResponse/RequestedSecurityToken/EncryptedData/KeyInfo/EncryptedKey/KeyInfo/SecurityTokenReference")
        @field:Element(name = "KeyIdentifier", required = false)
        private var keyIdentifier: String? = null,

        @field:Path(value = "Body/RequestSecurityTokenResponse/RequestedSecurityToken/EncryptedData/KeyInfo/EncryptedKey/CipherData")
        @field:Element(name = "CipherValue", required = false)
        private var firstToken: String? = null,

        @field:Path(value = "Body/RequestSecurityTokenResponse/RequestedSecurityToken/EncryptedData/CipherData")
        @field:Element(name = "CipherValue", required = false)
        private var secondToken: String? = null) : Decoder(req) {

    var token: DynamicAuthenticator.Token? = null
        get() {
            if (firstToken == null && secondToken == null && keyIdentifier == null && expirationDate == null) {
                return null
            }
            return DynamicAuthenticator.Token(firstToken!!, secondToken!!, keyIdentifier!!, expirationDate!!.crmFormatToDate())
        }

}