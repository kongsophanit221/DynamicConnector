package soteca.com.genisysandroid.framwork.model

import org.json.JSONObject
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

/**
 * Created by SovannMeasna on 4/25/18.
 */
@Root(strict = false)
data class RolePrivilege(
        @field:Element(name = "BusinessUnitId")
        var businessUnitId: String? = null,

        @field:Element(name = "Depth")
        var depth: String? = null,

        @field:Element(name = "PrivilegeId")
        var privilegeId: String? = null) {


    fun toJSONObject(): JSONObject {
        var values = HashMap<String, String>()
        values.put("BusinessUnitId", businessUnitId!!)
        values.put("Depth", depth!!)
        values.put("PrivilegeId", privilegeId!!)
        return JSONObject(values)
    }

    override fun toString(): String {
        return "businessUnitId: ${businessUnitId}, depth: ${depth}, privilegeId:${privilegeId}"
    }
}
