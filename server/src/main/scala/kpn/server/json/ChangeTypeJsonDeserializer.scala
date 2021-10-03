package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.custom.ChangeType

class ChangeTypeJsonDeserializer extends JsonDeserializer[ChangeType] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): ChangeType = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val name = node.asText
    if (name == null || name.isEmpty) {
      null
    }
    else {
      ChangeType.withName(name).getOrElse(
        throw JsonMappingException.from(
          jsonParser,
          "Could not deserialize change type"
        )
      )
    }
  }
}
