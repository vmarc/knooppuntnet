package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.base.MongoId

class MongoIdJsonSerializer extends JsonSerializer[MongoId] {
  override def serialize(mongoId: MongoId, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeStartObject()
    jsonGenerator.writeStringField("$oid", mongoId.oid)
    jsonGenerator.writeEndObject()
  }
}
