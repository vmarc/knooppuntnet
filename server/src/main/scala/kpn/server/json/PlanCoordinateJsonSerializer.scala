package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import kpn.api.common.planner.PlanCoordinate

class PlanCoordinateJsonSerializer extends JsonSerializer[PlanCoordinate] {
  override def serialize(planCoordinate: PlanCoordinate, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeArray(Array(planCoordinate.x,planCoordinate.y), 0, 2)
  }
}
