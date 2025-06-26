package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.monitor.MonitorReferenceType

class MonitorReferenceTypeJsonSerializer extends JsonSerializer[MonitorReferenceType] {
  override def serialize(monitorReferenceType: MonitorReferenceType, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(monitorReferenceType.entryName)
  }
}
