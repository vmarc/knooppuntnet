package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.RouteScope

class RouteScopeJsonSerializer extends JsonSerializer[RouteScope] {
  override def serialize(routeScope: RouteScope, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(routeScope.entryName)
  }
}
