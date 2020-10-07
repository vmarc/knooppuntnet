package kpn.core.poi

import java.io.ByteArrayOutputStream

import kpn.server.analyzer.engine.tiles.domain.Rectangle
import org.geotools.geojson.geom.GeometryJSON
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryCollection
import org.locationtech.jts.geom.GeometryFactory

class PoiLocationGeoJson {

  private val geomFactory = new GeometryFactory

  def geoJsonString(): String = {
    val boundingBoxes = PoiLocation.allBoundingBoxes.map(toGeometry)
    val geometryCollection = new GeometryCollection(boundingBoxes.toArray, geomFactory)
    val baos = new ByteArrayOutputStream()
    val g = new GeometryJSON()
    g.write(geometryCollection, baos)
    baos.toString
  }

  private def toGeometry(rectangle: Rectangle): Geometry = {
    val coordinates = Seq(
      new Coordinate(rectangle.xMin, rectangle.yMin),
      new Coordinate(rectangle.xMax, rectangle.yMin),
      new Coordinate(rectangle.xMax, rectangle.yMax),
      new Coordinate(rectangle.xMin, rectangle.yMax),
      new Coordinate(rectangle.xMin, rectangle.yMin)
    )
    geomFactory.createLineString(coordinates.toArray)
  }

}
