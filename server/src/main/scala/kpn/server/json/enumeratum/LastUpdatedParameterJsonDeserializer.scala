package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.location.LastUpdatedParameter

class LastUpdatedParameterJsonDeserializer extends JsonDeserializer[LastUpdatedParameter] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): LastUpdatedParameter = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    LastUpdatedParameter.valueOf(node.asText)
  }
}
