package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.location.BooleanParameter

class BooleanParameterJsonDeserializer extends JsonDeserializer[BooleanParameter] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): BooleanParameter = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    BooleanParameter.valueOf(node.asText)
  }
}
