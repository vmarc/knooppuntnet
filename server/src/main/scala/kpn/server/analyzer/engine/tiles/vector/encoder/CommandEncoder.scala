package kpn.server.analyzer.engine.tiles.vector.encoder

import java.util

import org.locationtech.jts.algorithm.CGAlgorithms
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.LinearRing
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.geom.MultiPoint
import org.locationtech.jts.geom.Polygon

class CommandEncoder {

  private var x: Int = 0
  private var y: Int = 0

  def makeCommands1(geometry: Geometry): Seq[Int] = {
    x = 0
    y = 0
    geometry match {
      case polygon: Polygon => processPolygon(polygon)
      case multiLineString: MultiLineString => processMultiLineString(multiLineString)
      case _ => makeCommands3(geometry.getCoordinates.toIndexedSeq, shouldClosePath(geometry), geometry.isInstanceOf[MultiPoint])
    }
  }

  /**
   * // // // Ex.: MoveTo(3, 6), LineTo(8, 12), LineTo(20, 34), ClosePath //
   * Encoded as: [ 9 3 6 18 5 6 12 22 15 ] // == command type 7 (ClosePath),
   * length 1 // ===== relative LineTo(+12, +22) == LineTo(20, 34) // ===
   * relative LineTo(+5, +6) == LineTo(8, 12) // == [00010 010] = command type
   * 2 (LineTo), length 2 // === relative MoveTo(+3, +6) // == [00001 001] =
   * command type 1 (MoveTo), length 1 // Commands are encoded as uint32
   * varints, vertex parameters are // encoded as sint32 varints (zigzag).
   * Vertex parameters are // also encoded as deltas to the previous position.
   * The original // position is (0,0)
   *
   * @param cs
   * @return
   */
  def makeCommands2(cs: Seq[Coordinate], closePathAtEnd: Boolean): Seq[Int] = {
    makeCommands3(cs, closePathAtEnd, multiPoint = false)
  }

  def makeCommands3(cs: Seq[Coordinate], closePathAtEnd: Boolean, multiPoint: Boolean): Seq[Int] = {

    val MoveTo = 1 // MoveTo: 1. (2 parameters follow)
    val LineTo = 2 // LineTo: 2. (2 parameters follow)
    val ClosePath = 7 // ClosePath: 7. (no parameters follow)


    if (cs.isEmpty) throw new IllegalArgumentException("empty geometry")
    val r = new util.ArrayList[Integer]
    var lineToIndex = 0
    var lineToLength = 0

    val scale = 1.0 // TODO eliminate

    var i = 0
    while ( {
      i < cs.length
    }) {
      val c = cs(i)
      if (i == 0) r.add(commandAndLength(MoveTo, if (multiPoint) cs.length
      else 1))
      val _x: Int = (c.x * scale).round.toInt
      val _y: Int = (c.y * scale).round.toInt
      // prevent point equal to the previous
      if (i > 0 && _x == x && _y == y) {
        lineToLength -= 1
      }
      else {

        // prevent double closing
        if (closePathAtEnd && cs.length > 1 && i == (cs.length - 1) && cs.head == c) {
          lineToLength -= 1
        }
        else {

          // delta, then zigzag
          r.add(ZigZagEncoder.encode(_x - x))
          r.add(ZigZagEncoder.encode(_y - y))
          x = _x
          y = _y
          if (i == 0 && cs.length > 1 && !multiPoint) { // can length be too long?
            lineToIndex = r.size
            lineToLength = cs.length - 1
            r.add(commandAndLength(LineTo, lineToLength))
          }

        }
      }
      {
        i += 1
        i - 1
      }
    }
    // update LineTo length
    if (lineToIndex > 0) if (lineToLength == 0) { // remove empty LineTo
      r.remove(lineToIndex)
    }
    else { // update LineTo with new length
      r.set(lineToIndex, commandAndLength(LineTo, lineToLength))
    }
    if (closePathAtEnd) r.add(commandAndLength(ClosePath, 1))
    import scala.jdk.CollectionConverters._
    r.asScala.toSeq.map(_.toInt)
  }

  private def commandAndLength(command: Int, repeat: Int): Int = {
    repeat << 3 | command
  }

  private def shouldClosePath(geometry: Geometry): Boolean = {
    geometry.isInstanceOf[Polygon] || geometry.isInstanceOf[LinearRing]
  }

  private def processMultiLineString(geometry: MultiLineString): Seq[Int] = {
    GeometryUtil.childGeometries(geometry).flatMap { childGeometry =>
      makeCommands2(childGeometry.getCoordinates.toIndexedSeq, closePathAtEnd = false)
    }
  }

  private def processPolygon(polygon: Polygon): Seq[Int] = {

    val commands = scala.collection.mutable.ListBuffer[Int]()
    // According to the vector tile specification, the exterior ring of a polygon
    // must be in clockwise order, while the interior ring in counter-clockwise order.
    // In the tile coordinate system, Y axis is positive down.
    //
    // However, in geographic coordinate system, Y axis is positive up.
    // Therefore, we must reverse the coordinates.
    // So, the code below will make sure that exterior ring is in counter-clockwise order
    // and interior ring in clockwise order.
    var exteriorRing = polygon.getExteriorRing
    if (!CGAlgorithms.isCCW(exteriorRing.getCoordinates)) exteriorRing = exteriorRing.reverse
    commands ++= makeCommands2(exteriorRing.getCoordinates.toIndexedSeq, closePathAtEnd = true)
    var i = 0
    while ( {
      i < polygon.getNumInteriorRing
    }) {
      var interiorRing = polygon.getInteriorRingN(i)
      if (CGAlgorithms.isCCW(interiorRing.getCoordinates)) interiorRing = interiorRing.reverse
      commands ++= makeCommands2(interiorRing.getCoordinates.toIndexedSeq, closePathAtEnd = true)

      {
        i += 1
        i - 1
      }
    }
    commands.toSeq
  }

}
