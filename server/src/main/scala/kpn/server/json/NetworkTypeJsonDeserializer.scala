package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import kpn.shared.NetworkType

class NetworkTypeJsonDeserializer extends JsonDeserializer[NetworkType] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): NetworkType = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val name = node.asText
    if (name == null || name.isEmpty) {
      null
    }
    else {
      NetworkType.withName(name).getOrElse(
        throw JsonMappingException.from(
          jsonParser,
          "Could not deserialize network type"
        )
      )
    }
  }
}
