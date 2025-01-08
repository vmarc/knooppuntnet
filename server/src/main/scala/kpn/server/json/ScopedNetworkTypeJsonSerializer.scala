package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.custom.ScopedRouteType

class scopedRouteTypeJsonSerializer extends JsonSerializer[ScopedRouteType] {
  override def serialize(scopedRouteType: ScopedRouteType, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(scopedRouteType.key)
  }
}
