package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.custom.LocationNodesType

class LocationNodesTypeJsonSerializer extends JsonSerializer[LocationNodesType] {
  override def serialize(locationNodesType: LocationNodesType, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(locationNodesType.name)
  }
}
