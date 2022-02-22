package kpn.server.analyzer.engine.tiles

import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteSegmentBuilder
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute

import scala.util.Failure
import scala.util.Success

class TileDataRouteBuilder(zoomLevel: Int) {

  def fromRouteInfo(route: RouteTileInfo): Option[TileDataRoute] = {
    val routeTileSegments = new RouteSegmentBuilder(zoomLevel).from(route)
    if (routeTileSegments.isEmpty) {
      None
    }
    else {
      Some(
        TileDataRoute(
          route._id,
          route.name,
          layer(route),
          surveyDate(route),
          state(route),
          routeTileSegments
        )
      )
    }
  }

  private def surveyDate(route: RouteTileInfo): Option[Day] = {
    SurveyDateAnalyzer.analyze(route.tags) match {
      case Success(surveyDate) => surveyDate
      case Failure(_) => None
    }
  }

  private def layer(route: RouteTileInfo): String = {
    if (route.facts.contains(Fact.RouteIncomplete)) {
      "incomplete-route"
    }
    else if (route.facts.exists(_.isError)) {
      "error-route"
    }
    else {
      "route"
    }
  }

  private def state(route: RouteTileInfo): Option[String] = {
    val supportedStates = Seq("proposed")
    route.tags("state").filter(supportedStates.contains)
  }
}
