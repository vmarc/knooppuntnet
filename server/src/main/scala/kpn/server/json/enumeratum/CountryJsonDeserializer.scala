package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.Country

class CountryJsonDeserializer extends JsonDeserializer[Country] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Country = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    Country.valueOf(node.asText)
  }
}
