package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.io.geojson.GeoJsonReader

class GeometryJsonDeserializer extends JsonDeserializer[Geometry] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): Geometry = {
    val node: JsonNode = jsonParser.getCodec.readTree(jsonParser)
    val json = node.toString
    new GeoJsonReader().read(json)
  }
}
