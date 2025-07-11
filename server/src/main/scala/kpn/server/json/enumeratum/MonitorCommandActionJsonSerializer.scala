package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.monitor.MonitorCommandAction

class MonitorCommandActionJsonSerializer extends JsonSerializer[MonitorCommandAction] {
  override def serialize(action: MonitorCommandAction, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(action.entryName)
  }
}
