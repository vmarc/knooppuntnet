package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.custom.LocationRoutesType

class LocationRoutesTypeJsonSerializer extends JsonSerializer[LocationRoutesType] {
  override def serialize(locationRoutesType: LocationRoutesType, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(locationRoutesType.name)
  }
}
