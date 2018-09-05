package soteca.com.genisysandroid.framwork.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "value", strict = false)
class EntityMetadata {
    @field:Element(name = "MetadataId")
    internal var metadataId: String? = null

    @field:Path(value = "Attributes")
    @field:ElementList(entry = "AttributeMetadata", inline = true, required = false)
    internal var attributes: ArrayList<AttributeMetadata>? = null  // in RetrieveAllEntitiesRequest this is nil (Have to test)

    @field:Path(value = "CanCreateViews")
    @field:Element(name = "Value")
    internal var canCreateViews: String? = null

    @field:Path(value = "DisplayName/UserLocalizedLabel")
    @field:Element(name = "Label", required = false)
    internal var displayName: String? = null

    @field:Element(name = "IsActivity", required = false)
    internal var isActivity: String? = null

    @field:Element(name = "IsActivityParty", required = false)
    internal var isActivityParty: String? = null

    @field:Element(name = "IsChildEntity", required = false)
    internal var isChildEntity: String? = null

    @field:Element(name = "IsCustomEntity", required = false)
    internal var isCustomEntity: String? = null

    @field:Element(name = "IsIntersect", required = false)
    internal var isIntersect: String? = null

    @field:Element(name = "IsQuickCreateEnabled", required = false)
    internal var isQuickCreateEnabled: String? = null

    @field:Element(name = "LogicalName")
    internal var logicalName: String? = null

    @field:Element(name = "ObjectTypeCode", required = false)
    internal var objectTypeCode: String? = null

    @field:Element(name = "OwnershipType", required = false)
    private var ownershipTypeValue: String = ""
    val ownershipType: OwnershipType?
        get() = OwnershipType.from(ownershipTypeValue)

    @field:Element(name = "PrimaryIdAttribute")
    internal var primaryIdAttribute: String? = null

    @field:Element(name = "PrimaryNameAttribute", required = false)
    internal var primaryNameAttribute: String? = null

    @field:Path(value = "Privileges")
    @field:ElementList(entry = "SecurityPrivilegeMetadata", inline = true, required = false)
    internal var privileges: ArrayList<SecurityPrivilege>? = null  // in RetrieveAllEntitiesRequest this is nil (Have to test)

    @field:Element(name = "SchemaName")
    internal var schemaName: String? = null

    @field:Element(name = "PrimaryImageAttribute", required = false)
    internal var primaryImageAttribute: String? = null

    @field:Element(name = "EntityColor", required = false)
    internal var entityColor: String? = null

    @field:Path(value = "OneToManyRelationships")
    @field:ElementList(entry = "OneToManyRelationshipMetadata", inline = true, required = false)
    private var oneToManyRelationshipArray: ArrayList<OneToManyRelationshipMetadata>? = null
    internal val oneToManyRelationships: Map<String, List<OneToManyRelationshipMetadata>>?
        get() {
            if (oneToManyRelationshipArray == null) {
                return null
            }
            return oneToManyRelationshipArray!!.groupBy {
                it.referencingEntity
            }
        }

    @field:Path(value = "ManyToManyRelationships")
    @field:ElementList(entry = "ManyToManyRelationshipMetadata", inline = true, required = false)
    private var manyToManyRelationshipArray: ArrayList<ManyToManyRelationshipMetadata>? = null
    internal val manyToManyRelationships: Map<String, List<ManyToManyRelationshipMetadata>>?
        get() {
            if (manyToManyRelationshipArray == null) {
                return null
            }
            return manyToManyRelationshipArray!!.groupBy {
                if (it.entity1LogicalName == logicalName)
                    it.entity2LogicalName
                else {
                    it.entity1LogicalName
                }
            }
        }

    override fun toString(): String {
//        return "size: ${decoders!!.size}"
        return "metadataId: ${metadataId} \n " +
                "attributes: ${attributes} \n" +
                "canCreateViews: ${canCreateViews} \n" +
                "displayName: ${displayName} \n" +
                "isActivity: ${isActivity} \n" +
                "isActivityParty: ${isActivityParty} \n" +
                "isChildEntity: ${isChildEntity} \n " +
                "isCustomEntity: ${isCustomEntity} \n" +
                "isIntersect: ${isIntersect} \n" +
                "isQuickCreateEnabled: ${isQuickCreateEnabled} \n" +
                "logicalName: ${logicalName} \n" +
                "objectTypeCode: ${objectTypeCode} \n" +
                "ownershipType: ${ownershipType} \n" +
                "primaryIdAttribute: ${primaryIdAttribute} \n" +
                "primaryNameAttribute: ${primaryNameAttribute} \n" +
                "privileges: ${privileges} \n" +
                "schemaName: ${schemaName} \n" +
                "primaryImageAttribute: ${primaryImageAttribute} \n" +
                "entityColor: ${entityColor} \n" +
                "OneToManyRelationships: ${oneToManyRelationships} \n" +
                "ManyToManyRelationships: ${manyToManyRelationships}"
    }

    @Root(strict = false)
    class OneToManyRelationshipMetadata {

        @field:Element(name = "MetadataId")
        var metadataId: String = ""

        @field:Element(name = "SchemaName")
        var schemaName: String = ""

        @field:Element(name = "SecurityTypes")
        private var securityTypeValue: String = ""
        val securityType: SecurityType
            get() = SecurityType.from(securityTypeValue)

        @field:Element(name = "IsCustomRelationship")
        var isCustomRelationship: String = ""

        @field:Element(name = "ReferencingEntity")
        var referencingEntity: String = ""

        @field:Element(name = "ReferencingAttribute")
        var referencingAttribute: String = ""

        override fun toString(): String {
            return "metadataId: ${metadataId} \n" +
                    "schemaName: ${schemaName} \n" +
                    "securityType: ${securityType} \n" +
                    "isCustomRelationship: ${isCustomRelationship} \n" +
                    "referencingEntity: ${referencingEntity} \n" +
                    "referencingAttribute: ${referencingAttribute}"
        }
    }

    @Root(strict = false)
    class ManyToManyRelationshipMetadata {

        @field:Element(name = "MetadataId")
        var metadataId: String = ""

        @field:Element(name = "SchemaName")
        var schemaName: String = ""

        @field:Element(name = "SecurityTypes")
        private var securityTypeValue: String = ""
        val securityType: SecurityType
            get() = SecurityType.from(securityTypeValue)

        @field:Element(name = "IsCustomRelationship")
        var isCustomRelationship: String = ""

        @field:Element(name = "IntersectEntityName")
        var intersectEntityName: String = ""

        @field:Element(name = "Entity1LogicalName")
        var entity1LogicalName: String = ""

        @field:Element(name = "Entity1IntersectAttribute")
        var entity1IntersectAttribute: String = ""

        @field:Element(name = "Entity2LogicalName")
        var entity2LogicalName: String = ""

        @field:Element(name = "Entity2IntersectAttribute")
        var entity2IntersectAttribute: String = ""

        override fun toString(): String {
            return "metadataId: ${metadataId} \n" +
                    "schemaName: ${schemaName} \n" +
                    "securityType: ${securityType} \n" +
                    "isCustomRelationship: ${isCustomRelationship} \n" +
                    "intersectEntityName: ${intersectEntityName} \n" +
                    "entity1LogicalName: ${entity1LogicalName} \n" +
                    "entity1IntersectAttribute: ${entity1IntersectAttribute} \n" +
                    "entity2LogicalName: ${entity2LogicalName} \n" +
                    "entity2IntersectAttribute: ${entity2IntersectAttribute}"
        }
    }

    enum class OwnershipType(val value: String) {
        businessOwned("BusinessOwned"),
        businessParented("BusinessParented"),
        organizationOwned("OrganizationOwned"),
        teamOwned("TeamOwned"),
        userOwned("UserOwned"),
        none("None");

        companion object {
            fun from(value: String): OwnershipType? {
                OwnershipType.values().forEach {
                    if (it.value == value)
                        return it
                }
                return null
            }
        }
    }

    enum class SecurityType(val value: String) {
        none("None"),
        append("Append"),
        inheritance("Inheritance"),
        pointer("Pointer"),
        parentChild("ParentChild");

        companion object {
            fun from(value: String): SecurityType = SecurityType.values().first { it.value == value }
        }
    }
}