package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.server.analyzer.engine.tiles.domain.CoordinateArray
import kpn.server.analyzer.engine.tiles.domain.CoordinateTransform.latLonCoordinatesToWorldCoordinatesSeq
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileDataRouteSegment
import kpn.server.json.Json

class RouteSegmentBuilder {

  def from(tileInfo: RouteTileInfo): Seq[TileDataRouteSegment] = {
    tileInfo.segments.flatMap { segment =>
      tileInfo.segmentElements.map { segmentElement =>
        val coordinates = Json.value(segmentElement.coordinates, classOf[CoordinateArray]).coordinates.toSeq
        val worldCoordinates = latLonCoordinatesToWorldCoordinatesSeq(coordinates)
        val pathIds = tileInfo.paths.filter(_.elementIds.contains(segmentElement.segmentElementId)).map(_.id)
        TileDataRouteSegment(
          segment.id,
          segmentElement.segmentElementId,
          pathIds = pathIds,
          oneWay = false, // TODO redesign tiles - still needed???
          segmentElement.surface,
          worldCoordinates
        )
      }
    }
  }
}
