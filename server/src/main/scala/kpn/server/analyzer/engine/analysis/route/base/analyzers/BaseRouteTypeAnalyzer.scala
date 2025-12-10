package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact.RouteUnsupportedRouteType
import kpn.api.common.RouteType
import kpn.api.custom.Tag
import kpn.api.custom.Tags

object BaseRouteTypeAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    val routeTypes = new BaseRouteTypeAnalyzer(context.relation.tags).analyze()
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

class BaseRouteTypeAnalyzer(tags: Seq[Tag]) {
  def analyze(): Seq[RouteType] = {
    Tags.values(tags, "route").flatMap {
      case "hiking" => Some(RouteType.hiking)
      case "walking" => Some(RouteType.hiking)
      case "foot" => Some(RouteType.hiking)
      case "bicycle" => Some(RouteType.cycling)
      case "horse" => Some(RouteType.horseRiding)
      case "canoe" => Some(RouteType.canoe)
      case "motorboat" => Some(RouteType.motorboat)
      case "inline_skates" => Some(RouteType.inlineSkating)
      case "mtb" => Some(RouteType.mtb)
      case _ => None
    }.distinct.sortBy(_.entryName)
  }
}
