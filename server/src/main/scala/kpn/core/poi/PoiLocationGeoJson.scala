package kpn.core.poi

import kpn.server.analyzer.engine.tiles.domain.Rectangle
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.io.geojson.GeoJsonWriter

class PoiLocationGeoJson {

  private val geometryFactory = new GeometryFactory

  def geoJsonString(): String = {
    val boundingBoxes = PoiLocation.allBoundingBoxes.map(toGeometry)
    val geometryCollection = new GeometryCollection(boundingBoxes.toArray, geometryFactory)
    val geoJsonWriter = new GeoJsonWriter()
    geoJsonWriter.setEncodeCRS(false)
    geoJsonWriter.write(geometryCollection)
  }

  private def toGeometry(rectangle: Rectangle): Geometry = {
    val coordinates = Seq(
      new Coordinate(rectangle.xMin, rectangle.yMin),
      new Coordinate(rectangle.xMax, rectangle.yMin),
      new Coordinate(rectangle.xMax, rectangle.yMax),
      new Coordinate(rectangle.xMin, rectangle.yMax),
      new Coordinate(rectangle.xMin, rectangle.yMin)
    )
    geometryFactory.createLineString(coordinates.toArray)
  }

}
