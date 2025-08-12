package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.location.SurveyParameter

class SurveyParameterJsonDeserializer extends JsonDeserializer[SurveyParameter] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): SurveyParameter = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    SurveyParameter.withName(node.asText)
  }
}
