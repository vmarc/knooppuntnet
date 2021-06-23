package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.route.RouteInfo

class RouteLabelsBuilder {

  def build(routeInfo: RouteInfo): Seq[String] = {
    val basicLabels = buildBasicLabels(routeInfo)
    val factLabels = routeInfo.facts.map(fact => s"fact-${fact.name}")
    val networkTypeLabels = Seq(s"network-type-${routeInfo.summary.networkType.name}")
    val locationLabels = routeInfo.analysis.locationAnalysis.locationNames.map(location => s"location-$location")
    basicLabels ++ factLabels ++ networkTypeLabels ++ locationLabels
  }

  private def buildBasicLabels(routeInfo: RouteInfo): Seq[String] = {
    Seq(
      if (routeInfo.active) Some("active") else None,
      if (routeInfo.orphan) Some("orphan") else None,
      if (routeInfo.lastSurvey.isDefined) Some("survey") else None,
      if (routeInfo.facts.nonEmpty) Some("facts") else None,
      if (routeInfo.summary.isBroken) Some("broken") else None,
    ).flatten
  }
}
