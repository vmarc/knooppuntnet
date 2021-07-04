package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.custom.Fact

class FactJsonDeserializer extends JsonDeserializer[Fact] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Fact = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val factName = node.asText
    Fact.withName(factName).getOrElse(
      throw JsonMappingException.from(
        jsonParser,
        s"Could not deserialize fact: $factName"
      )
    )
  }
}
