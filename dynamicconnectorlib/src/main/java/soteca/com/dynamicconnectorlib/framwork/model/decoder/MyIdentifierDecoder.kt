package soteca.com.genisysandroid.framwork.model.decoder

import org.simpleframework.xml.*
import soteca.com.genisysandroid.framwork.model.MyIdentifier
import soteca.com.genisysandroid.framwork.networking.Request

@Root(name = "Envelope", strict = false)
@NamespaceList(
        Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s"),
        Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts", prefix = "b"),
        Namespace(reference = "http://schemas.datacontract.org/2004/07/System.Collections.Generic", prefix = "d")
)
class MyIdentifierDecoder(
        private var req: Request? = null,

        @field:Path(value = "Body/ExecuteResponse/ExecuteResult")
        @field:ElementList(name = "Results")
        private var results: ArrayList<KeyValuePairOfstringanyType>? = null
) : Decoder(req) {

    var myIdentifier: MyIdentifier? = null
        get() {
            var identifier = MyIdentifier()

            if (results != null) {

                results!!.forEach {

                    when (it.key) {
                        "UserId" -> {
                            identifier.userId = it.value!!
                        }
                        "BusinessUnitId" -> {
                            identifier.businessUnitId = it.value!!
                        }
                        "OrganizationId" -> {
                            identifier.organizationId = it.value!!
                        }
                    }
                }
                return identifier
            }
            return null
        }

    @Root(name = "KeyValuePairOfstringanyType", strict = false)
    data class KeyValuePairOfstringanyType(
            @field:Element(name = "key")
            var key: String? = null,

            @field:Element(name = "value")
            var value: String? = null)
}