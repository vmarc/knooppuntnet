package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.bson.types.ObjectId

class ObjectIdJsonSerializer extends JsonSerializer[ObjectId] {
  override def serialize(objectId: ObjectId, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeStartObject()
    jsonGenerator.writeStringField("$oid", objectId.toHexString)
    jsonGenerator.writeEndObject()
  }
}
