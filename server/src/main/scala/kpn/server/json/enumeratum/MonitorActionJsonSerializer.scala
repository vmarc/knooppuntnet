package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.monitor.MonitorAction

class MonitorActionJsonSerializer extends JsonSerializer[MonitorAction] {
  override def serialize(monitorAction: MonitorAction, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(monitorAction.entryName)
  }
}
