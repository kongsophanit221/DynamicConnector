package soteca.com.genisysandroid.framwork.model

import org.simpleframework.xml.*

@Root(name = "fetch")
@Order(attributes = ["top", "count", "page", "mapping", "version"])
class FetchExpression(
        @field:Element(name = "entity", required = false)
        var entity: Entity? = null,

        @field:Attribute(name = "top", required = false)
        var top: Int? = null,

        @field:Attribute(name = "count", required = false)
        var count: Int? = null,

        @field:Attribute(name = "page", required = false)
        var page: Int? = null,

        @field:Attribute(name = "paging-cookie", required = false)
        var pagingCookie: String? = null,

        @field:Attribute(name = "returntotalrecordcount", required = false)
        var returnTotalRecordCount: Boolean? = null,

        @field:Attribute(name = "aggregate", required = false)
        var aggregate: Boolean? = null,

        @field:Attribute(name = "distinct", required = false)
        var distinct: Boolean? = null
) {

    @field:Attribute(name = "version", required = false)
    private var version: Float? = 1.0f

    @field:Attribute(name = "mapping", required = false)
    private var mapping: String? = "logical"

    companion object {

        fun fetchForms(entityCode: String): FetchExpression {
            val filter = Filter.singleCondition(Condition("objecttypecode", Operator.equal, entityCode))
            val fetchExpression = FetchExpression(Entity("systemform", filter = filter))

            return fetchExpression
        }

        fun countEntity(entityType: String, filter: Filter? = null, attribute: String, alias: String, includeNull: Boolean = true): FetchExpression {

            var aggregate = AggregateType.count

            if (!includeNull) {
                aggregate = AggregateType.columnCount
            }
            val attribute = Attributee(attribute, alias, aggregate)
            val entity = Entity(entityType, arrayListOf(attribute), null, filter)

            return FetchExpression(entity, aggregate = true)
        }

        fun fetch(count: Int? = null, entityType: String, page: Int? = null, pagingCookie: String? = null, attributes: ArrayList<String>? = null,
                  filter: Filter? = null, orderAttribute: String? = null, isDescending: Boolean = false): FetchExpression {
            val _attributes = attributes?.let { ArrayList(it!!.map { Attributee(it) }) }
            val _order = orderAttribute?.let { arrayListOf(Order(it, null, isDescending)) }
            val _entity = Entity(entityType, _attributes, null, filter, _order)

            return FetchExpression(_entity, count = count, page = page, pagingCookie = pagingCookie)
        }
    }

    @Root(strict = false)
    class Entity(name: String, val attributes: ArrayList<Attributee>? = null, linkEntities: ArrayList<LinkEntity>? = null, filter: Filter? = null, orders: ArrayList<Order>? = null) {

        @field:Attribute(name = "name", required = false)
        private var n: String? = name

        @field:ElementList(entry = "attribute", inline = true, required = false)
        private var attrs: ArrayList<Attributee>? = attributes

        @field:ElementList(entry = "link-entity", inline = true, required = false)
        private var les: ArrayList<LinkEntity>? = linkEntities

        @field:Element(name = "filter", required = false)
        private var f: Filter? = filter

        @field:ElementList(entry = "order", inline = true, required = false)
        private var os: ArrayList<Order>? = orders

        private var allAttributes: String? = null //= attributes.let { null } ?: "dasda"
            @Element(name = "all-attributes", required = false)
            get() {
                if (attributes == null || attributes!!.size <= 0) {
                    return ""
                }
                return null
            }
    }

    @Root(strict = false)
    class Attributee(
            @field:Attribute(name = "name", required = false)
            var name: String? = null,

            @field:Attribute(name = "alias", required = false)
            var alias: String? = null,

            var aggregate: AggregateType? = null,

            var dateGrouping: DateGroupingType? = null,

            var groupBy: FetchBoolType? = null
    ) {
        @field:Attribute(name = "aggregate", required = false)
        private var aggregateValue: String? = aggregate?.let { it.value }

        @field:Attribute(name = "dategrouping", required = false)
        private var dateGroupingValue: String? = dateGrouping?.let { it.value }

        @field:Attribute(name = "groupby", required = false)
        private var groupByValue: String? = groupBy?.let { it.value }
    }

    @Root(strict = false)
    enum class AggregateType(val value: String) {
        count("count"), min("min"), max("max"), sum("sum"),
        columnCount("countcolumn"), average("avg");

        override fun toString(): String {
            return value
        }
    }

    @Root(strict = false)
    enum class DateGroupingType(val value: String) {
        day("day"), week("week"), month("month"), quarter("quarter"), year("year"),
        fiscalPeriod("fiscal-period"), fiscalYear("fiscal-year");

        override fun toString(): String {
            return value
        }
    }

    @Root(strict = false)
    enum class FetchBoolType(val value: String) {
        `true`("true"), `false`("false"), _0("0"), _1("1");

        override fun toString(): String {
            return value
        }
    }

    @Root(strict = false)
    class LinkEntity(name: String, from: String, to: String, alias: String? = null, linkType: LinkType? = null, visible: Boolean? = false,
                     intersect: Boolean? = false, val attributes: ArrayList<Attributee>? = null, order: Order? = null, filter: Filter? = null,
                     linkEntities: ArrayList<LinkEntity>? = null) {

        @field:Attribute(name = "name", required = false)
        private var _name: String? = name

        @field:Attribute(name = "from", required = false)
        private var _from: String? = from

        @field:Attribute(name = "to", required = false)
        private var _to: String? = to

        @field:Attribute(name = "alias", required = false)
        private var _alias: String? = alias

        @field:Attribute(name = "link-type", required = false)
        private var linkTypeValue: String? = linkType?.let { it.value }

        @field:ElementList(entry = "attribute", inline = true, required = false)
        private var _attributes: ArrayList<Attributee>? = attributes

        @field:Attribute(name = "visible", required = false)
        private var _visible: Boolean? = visible

        @field:Attribute(name = "intersect", required = false)
        var _intersect: Boolean? = intersect

        @field:Element(name = "order", required = false)
        var _order: Order? = order

        @field:Element(name = "filter", required = false)
        var _filter: Filter? = filter

        @field:ElementList(entry = "link-entity", inline = true, required = false)
        var _linkEntities: ArrayList<LinkEntity>? = linkEntities

        var allAttributes: String? = null //= attributes.let { null } ?: "dasda"
            @Element(name = "all-attributes", required = false)
            get() {
                if (attributes == null || attributes!!.size <= 0) {
                    return ""
                }
                return null
            }

        companion object {

            fun singleJoin(linkEntity: FetchExpression.LinkEntity): ArrayList<LinkEntity> {
                return arrayListOf(linkEntity)
            }

            fun multipleJoin(linkEntities: ArrayList<LinkEntity>): ArrayList<LinkEntity> {
                return linkEntities
            }
        }
    }

    @Root(strict = false)
    enum class LinkType(val value: String) {
        INNER("inner"),
        OUTER("outer"),
        JOIN("join");

        override fun toString(): String {
            return value
        }
    }

    @Root(strict = false)
    class Filter(
            @field:Attribute(name = "type", required = false)
            var type: LogicalOperator = LogicalOperator.and,

            @field:ElementList(entry = "condition", inline = true, required = false)
            var conditions: ArrayList<Condition>? = null,

            @field:ElementList(entry = "filter", inline = true, required = false)
            var filters: ArrayList<Filter>? = null,

            @field:Attribute(name = "isquickfindfields", required = false)
            var isquickfindfields: Boolean? = null
    ) {

        companion object {

            fun singleCondition(condition: Condition): Filter {
                return Filter(LogicalOperator.and, arrayListOf(condition))
            }

            fun andConditions(conditions: ArrayList<Condition>): Filter {
                return Filter(LogicalOperator.and, conditions)
            }

            fun orConditions(conditions: ArrayList<Condition>): Filter {
                return Filter(LogicalOperator.or, conditions)
            }
        }
    }

    @Root(strict = false)
    enum class LogicalOperator {
        and, or
    }

    @Root(strict = false)
    class Condition() {

        @field:Attribute(name = "attribute", required = false)
        private var attribute: String? = null

        @field:Attribute(name = "operator", required = false)
        private var operator: String? = null

        @field:ElementList(entry = "value", inline = true, required = false)
        var values: ArrayList<Value>? = null

        @field:Attribute(name = "entityname", required = false)
        private var entityName: String? = null

        @field:Attribute(name = "column", required = false)
        private var column: String? = null

        @field:Attribute(name = "alias", required = false)
        private var alias: String? = null

        @field:Attribute(name = "aggregate", required = false)
        private var aggregate: String? = null

        @field:Attribute(name = "value", required = false)
        private var valueAttr: String? = null

        @field:Attribute(name = "uiName", required = false)
        private var uiNameAttr: String? = null

        @field:Attribute(name = "uitype", required = false)
        private var uiTypeAttr: String? = null

        constructor(attribute: String? = null, `operator`: Operator? = null, value: String? = null, values: ArrayList<String>? = null) : this() {
            this.attribute = attribute
            this.operator = `operator`?.let { it.value }

            values?.let {
                this.values = ArrayList()
                this.values!!.addAll(it.map { Value(it) })
            }

            value?.let {
                this.valueAttr = value
            }
        }

        constructor(attribute: String? = null, `operator`: Operator? = null, value: Value? = null, values: ArrayList<Value>? = null,
                    entityName: String? = null, column: String? = null, alias: String? = null, aggregate: AggregateType? = null) : this() {
            this.attribute = attribute
            this.operator = `operator`?.let { it.value }
            this.entityName = entityName
            this.column = column
            this.alias = alias
            this.aggregate = aggregate?.let { it.value }
            this.values = values

            value?.let {
                this.valueAttr = value.value
                this.uiNameAttr = value.uiName
                this.uiTypeAttr = value.uiType
            }
        }

        @Root(name = "value", strict = false)
        class Value(
                @field:Text
                var value: String = "",

                @field:Attribute(name = "uiname", required = false)
                var uiName: String? = null,

                @field:Attribute(name = "uitype", required = false)
                var uiType: String? = null
        )

    }

    @Root(strict = false)
    enum class Operator(val value: String) {
        equal("eq"), notEqual("neq"), notOn("ne"), greaterThan("gt"), greaterOrEqual("ge"),
        lessThan("lt"), lessOrEqual("le"), like("like"), notLike("not-like"), `in`("in"),
        notIn("not-in"), between("between"), notBetween("not-between"), `null`("null"),
        notNull("not-null"), yesterday("yesterday"), today("today"), tomorrow("tomorrow"),
        lastWeek("last-week"), thisWeek("this-week"), nextWeek("next-week"),
        lastMonth("last-month"), thisMonth("this-month"), nextMonth("next-month"), on("on"),
        onOrBefore("on-or-before"), onOrAfter("on-or-after"), lastYear("last-year"),
        thisYear("this-year"), nextYear("next-year"), lastXHours("last-x-hours"),
        nextXHours("next-x-hours"), lastXDays("last-x-days"), nextXDays("next-x-days"),
        lastXWeeks("last-x-weeks"), nextXWeeks("next-x-weeks"), lastXMonths("last-x-months"),
        nextXMonths("next-x-months"), lastXYears("last-x-years"), nextXYears("next-x-years"),
        olderThanXMinutes("olderthan-x-minutes"), olderThanXHours("olderthan-x-hours"),
        olderThanXDays("olderthan-x-days"), olderThanXWeeks("olderthan-x-weeks"),
        olderThanXMonths("olderthan-x-months"), olderThanXYears("olderthan-x-years"),
        equalUserId("eq-userid"), notEqualUserId("ne-userid"), equalUserTeams("eq-userteams"),
        equalUserOrUserTeams("eq-useroruserteams"), equalUserOrUserHierarchy("eq-useroruserhierarchy"),
        equalUserOrUserHierarchyAndTeams("eq-useroruserhierarchyandteams"), equalBusinessId("eq-businessid"),
        notEqualBusinessId("ne-businessid"), equalUserLanguage("eq-userlanguage"),
        thisFiscalYear("this-fiscal-year"), thisFiscalPeriod("this-fiscal-period"),
        nextFiscalYear("next-fiscal-year"), nextFiscalPeriod("next-fiscal-period"),
        lastFiscalYear("last-fiscal-year"), lastFiscalPeriod("last-fiscal-period"),
        nextXFiscalYear("next-x-fiscal-years"), nextXFiscalPeriod("next-x-fiscal-periods"),
        lastXFiscalYear("last-x-fiscal-years"), lastXFiscalPeriod("last-x-fiscal-periods"),
        inFiscalYear("in-fiscal-year"), inFiscalPeriod("in-fiscal-period"),
        inFiscalPeriodAndYear("in-fiscal-period-and-year"), inOrBeforeFiscalPeriodAndYear("in-or-before-fiscal-period-and-year"), inOrAfterFiscalPeriodAndYear("in-or-after-fiscal-period-and-year"), beginsWith("begins-with"),
        notBeginWith("not-begin-with"), endsWith("ends-with"), notEndWith("not-end-with"),
        under("under"), equalOrUnder("eq-or-under"), notUnder("not-under"), above("above"),
        equalOrAbove("eq-or-above"), lastSevenDays("last-seven-days");

        val isForInternalUse: Boolean
            get() {
                return this == Operator.notOn
            }

        override fun toString(): String {
            return value
        }
    }

    @Root(strict = false)
    class Order(
            @field:Attribute(name = "attribute", required = false)
            var attribute: String = "",

            @field:Attribute(name = "alias", required = false)
            var alias: String? = null,

            @field:Attribute(name = "descending", required = false)
            var descending: Boolean = false
    )
}