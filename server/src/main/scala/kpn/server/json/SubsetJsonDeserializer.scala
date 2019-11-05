package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import kpn.shared.Subset

class SubsetJsonDeserializer extends JsonDeserializer[Subset] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Subset = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val names = node.asText.split(":")
    Subset.of(names.head, names(1)).getOrElse(
      throw JsonMappingException.from(
        jsonParser,
        "Subset expected"
      )
    )
  }
}
