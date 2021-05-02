package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.custom.LocationRoutesType

class LocationRoutesTypeJsonDeserializer extends JsonDeserializer[LocationRoutesType] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): LocationRoutesType = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val name = node.asText
    if (name == null || name.isEmpty) {
      null
    }
    else {
      LocationRoutesType.withName(name).getOrElse(
        throw JsonMappingException.from(
          jsonParser,
          "Could not deserialize LocationRoutesType"
        )
      )
    }
  }
}
