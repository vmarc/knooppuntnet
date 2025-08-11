package kpn.server.json.enumeratum

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.location.LastUpdatedParameter

class LastUpdatedParameterJsonSerializer extends JsonSerializer[LastUpdatedParameter] {
  override def serialize(lastUpdatedParameter: LastUpdatedParameter, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(lastUpdatedParameter.toString)
  }
}
