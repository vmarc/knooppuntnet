package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.data.MemberType

class MemberTypeJsonDeserializer extends JsonDeserializer[MemberType] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): MemberType = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    MemberType.withName(node.asText)
  }
}
