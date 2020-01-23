package kpn.server.analyzer.engine.elevation

import kpn.core.util.Haversine
import kpn.server.analyzer.engine.tiles.domain.Point

import scala.annotation.tailrec

class ElevationCoordinatesBuilder {

  private val segmentDistance = 45

  def build(points: Seq[Point]): Seq[Point] = {
    buildCoordinates(segmentDistance, points, Seq.empty)
  }

  @tailrec
  private def buildCoordinates(remainingDistanceInSegment: Double, points: Seq[Point], result: Seq[Point]): Seq[Point] = {
    if (points.size == 1) {
      result ++ points
    }
    else {
      val point1 = points.head
      val point2 = points(1)
      val segmentLength = Haversine.distance(point1, point2)
      if (remainingDistanceInSegment < segmentLength) {
        val ratio = remainingDistanceInSegment / segmentLength
        val newStartPointX = point1.x + (ratio * (point2.x - point1.x))
        val newStartPointY = point1.y + (ratio * (point2.y - point1.y))
        val newStartPoint = Point(newStartPointX, newStartPointY)
        val newPoints = Seq(newStartPoint) ++ points.tail
        buildCoordinates(segmentDistance, newPoints, result :+ newStartPoint)
      }
      else {
        buildCoordinates(remainingDistanceInSegment - segmentLength, points.tail, result)
      }
    }
  }

}
