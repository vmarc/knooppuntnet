package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.ElementChangeType

class ElementChangeTypeJsonDeserializer extends JsonDeserializer[ElementChangeType] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): ElementChangeType = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    ElementChangeType.valueOf(node.asText)
  }
}
