package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.FeatureLayer

class FeatureLayerJsonSerializer extends JsonSerializer[FeatureLayer] {
  override def serialize(layer: FeatureLayer, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(layer.toString)
  }
}
