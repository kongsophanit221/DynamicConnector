package soteca.com.genisysandroid.framwork.model.decoder

import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root
import soteca.com.genisysandroid.framwork.networking.Request

@Root(name = "Envelope", strict = false)
@Namespace(reference = "http://schemas.datacontract.org/2004/07/System.Collections.Generic", prefix = "b")
data class TimeStampDecoder(
        private var req: Request? = null,

        @field:Path(value = "Body/ExecuteResponse/ExecuteResult/Results/KeyValuePairOfstringanyType")
        @field:Element(name = "value")
        var timeStamp: Int? = null) : Decoder(req)