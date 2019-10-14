package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.shared.Country
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class CountryJsonSerializer extends JsonSerializer[Country] {
  override def serialize(country: Country, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(country.domain)
  }
}
