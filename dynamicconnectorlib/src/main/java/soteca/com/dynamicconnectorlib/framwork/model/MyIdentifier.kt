package soteca.com.genisysandroid.framwork.model

import org.json.JSONObject

/**
 * Created by SovannMeasna on 4/25/18.
 */
class MyIdentifier {
    var userId: String = ""
    var businessUnitId: String = ""
    var organizationId: String = ""

    fun toHashMap(): HashMap<String, String> {
        return hashMapOf("userId" to userId, "businessUnitId" to businessUnitId, "organizationId" to organizationId)
    }

    fun toJSONObject(values: HashMap<String, String>): JSONObject {
        return JSONObject(values)
    }

    override fun toString(): String {
        return "userId: ${userId} \n businessUnitId: ${businessUnitId} \n organizationId: ${organizationId}"
    }

}