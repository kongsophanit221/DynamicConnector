package soteca.com.genisysandroid.framwork.model.decoder

import org.simpleframework.xml.*
import soteca.com.genisysandroid.framwork.model.RolePrivilege
import soteca.com.genisysandroid.framwork.networking.Request

@Root(name = "Envelope", strict = false)
@NamespaceList(
        Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s"),
        Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts", prefix = "b"),
        Namespace(reference = "http://schemas.datacontract.org/2004/07/System.Collections.Generic", prefix = "d"),
        Namespace(reference = "http://schemas.microsoft.com/crm/2011/Contracts", prefix = "c")
)
data class RolePrivilegeDecoder(
        private var req: Request? = null,

        @field:Path(value = "Body/ExecuteResponse/ExecuteResult/Results/KeyValuePairOfstringanyType/value")
        @field:ElementList(entry = "RolePrivilege", inline = true, required = false)
        var rolePrivileges: ArrayList<RolePrivilege>? = null) : Decoder(req)