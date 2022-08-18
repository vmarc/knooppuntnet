package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.base.ObjectId

class ObjectIdJsonSerializer extends JsonSerializer[ObjectId] {
  override def serialize(objectId: ObjectId, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeStartObject()
    jsonGenerator.writeStringField("$oid", objectId.oid)
    jsonGenerator.writeEndObject()
  }
}
