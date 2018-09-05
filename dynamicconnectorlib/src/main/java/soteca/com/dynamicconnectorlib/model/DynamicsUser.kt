package soteca.com.genisysandroid.framwork.model

import android.content.Context
import soteca.com.genisysandroid.framwork.helper.SharedPreferenceHelper

/****
 *
 * Created by sophanit on 5/25/18.
 */
class DynamicsUser(private val context: Context) {
    var userIdentify: MyIdentifier? = null
    var userRolePrivileges: ArrayList<RolePrivilege>? = null

    fun getUserInformation(): Pair<MyIdentifier?, ArrayList<RolePrivilege>?> {
        return Pair(userIdentify, userRolePrivileges)
    }

    fun getUserInformationOffLine(): HashMap<String, String> {
        return SharedPreferenceHelper.getInstance(context).getUserInformation() as HashMap<String, String>
    }
}