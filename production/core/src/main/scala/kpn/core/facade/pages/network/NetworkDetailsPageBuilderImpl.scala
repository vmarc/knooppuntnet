package kpn.core.facade.pages.network

import kpn.core.db.couch.Couch
import kpn.core.repository.NetworkRepository
import kpn.shared.NetworkFacts
import kpn.shared.network.NetworkDetailsPage
import kpn.shared.network.NetworkInfo

class NetworkDetailsPageBuilderImpl(
  networkRepository: NetworkRepository
) extends NetworkDetailsPageBuilder {

  def build(networkId: Long): Option[NetworkDetailsPage] = {
    if (networkId == 1) {
      Some(NetworkDetailsPageExample.page)
    }
    else {
      buildPage(networkId)
    }
  }

  private def buildPage(networkId: Long): Option[NetworkDetailsPage] = {
    networkRepository.network(networkId, Couch.uiTimeout).map(buildPageContents)
  }

  private def buildPageContents(networkInfo: NetworkInfo): NetworkDetailsPage = {
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
