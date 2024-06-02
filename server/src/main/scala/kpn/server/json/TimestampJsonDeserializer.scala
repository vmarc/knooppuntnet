package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.custom.Timestamp
import kpn.core.common.TimestampUtil

class TimestampJsonDeserializer(mongo: Boolean) extends JsonDeserializer[Timestamp] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Timestamp = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val timestamp = TimestampUtil.parseIso(node.asText)
    if (mongo) {
      TimestampUtil.toLocal(timestamp)
    }
    else {
      timestamp
    }
  }
}
