package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.RouteType

class routeTypeJsonSerializer extends JsonSerializer[RouteType] {
  override def serialize(routeType: RouteType, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(routeType.entryName)
  }
}
