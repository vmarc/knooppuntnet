package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.data.MemberType

class MemberTypeJsonSerializer extends JsonSerializer[MemberType] {
  override def serialize(memberType: MemberType, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(memberType.toString)
  }
}
