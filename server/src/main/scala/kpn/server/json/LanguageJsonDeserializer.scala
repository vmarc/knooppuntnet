package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.Language

class LanguageJsonDeserializer extends JsonDeserializer[Language] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Language = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    Language.withName(node.asText)
  }
}
