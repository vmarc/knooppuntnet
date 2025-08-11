package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.monitor.MonitorReferenceType

class MonitorReferenceTypeJsonDeserializer extends JsonDeserializer[MonitorReferenceType] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): MonitorReferenceType = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    MonitorReferenceType.valueOf(node.asText)
  }
}
