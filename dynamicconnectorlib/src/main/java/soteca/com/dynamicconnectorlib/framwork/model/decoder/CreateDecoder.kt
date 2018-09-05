package soteca.com.genisysandroid.framwork.model.decoder

import org.simpleframework.xml.*
import soteca.com.genisysandroid.framwork.networking.Request

@Root(name = "Envelope", strict = false)
@NamespaceList(
        Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s"),
        Namespace(reference = "http://www.w3.org/2005/08/addressing", prefix = "a"),
        Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", prefix = "u")
)

data class CreateDecoder(
        private var req: Request? = null,

        @field:Path(value = "Body/CreateResponse")
        @field:Element(name = "CreateResult")
        var createResult: String? = null
) : Decoder(req)