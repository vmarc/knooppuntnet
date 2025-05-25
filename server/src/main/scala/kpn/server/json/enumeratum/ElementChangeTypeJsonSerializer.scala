package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.ElementChangeType

class ElementChangeTypeJsonSerializer extends JsonSerializer[ElementChangeType] {
  override def serialize(elementChangeType: ElementChangeType, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(elementChangeType.entryName)
  }
}
