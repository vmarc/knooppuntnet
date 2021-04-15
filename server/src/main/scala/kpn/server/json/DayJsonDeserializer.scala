package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.custom.Day

class DayJsonDeserializer extends JsonDeserializer[Day] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Day = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val year = node.get("year").asInt
    val month = node.get("month").asInt
    val day = node.get("day") match {
      case null => None
      case n: JsonNode => Some(n.asInt)
    }
    Day(year, month, day)
  }
}
