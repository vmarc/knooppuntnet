package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.diff.TagDiffType

class TagDiffTypeJsonSerializer extends JsonSerializer[TagDiffType] {
  override def serialize(tagDiffType: TagDiffType, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(tagDiffType.entryName)
  }
}
