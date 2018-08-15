package kpn.core.facade.pages

import kpn.core.db.couch.Couch
import kpn.core.repository.NetworkRepository
import kpn.core.util.NaturalSorting
import kpn.shared.network.NetworkRoutesPage

class NetworkRoutesPageBuilderImpl(
  networkRepository: NetworkRepository
) extends NetworkRoutesPageBuilder {

  override def build(networkId: Long): Option[NetworkRoutesPage] = {
    networkRepository.network(networkId, Couch.uiTimeout).map { networkInfo =>
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
}
