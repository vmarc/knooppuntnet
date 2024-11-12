package kpn.server.analyzer.engine.tiles

import kpn.api.custom.Day
import kpn.api.custom.Fact
import kpn.server.analyzer.engine.analysis.common.SurveyDateAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteSegmentBuilder
import kpn.server.analyzer.engine.tiles.domain.RouteTileInfo
import kpn.server.analyzer.engine.tiles.domain.TileDataRoute

import scala.util.Failure
import scala.util.Success

object TileDataRouteBuilder {

  def fromRouteInfo(route: RouteTileInfo): TileDataRoute = {
    val routeTileSegments = new RouteSegmentBuilder().from(route)
    TileDataRoute(
      route._id,
      route.name,
      layer(route),
      surveyDate(route),
      state(route),
      routeTileSegments,
      route.tiles
    )
  }

  private def surveyDate(route: RouteTileInfo): Option[Day] = {
    SurveyDateAnalyzer.analyze(route) match {
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
    route.tagValue("state").filter(supportedStates.contains)
  }
}
