package kpn.server.analyzer.engine.tiles

import kpn.api.common.route.RouteInfo
import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.Fact
import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Point
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.TileRouteSegment
import kpn.core.util.Log
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier

class TileDataRouteBuilder(z: Int) {

  private val log = Log(classOf[TileDataRouteBuilder])

  private val distanceTolerance = {
    val tileX = (z - 7) * 65
    (Tile.lon(z, tileX + 1) - Tile.lon(z, tileX)) / 256d
  }

  private val geomFactory = new GeometryFactory

  def build(routeInfo: RouteInfo): Option[TileDataRoute] = {

    val surveyDate: Option[String] = routeInfo.tags("survey:date").flatMap { tagValue =>
      log.info(s"route ${routeInfo.id} surveyDate=$tagValue")
      if (tagValue.length >= "2020-08".length) {
        Some(tagValue.substring(0, "2020-08".length))
      }
      else {
        None
      }
    }

    routeInfo.analysis.flatMap { analysis =>

      // TODO MAP add some logic here to eliminate double ways (e.g. ways that are both part of forward and backward)
      val segments = analysis.map.unusedSegments ++
        analysis.map.forwardPath.toSeq.flatMap(_.segments) ++
        analysis.map.backwardPath.toSeq.flatMap(_.segments) ++
        analysis.map.startTentaclePaths.flatMap(_.segments) ++
        analysis.map.endTentaclePaths.flatMap(_.segments)

      val tileRouteSegments = segments.flatMap { segment =>

        val coordinates = (segment.source +: segment.fragments.map(_.trackPoint)).map { trackPoint =>
          val lat = trackPoint.lat.toDouble
          val lon = trackPoint.lon.toDouble
          new Coordinate(lon, lat)
        }

        if (coordinates.size < 2) {
          None
        }
        else {
          if (z < ZoomLevel.vectorTileMinZoom) {
            val lineString = geomFactory.createLineString(coordinates.toArray)
            val simplifiedLineString: LineString = DouglasPeuckerSimplifier.simplify(lineString, distanceTolerance).asInstanceOf[LineString]
            val simplifiedCoordinates = simplifiedLineString.getCoordinates.toSeq

            val lines = simplifiedCoordinates.sliding(2).flatMap { case Seq(c1, c2) =>
              val line = Line(Point(c1.x, c1.y), Point(c2.x, c2.y))

              // TODO MAP should check if line is within clipbounds of tile - return none if outside clipBounds

              if (line.length > 0.00000001) Some(line) else None
            }.toSeq

            Some(TileRouteSegment(segment.surface, lines))
          }
          else {
            val lines = coordinates.sliding(2).flatMap { case Seq(c1, c2) =>
              // TODO MAP should make sure that empty lines are eliminated long before this point !!!
              val line = Line(Point(c1.x, c1.y), Point(c2.x, c2.y))
              if (line.length > 0.00000001) Some(line) else None
            }.toSeq
            Some(TileRouteSegment(segment.surface, lines))
          }
        }
      }

      if (tileRouteSegments.isEmpty) {
        None
      }
      else {

        val layer = if (routeInfo.orphan) {
          "orphan-route"
        }
        else if (routeInfo.facts.contains(Fact.RouteIncomplete)) {
          "incomplete-route"
        }
        else if (routeInfo.facts.exists(_.isError)) {
          "error-route"
        }
        else {
          "route"
        }

        Some(TileDataRoute(routeInfo.id, routeInfo.summary.name, layer, surveyDate, tileRouteSegments))
      }
    }
  }
}
