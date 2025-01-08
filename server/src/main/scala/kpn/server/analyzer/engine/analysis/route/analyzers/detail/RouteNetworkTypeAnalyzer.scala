package kpn.server.analyzer.engine.analysis.route.analyzers.detail

import kpn.api.common.Fact.RouteUnsupportedRouteType
import kpn.api.common.RouteType
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

object RouterouteTypeAnalyzer extends RouteDetailAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    val routeTypes = new RouterouteTypeAnalyzer(context.relation.tags).analyze()
    if (routeTypes.isEmpty) {
      context.copy(
        abort = true,
        facts = context.facts :+ RouteUnsupportedRouteType
      )
    }
    else {
      context.copy(
        _routeTypes = Some(routeTypes)
      )
    }
  }
}

class RouterouteTypeAnalyzer(tags: Seq[Tag]) {
  def analyze(): Seq[RouteType] = {
    Tags.get(tags, "route") match {
      case None => Seq.empty
      case Some(routeTagValue) =>
        val values = routeTagValue.split(";").toSeq.map(_.trim)
        values.flatMap { value =>
          value match {
            case "hiking" => Some(RouteType.hiking)
            case "walking" => Some(RouteType.hiking)
            case "foot" => Some(RouteType.hiking)
            case "bicycle" => Some(RouteType.cycling)
            case "horse" => Some(RouteType.horseRiding)
            case "canoe" => Some(RouteType.canoe)
            case "motorboat" => Some(RouteType.motorboat)
            case "inline_skates" => Some(RouteType.inlineSkating)
            case _ => None
          }
        }
    }
  }
}
