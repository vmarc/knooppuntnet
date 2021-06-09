package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkRouteRow
import kpn.api.common.network.NetworkRoutesPage
import kpn.api.custom.Fact
import kpn.core.util.NaturalSorting
import kpn.server.api.analysis.pages.SurveyDateInfoBuilder
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.MongoNetworkRepository
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class NetworkRoutesPageBuilderImpl(
  // old
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository,
  // new
  mongoEnabled: Boolean,
  mongoNetworkRepository: MongoNetworkRepository
) extends NetworkRoutesPageBuilder {

  override def build(networkId: Long): Option[NetworkRoutesPage] = {
    if (networkId == 1) {
      Some(NetworkRoutesPageExample.page)
    }
    else {
      if (mongoEnabled) {
        mongoBuildPage(networkId)
      }
      else {
        oldBuildPage(networkId)
      }
    }
  }

  private def mongoBuildPage(networkId: Long): Option[NetworkRoutesPage] = {
    mongoNetworkRepository.networkWithId(networkId).map(mongoBuildPageContents)
  }

  private def mongoBuildPageContents(networkInfo: NetworkInfo): NetworkRoutesPage = {

    val changeCount = mongoNetworkRepository.networkChangeCount(networkInfo.attributes.id)

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
      SurveyDateInfoBuilder.dateInfo,
      networkInfo.attributes.networkType,
      NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
      routes
    )
  }

  private def oldBuildPage(networkId: Long): Option[NetworkRoutesPage] = {
    networkRepository.network(networkId).map(oldBuildPageContents)
  }

  private def oldBuildPageContents(networkInfo: NetworkInfo): NetworkRoutesPage = {

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
      SurveyDateInfoBuilder.dateInfo,
      networkInfo.attributes.networkType,
      NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
      routes
    )
  }
}
