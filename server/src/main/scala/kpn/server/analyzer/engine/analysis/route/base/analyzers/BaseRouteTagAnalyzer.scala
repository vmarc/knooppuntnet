package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact
import kpn.api.common.Fact.RouteTagInvalid
import kpn.api.common.Fact.RouteTagMissing
import kpn.api.common.RouteType
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Tags

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
          val scopedRouteTypeOption = context.relation.tagValue("network").flatMap { key =>
            ScopedRouteType.withKey(key).flatMap { scopedRouteType =>
              facts.addAll(assertTagValueMatchesRouteType(scopedRouteType.routeType, routeTagValue))
              Some(scopedRouteType)
            }
          }
          context.copy(
            superRoute = superRoute,
            nodeNetwork = nodeNetwork,
            _scopedRouteType = Some(scopedRouteTypeOption)
          ).withFacts(facts.toSeq *)
      }
    }
  }

  private def assertTagValueMatchesRouteType(routeType: RouteType, routeTagValue: String): Seq[Fact] = {

    val routeTagValues = Tags.splitAndNormalize(routeTagValue)

    val facts = ListBuffer[Fact]()
    if (routeType == RouteType.hiking) {
      if (!routeTagValues.exists(value => Seq("hiking", "walking", "foot").contains(value))) {
        facts += RouteTagInvalid
      }
    }
    else if (routeType == RouteType.cycling) {
      if (!routeTagValues.contains("bicycle")) {
        facts += RouteTagInvalid
      }
    }
    else if (routeType == RouteType.horseRiding) {
      if (!routeTagValues.contains("horse")) {
        facts += RouteTagInvalid
      }
    }
    else if (routeType == RouteType.horseRiding) {
      if (!routeTagValues.contains("horse")) {
        facts += RouteTagInvalid
      }
    }
    else if (routeType == RouteType.canoe) {
      if (!routeTagValues.contains("canoe")) {
        facts += RouteTagInvalid
      }
    }
    else if (routeType == RouteType.motorboat) {
      if (!routeTagValues.contains("motorboat")) {
        facts += RouteTagInvalid
      }
    }
    else if (routeType == RouteType.inlineSkating) {
      if (!routeTagValues.contains("inline_skates")) {
        facts += RouteTagInvalid
      }
    }
    facts.toSeq
  }
}
