package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.search.ConditionOperator

class ConditionOperatorJsonSerializer extends JsonSerializer[ConditionOperator] {
  override def serialize(operator: ConditionOperator, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(operator.entryName)
  }
}
