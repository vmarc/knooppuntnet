package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.search.ConditionGroupOperator

class ConditionGroupOperatorJsonSerializer extends JsonSerializer[ConditionGroupOperator] {
  override def serialize(operator: ConditionGroupOperator, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(operator.entryName)
  }
}
