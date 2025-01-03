package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.route.LinkDirection

class LinkDirectionJsonDeserializer extends JsonDeserializer[LinkDirection] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): LinkDirection = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    LinkDirection.withName(node.asText)
  }
}
