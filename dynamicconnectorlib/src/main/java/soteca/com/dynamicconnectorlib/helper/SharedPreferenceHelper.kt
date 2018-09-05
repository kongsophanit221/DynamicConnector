package soteca.com.genisysandroid.framwork.helper

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

/**
 * Created by SovannMeasna on 4/11/18.
 */
class SharedPreferenceHelper(ctx: Context) {

    private val ACCESS_TOKEN_DETAIL = "ACCESS_TOKEN_DETAIL"
    private val DYNAMICS_CONFIGURATION = "DYNAMICS_CONFIGURATION"
    private val USER_INFORMATION = "USER_INFORMATION"

    companion object {

        private var prefs: SharedPreferences? = null
        private var prefsHelper: SharedPreferenceHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): SharedPreferenceHelper {
            if (prefsHelper == null) {
                prefsHelper = SharedPreferenceHelper(ctx)
                prefs = ctx.getSharedPreferences("dynamic_connector", 0)
            }
            return prefsHelper!!
        }
    }

    /**
     * Access Token Detail
     */
    fun setAccessTokenDetail(values: HashMap<String, String>) {
        val jsonObject = JSONObject(values)
        val editor = prefs!!.edit()
        editor.putString(ACCESS_TOKEN_DETAIL, jsonObject.toString())
        editor.apply()
    }

    fun getAccessTokenDetail(): HashMap<String, String>? {
        val jsonObjectString = prefs!!.getString(ACCESS_TOKEN_DETAIL, "")

        if (jsonObjectString == "") {
            return null
        }

        val jsonObject = JSONObject(jsonObjectString)
        var values = HashMap<String, String>()
        jsonObject.keys().forEach {
            values.put(it, jsonObject.getString(it))
        }
        return values
    }

    fun deleteAccessTokenDetail() {
        prefs!!.edit().remove(ACCESS_TOKEN_DETAIL).commit()
    }

    /**
     * Dynamic Configuration
     */
    fun setConfiguration(values: HashMap<String, String>) {
        val jsonObject = JSONObject(values)
        val editor = prefs!!.edit()
        editor.putString(DYNAMICS_CONFIGURATION, jsonObject.toString())
        editor.apply()
    }

    fun getConfiguration(): HashMap<String, String>? {
        val jsonObjectString = prefs!!.getString(DYNAMICS_CONFIGURATION, "")

        if (jsonObjectString == "") {
            return null
        }

        val jsonObject = JSONObject(jsonObjectString)
        var values = HashMap<String, String>()
        jsonObject.keys().forEach {
            values.put(it, jsonObject.getString(it))
        }
        return values
    }

    fun deleteConfiguration() {
        prefs!!.edit().remove(DYNAMICS_CONFIGURATION).commit()
    }

    /*
    * User Information
    * */
    fun setUserInformation(values: HashMap<String, Any>) {
        val jsonObject = JSONObject(values)
        val editor = prefs!!.edit()
        editor.putString(USER_INFORMATION, jsonObject.toString())
        editor.apply()
    }

    fun getUserInformation(): HashMap<String, Any>? {
        val jsonObjectString = prefs!!.getString(USER_INFORMATION, "")

        if (jsonObjectString == "") {
            return null
        }

        val jsonObject = JSONObject(jsonObjectString)
        val values = HashMap<String, Any>()
        jsonObject.keys().forEach {
            values[it] = jsonObject.getString(it)
        }
        return values
    }

    fun deleteUserInformation() {
        prefs!!.edit().remove(USER_INFORMATION).apply()
    }

    /*
    * User Information
    * */
    fun setValues(key: String, values: HashMap<String, String>) {
        val jsonObject = JSONObject(values)
        val editor = prefs!!.edit()
        editor.putString(key, jsonObject.toString())
        editor.apply()
    }

    fun getValues(key: String): HashMap<String, String>? {
        val jsonObjectString = prefs!!.getString(key, "")

        if (jsonObjectString == "") {
            return null
        }

        val jsonObject = JSONObject(jsonObjectString)
        val values = HashMap<String, String>()
        jsonObject.keys().forEach {
            values[it] = jsonObject.getString(it)
        }
        return values
    }

    fun deleteValue(key: String) {
        prefs!!.edit().remove(key).apply()
    }
}