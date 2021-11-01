package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.custom.Timestamp2

class Timestamp2JsonSerializer extends JsonSerializer[Timestamp2] {
  override def serialize(timestamp: Timestamp2, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeRawValue(s"""{"$$date":"${timestamp.iso}"}""")
  }
}
