package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.NetworkScope

class NetworkScopeJsonDeserializer extends JsonDeserializer[NetworkScope] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): NetworkScope = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    NetworkScope.withName(node.asText)
  }
}
