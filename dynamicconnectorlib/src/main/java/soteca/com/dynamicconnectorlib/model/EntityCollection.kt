package soteca.com.genisysandroid.framwork.model

import android.util.Log
import org.simpleframework.xml.*
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.convert.Convert
import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.Node
import org.simpleframework.xml.stream.OutputNode
import soteca.com.genisysandroid.framwork.helper.crmFormatToDate
import soteca.com.genisysandroid.framwork.helper.crmFormatToString
import java.util.*
import kotlin.collections.ArrayList

/****
 *
 * Created by sophanit on 6/8/18.
 */

@Root(name = "value", strict = false)
data class EntityCollection(
        @field:Element(name = "EntityName")
        var entityName: String = "",

        @field:Element(name = "MoreRecords", required = false)
        var moreRecords: Boolean? = false,

        @field:Element(name = "PagingCookie", required = false)
        var pagingCookie: String? = null,

        @field:Element(name = "TotalRecordCount", required = false)
        var totalRecordCount: Int? = null,

        @field:Path(value = "Entities")
        @field:Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts")
        @field:ElementList(entry = "Entity", inline = true, required = false)
        var entityList: ArrayList<EntityCollection.Entity>? = null

) {

    @Root(name = "Entity", strict = false)
    class Entity(

            @field:Element(name = "Attributes", required = false)
            @field:Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts")
            var attribute: Attribute? = null,

            @field:Element(name = "Id", required = false)
            @field:Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts")
            var id: String? = null,

            @field:Element(name = "LogicalName", required = false)
            @field:Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts")
            var logicalName: String? = null
    )
//    {
//        override fun toString(): String {
//            val keyValuePair = keyValuePairList ?: ArrayList<KeyValuePairOfstringanyType>()
//            var keyValueString = ""
//            keyValuePair.forEach {
//                keyValueString += it.toString()
//            }
//
//            return "id: ${id} logicalName: ${logicalName}, Size: ${keyValuePairList!!.size}, Value: ${keyValueString}"
//        }
//    }

    @Root(name = "Attributes", strict = false)
    @Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts")
    @NamespaceList(
            Namespace(reference = "http://schemas.datacontract.org/2004/07/System.Collections.Generic", prefix = "c"),
            Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts")
    )
    class Attribute(
            @field:ElementList(entry = "KeyValuePairOfstringanyType", inline = true, required = false)
            @field:Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts")
            var keyValuePairList: ArrayList<KeyValuePairOfstringanyType>? = null
    ) {
        operator fun get(key: String): ValueType? {
            val vt = keyValuePairList!!.filter {
                it.key == key
            }
            if (vt.size == 0) {
                return null
            }
            return vt.first().value
        }

        operator fun set(key: String, value: ValueType?) {
            val vt = keyValuePairList!!.filter {
                it.key == key
            }
            if (!vt.isEmpty()) {
                vt.first().valueType = Value(value)
            } else {
                keyValuePairList!!.add(KeyValuePairOfstringanyType(key, Value(value)))
            }
        }

        fun of(vararg pairs: Pair<String, ValueType>): Attribute {
            var kvList = ArrayList<KeyValuePairOfstringanyType>()
            pairs.forEach {
                kvList.add(KeyValuePairOfstringanyType(it.first, Value(it.second)))
            }
            return Attribute(kvList)
        }
    }

    @Root(name = "KeyValuePairOfstringanyType", strict = false)
    @Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts")
    data class KeyValuePairOfstringanyType(
            @field:Element(name = "key", required = false)
            @field:Namespace(reference = "http://schemas.datacontract.org/2004/07/System.Collections.Generic", prefix = "c")
            var key: String? = null,

            @field:Element(name = "value", required = false)
            @field:Namespace(reference = "http://schemas.datacontract.org/2004/07/System.Collections.Generic", prefix = "c")
            var valueType: Value? = null) {

        val value: ValueType?
            get() = valueType!!.value

        override fun toString(): String {
            return "key: ${key} value:${value!!.associatedValue}"
        }
    }

    @Convert(value = ValueConverter::class)
    @Root(strict = false)
    data class Value(
            var value: ValueType? = null
    )

    sealed class ValueType {
        class optionSetValue(val value: String) : ValueType()
        class dateTime(val value: Date) : ValueType()
        class boolean(val value: Boolean) : ValueType()
        class guid(val value: String) : ValueType()
        class string(val value: String) : ValueType()
        class int(val value: Int) : ValueType()
        class decimal(val value: Double) : ValueType()
        class long(val value: Long) : ValueType()
        class double(val value: Double) : ValueType()
        class money(val value: Double, var isMultiExe: Boolean = false) : ValueType()
        class data(val value: String) : ValueType()
        class entityReference(val value: EntityReference, var isMultiExe: Boolean = false) : ValueType()
        class entityCollection(val value: EntityCollection) : ValueType()
        class aliasedValue(val value: AliasedType) : ValueType()

        val associatedValue: Any
            get() {
                when (this) {
                    is optionSetValue -> return value
                    is dateTime -> return value
                    is boolean -> return value
                    is guid -> return value
                    is string -> return value
                    is int -> return value
                    is decimal -> return value
                    is long -> return value
                    is double -> return value
                    is money -> return value
                    is data -> return value
                    is entityReference -> return value
                    is entityCollection -> return value
                    is aliasedValue -> return value
//                    is moneyMultiExe -> return value
                }
            }
    }

    enum class Type(val value: String) {
        OPTION_SET_VALUE("b:OptionSetValue"),
        DATE_TIME("d:dateTime"),
        BOOLEAN("d:boolean"),
        BOOLEAN_MANAGED_PROPERTY("BooleanManagedProperty"),
        GUID("d:guid"),
        STRING("d:string"),
        INT("d:int"),
        DOUBLE("d:double"),
        DECIMAL("d:decimal"),
        LONG("long"),
        MONEY("b:Money"),
        ENTITY_REFERENCE("b:EntityReference"),
        ENTITY_COLLECTION("b:EntityCollection"),
        ALIASED_VALUE("b:AliasedValue"),
        DATA("base64Binary");
//        MONEY_MULTI_EXE("a:Money");

        companion object {
            fun from(findValue: String): Type = Type.values().first { it.value == findValue }
        }
    }

    class ValueConverter : Converter<Value> {

        override fun write(node: OutputNode?, value: Value?) {
            value?.let {
                when (it.value!!) {

                    is ValueType.boolean -> {
                        node!!.setAttribute("i:type", Type.BOOLEAN.value)
                        node.namespaces.setReference("http://www.w3.org/2001/XMLSchema", "d")
                        node.value = ((it.value as ValueType.boolean).value).toString()
                    }

                    is EntityCollection.ValueType.optionSetValue -> {

                        node!!.setAttribute("i:type", Type.OPTION_SET_VALUE.value)
                        val nodeContainer = node.getChild("Value")
                        nodeContainer.namespaces.setReference("http://schemas.microsoft.com/xrm/2011/Contracts", "b")
                        nodeContainer.reference = "http://schemas.microsoft.com/xrm/2011/Contracts"
                        nodeContainer.value = (it.value as ValueType.optionSetValue).value
                    }

                    is EntityCollection.ValueType.dateTime -> {
                        node!!.namespaces.setReference("http://schemas.microsoft.com/2003/10/Serialization/", "d")
                        node.setAttribute("i:type", Type.DATE_TIME.value)
                        node.value = ((it.value as ValueType.dateTime).value).crmFormatToString()
                    }

                    is EntityCollection.ValueType.guid -> {
                        node!!.namespaces.setReference("http://schemas.microsoft.com/2003/10/Serialization/", "d")
                        node.setAttribute("i:type", Type.GUID.value)
                        node.value = (it.value as ValueType.guid).value
                    }

                    is EntityCollection.ValueType.string -> {
                        node!!.namespaces.setReference("http://www.w3.org/2001/XMLSchema", "d")
                        node.setAttribute("i:type", Type.STRING.value)
                        val aa = (it.value as ValueType.string).value
                        node.value = (it.value as ValueType.string).value

                    }

                    is EntityCollection.ValueType.int -> {
                        node!!.namespaces.setReference("http://www.w3.org/2001/XMLSchema", "d")
                        node.setAttribute("i:type", Type.INT.value)
                        node.value = (it.value as ValueType.int).value.toString()
                    }

                    is EntityCollection.ValueType.decimal -> {
                        node!!.namespaces.setReference("http://www.w3.org/2001/XMLSchema", "d")
                        node.setAttribute("i:type", Type.DECIMAL.value)
                        node.value = (it.value as ValueType.decimal).value.toString()
                    }

                    is EntityCollection.ValueType.long -> {
                        node!!.namespaces.setReference("http://www.w3.org/2001/XMLSchema", "d")
                        node.setAttribute("i:type", Type.LONG.value)
                        node.value = (it.value as ValueType.long).value.toString()
                    }

                    is EntityCollection.ValueType.double -> {
                        node!!.namespaces.setReference("http://www.w3.org/2001/XMLSchema", "d")
                        node.setAttribute("i:type", Type.DOUBLE.value)
                        node.value = (it.value as ValueType.double).value.toString()
                    }

                    is EntityCollection.ValueType.money -> {
                        val money = it.value as ValueType.money
                        var nodeContainer: Node?

                        if (money.isMultiExe) {
                            node!!.setAttribute("i:type", "a:Money")
                            nodeContainer = node!!.getChild("Value")
                            nodeContainer.reference = "http://schemas.microsoft.com/xrm/2011/Contracts"
                        } else {
                            node!!.setAttribute("i:type", Type.MONEY.value)
                            nodeContainer = node!!.getChild("Value")
                            nodeContainer.reference = "http://schemas.microsoft.com/xrm/2011/Contracts"
                        }

                        nodeContainer.value = money.value.toString()
                    }

                    is EntityCollection.ValueType.entityReference -> {
                        val entityReferenceValueType = it.value as ValueType.entityReference

                        if (entityReferenceValueType.isMultiExe) {
                            node!!.setAttribute("i:type", "a:EntityReference")
                        } else {
                            node!!.setAttribute("i:type", Type.ENTITY_REFERENCE.value)
                        }

                        val entityReference = entityReferenceValueType.value
                        val nodeId = node.getChild("Id")
                        nodeId.reference = "http://schemas.microsoft.com/xrm/2011/Contracts"
                        nodeId.value = entityReference.id

                        val nodeLogicalName = node.getChild("LogicalName")
                        nodeLogicalName.reference = "http://schemas.microsoft.com/xrm/2011/Contracts"
                        nodeLogicalName.value = entityReference.logicalName

                        entityReference.name?.let {
                            val nodeName = node.getChild("Name")
                            nodeName.reference = "http://schemas.microsoft.com/xrm/2011/Contracts"
                            nodeName.value = it
                        }
                    }

                    is EntityCollection.ValueType.aliasedValue -> TODO()

                    is EntityCollection.ValueType.data -> TODO()

                    else -> {

                    }
                }

            }
        }

        override fun read(node: InputNode?): Value {

            val type = node!!.getAttribute("type").value
            val value = Value()
            val valueType = Type.from(type)

            when (valueType) {
                Type.OPTION_SET_VALUE -> {
                    value.value = EntityCollection.ValueType.optionSetValue(node.getNext("Value").value)
                }
                Type.DATE_TIME -> {
                    value.value = EntityCollection.ValueType.dateTime(node.value.crmFormatToDate())
                }
                Type.BOOLEAN -> {
                    value.value = EntityCollection.ValueType.boolean(node.value!!.toBoolean())
                }
                Type.DOUBLE -> {
                    value.value = EntityCollection.ValueType.double(node.value.toDouble())
                }
                Type.DECIMAL -> {
                    value.value = EntityCollection.ValueType.decimal(node.value.toDouble())
                }
                Type.GUID -> {
                    value.value = EntityCollection.ValueType.guid(node.value)
                }
                Type.INT -> {
                    value.value = EntityCollection.ValueType.int(node.value.toInt())
                }
                Type.STRING -> {
                    value.value = EntityCollection.ValueType.string(node.value)
                }
                Type.ENTITY_REFERENCE -> {
                    val serializer = Persister()
                    val entityReference = serializer.read(EntityReference::class.java, node)
                    value.value = EntityCollection.ValueType.entityReference(entityReference)
                }
                Type.LONG -> {
                    value.value = EntityCollection.ValueType.long(node.value.toLong())
                }
                Type.MONEY -> {
                    value.value = EntityCollection.ValueType.money(node.getNext("Value").value.toDouble())
                }
                Type.ENTITY_COLLECTION -> {
                    val serializer = Persister()
                    val entityCollection = serializer.read(EntityCollection::class.java, node)
                    value.value = EntityCollection.ValueType.entityCollection(entityCollection)
                }
                Type.ALIASED_VALUE -> {
                    val serializer = Persister(AnnotationStrategy())
                    val aliasedType = serializer.read(AliasedType::class.java, node)
                    value.value = EntityCollection.ValueType.aliasedValue(aliasedType)
                }
                Type.DATA -> {
                    value.value = EntityCollection.ValueType.data(node.value)
                }
                Type.BOOLEAN_MANAGED_PROPERTY -> ""
            }
            return value
        }
    }
}

@Root(strict = false)
@Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts")
data class AliasedType(
        @field:Element(name = "AttributeLogicalName", required = false)
        var attributeLogicalName: String? = null,

        @field:Element(name = "EntityLogicalName", required = false)
        var entityLogicalName: String? = null,

        @field:Element(name = "Value", required = false)
        private var valueType: EntityCollection.Value? = null
) {

    val value: EntityCollection.ValueType
        get() = valueType!!.value!!

    override fun toString(): String {
        return attributeLogicalName + " " + entityLogicalName + " " + value
    }
}

@Root(strict = false)
data class EntityReference(
        @field:Element(name = "Id", required = false)
        var id: String? = null,

        @field:Element(name = "LogicalName", required = false)
        var logicalName: String? = null,

        @field:Element(name = "Name", required = false)
        var name: String? = null
)
