package soteca.com.genisysandroid.framwork.model.encoder.body

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root
import org.simpleframework.xml.convert.Convert
import soteca.com.genisysandroid.framwork.model.EntityCollection
import soteca.com.genisysandroid.framwork.model.EntityReference
import soteca.com.genisysandroid.framwork.model.FetchExpression


// For Multi Execute (create, delete, update....etc)
@Root(name = "OrganizationRequest")
@Namespace(reference = "http://schemas.microsoft.com/xrm/2012/Contracts")
class ActionRequest() {

    @field:Attribute(name = "type")
    @field:Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance")
    private var type: String = ""

    @field:Element(name = "RequestName")
    @field:Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts")
    private var requestName: String = ""

    @field:Element(name = "RequestId")
    private var requestNil: RequestNil = RequestNil(true)

    @field:Element(name = "Parameters")
    @field:Namespace(reference = "http://schemas.datacontract.org/2004/07/System.Collections.Generic", prefix = "b")
    private var parameters: Parameters? = null

    enum class Action(val value: String) {
        create("Create"), update("Update"), delete("Delete")
    }

    constructor(action: Action, entity: EntityCollection.Entity? = null, entityReference: EntityReference? = null) : this() {
        this.type = "a:" + action.value + "Request"
        this.requestName = action.value

        if (action == Action.delete) {
            val keyValues = arrayListOf(KeyValuePair("Target", ValueType.entityReference2("a:EntityReference", entityReference!!)))
            this.parameters = Parameters(keyValues)

            return
        }

        var e = entity
        if (action == Action.create) {
            e = EntityCollection.Entity(entity?.attribute, "00000000-0000-0000-0000-000000000000", entity?.logicalName)
        }
        val keyValues = arrayListOf(KeyValuePair("Target", ValueType.entity("a:Entity", e!!)))
        this.parameters = Parameters(keyValues)


    }

    constructor(action: Action, id: String = "00000000-0000-0000-0000-000000000000", logicalName: String, attribute: EntityCollection.Attribute? = null) : this() {
        this.type = "a:" + action.value + "Request"
        this.requestName = action.value

        if (action == Action.delete) {
            val keyValues = arrayListOf(KeyValuePair("Target", ValueType.entityReference2("a:EntityReference", EntityReference(id, logicalName, null))))
            this.parameters = Parameters(keyValues)

            return
        }

        val keyValues = arrayListOf(KeyValuePair("Target", ValueType.entity("a:Entity", EntityCollection.Entity(attribute, id, logicalName))))
        this.parameters = Parameters(keyValues)
    }
}