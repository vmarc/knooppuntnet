package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.ChangeType

class ChangeTypeJsonSerializer extends JsonSerializer[ChangeType] {
  override def serialize(changeType: ChangeType, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(changeType.entryName)
  }
}
