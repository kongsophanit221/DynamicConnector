package soteca.com.genisysandroid.framwork.model.encoder.body

import android.util.Log
import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.convert.Convert
import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.stream.Format
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode
import soteca.com.genisysandroid.framwork.helper.decodeSpecialCharacter
import soteca.com.genisysandroid.framwork.model.EntityCollection
import soteca.com.genisysandroid.framwork.model.EntityReference
import soteca.com.genisysandroid.framwork.model.FetchExpression
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.OutputStream

@Root(name = "request")
@Convert(RequestEncoderConverter::class)
class RequestEncoder() {

    enum class RequestTypeName(val value: Pair<String, String>) {
        TIME_STAMP("a:RetrieveTimestampRequest" to "RetrieveTimestamp"),
        WHOAMI("b:WhoAmIRequest" to "WhoAmI"),
        USER_PRIVILEGE("b:RetrieveUserPrivilegesRequest" to "RetrieveUserPrivileges"),
        RETRIEVE_ENTITY("a:RetrieveEntityRequest" to "RetrieveEntity"),
        ALL_ENTITY("a:RetrieveAllEntitiesRequest" to "RetrieveAllEntities"),
        RETRIEVE_MULTIPLE("b:RetrieveMultipleRequest" to "RetrieveMultiple"),
        SET_STATE("b:SetStateRequest" to "SetState"),
        STATE_REQUEST_FULFILL("b:FulfillSalesOrderRequest" to "FulfillSalesOrder"),
        EXECUTE_MULTIPLE("a:ExecuteMultipleRequest" to "ExecuteMultiple")
    }

    companion object {

        @Synchronized
        fun timeStamp(): RequestEncoder {
            val typeName = RequestTypeName.TIME_STAMP.value
            return RequestEncoder(typeName.first, typeName.second)
        }

        fun whoAmI(): RequestEncoder {
            val typeName = RequestTypeName.WHOAMI.value
            return RequestEncoder(typeName.first, typeName.second)
        }

        fun userPrivileges(userId: String): RequestEncoder {
            val typeName = RequestTypeName.USER_PRIVILEGE.value
            val keyValues = listOf(KeyValuePair("UserId", ValueType.value("d:guid", userId, "http://schemas.microsoft.com/2003/10/Serialization/" to "d")))
            return RequestEncoder(typeName.first, typeName.second, keyValues)
        }

        fun entities(metadataId: String): RequestEncoder {
            val typeName = RequestTypeName.RETRIEVE_ENTITY.value
            val keyValues = listOf(
                    KeyValuePair("MetadataId", ValueType.value("ser:guid", metadataId, "http://schemas.microsoft.com/2003/10/Serialization/" to "ser")),
                    KeyValuePair("EntityFilters", ValueType.value("c:EntityFilters", "Entity Attributes Privileges Relationships", "http://schemas.microsoft.com/xrm/2011/Metadata" to "c")),
                    KeyValuePair("RetrieveAsIfPublished", ValueType.value("c:boolean", "false", "http://www.w3.org/2001/XMLSchema" to "c")))
            return RequestEncoder(typeName.first, typeName.second, keyValues)
        }

        fun allEntities(): RequestEncoder {
            val typeName = RequestTypeName.ALL_ENTITY.value
            val keyValues = listOf(
                    KeyValuePair("EntityFilters", ValueType.value("c:EntityFilters", "Entity", "http://schemas.microsoft.com/xrm/2011/Metadata" to "c")),
                    KeyValuePair("RetrieveAsIfPublished", ValueType.value("c:boolean", "false", "http://www.w3.org/2001/XMLSchema" to "c"))
            )
            return RequestEncoder(typeName.first, typeName.second, keyValues)
        }

        fun multipleRetreive(fetchExpression: FetchExpression): RequestEncoder {
            val typeName = RequestTypeName.RETRIEVE_MULTIPLE.value
            val keyValues = listOf(
                    KeyValuePair("Query", ValueType.query("a:FetchExpression", fetchExpression))
            )
            return RequestEncoder(typeName.first, typeName.second, keyValues)
        }

        fun setState(type: String, id: String, stateCode: String, statusCode: String): RequestEncoder {
            val typeName = RequestTypeName.SET_STATE.value
            val keyValues = listOf(
                    KeyValuePair("State", ValueType.valueC("b:OptionSetValue", stateCode)),
                    KeyValuePair("EntityMoniker", ValueType.entityReference("a:EntityReference", EntityReference(id, type, null))),
                    KeyValuePair("Status", ValueType.valueC("b:OptionSetValue", statusCode))
            )
            return RequestEncoder(typeName.first, typeName.second, keyValues, null)
        }

        fun stateRequest(type: String, id: String, statusCode: String): RequestEncoder {
            val typeName = RequestTypeName.STATE_REQUEST_FULFILL.value
            val keyValues = listOf(
                    KeyValuePair("salesorderid", ValueType.entityReference("b:EntityReference", EntityReference(id, type, null)), typeName.second)
            )
            val params = Parameters(keyValues, typeName.second, id, type)
            return RequestEncoder(typeName.first, typeName.second, params)
        }

        fun multipleExecute(actionRequests: ArrayList<ActionRequest>): RequestEncoder {
            val typeName = RequestTypeName.EXECUTE_MULTIPLE.value
            val keyValues = listOf(
                    KeyValuePair("Settings", ValueType.settings("c:ExecuteMultipleSettings", "true", "true")),
                    KeyValuePair("Requests", ValueType.actions("c:OrganizationRequestCollection", actionRequests))
            )

            return RequestEncoder(typeName.first, typeName.second, keyValues)
        }
    }

    var typeRequest: String? = null
    var nameRequest: String? = null
    var requestNil: RequestNil? = null
    var paramsRequest: Parameters? = null

    private constructor(typeRequest: String, nameRequest: String, keyValues: List<KeyValuePair>? = null) : this() {
        this.typeRequest = typeRequest
        this.nameRequest = nameRequest
        this.requestNil = RequestNil(true)
        this.paramsRequest = Parameters(keyValues, nameRequest)
    }

    //setState
    private constructor(typeRequest: String, nameRequest: String, keyValues: List<KeyValuePair>? = null, requestNil: RequestNil? = null) : this() {
        this.typeRequest = typeRequest
        this.nameRequest = nameRequest
        this.requestNil = requestNil
        this.paramsRequest = Parameters(keyValues, nameRequest)
    }

    //requestState
    private constructor(typeRequest: String, nameRequest: String, params: Parameters) : this() {
        this.typeRequest = typeRequest
        this.nameRequest = nameRequest
        this.paramsRequest = params
    }
}


@Root(name = "Parameters")
@Convert(ParamConverter::class)
class Parameters() {
    var keyValues: List<KeyValuePair>? = null
    var requestName: String = ""
    var id: String = ""
    var logicalName = ""

    constructor(keyValues: List<KeyValuePair>?, requestName: String = "") : this() {
        this.keyValues = keyValues
        this.requestName = requestName
    }

    constructor(keyValues: List<KeyValuePair>?, requestName: String, id: String, logicalName: String) : this() {
        this.keyValues = keyValues
        this.requestName = requestName
        this.id = id
        this.logicalName = logicalName
    }
}

@Root(name = "KeyValuePairOfstringanyType")
@Convert(KeyValueConverter::class)
data class KeyValuePair(val key: String, val value: ValueType, val requestName: String = "")

@Root(name = "value")
@Convert(ValueConverter::class)
sealed class ValueType(val type: String) {
    class value(type: String, val text: String, val namespace: Pair<String, String>? = null) : ValueType(type)
    class query(type: String, val fetchExpression: FetchExpression) : ValueType(type) // for query retreive multiple
    class valueC(type: String, val text: String) : ValueType(type)
    class entityReference(type: String, val entityReference: EntityReference) : ValueType(type)
    class settings(type: String, val continueOnError: String, val returnResponses: String) : ValueType(type)
    class actions(type: String, val actions: ArrayList<ActionRequest>) : ValueType(type)
    class entity(type: String, val entity: EntityCollection.Entity) : ValueType(type)
    class entityReference2(type: String, val entityReference: EntityReference) : ValueType(type)
}

@Root(name = "RequestId")
@Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts", prefix = "a")
class RequestNil(val isNil: Boolean) {
    @field:Attribute(name = "nil")
    @field:Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "i")
    private var nil: Boolean? = isNil
}


/**
 * Custom Converter
 * */
class RequestEncoderConverter : Converter<RequestEncoder> {

    override fun write(node: OutputNode?, value: RequestEncoder?) {

        val entityTypeName = RequestEncoder.RequestTypeName.RETRIEVE_ENTITY.value
        val allEntityTypeName = RequestEncoder.RequestTypeName.ALL_ENTITY.value
        val multipleTypeName = RequestEncoder.RequestTypeName.RETRIEVE_MULTIPLE.value

        when (value!!.nameRequest) {
            entityTypeName.second, allEntityTypeName.second -> {
                node!!.namespaces.setReference("http://schemas.microsoft.com/xrm/2011/Contracts", "a")
            }
            multipleTypeName.second -> {
                node!!.namespaces.setReference("http://schemas.microsoft.com/xrm/2011/Contracts", "a")
                node!!.namespaces.setReference("http://www.w3.org/2001/XMLSchema-instance", "i")
            }
            else -> {
                node!!.namespaces.setReference("http://schemas.microsoft.com/xrm/2011/Contracts", "a")
                node!!.namespaces.setReference("http://schemas.microsoft.com/crm/2011/Contracts", "b")
            }
        }

        node!!.setAttribute("type", value!!.typeRequest).reference = "http://www.w3.org/2001/XMLSchema-instance"

        val format = Format(0)
        val serializer = Persister(AnnotationStrategy(), format)
        serializer.write(value!!.paramsRequest, node!!)

        if (value!!.requestNil != null) {
            serializer.write(value!!.requestNil, node!!)
        }


        val requestChildNode = node!!.getChild("RequestName")
        requestChildNode.reference = "http://schemas.microsoft.com/xrm/2011/Contracts"
        requestChildNode.value = value!!.nameRequest
    }

    override fun read(node: InputNode?): RequestEncoder {
        throw UnsupportedOperationException("Not supported yet.")
    }
}

class ParamConverter : Converter<Parameters> {

    override fun write(node: OutputNode?, value: Parameters?) {

        val format = Format(0)
        val serializer = Persister(AnnotationStrategy(), format)

        node!!.reference = "http://schemas.microsoft.com/xrm/2011/Contracts"

        when (value!!.requestName) {
            RequestEncoder.RequestTypeName.TIME_STAMP.value.second, RequestEncoder.RequestTypeName.WHOAMI.value.second,
            RequestEncoder.RequestTypeName.USER_PRIVILEGE.value.second, RequestEncoder.RequestTypeName.RETRIEVE_MULTIPLE.value.second,
            RequestEncoder.RequestTypeName.STATE_REQUEST_FULFILL.value.second -> {

                node!!.namespaces.setReference("http://schemas.datacontract.org/2004/07/System.Collections.Generic", "c")

                if (value!!.keyValues != null) {
                    value!!.keyValues!!.forEach {
                        serializer.write(it, node!!)
                    }
                }
            }
            RequestEncoder.RequestTypeName.STATE_REQUEST_FULFILL.value.second -> {

                node!!.setAttribute("xmlns:b", "http://schemas.microsoft.com/xrm/2011/Contracts")
                node!!.setAttribute("xmlns:i", "http://www.w3.org/2001/XMLSchema-instance")

                val attributesChild = node!!.getChild("Attributes")
                attributesChild.reference = "http://schemas.microsoft.com/crm/2011/Contracts"
                attributesChild.namespaces.setReference("http://schemas.datacontract.org/2004/07/System.Collections.Generic", "c")

                if (value!!.keyValues != null) {
                    value!!.keyValues!!.forEach {
                        serializer.write(it, attributesChild)
                    }
                }

                val idChild = node!!.getChild("Id")
                idChild.reference = "http://schemas.microsoft.com/crm/2011/Contracts"
                idChild.value = "00000000-0000-0000-0000-000000000000"

                val logicalNameChild = node!!.getChild("LogicalName")
                logicalNameChild.reference = "http://schemas.microsoft.com/crm/2011/Contracts"
                logicalNameChild.value = "orderclose"

            }
            else -> { // Execute multi Create, Update, Delete Request
                node!!.namespaces.setReference("http://schemas.datacontract.org/2004/07/System.Collections.Generic", "b")


                if (value!!.keyValues != null) {
                    value!!.keyValues!!.forEach {

                        if (it.key == "Target") {
                            node!!.setAttribute("xmlns:b", "http://schemas.datacontract.org/2004/07/System.Collections.Generic")
                        }
                        serializer.write(it, node!!)
                    }
                }
            }
        }
    }

    override fun read(node: InputNode?): Parameters {
        throw UnsupportedOperationException("Not supported yet.")
    }
}

class KeyValueConverter : Converter<KeyValuePair> {

    override fun write(node: OutputNode?, value: KeyValuePair?) {
        val format = Format(0)
        val serializer = Persister(AnnotationStrategy(), format)

        if (value!!.requestName == RequestEncoder.RequestTypeName.STATE_REQUEST_FULFILL.value.second) {
            node!!.reference = "http://schemas.microsoft.com/crm/2011/Contracts"
        } else {
            node!!.reference = "http://schemas.microsoft.com/xrm/2011/Contracts"
        }


        val keyChildNode = node!!.getChild("key")
        keyChildNode.reference = "http://schemas.datacontract.org/2004/07/System.Collections.Generic"
        keyChildNode.value = value!!.key

        serializer.write(value!!.value, node)
    }

    override fun read(node: InputNode?): KeyValuePair {
        throw UnsupportedOperationException("Not supported yet.")
    }
}

class ValueConverter : Converter<ValueType> {

    private var crmReference = "http://schemas.microsoft.com/crm/2011/Contracts"
    private var xrmContact2011 = "http://schemas.microsoft.com/xrm/2011/Contracts"
    private var xrmContact2012 = "http://schemas.microsoft.com/xrm/2012/Contracts"

    override fun write(node: OutputNode?, value: ValueType?) {

        node!!.reference = "http://schemas.datacontract.org/2004/07/System.Collections.Generic"
        node!!.setAttribute("type", value!!.type).reference = "http://www.w3.org/2001/XMLSchema-instance"
        node!!.name = "value"

        when (value) {
            is ValueType.value -> {
                if (value.namespace != null) {
                    node!!.namespaces.setReference(value.namespace!!.first, value.namespace!!.second)
                }
                node!!.value = value.text
            }
            is ValueType.query -> {
                val childNode = node!!.getChild("a:Query")
                childNode.reference = crmReference

                val format = Format(0)
                val serializer = Persister(AnnotationStrategy(), format)
                val outPutStream = ByteArrayOutputStream()
                serializer.write(value.fetchExpression, outPutStream)
                val content = outPutStream.toString()
                childNode.value = outPutStream.toString()
            }
            is ValueType.valueC -> {
                createClideNode(node, "Value", value.text, crmReference)
            }
            is ValueType.entityReference -> {
                val entityReference = value.entityReference
                createClideNode(node!!, "Id", entityReference.id, crmReference)
                createClideNode(node!!, "LogicalName", entityReference.logicalName, crmReference)
                createClideNode(node!!, "Name", entityReference.name, crmReference)
            }
            is ValueType.settings -> {
                node!!.namespaces.setReference(xrmContact2012, "c")
                createClideNode(node!!, "ContinueOnError", value.continueOnError, xrmContact2012)
                createClideNode(node!!, "ReturnResponses", value.returnResponses, xrmContact2012)
            }
            is ValueType.actions -> {
                node!!.namespaces.setReference(xrmContact2012, "c")
                val actions = value.actions
                val format = Format(0)
                val serializer = Persister(AnnotationStrategy(), format)

                actions.forEach {
                    serializer.write(it, node!!)
                }
            }
            is ValueType.entity -> {
                val entity = value.entity

                if (entity.attribute != null && entity.attribute!!.keyValuePairList!!.size > 0) {
                    val format = Format(0)
                    val serializer = Persister(AnnotationStrategy(), format)
                    serializer.write(entity.attribute, node!!)
                } else {
                    createClideNode(node!!, "Attributes", "", xrmContact2011)
                }

                createClideNode(node!!, "Id", entity.id, xrmContact2011)
                createClideNode(node!!, "LogicalName", entity.logicalName, xrmContact2011)
            }

            is ValueType.entityReference2 -> {
                val entityReference = value.entityReference
                createClideNode(node!!, "Id", entityReference.id, xrmContact2011)
                createClideNode(node!!, "LogicalName", entityReference.logicalName, xrmContact2011)

                entityReference.name?.let {
                    createClideNode(node!!, "Name", entityReference.name, xrmContact2011)
                }

            }
        }
    }

    override fun read(node: InputNode?): ValueType {
        throw UnsupportedOperationException("Not supported yet.")
    }

    private fun createClideNode(node: OutputNode?, name: String, value: String?, reference: String? = null, prefix: String? = null) {
        if (value != null) {
            val childNode = node!!.getChild(name)
            childNode.value = value

            if (reference != null && prefix != null) {
                childNode.namespaces.setReference(reference, prefix)
            } else if (reference != null) {
                childNode.reference = reference
            }
        }
    }
}