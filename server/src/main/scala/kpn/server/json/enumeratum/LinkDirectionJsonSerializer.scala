package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.route.LinkDirection

class LinkDirectionJsonSerializer extends JsonSerializer[LinkDirection] {
  override def serialize(linkDirection: LinkDirection, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(linkDirection.toString)
  }
}
