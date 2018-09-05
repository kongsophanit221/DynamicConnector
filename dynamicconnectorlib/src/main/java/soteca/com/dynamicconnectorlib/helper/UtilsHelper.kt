package soteca.com.genisysandroid.framwork.helper

import java.util.*

class UtilsHelper {

    companion object {

        private var shared: UtilsHelper? = null

        @Synchronized
        fun getInstance(): UtilsHelper {
            if (shared == null) {
                shared = UtilsHelper()
            }
            return shared!!
        }
    }

    val now: String
        get() {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            return calendar.time.crmFormatToString()
        }

    val tomorrow: String
        get () {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            return calendar.time.crmFormatToString()
        }

    val uuid: String
        get () {
            return UUID.randomUUID().toString()
        }
}