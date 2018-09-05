package soteca.com.genisysandroid.framwork.model.decoder

import soteca.com.genisysandroid.framwork.networking.Request

data class StringDecoder(
        private var req: Request? = null,
        var text: String? = null) : Decoder(req)