package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.shared.NetworkType

class NetworkTypeJsonSerializer extends JsonSerializer[NetworkType] {
  override def serialize(networkType: NetworkType, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(networkType.name)
  }
}
