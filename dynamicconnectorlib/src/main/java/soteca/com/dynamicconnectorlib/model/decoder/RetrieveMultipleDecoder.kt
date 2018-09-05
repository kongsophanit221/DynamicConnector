package soteca.com.genisysandroid.framwork.model.decoder

import org.simpleframework.xml.*
import soteca.com.genisysandroid.framwork.model.EntityCollection
import soteca.com.genisysandroid.framwork.networking.Request

/****
 *
 * Created by sophanit on 6/8/18.
 */

@Root(name = "Envelope", strict = false)
@NamespaceList(
        Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s"),
        Namespace(reference = "http://www.w3.org/2005/08/addressing", prefix = "a"),
        Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", prefix = "u"),
        Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts", prefix = "b"),
        Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "i"),
        Namespace(reference = "http://schemas.datacontract.org/2004/07/System.Collections.Generic", prefix = "c")
)
data class RetrieveMultipleDecoder(
        private var req: Request? = null,

        @field:Path("Body/ExecuteResponse/ExecuteResult/Results/KeyValuePairOfstringanyType")
        @field:Element(name = "value")
        var results: EntityCollection? = null) : Decoder(req)
