package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkMapPage
import kpn.core.db.couch.Couch
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class NetworkMapPageBuilderImpl(
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository
) extends NetworkMapPageBuilder {

  def build(networkId: Long): Option[NetworkMapPage] = {
    if (networkId == 1) {
      Some(NetworkMapPageExample.page)
    }
    else {
      buildPage(networkId)
    }
  }

  private def buildPage(networkId: Long): Option[NetworkMapPage] = {
    networkRepository.network(networkId, Couch.uiTimeout).map(buildPageContents)
  }

  private def buildPageContents(networkInfo: NetworkInfo): NetworkMapPage = {
    val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)
    val nodes = networkInfo.detail.toSeq.flatMap(_.nodes)
    NetworkMapPage(
      NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
      nodes,
      networkInfo.nodeRefs,
      networkInfo.routeRefs
    )
  }
}
