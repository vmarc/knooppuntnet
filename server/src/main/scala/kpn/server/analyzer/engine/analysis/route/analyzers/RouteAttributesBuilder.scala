package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.route.RouteInfo

class RouteAttributesBuilder {

  def build(routeInfo: RouteInfo): Seq[String] = {
    val basicAttributes = buildBasicAttributes(routeInfo)
    val factAttributes = routeInfo.facts.map(fact => s"fact-${fact.name}")
    val networkTypeAttributes = Seq(s"network-type-${routeInfo.summary.networkType.name}")
    val locationAttributes = routeInfo.analysis.locationAnalysis.locationNames.map(location => s"location-$location")
    basicAttributes ++ factAttributes ++ networkTypeAttributes ++ locationAttributes
  }

  private def buildBasicAttributes(routeInfo: RouteInfo): Seq[String] = {
    Seq(
      if (routeInfo.active) Some("active") else None,
      if (routeInfo.orphan) Some("orphan") else None,
      if (routeInfo.lastSurvey.isDefined) Some("survey") else None,
      if (routeInfo.facts.nonEmpty) Some("facts") else None,
      if (routeInfo.summary.isBroken) Some("broken") else None,
    ).flatten
  }
}
