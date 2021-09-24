package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackPoint
import kpn.api.common.common.TrackSegment
import kpn.api.common.route.RouteMap
import kpn.core.util.GeoJsonLineStringGeometry
import kpn.server.json.Json

object RoutePrinter {

  def printMap(map: RouteMap): Unit = {
    map.freePaths.foreach(path => printPath("free", path))
    map.forwardPath.foreach(path => printPath("forward", path))
    map.backwardPath.foreach(path => printPath("backward", path))
    map.startTentaclePaths.foreach(path => printPath("startTentacle", path))
    map.endTentaclePaths.foreach(path => printPath("endTentacle", path))
    map.unusedSegments.foreach(printSegment)
  }

  private def printPath(name: String, trackPath: TrackPath): Unit = {
    print(s"$name pathId=${trackPath.pathId}, ")
    print(s"startNodeId=${trackPath.startNodeId}, ")
    print(s"endNodeId=${trackPath.endNodeId}, ")
    print(s"meters=${trackPath.meters}, ")
    print(s"oneWay=${trackPath.oneWay}, ")
    println(s"segmentCount=${trackPath.segments.size}")
    printPoints(trackPath.trackPoints)
  }

  private def printSegment(segment: TrackSegment): Unit = {
    print(s"unused segment")
    printPoints(segment.trackPoints)
  }

  private def printPoints(trackPoints: Seq[TrackPoint]): Unit = {
    val coordinates = trackPoints.toArray.map(c => Array(c.lon.toDouble, c.lat.toDouble))
    val line = GeoJsonLineStringGeometry(
      "LineString",
      coordinates
    )
    val json = Json.objectMapper.writerWithDefaultPrettyPrinter()
    println(json.writeValueAsString(line))
  }

}
