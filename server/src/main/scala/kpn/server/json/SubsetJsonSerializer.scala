package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.custom.Subset

class SubsetJsonSerializer extends JsonSerializer[Subset] {
  override def serialize(subset: Subset, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(s"${subset.country.domain}:${subset.networkType.name}")
  }
}
