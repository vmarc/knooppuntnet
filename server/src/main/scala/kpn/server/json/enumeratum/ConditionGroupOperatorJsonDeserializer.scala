package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.search.ConditionGroupOperator

class ConditionGroupOperatorJsonDeserializer extends JsonDeserializer[ConditionGroupOperator] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): ConditionGroupOperator = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    ConditionGroupOperator.withName(node.asText)
  }
}
