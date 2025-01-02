package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.search.ConditionOperator

class ConditionOperatorJsonDeserializer extends JsonDeserializer[ConditionOperator] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): ConditionOperator = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    ConditionOperator.withName(node.asText)
  }
}
