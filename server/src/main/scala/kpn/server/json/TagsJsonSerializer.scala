package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.shared.data.Tags
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class TagsJsonSerializer extends JsonSerializer[Tags] {
  override def serialize(tags: Tags, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    val json = tags.tags.map(kv => s"""["${kv.key}","${kv.value}"]""").mkString("[", ",", "]")
    jsonGenerator.writeRawValue(json)
  }
}
