package soteca.com.genisysandroid.framwork

/****
 *
 * Created by sophanit on 3/26/18.
 */
class SoapExecuteTag() : SoapContent {

    enum class SoapExecuteTagType {
        timeStamp, retrieveUserPrivileges, whoAmI,
        retrieveAllEntities, retrieveMultiple, retrieveEntity, assign, setState, stateRequest, associate, disassociate,
        createRecord, updateRecord, deleteRecord
    }

    private var type: SoapExecuteTagType? = null
    private var id: String = ""
    private var fetchExpression: String = ""
    private var entityType = ""
    private var attributes = ""
    private var order = ""
    private var filters = ""
    private var paging = ""
    private var primaryKey = ""
    private var metadataId = ""
    private var entityId = ""
    private var assingnee = ""
    private var assigneeId = ""
    private var statusCode = ""
    private var stateCode = ""
    private var request = ""
    private var keyValuePair = ""
    private var entitytype = ""
    private var targetId = ""
    private var targetType = ""
    private var relationshipName = ""
    private var relatedId = ""
    private var relatedType = ""

    // timeStamp, myIdentity, allEntities
    constructor(type: SoapExecuteTagType) : this() {
        this.type = type
    }

//    private var cc: String = ""
//
//    constructor(c: String) : this() {
//        this.cc = c
//    }

    // userPrivilege
    //retrieveMultiple
    // retrieveEntity
    constructor(idOrFetchExpression: String, type: SoapExecuteTagType) : this() {
        if (type == SoapExecuteTagType.retrieveUserPrivileges) {
            this.id = idOrFetchExpression
        } else if (type == SoapExecuteTagType.retrieveMultiple) {
            this.fetchExpression = idOrFetchExpression
        } else if (type == SoapExecuteTagType.retrieveEntity) {
            this.metadataId = idOrFetchExpression
        }
        this.type = type
    }

    // createRecord, deleteRecord
    constructor(entityType: String, metadataIdOrKeyValuePairOrEntityId: String, type: SoapExecuteTagType) : this() {

        if (type == SoapExecuteTagType.createRecord) {
            this.entityType = entityType
            this.keyValuePair = metadataIdOrKeyValuePairOrEntityId
        } else if (type == SoapExecuteTagType.deleteRecord) {
            this.entityType = entityType
            this.entityId = metadataIdOrKeyValuePairOrEntityId
        }
        this.type = type
    }

    // updateRecord
    constructor(entityType: String, primaryKeyOrEntityId: String, filtersOrKeyValuePair: String, type: SoapExecuteTagType) : this() {
        this.entityType = entityType
        this.entityId = primaryKeyOrEntityId
        this.keyValuePair = filtersOrKeyValuePair
        this.type = type
    }

    // assign, setState, stateRequest
    constructor(entityType: String, entityId: String, assigneeIdOrStatusCode: String, assingneeOrStateCodeOrRequest: String, type: SoapExecuteTagType) : this() {

        if (type == SoapExecuteTagType.assign) {
            this.entityType = entityType
            this.entityId = entityId
            this.assigneeId = assigneeIdOrStatusCode
            this.assingnee = assingneeOrStateCodeOrRequest
        } else if (type == SoapExecuteTagType.setState) {
            this.entityType = entityType
            this.entityId = entityId
            this.statusCode = assigneeIdOrStatusCode
            this.stateCode = assingneeOrStateCodeOrRequest
        } else if (type == SoapExecuteTagType.stateRequest) {
            this.entitytype = entityType
            this.entityId = entityId
            this.statusCode = assigneeIdOrStatusCode
            this.request = assingneeOrStateCodeOrRequest
        }
        this.type = type
    }

    // entityFetch
    // associate
    // disassociate
    constructor(entityTypeOrTargetId: String, attributesOrTargetType: String, orderOrRelationshipName: String, filtersOrRelatedId: String, pagingOrRelatedType: String, type: SoapExecuteTagType) : this() {

        if (type == SoapExecuteTagType.retrieveMultiple) {
            this.entityType = entityTypeOrTargetId
            this.attributes = attributesOrTargetType
            this.order = orderOrRelationshipName
            this.filters = filtersOrRelatedId
            this.paging = pagingOrRelatedType
        } else {
            this.targetId = entityTypeOrTargetId
            this.targetType = attributesOrTargetType
            this.relationshipName = orderOrRelationshipName
            this.relatedId = filtersOrRelatedId
            this.relatedType = pagingOrRelatedType
        }
        this.type = type
    }

    override val content: String
        get() {
//            if (cc != "") {
//                return cc
//            }
            when (type) {
                SoapExecuteTagType.timeStamp -> return "<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\" " +
                        "xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><request i:type=\"a:RetrieveTimestampRequest\" " +
                        "xmlns:a=\"http://schemas.microsoft.com/xrm/2011/Contracts\" " +
                        "xmlns:b=\"http://schemas.microsoft.com/crm/2011/Contracts\"><a:Parameters " +
                        "xmlns:c=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\" /><a:RequestId i:nil=\"true\"/>" +
                        "<a:RequestName>RetrieveTimestamp</a:RequestName></request></Execute>"
                SoapExecuteTagType.retrieveUserPrivileges -> return "<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\" xmlns:i=\"http://" +
                        "www.w3.org/2001/XMLSchema-instance\"><request i:type=\"b:RetrieveUserPrivilegesRequest\" xmlns:a=\"http:" +
                        "//schemas.microsoft.com/xrm/2011/Contracts\" xmlns:b=\"http://schemas.microsoft.com/crm/2011/Contracts\">" +
                        "<a:Parameters xmlns:c=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\">" +
                        "<a:KeyValuePairOfstringanyType><c:key>UserId</c:key><c:value i:type=\"d:guid\" xmlns:d=\"http:" +
                        "//schemas.microsoft.com/2003/10/Serialization/\">$id</c:value></a:KeyValuePairOfstringanyType>" +
                        "</a:Parameters><a:RequestId i:nil=\"true\"/><a:RequestName>RetrieveUserPrivileges</a:RequestName>" +
                        "</request></Execute>"
                SoapExecuteTagType.whoAmI -> return "<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\" xmlns:i=\"http:" +
                        "//www.w3.org/2001/XMLSchema-instance\"><request i:type=\"b:WhoAmIRequest\" xmlns:a=\"http://" +
                        "schemas.microsoft.com/xrm/2011/Contracts\" xmlns:b=\"http://schemas.microsoft.com/crm/2011/Contracts\">" +
                        "<a:Parameters xmlns:c=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\"/>" +
                        "<a:RequestId i:nil=\"true\"/><a:RequestName>WhoAmI</a:RequestName></request></Execute>"
                SoapExecuteTagType.retrieveAllEntities -> return "<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\" xmlns:i=\"http://" +
                        "www.w3.org/2001/XMLSchema-instance\"><request i:type=\"a:RetrieveAllEntitiesRequest\" xmlns:a=\"http://" +
                        "schemas.microsoft.com/xrm/2011/Contracts\"><a:Parameters xmlns:b=\"http://schemas.datacontract.org/2004/07/" +
                        "System.Collections.Generic\"><a:KeyValuePairOfstringanyType><b:key>EntityFilters</b:key><b:" +
                        "value i:type=\"c:EntityFilters\" xmlns:c=\"http://schemas.microsoft.com/xrm/2011/Metadata\">Entity</b:value>" +
                        "</a:KeyValuePairOfstringanyType><a:KeyValuePairOfstringanyType><b:key>RetrieveAsIfPublished</b:key><b:" +
                        "value i:type=\"c:boolean\" xmlns:c=\"http://www.w3.org/2001/XMLSchema\">false</b:value>" +
                        "</a:KeyValuePairOfstringanyType></a:Parameters><a:RequestId i:nil=\"true\" /><a:RequestName>" +
                        "RetrieveAllEntities</a:RequestName></request></Execute>"
                SoapExecuteTagType.retrieveMultiple -> return "<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\">" +
                        "<request i:type=\"b:RetrieveMultipleRequest\" xmlns:b=\"http://schemas.microsoft.com/xrm/2011/Contracts\" " +
                        "xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                        "<b:Parameters xmlns:c=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\">" +
                        "<b:KeyValuePairOfstringanyType><c:key>Query</c:key><c:value i:type=\"b:FetchExpression\">" +
                        "<b:Query>$fetchExpression</b:Query></c:value></b:KeyValuePairOfstringanyType>" +
                        "</b:Parameters><b:RequestId i:nil=\"true\"/><b:RequestName>RetrieveMultiple</b:RequestName></request></Execute>"

            /*"<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\">" +
                        "<request i:type=\"b:RetrieveMultipleRequest\" xmlns:b=\"http://schemas.microsoft.com/xrm/2011/" +
                        "Contracts\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\"><b:Parameters xmlns:c=\"http://" +
                        "schemas.datacontract.org/2004/07/System.Collections.Generic\"><b:KeyValuePairOfstringanyType>" +
                        "<c:key>Query</c:key><c:value i:type=\"b:FetchExpression\">" +
                        "<b:Query>&lt;fetch mapping=\"logical\" $paging " +
                        "version=\"1.0\"&gt;&#xD;&lt;entity name=\"$entityType\"&gt;&#xD;$attributes$order&lt;filter " +
                        "type=\"and\"&gt;$filters&lt;/filter&gt;&lt;/entity&gt;&#xD;&lt;/fetch&gt;</b:Query></c:value>" +
                        "</b:KeyValuePairOfstringanyType></b:Parameters><b:RequestId i:nil=\"true\"/>" +
                        "<b:RequestName>RetrieveMultiple</b:RequestName></request></Execute>"*/

                SoapExecuteTagType.retrieveEntity -> return "<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\" xmlns:i=\"http://" +
                        "www.w3.org/2001/XMLSchema-instance\"><request i:type=\"a:RetrieveEntityRequest\" xmlns:a=\"http://schemas.microsoft.com/xrm/2011/Contracts\">" +
                        "<a:Parameters xmlns:b=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\"><a:KeyValuePairOfstringanyType><b:key>MetadataId</b:key>" +
                        "<b:value i:type=\"ser:guid\" xmlns:ser=\"http://schemas.microsoft.com/2003/10/Serialization/\">$metadataId</b:value></a:KeyValuePairOfstringanyType>" +
                        "<a:KeyValuePairOfstringanyType><b:key>EntityFilters</b:key><b:value i:type=\"c:EntityFilters\" xmlns:c=\"http://" +
                        "schemas.microsoft.com/xrm/2011/Metadata\">Entity Attributes Privileges Relationships</b:value></a:KeyValuePairOfstringanyType><a:KeyValuePairOfstringanyType>" +
                        "<b:key>RetrieveAsIfPublished</b:key><b:value i:type=\"c:boolean\" xmlns:c=\"http://www.w3.org/2001/XMLSchema\">false</b:value>" +
                        "</a:KeyValuePairOfstringanyType></a:Parameters><a:RequestId i:nil=\"true\" /><a:RequestName>RetrieveEntity</a:RequestName></request></Execute>"
                SoapExecuteTagType.assign -> return "<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\" xmlns:i=\"http://" +
                        "www.w3.org/2001/XMLSchema-instance\"><request i:type=\"b:AssignRequest\" xmlns:a=\"http://" +
                        "schemas.microsoft.com/xrm/2011/Contracts\" xmlns:b=\"http://schemas.microsoft.com/crm/2011/Contracts\">" +
                        "<a:Parameters xmlns:c=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\"><a:KeyValuePairOfstringanyType>" +
                        "<c:key>Target</c:key><c:value i:type=\"a:EntityReference\"><a:Id>$id</a:Id><a:LogicalName>$entityType</a:LogicalName>" +
                        "<a:Name i:nil=\"true\" /></c:value></a:KeyValuePairOfstringanyType><a:KeyValuePairOfstringanyType><c:key>Assignee</c:key>" +
                        "<c:value i:type=\"a:EntityReference\"><a:Id>$assigneeId</a:Id><a:LogicalName>$assingnee</a:LogicalName>" +
                        "<a:Name i:nil=\"true\" /></c:value></a:KeyValuePairOfstringanyType></a:Parameters><a:RequestId i:nil=\"true\" />" +
                        "<a:RequestName>Assign</a:RequestName></request></Execute>"
                SoapExecuteTagType.setState -> return "<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\" xmlns:i=\"http://" +
                        "www.w3.org/2001/XMLSchema-instance\"><request i:type=\"b:SetStateRequest\" xmlns:a=\"http://" +
                        "schemas.microsoft.com/xrm/2011/Contracts\" xmlns:b=\"http://schemas.microsoft.com/crm/2011/Contracts\">" +
                        "<a:Parameters xmlns:c=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\"><a:KeyValuePairOfstringanyType>" +
                        "<c:key>EntityMoniker</c:key><c:value i:type=\"a:EntityReference\"><a:Id>$id)</a:Id><a:LogicalName>$entityType</a:LogicalName>" +
                        "<a:Name i:nil=\"true\" /></c:value></a:KeyValuePairOfstringanyType><a:KeyValuePairOfstringanyType><c:key>State</c:key>" +
                        "<c:value i:type=\"a:OptionSetValue\"><a:Value>$stateCode</a:Value></c:value></a:KeyValuePairOfstringanyType>" +
                        "<a:KeyValuePairOfstringanyType><c:key>Status</c:key><c:value i:type=\"a:OptionSetValue\"><a:Value>$statusCode</a:Value>" +
                        "</c:value></a:KeyValuePairOfstringanyType></a:Parameters><a:RequestId i:nil=\"true\" /><a:RequestName>SetState</a:RequestName>" +
                        "</request></Execute>"
                SoapExecuteTagType.stateRequest -> return "<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\" xmlns:i=\"http://" +
                        "www.w3.org/2001/XMLSchema-instance\"><request i:type=\"b:\\(request)Request\" xmlns:a=\"http://" +
                        "schemas.microsoft.com/xrm/2011/Contracts\" xmlns:b=\"http://schemas.microsoft.com/crm/2011/Contracts\">" +
                        "<a:Parameters xmlns:c=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\">" +
                        "<a:KeyValuePairOfstringanyType><c:key>OrderClose</c:key><c:value i:type=\"a:Entity\"><a:Attributes>" +
                        "<a:KeyValuePairOfstringanyType><c:key>salesorderid</c:key><c:value i:type=\"a:EntityReference\"><a:Id>$id</a:Id>" +
                        "<a:LogicalName>$entityType</a:LogicalName><a:Name i:nil=\"true\" /></c:value></a:KeyValuePairOfstringanyType></a:Attributes>" +
                        "<a:EntityState i:nil=\"true\" /><a:FormattedValues /><a:Id>00000000-0000-0000-0000-000000000000</a:Id>" +
                        "<a:LogicalName>orderclose</a:LogicalName><a:RelatedEntities /></c:value></a:KeyValuePairOfstringanyType>" +
                        "<a:KeyValuePairOfstringanyType><c:key>Status</c:key><c:value i:type=\"a:OptionSetValue\">" +
                        "<a:Value>$statusCode</a:Value></c:value></a:KeyValuePairOfstringanyType></a:Parameters><a:RequestId i:nil=\"true\" />" +
                        "<a:RequestName>$request</a:RequestName></request></Execute>"
                SoapExecuteTagType.associate -> return "<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\" xmlns:i=\"http://" +
                        "www.w3.org/2001/XMLSchema-instance\"><request i:type=\"a:AssociateRequest\" xmlns:a=\"http://schemas.microsoft.com/xrm/2011/Contracts\">" +
                        "<a:Parameters xmlns:b=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\"><a:KeyValuePairOfstringanyType><b:key>Target</b:key>" +
                        "<b:value i:type=\"a:EntityReference\"><a:Id>$targetId</a:Id><a:LogicalName>$targetType</a:LogicalName><a:Name i:nil=\"true\" /></b:value></a:KeyValuePairOfstringanyType>" +
                        "<a:KeyValuePairOfstringanyType><b:key>Relationship</b:key><b:value i:type=\"a:Relationship\"><a:PrimaryEntityRole i:nil=\"true\" />" +
                        "<a:SchemaName>$relationshipName</a:SchemaName></b:value></a:KeyValuePairOfstringanyType><a:KeyValuePairOfstringanyType><b:key>RelatedEntities</b:key>" +
                        "<b:value i:type=\"a:EntityReferenceCollection\"><a:EntityReference><a:Id>$relatedId</a:Id><a:LogicalName>$relatedType</a:LogicalName><a:Name i:nil=\"true\" />" +
                        "</a:EntityReference></b:value></a:KeyValuePairOfstringanyType></a:Parameters><a:RequestId i:nil=\"true\" /><a:RequestName>Associate</a:RequestName></request></Execute>"
                SoapExecuteTagType.disassociate -> return "<Execute xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\" xmlns:i=\"http://" +
                        "www.w3.org/2001/XMLSchema-instance\"><request i:type=\"a:AssociateRequest\" xmlns:a=\"http://schemas.microsoft.com/xrm/2011/Contracts\">" +
                        "<a:Parameters xmlns:b=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\"><a:KeyValuePairOfstringanyType><b:key>Target</b:key>" +
                        "<b:value i:type=\"a:EntityReference\"><a:Id>$targetId</a:Id><a:LogicalName>$targetType</a:LogicalName><a:Name i:nil=\"true\" /></b:value>" +
                        "</a:KeyValuePairOfstringanyType><a:KeyValuePairOfstringanyType><b:key>Relationship</b:key><b:value i:type=\"a:Relationship\">" +
                        "<a:PrimaryEntityRole i:nil=\"true\" /><a:SchemaName>$relationshipName</a:SchemaName></b:value></a:KeyValuePairOfstringanyType>" +
                        "<a:KeyValuePairOfstringanyType><b:key>RelatedEntities</b:key><b:value i:type=\"a:EntityReferenceCollection\"><a:EntityReference><a:Id>$relatedId</a:Id>" +
                        "<a:LogicalName>$relatedType</a:LogicalName><a:Name i:nil=\"true\" /></a:EntityReference></b:value></a:KeyValuePairOfstringanyType></a:Parameters>" +
                        "<a:RequestId i:nil=\"true\" /><a:RequestName>Associate</a:RequestName></request></Execute>"
                SoapExecuteTagType.createRecord -> return "<Create xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\"><entity xmlns:b=\"http://" +
                        "schemas.microsoft.com/xrm/2011/Contracts\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                        "<b:Attributes xmlns:c=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\">$keyValuePair</b:Attributes>" +
                        "<b:EntityState i:nil=\"true\"/><b:FormattedValues xmlns:c=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\"/>" +
                        "<b:Id>00000000-0000-0000-0000-000000000000</b:Id><b:LogicalName>$entityType</b:LogicalName><b:RelatedEntities xmlns:c=\"http://" +
                        "schemas.datacontract.org/2004/07/System.Collections.Generic\"/></entity></Create>"
                SoapExecuteTagType.updateRecord -> return "<Update xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\"><entity xmlns:b=\"http://" +
                        "schemas.microsoft.com/xrm/2011/Contracts\" xmlns:i=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                        "<b:Attributes xmlns:c=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\">$keyValuePair</b:Attributes>" +
                        "<b:EntityState i:nil=\"true\"/><b:FormattedValues xmlns:c=\"http://schemas.datacontract.org/2004/07/System.Collections.Generic\"/>" +
                        "<b:Id>$id</b:Id><b:LogicalName>$entityType</b:LogicalName><b:RelatedEntities xmlns:c=\"http://" +
                        "schemas.datacontract.org/2004/07/System.Collections.Generic\"/></entity></Update>"
                SoapExecuteTagType.deleteRecord -> return "<Delete xmlns=\"http://schemas.microsoft.com/xrm/2011/Contracts/Services\">" +
                        "<entityName>$entityType</entityName><id>$id</id></Delete>"
            }
            return ""
        }


}

