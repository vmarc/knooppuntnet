package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import kpn.api.common.FeatureLayer

class FeatureLayerJsonDeserializer extends JsonDeserializer[FeatureLayer] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): FeatureLayer = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    FeatureLayer.withName(node.asText)
  }
}
