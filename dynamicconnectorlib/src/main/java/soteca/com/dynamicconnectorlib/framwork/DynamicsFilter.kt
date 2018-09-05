package soteca.com.genisysandroid.framwork

/**
 * Created by SovannMeasna on 3/26/18.
 */

class DynamicsFilter() {

    enum class BinaryLogicalOperator() {
        and, or
    }

    enum class CRMFilterOperator(val operator: String) {
        equal("eq"), like("like"), onOrBefore("on-or-before"), onOrAfter("on-or-after")
    }

    private var binaryLogicalOperator: BinaryLogicalOperator? = null
    private var childFilters: ArrayList<DynamicsFilter>? = null

    var attribute: String? = ""
    var value: String? = ""
    var filterOperator: CRMFilterOperator? = null

    constructor(binaryLogicalOperator: BinaryLogicalOperator, childFilters: ArrayList<DynamicsFilter>) : this() {
        this.binaryLogicalOperator = binaryLogicalOperator
        this.childFilters = childFilters
    }

    constructor(attribute: String, value: String, filterOperator: CRMFilterOperator) : this() {
        this.attribute = attribute
        this.value = value
        this.filterOperator = filterOperator
    }

    var filterString: String = {

        if (childFilters!!.size == 1) {
            childFilters!![0].filterString
        } else if (attribute!! != null) {
            val operator = filterOperator!!.operator
            "&lt;condition attribute=$attribute operator= $operator value=$value/&gt;"
        } else {

            /** Swift
            //            var filterString = String()
            //            filterString.append("&lt;filter type=\"\(binaryLogicalOperator!.rawValue)\"&gt;")
            //            filterString.append(childFilters!.reduce("") { "\($0)\($1.filterString)" })
            //
            //            return filterString+"&lt;/filter&gt;"
             **/

//            var filterStringBuilder = StringBuilder()
//            val binaryLogic = binaryLogicalOperator!!
//            filterStringBuilder.append("&lt;filter type=$binaryLogic&gt;")

            ""
        }

    }.toString()
}