package soteca.com.genisysandroid.framwork.helper

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by SovannMeasna on 4/11/18.
 */
class ExtensionHelper {
}

/**
 * SimpleDateFormat Extension
 */

fun Date.crmFormatToString(): String {
    var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")

    return simpleDateFormat.format(this)
}

/**
 * String
 **/

fun String.crmFormatToDate(): Date {
    var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    if (this.length > 20)
        simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    simpleDateFormat.timeZone = TimeZone.getTimeZone("GMT")

    return simpleDateFormat.parse(this)
}

fun String.toBool(): Boolean {
    if (this == "1" || this.toLowerCase() == "true") {
        return true
    }
    return false
}

fun String.decodeSpecialCharacter(): String {
    return this.replace("&amp;", "&")
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&apos;", "'")
}

