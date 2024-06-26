package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact.RouteUnsupportedNetworkType
import kpn.api.custom.NetworkType
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

object RouteNetworkTypeAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val networkTypes = new RouteNetworkTypeAnalyzer(context.relation.tags).analyze()
    if (networkTypes.isEmpty) {
      context.copy(
        facts = context.facts :+ RouteUnsupportedNetworkType
      )
    }
    else {
      context.copy(
        _networkTypes = Some(networkTypes)
      )
    }
  }
}

class RouteNetworkTypeAnalyzer(tags: Seq[Tag]) {
  def analyze(): Seq[NetworkType] = {
    Tags.get(tags, "route") match {
      case None => Seq.empty
      case Some(routeTagValue) =>
        val values = routeTagValue.split(";").toSeq.map(_.trim)
        values.flatMap { value =>
          value match {
            case "hiking" => Some(NetworkType.hiking)
            case "walking" => Some(NetworkType.hiking)
            case "foot" => Some(NetworkType.hiking)
            case "bicycle" => Some(NetworkType.cycling)
            case "horse" => Some(NetworkType.horseRiding)
            case "canoe" => Some(NetworkType.canoe)
            case "motorboat" => Some(NetworkType.motorboat)
            case "inline_skates" => Some(NetworkType.inlineSkating)
            case _ => None
          }
        }
    }
  }
}
