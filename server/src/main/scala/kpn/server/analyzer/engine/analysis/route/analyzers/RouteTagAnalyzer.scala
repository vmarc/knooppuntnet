package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.Fact
import kpn.api.common.Fact.RouteTagInvalid
import kpn.api.common.Fact.RouteTagMissing
import kpn.api.common.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

import scala.collection.mutable.ListBuffer

object RouteTagAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteTagAnalyzer(context).analyze
  }
}

class RouteTagAnalyzer(context: RouteDetailAnalysisContext) {

  def analyze: RouteDetailAnalysisContext = {
    if (!context.relation.hasTag("type", "route", "superroute")) {
      context.copy(abort = true).withFacts(RouteTagMissing)
    }
    else {
      context.relation.tagValue("route") match {
        case None =>
          context.copy(abort = true).withFacts(RouteTagMissing)
        case Some(routeTagValue) =>
          val superRoute = context.relation.hasTag("type", "superroute")
          val nodeNetwork = context.relation.hasTag("network:type", "node_network")
          val facts = ListBuffer[Fact]()
          val scopedNetworkTypeOption = context.relation.tagValue("network") match {
            case None => None
            case Some(key) =>
              ScopedNetworkType.withKey(key) match {
                case None => None
                case Some(scopedNetworkType) =>
                  facts.addAll(assertTagValueMatchesNetworkType(scopedNetworkType.networkType, routeTagValue))
                  Some(scopedNetworkType)
              }
          }
          context.copy(
            superRoute = superRoute,
            nodeNetwork = nodeNetwork,
            scopedNetworkTypeOption = scopedNetworkTypeOption
          ).withFacts(facts.toSeq: _*)
      }
    }
  }

  private def assertTagValueMatchesNetworkType(networkType: NetworkType, routeTagValue: String): Seq[Fact] = {

    val facts = ListBuffer[Fact]()
    if (networkType == NetworkType.hiking) {
      if (!Seq("hiking", "walking", "foot").contains(routeTagValue)) {
        facts += RouteTagInvalid
      }
    }
    else if (networkType == NetworkType.cycling) {
      if (routeTagValue != "bicycle") {
        facts += RouteTagInvalid
      }
    }
    else if (networkType == NetworkType.horseRiding) {
      if (routeTagValue != "horse") {
        facts += RouteTagInvalid
      }
    }
    else if (networkType == NetworkType.horseRiding) {
      if (routeTagValue != "horse") {
        facts += RouteTagInvalid
      }
    }
    else if (networkType == NetworkType.canoe) {
      if (routeTagValue != "canoe") {
        facts += RouteTagInvalid
      }
    }
    else if (networkType == NetworkType.motorboat) {
      if (routeTagValue != "motorboat") {
        facts += RouteTagInvalid
      }
    }
    else if (networkType == NetworkType.inlineSkating) {
      if (routeTagValue != "inline_skates") {
        facts += RouteTagInvalid
      }
    }
    facts.toSeq
  }
}
