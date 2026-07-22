package kpn.server.analyzer.engine.monitor.analysis

import kpn.server.analyzer.engine.monitor.analysis.MonitorRouteAnalysisSupport.toMeters
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory

import scala.annotation.tailrec

object LineSimplifier {

  private val geometryFactory = new GeometryFactory

  def simplify(coordinates: List[Coordinate]): List[Coordinate] = {
    if (coordinates.sizeIs < 3) {
      coordinates
    }
    else {
      process(
        List(coordinates.head),
        coordinates.head,
        coordinates.tail
      ).reverse
    }
  }

  @tailrec
  private def process(
    selectedCoordinates: List[Coordinate],
    currentCoordinate: Coordinate,
    remainder: List[Coordinate]
  ): List[Coordinate] = {
    
    remainder.headOption match {
      case None => selectedCoordinates
      case Some(middleCoordinate) =>
        val newRemainder = remainder.tail
        newRemainder.headOption match {
          case None => middleCoordinate :: selectedCoordinates
          case Some(nextCoordinate) =>
            val lineString = geometryFactory.createLineString(Array(currentCoordinate, nextCoordinate))
            val point = geometryFactory.createPoint(middleCoordinate)
            val distance = toMeters(lineString.distance(point))
            if (distance < 0.5d) {
              // swallow the  middle coordinate
              process(selectedCoordinates, currentCoordinate, newRemainder)
            }
            else {
              // keep the  middle coordinate, and continue simplifying
              process(middleCoordinate :: selectedCoordinates, middleCoordinate, newRemainder)
            }
        }
    }
  }
}
