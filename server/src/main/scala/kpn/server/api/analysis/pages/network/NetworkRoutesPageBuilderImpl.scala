package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkRouteRow
import kpn.api.common.network.NetworkRoutesPage
import kpn.api.custom.Fact
import kpn.core.db.couch.Couch
import kpn.core.util.NaturalSorting
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class NetworkRoutesPageBuilderImpl(
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository
) extends NetworkRoutesPageBuilder {

  override def build(networkId: Long): Option[NetworkRoutesPage] = {
    if (networkId == 1) {
      Some(NetworkRoutesPageExample.page)
    }
    else {
      buildPage(networkId)
    }
  }

  private def buildPage(networkId: Long): Option[NetworkRoutesPage] = {
    networkRepository.network(networkId).map(buildPageContents)
  }

  private def buildPageContents(networkInfo: NetworkInfo): NetworkRoutesPage = {

    val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)

    val detailRoutes = networkInfo.detail match {
      case Some(detail) => NaturalSorting.sortBy(detail.routes)(_.name)
      case None => Seq()
    }

    val routes = detailRoutes.map { route =>
      NetworkRouteRow(
        route.id,
        route.name,
        route.length,
        route.role,
        investigate = route.facts.contains(Fact.RouteBroken),
        accessible = !route.facts.contains(Fact.RouteUnaccessible),
        roleConnection = route.role.contains("connection"),
        lastUpdated = route.lastUpdated,
        lastSurvey = route.lastSurvey
      )
    }

    NetworkRoutesPage(
      TimeInfoBuilder.timeInfo,
      networkInfo.attributes.networkType,
      NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
      routes
    )
  }
}
