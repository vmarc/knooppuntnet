package kpn.core.facade.pages

import kpn.core.db.couch.Couch
import kpn.core.repository.NetworkRepository
import kpn.core.util.NaturalSorting
import kpn.shared.network.NetworkNodesPage

class NetworkNodesPageBuilderImpl(
  networkRepository: NetworkRepository
) extends NetworkNodesPageBuilder {

  override def build(networkId: Long): Option[NetworkNodesPage] = {
    networkRepository.network(networkId, Couch.uiTimeout).map { networkInfo =>
      val nodes = networkInfo.detail match {
        case Some(detail) => NaturalSorting.sortBy(detail.nodes)(_.name)
        case None => Seq()
      }
      NetworkNodesPage(
        TimeInfoBuilder.timeInfo,
        NetworkSummaryBuilder.toSummary(networkInfo),
        networkInfo.attributes.networkType,
        nodes,
        networkInfo.routeRefs
      )
    }
  }
}
