package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.RouteType

class routeTypeJsonDeserializer extends JsonDeserializer[RouteType] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): RouteType = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    RouteType.withName(node.asText)
  }
}
