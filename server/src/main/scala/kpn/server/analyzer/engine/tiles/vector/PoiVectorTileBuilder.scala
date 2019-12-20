package kpn.server.analyzer.engine.tiles.vector

import kpn.server.analyzer.engine.tiles.PoiTileData
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.vector.encoder.VectorTileEncoder
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Point
import org.springframework.stereotype.Component

import scala.collection.immutable.ListMap

@Component
class PoiVectorTileBuilder {

  def build(data: PoiTileData): Array[Byte] = {

    val geomFactory = new GeometryFactory

    val encoder = new VectorTileEncoder()

    def scaleLat(lat: Double): Double = {
      Tile.EXTENT - ((lat - data.tile.bounds.yMin) * Tile.EXTENT / (data.tile.bounds.yMax - data.tile.bounds.yMin))
    }

    def scaleLon(lon: Double): Double = {
      (lon - data.tile.bounds.xMin) * Tile.EXTENT / (data.tile.bounds.xMax - data.tile.bounds.xMin)
    }

    data.pois.foreach { poi =>
      val point: Point = geomFactory.createPoint(new Coordinate(scaleLon(poi.lon), scaleLat(poi.lat)))
      val userData = ListMap(
        "type" -> poi.elementType,
        "id" -> poi.elementId.toString
      )
      encoder.addPointFeature(poi.layer, userData, point)
    }

    encoder.encode
  }

}
