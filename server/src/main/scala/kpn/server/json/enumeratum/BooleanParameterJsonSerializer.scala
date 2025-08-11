package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.location.BooleanParameter

class BooleanParameterJsonSerializer extends JsonSerializer[BooleanParameter] {
  override def serialize(booleanParameter: BooleanParameter, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(booleanParameter.toString)
  }
}
