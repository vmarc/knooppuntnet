package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.shared.Timestamp
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class TimestampJsonSerializer extends JsonSerializer[Timestamp] {
  override def serialize(timestamp: Timestamp, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(timestamp.iso)
  }
}
