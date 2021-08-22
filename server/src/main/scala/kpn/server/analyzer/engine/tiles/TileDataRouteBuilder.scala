package kpn.server.analyzer.engine.tiles

import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackSegment
import kpn.api.common.tiles.ZoomLevel
import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.tiles.domain.Line
import kpn.server.analyzer.engine.tiles.domain.Point
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.Tile
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute
import kpn.server.analyzer.engine.tiles.domain.TileRouteSegment
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier

import scala.util.Failure
import scala.util.Success

class TileDataRouteBuilder(z: Int) {

  private val distanceTolerance = {
    val tileX = (z - 7) * 65
    (Tile.lon(z, tileX + 1) - Tile.lon(z, tileX)) / 256d
  }

  private val geomFactory = new GeometryFactory

  def fromRouteInfo(route: RouteTileInfo): Option[TileDataRoute] = {

    val surveyDateTry = SurveyDateAnalyzer.analyze(route.tags)
    val surveyDate = surveyDateTry match {
      case Success(surveyDate) => surveyDate
      case Failure(_) => None
    }

    val forwardPathTrackPoints = route.forwardPath.toSeq.flatMap(_.trackPoints)
    val backwardPathTrackPoints = route.backwardPath.toSeq.flatMap(_.trackPoints)

    val mainTileRouteSegments = if (forwardPathTrackPoints == backwardPathTrackPoints.reverse) {
      route.forwardPath.toSeq.map(_.copy(oneWay = false)).flatMap(toTileRouteSegments)
    }
    else {
      route.forwardPath.toSeq.flatMap(toTileRouteSegments) ++
        route.backwardPath.toSeq.flatMap(toTileRouteSegments)
    }

    val tileRouteSegments = {
      val freePathSegments = route.freePaths.flatMap(toTileRouteSegments)
      val startTentacleSegments = route.startTentaclePaths.flatMap(toTileRouteSegments)
      val endTentacleSegments = route.endTentaclePaths.flatMap(toTileRouteSegments)
      freePathSegments ++ mainTileRouteSegments ++ startTentacleSegments ++ endTentacleSegments
    }

    if (tileRouteSegments.isEmpty) {
      None
    }
    else {

      val layer = if (route.facts.contains(Fact.RouteIncomplete)) {
        "incomplete-route"
      }
      else if (route.facts.exists(_.isError)) {
        "error-route"
      }
      else {
        "route"
      }

      val supportedStates = Seq("proposed")
      val state = route.tags("state").filter(supportedStates.contains)

      Some(
        TileDataRoute(
          route._id,
          route.name,
          layer,
          surveyDate,
          state,
          tileRouteSegments
        )
      )
    }
  }

  private def toTileRouteSegments(trackPath: TrackPath): Seq[TileRouteSegment] = {
    trackPath.segments.flatMap(segment => toTileRouteSegment(trackPath.pathId, trackPath.oneWay, segment))
  }

  private def toTileRouteSegment(pathId: Long, oneWay: Boolean, trackSegment: TrackSegment): Option[TileRouteSegment] = {
    val coordinates = (trackSegment.source +: trackSegment.fragments.map(_.trackPoint)).map { trackPoint =>
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

        Some(TileRouteSegment(pathId, oneWay, trackSegment.surface, lines))
      }
      else {
        val lines = coordinates.sliding(2).flatMap { case Seq(c1, c2) =>
          // TODO MAP should make sure that empty lines are eliminated long before this point !!!
          val line = Line(Point(c1.x, c1.y), Point(c2.x, c2.y))
          if (line.length > 0.00000001) Some(line) else None
        }.toSeq
        Some(TileRouteSegment(pathId, oneWay, trackSegment.surface, lines))
      }
    }
  }
}
