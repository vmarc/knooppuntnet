package kpn.server.analyzer.engine.analysis.network.info

import kpn.api.common.network.NetworkDetail
import kpn.api.common.network.NetworkRouteRow
import kpn.api.common.network.NetworkSummary
import kpn.api.custom.Fact
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.core.util.NaturalSorting
import kpn.server.analyzer.engine.analysis.network.info.domain.NetworkInfoAnalysisContext

class NetworkInfoDocBuilder(context: NetworkInfoAnalysisContext) {

  def build(): NetworkInfoDoc = {

    val summary = buildSummary()
    val detail = buildDetail()
    val routes = buildNetworkRoutes()

    NetworkInfoDoc(
      context.networkDoc._id,
      context.networkDoc.active,
      context.networkDoc.country,
      summary,
      detail,
      context.facts,
      context.nodeDetails,
      routes,
      context.nodeIds
    )
  }

  private def buildSummary(): NetworkSummary = {
    NetworkSummary(
      context.networkDoc.name,
      context.networkDoc.networkType,
      context.networkDoc.networkScope,
      context.facts.size,
      context.nodeDetails.size,
      context.routeDetails.size,
      context.changeCount,
      context.networkDoc.active
    )
  }

  private def buildDetail(): NetworkDetail = {
    NetworkDetail(
      context.km,
      context.meters,
      context.networkDoc.lastUpdated,
      context.networkDoc.relationLastUpdated,
      context.lastSurvey,
      context.networkDoc.tags,
      context.brokenRouteCount,
      context.brokenRoutePercentage,
      context.integrity,
      context.unaccessibleRouteCount,
      context.connectionCount,
      context.center
    )
  }

  private def buildNetworkRoutes(): Seq[NetworkRouteRow] = {
    val sortedRouteDetails = NaturalSorting.sortBy(context.routeDetails)(_.name)
    sortedRouteDetails.map { routeDetail =>
      val role = context.networkDoc.routeRefs.find(_.routeId == routeDetail.id).flatMap(_.role)
      NetworkRouteRow(
        routeDetail.id,
        routeDetail.name,
        routeDetail.length,
        role,
        investigate = routeDetail.facts.contains(Fact.RouteBroken),
        accessible = !routeDetail.facts.contains(Fact.RouteUnaccessible),
        roleConnection = role.contains("connection"),
        proposed = routeDetail.proposed,
        lastUpdated = routeDetail.lastUpdated,
        lastSurvey = routeDetail.lastSurvey
      )
    }
  }
}
