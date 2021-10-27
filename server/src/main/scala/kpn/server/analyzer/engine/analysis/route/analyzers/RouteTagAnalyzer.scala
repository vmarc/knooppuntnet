package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteTagInvalid
import kpn.api.custom.Fact.RouteTagMissing
import kpn.api.custom.Fact.RouteUnsupportedNetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

import scala.collection.mutable.ListBuffer

object RouteTagAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteTagAnalyzer(context).analyze
  }
}

class RouteTagAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {

    val facts = ListBuffer[Fact]()

    val scopedNetworkTypeOption = context.relation.tags("network") match {
      case None => None
      case Some(key) =>
        ScopedNetworkType.withKey(key) match {
          case None =>
            facts += RouteUnsupportedNetworkType
            None

          case Some(scopedNetworkType) =>
            context.relation.tags("route") match {
              case None => facts += RouteTagMissing
              case Some(routeTagValue) =>
                if(!context.relation.tags.has("route", scopedNetworkType.networkType.routeTagValues:_*)) {
                  facts += RouteTagInvalid
                }
            }
            Some(scopedNetworkType)
        }
    }

    context.copy(
      abort = scopedNetworkTypeOption.isEmpty,
      scopedNetworkTypeOption = scopedNetworkTypeOption
    ).withFacts(facts.toSeq: _*)
  }
}
