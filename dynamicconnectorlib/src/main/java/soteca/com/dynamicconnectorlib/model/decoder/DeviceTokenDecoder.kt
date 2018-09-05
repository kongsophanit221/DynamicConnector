package soteca.com.genisysandroid.framwork.model.decoder

import org.simpleframework.xml.*
import soteca.com.genisysandroid.framwork.networking.Request

@Root(name = "Envelope", strict = false)
@NamespaceList(
        Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "S"),
        Namespace(reference = "http://schemas.xmlsoap.org/ws/2005/02/trust", prefix = "wst")
)

data class DeviceTokenDecoder(
        private var req: Request? = null,

        @field:Path(value = "Body/RequestSecurityTokenResponse/RequestedSecurityToken/EncryptedData/CipherData")
        @field:Element(name = "CipherValue")
        var deviceToken: String? = null) : Decoder(req)