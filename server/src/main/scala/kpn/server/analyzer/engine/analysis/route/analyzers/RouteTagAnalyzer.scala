package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteTagInvalid
import kpn.api.custom.Fact.RouteTagMissing
import kpn.api.custom.Fact.RouteUnsupportedNetworkType
import kpn.api.custom.NetworkType
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
    if (!context.relation.hasTag("type", "route", "superroute")) {
      context.copy(abort = true).withFacts(RouteTagMissing)
    }
    else {
      context.relation.tagValue("route") match {
        case None => context.copy(abort = true).withFacts(RouteTagMissing)
        case Some(routeTagValue) =>
          NetworkType.all.find(_.routeTagValues.contains(routeTagValue)) match {
            case None => context.copy(abort = true).withFacts(RouteUnsupportedNetworkType)
            case Some(networkType) =>
              val superRoute = context.relation.hasTag("type", "superroute")
              val nodeNetwork = context.relation.hasTag("network:type", "node_network")
              val facts = ListBuffer[Fact]()
              val scopedNetworkTypeOption = context.relation.tagValue("network") match {
                case None => None
                case Some(key) =>
                  ScopedNetworkType.withKey(key) match {
                    case None => None
                    case Some(scopedNetworkType) =>
                      if (!context.relation.hasTag("route", scopedNetworkType.networkType.routeTagValues *)) {
                        facts += RouteTagInvalid
                      }
                      Some(scopedNetworkType)
                  }
              }
              context.copy(
                superRoute = superRoute,
                nodeNetwork = nodeNetwork,
                _networkType = Some(networkType),
                scopedNetworkTypeOption = scopedNetworkTypeOption
              ).withFacts(facts.toSeq *)
          }
      }
    }
  }
}
