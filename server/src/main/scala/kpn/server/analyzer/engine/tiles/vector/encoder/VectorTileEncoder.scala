package kpn.server.analyzer.engine.tiles.vector.encoder

import kpn.server.analyzer.engine.tiles.domain.ClipBuffer
import kpn.server.analyzer.engine.tiles.domain.OldTile
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.geom.Point
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.geom.TopologyException
import org.locationtech.jts.io.ParseException
import org.locationtech.jts.io.WKTReader

class VectorTileEncoder(clipBuffer: ClipBuffer = OldTile.CLIP_BUFFER) {

  private val layers = new VectorTileLayers()

  private val extent = OldTile.EXTENT

  private val tileEnvelope: Polygon = buildTileEnvelope()

  def addPointFeature(layerName: String, attributes: Map[String, String], point: Point): Unit = {
    if (!pointInsideTileEnvelope(point)) {
      return
    }
    addFeature(layerName, attributes, point)
  }

  def addMultiLineStringFeature(layerName: String, attributes: Map[String, String], geometry: MultiLineString): Unit = {

    // ignore small lines
    if (geometry.getLength < 1.0d) {
      return
    }

    val clippedGeometry = clipGeometry(geometry)

    // ignore geometry if empty after clipping
    if (clippedGeometry.isEmpty) {
      return
    }

    addFeature(layerName, attributes, clippedGeometry)
  }

  def addLineStringFeature(layerName: String, attributes: Map[String, String], geometry: LineString): Unit = {

    // ignore small lines
    if (geometry.getLength < 1.0d) {
      return
    }

    val clippedGeometry = clipGeometry(geometry)

    // ignore geometry if empty after clipping
    if (clippedGeometry.isEmpty) {
      return
    }

    addFeature(layerName, attributes, clippedGeometry)
  }

  private def addFeature(layerName: String, attributes: Map[String, String], geometry: Geometry): Unit = {
    val layer = layers.layerWithName(layerName)
    val tags = attributes.filterNot(_._2 == null).toSeq.flatMap { case (key, value) => Seq(layer.key(key), layer.value(value)) }
    val feature = VectorTileFeature(geometry, tags)
    layer.addFeature(feature)
  }


  private def pointInsideTileEnvelope(point: Point): Boolean = {
    tileEnvelope.getEnvelopeInternal.covers(point.getCoordinate)
  }

  private def clipGeometry(geometry: Geometry): Geometry = {
    try {
      var clippedGeometry = tileEnvelope.intersection(geometry)
      // some times a intersection is returned as an empty geometry.
      // going via wkt fixes the problem.
      if (clippedGeometry.isEmpty && geometry.intersects(tileEnvelope)) {
        val originalViaWkt = new WKTReader().read(geometry.toText)
        clippedGeometry = tileEnvelope.intersection(originalViaWkt)
      }
      clippedGeometry
    } catch {
      case e: TopologyException =>
        // could not intersect. original geometry will be used instead
        geometry
      case e1: ParseException =>
        // could not encode/decode WKT. original geometry will be used instead
        geometry
    }
  }

  def encode: Array[Byte] = {
    new VectorTileEncoderEncoder(layers, extent).encode
  }

  private def buildTileEnvelope(): Polygon = {
    val size = extent.toDouble
    val coords = new Array[Coordinate](5)
    coords(0) = new Coordinate(0d - clipBuffer.left, size + clipBuffer.bottom)
    coords(1) = new Coordinate(size + clipBuffer.right, size + clipBuffer.bottom)
    coords(2) = new Coordinate(size + clipBuffer.right, 0d - clipBuffer.top)
    coords(3) = new Coordinate(0d - clipBuffer.left, 0d - clipBuffer.top)
    coords(4) = coords(0)
    new GeometryFactory().createPolygon(coords)
  }
}
