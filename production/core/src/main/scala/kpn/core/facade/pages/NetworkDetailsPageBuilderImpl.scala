package kpn.core.facade.pages

import kpn.core.db.couch.Couch
import kpn.core.repository.NetworkRepository
import kpn.shared.NetworkFacts
import kpn.shared.network.NetworkDetailsPage

class NetworkDetailsPageBuilderImpl(
  networkRepository: NetworkRepository
) extends NetworkDetailsPageBuilder {

  def build(networkId: Long): Option[NetworkDetailsPage] = {
    networkRepository.network(networkId, Couch.uiTimeout).map { networkInfo =>
      NetworkDetailsPage(
        NetworkSummaryBuilder.toSummary(networkInfo),
        networkInfo.active,
        networkInfo.ignored,
        networkInfo.attributes,
        networkInfo.tags,
        networkInfo.detail.map(_.networkFacts).getOrElse(NetworkFacts())
      )
    }
  }
}
