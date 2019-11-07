package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.shared.NetworkScope

class NetworkScopeJsonSerializer extends JsonSerializer[NetworkScope] {
  override def serialize(networkScope: NetworkScope, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(networkScope.name)
  }
}
