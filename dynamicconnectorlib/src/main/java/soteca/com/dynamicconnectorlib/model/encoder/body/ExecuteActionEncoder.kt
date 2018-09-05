package soteca.com.genisysandroid.framwork.model.encoder.body

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.convert.Convert
import org.simpleframework.xml.convert.Converter
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.stream.Format
import org.simpleframework.xml.stream.InputNode
import org.simpleframework.xml.stream.OutputNode

@Root(name = "Execute")
@Convert(value = ExecuteActionEncoderConverter::class)
class ExecuteActionEncoder(val request: RequestEncoder) : BodyEncoder.ActionEncoder() {

    @field:Element(name = "request")
    private var r = request
}

class ExecuteActionEncoderConverter : Converter<ExecuteActionEncoder> {

    override fun write(node: OutputNode?, value: ExecuteActionEncoder?) {

        val multipleTypeName = RequestEncoder.RequestTypeName.RETRIEVE_MULTIPLE.value

        node!!.namespaces.setReference("http://schemas.microsoft.com/xrm/2011/Contracts/Services")

        if (value!!.request.nameRequest != multipleTypeName.second) {
            node!!.namespaces.setReference("http://www.w3.org/2001/XMLSchema-instance", "i")
        }

        val format = Format(0)
        val serializer = Persister(AnnotationStrategy(), format)
        serializer.write(value!!.request, node!!)
    }

    override fun read(node: InputNode?): ExecuteActionEncoder {
        throw UnsupportedOperationException("Not supported yet.")
    }
}
