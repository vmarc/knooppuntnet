package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.ChangeType

class ChangeTypeJsonDeserializer extends JsonDeserializer[ChangeType] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): ChangeType = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    ChangeType.valueOf(node.asText)
  }
}
