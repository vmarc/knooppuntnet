package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.monitor.MonitorAction

class MonitorActionJsonDeserializer extends JsonDeserializer[MonitorAction] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): MonitorAction = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    MonitorAction.withName(node.asText)
  }
}
