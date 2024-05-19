package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.location.SurveyParameter

class SurveyParameterJsonSerializer extends JsonSerializer[SurveyParameter] {
  override def serialize(surveyParameter: SurveyParameter, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(surveyParameter.entryName)
  }
}
