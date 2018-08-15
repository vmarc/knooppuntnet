package kpn.core.facade.pages

import kpn.core.db.couch.Couch
import kpn.core.repository.NetworkRepository
import kpn.shared.network.NetworkMapPage

class NetworkMapPageBuilderImpl(
  networkRepository: NetworkRepository
) extends NetworkMapPageBuilder {

  def build(networkId: Long): Option[NetworkMapPage] = {
    networkRepository.network(networkId, Couch.uiTimeout).map { networkInfo =>
      val nodes = networkInfo.detail.toSeq.flatMap(_.nodes)
      NetworkMapPage(
        NetworkSummaryBuilder.toSummary(networkInfo),
        nodes,
        networkInfo.nodeRefs,
        networkInfo.routeRefs
      )
    }
  }
}
