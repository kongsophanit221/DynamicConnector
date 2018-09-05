package soteca.com.genisysandroid.framwork.model.decoder

import org.simpleframework.xml.*
import soteca.com.genisysandroid.framwork.networking.ResponseError

@Root(name = "Envelope", strict = false)
@NamespaceList(
        Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
)
data class ErrorDecoder(
        @field:Path(value = "Body")
        @field:Element(name = "Fault")
        var error: ResponseError? = null)