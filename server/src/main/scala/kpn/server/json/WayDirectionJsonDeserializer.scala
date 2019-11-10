package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.route.Backward
import kpn.api.common.route.Both
import kpn.api.common.route.Forward
import kpn.api.common.route.WayDirection

class WayDirectionJsonDeserializer extends JsonDeserializer[WayDirection] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): WayDirection = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    node.asText match {
      case "Both" => Both
      case "Forward" => Forward
      case "Backward" => Backward
      case something =>
        throw JsonMappingException.from(
          jsonParser,
          s"Expected a value Both|forward|Backward instead of $something"
        )
    }
  }
}
