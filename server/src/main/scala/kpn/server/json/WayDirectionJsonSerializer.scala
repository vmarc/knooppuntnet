package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.shared.route.WayDirection

class WayDirectionJsonSerializer extends JsonSerializer[WayDirection] {
  override def serialize(wayDirection: WayDirection, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(wayDirection.toString)
  }
}
