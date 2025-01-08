package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.custom.ScopedRouteType

class scopedRouteTypeJsonDeserializer extends JsonDeserializer[ScopedRouteType] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): ScopedRouteType = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val key = node.asText
    if (key == null || key.isEmpty) {
      null
    }
    else {
      ScopedRouteType.withKey(key).getOrElse(
        throw JsonMappingException.from(
          jsonParser,
          "Could not deserialize scopedRouteType"
        )
      )
    }
  }
}
