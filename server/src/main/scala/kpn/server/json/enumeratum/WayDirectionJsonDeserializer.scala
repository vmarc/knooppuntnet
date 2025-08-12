package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.route.WayDirection

class WayDirectionJsonDeserializer extends JsonDeserializer[WayDirection] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): WayDirection = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    WayDirection.withName(node.asText)
  }
}
