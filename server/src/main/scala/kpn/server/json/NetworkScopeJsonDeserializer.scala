package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import kpn.shared.NetworkScope

class NetworkScopeJsonDeserializer extends JsonDeserializer[NetworkScope] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): NetworkScope = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val name = node.asText
    if (name == null || name.isEmpty) {
      null
    }
    else {
      NetworkScope.withName(name).getOrElse(
        throw JsonMappingException.from(
          jsonParser,
          "Could not deserialize network scope"
        )
      )
    }
  }
}
