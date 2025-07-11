package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.monitor.MonitorCommandAction

class MonitorCommandActionJsonDeserializer extends JsonDeserializer[MonitorCommandAction] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): MonitorCommandAction = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    MonitorCommandAction.withName(node.asText)
  }
}
