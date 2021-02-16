package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.custom.Tags
import org.apache.commons.text.StringEscapeUtils

class TagsJsonSerializer extends JsonSerializer[Tags] {
  override def serialize(tags: Tags, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    val json = tags.tags.map(kv => s"""{"key":"${StringEscapeUtils.escapeJson(kv.key)}","value":"${StringEscapeUtils.escapeJson(kv.value)}"}""").mkString("""{"tags":[""", ",", "]}")
    jsonGenerator.writeRawValue(json)
  }
}
