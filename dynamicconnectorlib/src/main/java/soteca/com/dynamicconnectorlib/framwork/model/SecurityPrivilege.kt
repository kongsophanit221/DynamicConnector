package soteca.com.genisysandroid.framwork.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Created by SovannMeasna on 4/24/18.
 */
@Root
class SecurityPrivilege {
    @field:Element(name = "Name")
    var name: String = ""

    @field:Element(name = "CanBeBasic")
    var canBeBasic: Boolean = false

    @field:Element(name = "CanBeDeep")
    var canBeDeep: Boolean = false

    @field:Element(name = "CanBeLocal")
    var canBeLocal: Boolean = false

    @field:Element(name = "CanBeGlobal")
    var canBeGlobal: Boolean = false

    @field:Element(name = "CanBeEntityReference")
    var canBeEntityReference: Boolean = false

    @field:Element(name = "CanBeParentEntityReference")
    var canBeParentEntityReference: Boolean = false

    @field:Element(name = "PrivilegeId")
    var id: String = ""

    @field:Element(name = "PrivilegeType")
    private var privilegeTypeValue: String = ""
    val type: Type
        get() = Type.from(privilegeTypeValue)

    enum class Type(val value: String) {
        create("Create"),
        write("Write"),
        append("Append"),
        read("Read"),
        delete("Delete"),
        appendTo("AppendTo"),
        assign("Assign"),
        share("Share"),
        none("None");

        companion object {
            fun from(value: String): Type = Type.values().first { it.value == value }
        }
    }

    override fun toString(): String {
        return "name: ${name}, canBeBasic: ${canBeBasic}, CanBeDeep: ${canBeDeep}, " +
                "CanBeLocal: ${canBeLocal}, CanBeGlobal: ${canBeGlobal}, " +
                "CanBeEntityReference: ${canBeEntityReference}, canBeParentEntityReference: ${canBeParentEntityReference}, " +
                "id: ${id}, type:${type}"
    }
}