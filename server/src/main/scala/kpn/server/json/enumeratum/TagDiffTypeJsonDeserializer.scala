package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.diff.TagDiffType

class TagDiffTypeJsonDeserializer extends JsonDeserializer[TagDiffType] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): TagDiffType = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    TagDiffType.valueOf(node.asText)
  }
}
