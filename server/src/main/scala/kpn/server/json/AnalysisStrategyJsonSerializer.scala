package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.AnalysisStrategy

class AnalysisStrategyJsonSerializer extends JsonSerializer[AnalysisStrategy] {
  override def serialize(analysisStrategy: AnalysisStrategy, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeString(analysisStrategy.entryName)
  }
}
