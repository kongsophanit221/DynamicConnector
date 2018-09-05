package soteca.com.genisysandroid.framwork.model.decoder

import org.simpleframework.xml.*
import soteca.com.genisysandroid.framwork.model.EntityMetadata
import soteca.com.genisysandroid.framwork.networking.Request

/**
 * Created by SovannMeasna on 4/20/18.
 */
@Root(name = "Envelope", strict = false)
@NamespaceList(
        Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s"),
        Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts", prefix = "b"),
        Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Metadata", prefix = "d"),
        Namespace(reference = "http://schemas.datacontract.org/2004/07/System.Collections.Generic", prefix = "c")
)
data class EntityMetadataDecoder(
        private var req: Request? = null,

        @field:Path(value = "Body/ExecuteResponse/ExecuteResult/Results/KeyValuePairOfstringanyType")
        @field:Element(name = "value", required = false)
        var entityMetadata: EntityMetadata? = null
) : Decoder(req)