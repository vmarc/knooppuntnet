package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.AnalysisStrategy

class AnalysisStrategyJsonDeserializer extends JsonDeserializer[AnalysisStrategy] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): AnalysisStrategy = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    AnalysisStrategy.withName(node.asText)
  }
}
