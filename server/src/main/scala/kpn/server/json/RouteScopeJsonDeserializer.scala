package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.RouteScope

class RouteScopeJsonDeserializer extends JsonDeserializer[RouteScope] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): RouteScope = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    RouteScope.withName(node.asText)
  }
}
