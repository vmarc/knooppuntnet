package kpn.server.analyzer.engine.tiles.vector

import kpn.server.analyzer.engine.tiles.PoiTileData
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latToWorldY
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.lonToWorldX
import no.ecc.vectortile.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.springframework.stereotype.Component

@Component
class PoiVectorTileBuilder {

  def build(data: PoiTileData): Array[Byte] = {
    val geometryFactory = new GeometryFactory
    val encoder = new VectorTileEncoder(data.tile.poiExtent, data.tile.poiClipBufferSize, false)
    data.pois.foreach { poi =>
      val worldCoordinate = new Coordinate(lonToWorldX(poi.lon), latToWorldY(poi.lat))
      val coordinate = data.tile.poiScale(worldCoordinate)
      val coordinateInt = new Coordinate(coordinate.x.toInt, coordinate.y.toInt)
      val point = geometryFactory.createPoint(coordinateInt)
      val userData = new java.util.HashMap[String, String]()
      userData.put("type", poi.elementType)
      userData.put("id", poi.elementId.toString)
      encoder.addFeature(poi.layer, userData, point)
    }
    encoder.encode
  }
}
