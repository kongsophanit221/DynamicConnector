package soteca.com.genisysandroid.framwork.networking

/**
 * Created by SovannMeasna on 4/6/18.
 */
interface Response {
    val isStatusOK: Boolean
        get() = false
    val data: String
        get() = ""
    val error: Errors?
        get() = null
    val request: Request?
        get() = null
}
