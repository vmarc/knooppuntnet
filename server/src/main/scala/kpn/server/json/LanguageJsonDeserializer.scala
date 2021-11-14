package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.DE
import kpn.api.common.EN
import kpn.api.common.FR
import kpn.api.common.Language
import kpn.api.common.NL

class LanguageJsonDeserializer extends JsonDeserializer[Language] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Language = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    node.asText match {
      case "en" => EN
      case "nl" => NL
      case "de" => DE
      case "fr" => FR
      case something =>
        throw JsonMappingException.from(
          jsonParser,
          s"Could not deserialize language '$something'"
        )
    }
  }
}
