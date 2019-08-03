package kpn.core.facade.pages.network

import kpn.core.db.couch.Couch
import kpn.core.facade.pages.TimeInfoBuilder
import kpn.core.repository.NetworkRepository
import kpn.core.util.NaturalSorting
import kpn.shared.network.NetworkInfo
import kpn.shared.network.NetworkRoutesPage

class NetworkRoutesPageBuilderImpl(
  networkRepository: NetworkRepository
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
    networkRepository.network(networkId, Couch.uiTimeout).map(buildPageContents)
  }

  private def buildPageContents(networkInfo: NetworkInfo): NetworkRoutesPage = {
    val routes = networkInfo.detail match {
      case Some(detail) => NaturalSorting.sortBy(detail.routes)(_.name)
      case None => Seq()
    }
    NetworkRoutesPage(
      TimeInfoBuilder.timeInfo,
      networkInfo.attributes.networkType,
      NetworkSummaryBuilder.toSummary(networkInfo),
      routes
    )
  }
}
