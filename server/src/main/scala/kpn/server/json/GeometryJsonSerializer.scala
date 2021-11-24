package kpn.server.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.io.geojson.GeoJsonWriter

class GeometryJsonSerializer extends JsonSerializer[Geometry] {
  override def serialize(geometry: Geometry, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider): Unit = {
    val json = new GeoJsonWriter().write(geometry)
    jsonGenerator.writeRawValue(json)
  }
}
