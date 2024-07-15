package kpn.server.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.node.ArrayNode
import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import org.locationtech.jts.geom.Coordinate

class CoordinateArrayJsonDeserializer extends JsonDeserializer[CoordinateArray] {
  override def deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): CoordinateArray = {
    val node: ArrayNode = jsonParser.getCodec.readTree(jsonParser)
    val coordinates = (0 until node.size()).toArray.map { index =>
      val latlon = node.get(index)
      val lat = latlon.get(0).asDouble()
      val lon = latlon.get(1).asDouble()
      new Coordinate(lon, lat)
    }
    CoordinateArray(coordinates)
  }
}
