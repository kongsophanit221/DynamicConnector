package soteca.com.genisysandroid.framwork.model

import org.simpleframework.xml.*

/**
 * Created by SovannMeasna on 4/24/18.
 */
@Root(name = "AttributeMetadata", strict = false)
@NamespaceList(
        Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts", prefix = "b"),
        Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "i"),
        Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Metadata", prefix = "d")
)
class AttributeMetadata {

    @field:Element(name = "MetadataId")
    var metadataId: String? = null

    @field:Path(value = "DisplayName/UserLocalizedLabel")
    @field:Element(name = "Label", required = false)
    var displayName: String? = null

    @field:Element(name = "IsCustomAttribute")
    var isCustomAttribute: String? = null

    @field:Element(name = "IsValidForCreate")
    var isValidForCreate: String? = null

    @field:Element(name = "IsValidForRead")
    var isValidForRead: String? = null

    @field:Element(name = "IsValidForUpdate")
    var isValidForUpdate: String? = null

    @field:Element(name = "LogicalName")
    var logicalName: String? = null

    @field:Element(name = "SchemaName")
    var schemaName: String? = null

    @field:Element(name = "DefaultValue", required = false)
    var defaultValue: String? = null

    @field:Path(value = "RequiredLevel")
    @field:Element(name = "Value", required = false)
    private var requiredLevelValue: String = ""

    val requiredLevel: RequiredLevel
        get() = RequiredLevel.from(requiredLevelValue)

    @field:Element(name = "FormulaDefinition", required = false)
    private var formulaDefinition: String? = null

    @field:Element(name = "SourceType", required = false)
    private var sourceTypeValue: String? = null

    val sourceType: SourceType?
        get() {
            when (sourceTypeValue) {
                "0" -> return SourceType.simple()
                "1" -> return SourceType.calculated(formulaDefinition)
                "2" -> return SourceType.rollup()
            }
            return null
        }

    @field:Element(name = "AttributeType")
    private var attributeTypeString: String? = null

    // -- String --
    @field:Path(value = "FormatName")
    @field:Element(name = "Value", required = false)
    private var formatValue: String? = null

    @field:Element(name = "MaxLength", required = false)
    private var maxLength: String? = null

    // -- Boolean, PickList, StateCode, StatusCode --
    @field:Element(name = "OptionSet", required = false)
    private var booleanTypeInfo: AttributeType.BooleanTypeInfo? = null

    // -- Double, Decimal, BigInt --
    @field:Element(name = "MinValue", required = false)
    private var min: String? = null

    @field:Element(name = "MaxValue", required = false)
    private var max: String? = null

    @field:Element(name = "Precision", required = false)
    private var precision: String? = null

    private val numericTypeInfo
        get() = AttributeType.NumericTypeInfo(min, max, precision)

    // -- Integer --
    @field:Element(name = "Format", required = false)
    private var format: String? = null

    // -- Look Up --
    @field:Path(value = "Targets")
    @field:ElementList(name = "string", inline = true, required = false)
    private var targets: ArrayList<String>? = null

    val attributeType: AttributeType
        get() {
            val attributeTypeValue = AttributeType.Type.from(attributeTypeString!!)

            when (attributeTypeValue) {
                AttributeType.Type.string -> {

                    val stringFormatOption = AttributeType.StringFormatOption.Type.from(formatValue!!)

                    when (stringFormatOption) {
                        AttributeType.StringFormatOption.Type.phone -> return AttributeType.string(AttributeType.StringFormatOption.phone(maxLength!!))
                        AttributeType.StringFormatOption.Type.email -> return AttributeType.string(AttributeType.StringFormatOption.email(maxLength!!))
                        AttributeType.StringFormatOption.Type.url -> return AttributeType.string(AttributeType.StringFormatOption.url(maxLength!!))
                        AttributeType.StringFormatOption.Type.text -> return AttributeType.string(AttributeType.StringFormatOption.text(maxLength!!))
                    }
                }
                AttributeType.Type.boolean -> return AttributeType.boolean(booleanTypeInfo!!)
                AttributeType.Type.double -> return AttributeType.double(numericTypeInfo)
                AttributeType.Type.decimal -> return AttributeType.decimal(numericTypeInfo)
                AttributeType.Type.integer -> {

                    val integerFormatOption = AttributeType.IntegerFormatOption.Type.from(format!!)

                    when (integerFormatOption) {
                        AttributeType.IntegerFormatOption.Type.none -> return AttributeType.integer(AttributeType.IntegerFormatOption.none(numericTypeInfo!!))
                        AttributeType.IntegerFormatOption.Type.duration -> return AttributeType.integer(AttributeType.IntegerFormatOption.duration(numericTypeInfo!!))
                        AttributeType.IntegerFormatOption.Type.timeZone -> return AttributeType.integer(AttributeType.IntegerFormatOption.timeZone(numericTypeInfo!!))
                        AttributeType.IntegerFormatOption.Type.languageLCIDCode -> return AttributeType.integer(AttributeType.IntegerFormatOption.languageLCIDCode(numericTypeInfo!!))
                    }
                }
                AttributeType.Type.bigint -> return AttributeType.bigInt(numericTypeInfo)
                AttributeType.Type.lookup -> {

                    if (targets!!.size == 1) {
                        return AttributeType.lookup(AttributeType.LookupType.simple(targets!![0]))
                    } else {
                        return AttributeType.lookup(AttributeType.LookupType.regarding(targets!!))
                    }
                }
                AttributeType.Type.customer -> return AttributeType.customer()
                AttributeType.Type.owner -> return AttributeType.owner()
                AttributeType.Type.partylist -> return AttributeType.partyList()
                AttributeType.Type.picklist -> return AttributeType.pickList(booleanTypeInfo!!)
                AttributeType.Type.datetime -> {
                    if (this.format == "DateAndTime") {
                        return AttributeType.datetime(AttributeType.DateTimeFormatOption.dateAndTime(format!!))
                    } else {
                        return AttributeType.datetime(AttributeType.DateTimeFormatOption.dateOnly(format!!))
                    }
                }
                AttributeType.Type.state -> return AttributeType.stateCode(booleanTypeInfo!!)
                AttributeType.Type.status -> return AttributeType.statusCode(booleanTypeInfo!!)
                AttributeType.Type.uniqueidentifier -> return AttributeType.uniqueIdentifier()
                AttributeType.Type.managedproperty -> return AttributeType.managedProperty()
                AttributeType.Type.money -> return AttributeType.money(numericTypeInfo)
                AttributeType.Type.memo -> return AttributeType.memo(maxLength!!)
                AttributeType.Type.entityname -> return AttributeType.entity()
                AttributeType.Type.partylist -> return AttributeType.partyList()
                AttributeType.Type.virtual -> return AttributeType.virtual()
            }
        }


    override fun toString(): String {
        return "metadataId: ${metadataId} \n " +
                "displayName: ${displayName} \n " +
                "isCustomAttribute:${isCustomAttribute} \n" +
                "isValidForCreate:${isValidForCreate} \n " +
                "isValidForRead: ${isValidForRead} \n " +
                "isValidForUpdate: ${isValidForUpdate} \n " +
                "logicalName: ${logicalName} \n " +
                "schemaName: ${schemaName} \n" +
                "defaultValue: ${defaultValue} \n" +
                "attributeType: ${attributeType} \n" +
                "requiredLevel: ${requiredLevel} \n" +
                "sourceType: ${sourceType}"
    }

    sealed class AttributeType {

        enum class Type(val value: String) {
            string("String"),
            uniqueidentifier("Uniqueidentifier"),
            memo("Memo"),
            integer("Integer"),
            picklist("Picklist"),
            state("State"),
            status("Status"),
            bigint("BigInt"),
            decimal("Decimal"),
            money("Money"),
            double("Double"),
            boolean("Boolean"),
            managedproperty("ManagedProperty"),
            datetime("DateTime"),
            lookup("Lookup"),
            owner("Owner"),
            customer("Customer"),
            entityname("EntityName"),
            partylist("PartyList"),
            virtual("Virtual");

            companion object {
                fun from(value: String): Type = Type.values().first { it.value == value }
            }
        }

        class string(val stringFormatOption: StringFormatOption) : AttributeType()
        class boolean(val booleanTypeInfo: BooleanTypeInfo) : AttributeType()
        class double(val numericTypeInfo: NumericTypeInfo) : AttributeType()
        class decimal(val numericTypeInfo: NumericTypeInfo) : AttributeType()
        class integer(val optionFormatOption: IntegerFormatOption) : AttributeType()
        class bigInt(val numericTypeInfo: NumericTypeInfo) : AttributeType()
        class lookup(val lookupType: LookupType) : AttributeType()
        class customer : AttributeType() // Not yet test, seem iOS not yet done
        class owner : AttributeType() // Not yet test, seem iOS not yet done
        class partyList : AttributeType() // Not yet test, seem iOS not yet done
        class pickList(val booleanTypeInfo: BooleanTypeInfo) : AttributeType()
        class datetime(val dateTimeFormatOption: DateTimeFormatOption) : AttributeType()
        class stateCode(val booleanTypeInfo: BooleanTypeInfo) : AttributeType()
        class statusCode(val booleanTypeInfo: BooleanTypeInfo) : AttributeType()
        class uniqueIdentifier : AttributeType() // Not yet test, seem iOS not yet done
        class managedProperty : AttributeType() // Not yet test, seem iOS not yet done
        class money(val numericTypeInfo: NumericTypeInfo) : AttributeType()
        class memo(val maxLength: String) : AttributeType()
        class entity : AttributeType() // Not yet test, seem iOS not yet done
        class virtual : AttributeType() // Not yet test, seem iOS not yet done


        override fun toString(): String {
            when (this) {
                is string -> return "string(${this.stringFormatOption})"
                is boolean -> return "boolean(${this.booleanTypeInfo})"
                is double -> return "double(${this.numericTypeInfo})"
                is decimal -> return "decimal(${this.numericTypeInfo})"
                is integer -> return "integer(${this.optionFormatOption})"
                is bigInt -> return "bigInt(${this.numericTypeInfo})"
                is lookup -> return "lookup(${this.lookupType})"
                is customer -> return "customer"
                is owner -> return "owner"
                is partyList -> return "partyList"
                is pickList -> return "pickList(options: ${this.booleanTypeInfo.optionMetadatas})"
                is datetime -> return "datetime(${this.dateTimeFormatOption})"
                is stateCode -> return "stateCode(${this.booleanTypeInfo.optionMetadatas})"
                is statusCode -> return "statusCode(${this.booleanTypeInfo.optionMetadatas})"
                is uniqueIdentifier -> return "uniqueIdentifier"
                is managedProperty -> return "managedProperty"
                is money -> return "money(${this.numericTypeInfo})"
                is memo -> return "memo(${this.maxLength})"
                is entity -> return "entity"
                is virtual -> return "virtual"

            }
            return "Not match AttributeType"
        }

        // String
        sealed class StringFormatOption {

            enum class Type(val value: String) {
                phone("Phone"),
                text("Text"),
                url("Url"),
                email("Email");

                companion object {
                    fun from(value: String): StringFormatOption.Type {
                        StringFormatOption.Type.values().forEach {
                            if (it.value == value) {
                                return it
                            }
                        }
                        return StringFormatOption.Type.text
                    }
                }
            }

            class email(val maxLength: String) : StringFormatOption()
            class text(val maxLength: String) : StringFormatOption()
            class url(val maxLength: String) : StringFormatOption()
            class phone(val maxLength: String) : StringFormatOption()

            override fun toString(): String {
                when (this) {
                    is phone -> return "phone(${this.maxLength})"
                    is email -> return "email(${this.maxLength})"
                    is url -> return "url(${this.maxLength})"
                    is text -> return "text(${this.maxLength})"
                }
                return "Not match StringFormatOption"
            }
        }

        // Boolean, Pick List
        @Root(name = "OptionSet", strict = false)
        data class BooleanTypeInfo(
                @field:Element(name = "FalseOption", required = false)
                var `false`: Option? = null,

                @field:Element(name = "TrueOption", required = false)
                var `true`: Option? = null,

                @field:Path("Options")
                @field:ElementList(entry = "OptionMetadata", inline = true, required = false)
                var optionMetadatas: ArrayList<AttributeType.Option>? = null
        ) {
            override fun toString(): String {
                return "true: ${`true`}, false: ${`false`}"
            }
        }

        // Helper Option Class
        @Root(strict = false)
        data class Option(
                @field:Path(value = "Label/LocalizedLabels/LocalizedLabel")
                @field:Element(name = "Label", required = false)
                var label: String = "",

                @field:Element(name = "Value", required = false)
                var value: String = "",

                @field:Element(name = "DefaultStatus", required = false)
                var stateCode: String = "",

                @field:Element(name = "State", required = false)
                var statusCode: String = ""
        ) {
            override fun toString(): String {
                return "label=${label}, value=${value}, stateCode=${stateCode}, statusCode=${statusCode}"
            }
        }

        // Double, Decimal
        data class NumericTypeInfo(
                var min: String? = null,

                var max: String? = null,

                var precision: String? = null
        ) {
            override fun toString(): String {
                return "min: ${min}, max: ${max}, precision: ${precision}"
            }
        }

        // Integer
        sealed class IntegerFormatOption {

            enum class Type(val value: String) {
                none("None"),
                duration("Duration"),
                timeZone("TimeZone"),
                languageLCIDCode("Language");

                companion object {
                    fun from(value: String): IntegerFormatOption.Type = IntegerFormatOption.Type.values().first { it.value == value }
                }
            }

            class none(val info: NumericTypeInfo) : IntegerFormatOption()
            class duration(val info: NumericTypeInfo) : IntegerFormatOption()
            class timeZone(val info: NumericTypeInfo) : IntegerFormatOption()
            class languageLCIDCode(val info: NumericTypeInfo) : IntegerFormatOption()

            override fun toString(): String {
                when (this) {
                    is none -> return "none(${this.info})"
                    is duration -> return "duration(${this.info})"
                    is timeZone -> return "timeZone(${this.info})"
                    is languageLCIDCode -> return "languageLCIDCode(${this.info})"
                }
                return "Not match IntegerFormatOption"
            }
        }

        // LookUp
        sealed class LookupType {

            class simple(val value: String) : LookupType()
            class regarding(val values: ArrayList<String>) : LookupType()

            override fun toString(): String {
                when (this) {
                    is simple -> return "simple(${this.value})"
                    is regarding -> return "regarding(${this.values})"
                }
                return "Not match LookupType"
            }
        }

        // DateTime
        sealed class DateTimeFormatOption {

            class dateAndTime(val value: String) : DateTimeFormatOption()
            class dateOnly(val value: String) : DateTimeFormatOption()

            override fun toString(): String {
                when (this) {
                    is dateAndTime -> return "dateAndTime(${this.value})"
                    is dateOnly -> return "dateOnly(${this.value})"
                }
                return "Not match DateTimeFormatOption"
            }
        }
    }

    // Required Level
    enum class RequiredLevel(val value: String) {
        none("None"),
        recommended("Recommended"),
        applicationRequired("ApplicationRequired"),
        systemRequired("SystemRequired");

        companion object {
            fun from(value: String): RequiredLevel = RequiredLevel.values().first { it.value == value }
        }
    }

    // Source Type
    sealed class SourceType {

        class simple() : SourceType()
        class rollup() : SourceType()
        class calculated(val formula: String?) : SourceType()

        override fun toString(): String {
            when (this) {
                is simple -> return "simple"
                is rollup -> return "rollup"
                is calculated -> return "calculated(${this.formula})"
            }
            return "Not match SourceType"
        }
    }
}