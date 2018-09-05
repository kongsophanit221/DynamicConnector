package soteca.com.genisysandroid.framwork.model.encoder.body

import org.simpleframework.xml.*

@Root(name = "RequestSecurityToken")
@Namespace(reference = "http://schemas.xmlsoap.org/ws/2005/02/trust", prefix = "t")
@Order(elements = arrayOf("AppliesTo", "PolicyReference", "RequestType"))
class RequestSecurityTokenEncoder() : BodyEncoder.ActionEncoder() {

    companion object {
        fun deviceToken(address: String): RequestSecurityTokenEncoder {
            return RequestSecurityTokenEncoder(address)
        }

        fun securityToken(address: String): RequestSecurityTokenEncoder {
            return RequestSecurityTokenEncoder(address)
        }
    }

    @field:Element(name = "AppliesTo")
    private var appliesTo: AppliesTo? = null

    @field:Element(name = "RequestType")
    @field:Namespace(reference = "http://schemas.xmlsoap.org/ws/2005/02/trust", prefix = "t")
    private var requestType = "http://schemas.xmlsoap.org/ws/2005/02/trust/Issue"

    @field:Element(name = "PolicyReference", required = false)
    @field:Namespace(reference = "http://schemas.xmlsoap.org/ws/2004/09/policy", prefix = "wsp")
    private var policyReference: PolicyReference? = null

    private constructor(address: String, isHasPolicyReference: Boolean = false) : this() {
        this.appliesTo = AppliesTo(address)

        if (isHasPolicyReference) {
            policyReference = PolicyReference()
        }
    }

    @Root(name = "AppliesTo")
    @Namespace(reference = "http://schemas.xmlsoap.org/ws/2004/09/policy", prefix = "wsp")
    class AppliesTo(address: String) {
        @field:Element(name = "EndpointReference")
        private var endpointReference = EndpointReference(address)
    }

    @Root(name = "EndpointReference")
    @Namespace(reference = "http://www.w3.org/2005/08/addressing", prefix = "a")
    class EndpointReference(address: String) {
        @field:Element(name = "Address")
        @field:Namespace(reference = "http://www.w3.org/2005/08/addressing", prefix = "a")
        private var address = address
    }

    @Root(name = "PolicyReference")
    @Namespace(reference = "http://schemas.xmlsoap.org/ws/2004/09/policy", prefix = "wsp")
    class PolicyReference {
        @field:Attribute(name = "URI")
        private var uri = "MBI_FED_SSL"
    }
}