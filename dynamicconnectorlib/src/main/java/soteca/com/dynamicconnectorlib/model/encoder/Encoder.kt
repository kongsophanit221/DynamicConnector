package soteca.com.genisysandroid.framwork.model.encoder

import org.simpleframework.xml.*
import org.simpleframework.xml.convert.AnnotationStrategy
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.stream.Format
import soteca.com.genisysandroid.framwork.helper.UtilsHelper
import soteca.com.genisysandroid.framwork.helper.decodeSpecialCharacter
import soteca.com.genisysandroid.framwork.model.decoder.RetrieveMultipleDecoder
import soteca.com.genisysandroid.framwork.model.encoder.body.BodyEncoder
import soteca.com.genisysandroid.framwork.model.encoder.body.RequestSecurityTokenEncoder
import soteca.com.genisysandroid.framwork.model.encoder.header.HeaderEncoder
import soteca.com.genisysandroid.framwork.model.encoder.header.SecurityEncoder
import java.io.ByteArrayOutputStream
import java.util.*

interface Encoder {
    fun toByteArray(): ByteArray?
}

@NamespaceList(
        Namespace(reference = "http://www.w3.org/2005/08/addressing", prefix = "a"),
        Namespace(reference = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", prefix = "u"),
        Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
)
@Root(name = "Envelope")
@Order(elements = arrayOf("Header", "Body"))
@Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
class EnvelopeEncoder(header: HeaderEncoder, val body: BodyEncoder) : Encoder {

    @field:Element(name = "Header")
    @field:Namespace(reference = "http://www.w3.org/2003/05/soap-envelope", prefix = "s")
    var h = header

    @field:Element(name = "Body")
    var b = body


    override fun toByteArray(): ByteArray? {
        val format = Format(0)
        val outputStream = ByteArrayOutputStream()
        val serializer = Persister(AnnotationStrategy(), format)
        serializer.write(this, outputStream)

        if (body.a is RequestSecurityTokenEncoder) {
            return outputStream.toString().decodeSpecialCharacter().toByteArray(Charsets.UTF_8)
        }

        return outputStream.toByteArray()
    }
}


@Root(name = "DeviceAddRequest")
@Order(elements = arrayOf("ClientInfo", "Authentication"))
class DeviceAddRequestEncoder(credential: HashMap<String, String>?) : Encoder {

    @field:Element(name = "ClientInfo")
    private var clientInfo = ClientInfo()

    @field:Element(name = "Authentication")
    private var authentication = Authentication(credential)


    override fun toByteArray(): ByteArray? {
        val format = Format(0)
        val outputStream = ByteArrayOutputStream()
        val serializer = Persister(format)
        serializer.write(this, outputStream)

        return outputStream.toByteArray()
    }

    @Root(name = "ClientInfo")
    class ClientInfo {
        @field:Attribute(name = "name")
        private var name = UtilsHelper.getInstance().uuid

        @field:Attribute(name = "version")
        private var version = "1.0"
    }

    @Root(name = "Authentication")
    class Authentication(credential: HashMap<String, String>?) {
        @field:Element(name = "Membername")
        private var memberName = credential!!["deviceName"]

        @field:Element(name = "Password")
        private var passsword = credential!!["devicePassword"]
    }

}
