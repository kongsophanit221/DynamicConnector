package soteca.com.genisysandroid.framwork.model.decoder

import org.simpleframework.xml.*
import org.simpleframework.xml.convert.Convert
import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode
import soteca.com.genisysandroid.framwork.networking.Request

@Root(name = "Envelope", strict = false)
@NamespaceList(
        Namespace(reference = "http://schemas.microsoft.com/xrm/2011/Contracts", prefix = "b"),
        Namespace(reference = "http://schemas.microsoft.com/xrm/2012/Contracts", prefix = "d"),
        Namespace(reference = "http://schemas.datacontract.org/2004/07/System.Collections.Generic", prefix = "c")
)
class ExecuteMultipleDecoder(
        private var req: Request? = null,

        @field:Path(value = "Body/ExecuteResponse/ExecuteResult/Results")
        @field:ElementList(entry = "KeyValuePairOfstringanyType", inline = true)
        private var keyValues: ArrayList<KeyValuePairOfstringanyType>? = null) : Decoder(req) {

    val responseItems: ArrayList<ExecuteMultipleResponseItem>?
        get() {
            keyValues?.forEach {
                if (it.key == "Responses") {
                    return it.value?.responseItems
                }
            }
            return null
        }

    @Root(name = "KeyValuePairOfstringanyType", strict = false)
    class KeyValuePairOfstringanyType(
            @field:Element(name = "key")
            var key: String = "",

            @field:Element(name = "value", required = false)
            var value: Value? = null
    )

    @Root(name = "value", strict = false)
    class Value(
            @field:ElementList(entry = "ExecuteMultipleResponseItem", inline = true, required = false)
            var responseItems: ArrayList<ExecuteMultipleResponseItem>? = null)
}

@Root(name = "ExecuteMultipleResponseItem", strict = false)
@Namespace(reference = "http://schemas.microsoft.com/xrm/2012/Contracts", prefix = "d")
@Convert(ExecuteMultipleConverter::class)
class ExecuteMultipleResponseItem(
        var requestIndex: Int = 0,
        var isSuccess: Boolean = false,
        var id: String = "",
        var responseName: String = ""
)


private class ExecuteMultipleConverter : Converter<ExecuteMultipleResponseItem> {

    private val TAG = "tExecuteMulti"

    override fun write(node: OutputNode?, value: ExecuteMultipleResponseItem?) {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun read(node: InputNode?): ExecuteMultipleResponseItem {
        val item = ExecuteMultipleResponseItem()
        item.isSuccess = false

        val faultElement = node!!.next
        val faultAttributes = faultElement.attributes

        if (faultAttributes.count() > 0) {
            item.isSuccess = true
        }
        faultElement.skip()

        val requestIndexElement = node!!.next
        item.requestIndex = requestIndexElement.value.toInt()
        requestIndexElement.skip()

        val responseElement = node!!.next
        val isReultNil = responseElement.getAttribute("nil")

        if (isReultNil == null) {
            val responseNameElement = responseElement.next
            item.responseName = responseNameElement.value
            responseNameElement.skip()
            val resultElement = responseElement.next
            val keyValues = resultElement.next

            if (keyValues != null) {
                val key = keyValues.next
                key.skip()
                val value = keyValues.next
                item.id = value.value
            }
        }

        return item
    }
}