package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.custom.Timestamp
import kpn.core.common.TimestampUtil

class TimestampJsonDeserializer extends JsonDeserializer[Timestamp] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Timestamp = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    TimestampUtil.parseIso(node.asText)
  }
}
