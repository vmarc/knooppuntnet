package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact
import kpn.api.common.Fact.RouteTagInvalid
import kpn.api.common.Fact.RouteTagMissing
import kpn.api.common.RouteType
import kpn.api.custom.ScopedRouteType

import scala.collection.mutable.ListBuffer

object BaseRouteTagAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteTagAnalyzer(context).analyze
  }
}

class BaseRouteTagAnalyzer(context: BaseRouteAnalysisContext) {

  def analyze: BaseRouteAnalysisContext = {
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
          val scopedRouteTypeOption = context.relation.tagValue("network") match {
            case None => None
            case Some(key) =>
              ScopedRouteType.withKey(key) match {
                case None => None
                case Some(scopedRouteType) =>
                  facts.addAll(assertTagValueMatchesrouteType(scopedRouteType.routeType, routeTagValue))
                  Some(scopedRouteType)
              }
          }
          context.copy(
            superRoute = superRoute,
            nodeNetwork = nodeNetwork,
            scopedRouteTypeOption = scopedRouteTypeOption
          ).withFacts(facts.toSeq: _*)
      }
    }
  }

  private def assertTagValueMatchesrouteType(routeType: RouteType, routeTagValue: String): Seq[Fact] = {

    val facts = ListBuffer[Fact]()
    if (routeType == RouteType.hiking) {
      if (!Seq("hiking", "walking", "foot").contains(routeTagValue)) {
        facts += RouteTagInvalid
      }
    }
    else if (routeType == RouteType.cycling) {
      if (routeTagValue != "bicycle") {
        facts += RouteTagInvalid
      }
    }
    else if (routeType == RouteType.horseRiding) {
      if (routeTagValue != "horse") {
        facts += RouteTagInvalid
      }
    }
    else if (routeType == RouteType.horseRiding) {
      if (routeTagValue != "horse") {
        facts += RouteTagInvalid
      }
    }
    else if (routeType == RouteType.canoe) {
      if (routeTagValue != "canoe") {
        facts += RouteTagInvalid
      }
    }
    else if (routeType == RouteType.motorboat) {
      if (routeTagValue != "motorboat") {
        facts += RouteTagInvalid
      }
    }
    else if (routeType == RouteType.inlineSkating) {
      if (routeTagValue != "inline_skates") {
        facts += RouteTagInvalid
      }
    }
    facts.toSeq
  }
}
