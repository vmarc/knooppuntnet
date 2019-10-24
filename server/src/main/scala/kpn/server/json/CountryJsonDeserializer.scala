package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import kpn.shared.Country
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class CountryJsonDeserializer extends JsonDeserializer[Country] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Country = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val domain = node.asText
    if (domain == null || domain.isEmpty) {
      null
    }
    else {
      Country.withDomain(domain).getOrElse(
        throw JsonMappingException.from(
          jsonParser,
          "Could not deserialize country"
        )
      )
    }
  }
}
