package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.changes.ChangeAction

class ChangeActionJsonSerializer extends JsonSerializer[ChangeAction] {
  override def serialize(changeAction: ChangeAction, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(changeAction.entryName)
  }
}
