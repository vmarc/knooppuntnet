package kpn.server.api.analysis.pages.network

import kpn.api.common.NetworkFacts
import kpn.api.common.network.NetworkDetailsPage
import kpn.api.common.network.NetworkInfo
import kpn.core.db.couch.Couch
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class NetworkDetailsPageBuilderImpl(
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository
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
    val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)
    NetworkDetailsPage(
      NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
      networkInfo.active,
      networkInfo.attributes,
      networkInfo.tags,
      networkInfo.detail.map(_.networkFacts).getOrElse(NetworkFacts())
    )
  }

}
