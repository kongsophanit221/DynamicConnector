package soteca.com.genisysandroid.framwork.model.encoder.body

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementUnion
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

@Root(name = "Body")
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
class BodyEncoder(action: ActionEncoder) {

    @field:ElementUnion(
            Element(name = "Create", type = Create::class),
            Element(name = "Update", type = Update::class),
            Element(name = "Delete", type = Delete::class),
            Element(name = "Execute", type = ExecuteActionEncoder::class),
            Element(name = "RequestSecurityToken", type = RequestSecurityTokenEncoder::class)
    )
    var a = action

    open class ActionEncoder() { //Execute, RequestSecurityToken...

    }
}