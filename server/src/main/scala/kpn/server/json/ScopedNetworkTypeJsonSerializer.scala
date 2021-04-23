package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.custom.ScopedNetworkType

class ScopedNetworkTypeJsonSerializer extends JsonSerializer[ScopedNetworkType] {
  override def serialize(scopedNetworkType: ScopedNetworkType, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(scopedNetworkType.key)
  }
}