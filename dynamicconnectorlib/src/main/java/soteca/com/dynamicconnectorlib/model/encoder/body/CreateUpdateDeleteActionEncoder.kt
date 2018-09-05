package soteca.com.genisysandroid.framwork.model.encoder.body

import org.simpleframework.xml.*
import soteca.com.genisysandroid.framwork.model.EntityCollection

@Root(name = "Create")
@Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts/Services")
data class Create(
        @field:Element(name = "entity")
        @field:NamespaceList(Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts", prefix = "b"),
                Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "i"))
        private var entity: EntityCollection.Entity
) : BodyEncoder.ActionEncoder()

@Root(name = "Delete")
@Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts/Services")
data class Delete(
        @field:Element(name = "entityName")
        private val entityName: String,

        @field:Element(name = "id")
        private val id: String
) : BodyEncoder.ActionEncoder()

@Root(name = "Update")
@Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts/Services")
data class Update(
        @field:Element(name = "entity")
        @field:NamespaceList(Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts", prefix = "b"),
                Namespace(reference = "http://www.w3.org/2001/XMLSchema-instance", prefix = "i"))
        private var entity: EntityCollection.Entity
) : BodyEncoder.ActionEncoder()



