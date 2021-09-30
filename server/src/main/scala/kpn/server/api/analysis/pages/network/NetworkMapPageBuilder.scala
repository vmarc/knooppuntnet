package kpn.server.api.analysis.pages.network

import kpn.api.common.Bounds
import kpn.api.common.network.NetworkMapNode
import kpn.api.common.network.NetworkMapPage
import kpn.core.doc.NetworkInfoDoc
import kpn.server.repository.NetworkInfoRepository
import org.springframework.stereotype.Component

@Component
class NetworkMapPageBuilder(
  networkInfoRepository: NetworkInfoRepository
) {

  def build(networkId: Long): Option[NetworkMapPage] = {
    if (networkId == 1) {
      Some(NetworkMapPageExample.page)
    }
    else {
      buildPage(networkId)
    }
  }

  private def buildPage(networkId: Long): Option[NetworkMapPage] = {
    networkInfoRepository.findById(networkId).map(buildPageContents)
  }

  private def buildPageContents(networkInfo: NetworkInfoDoc): NetworkMapPage = {

    val networkNodeInfos = networkInfo.nodes
    val bounds = Bounds.from(networkNodeInfos)

    val nodes = networkNodeInfos.map { networkNodeInfo =>
      NetworkMapNode(
        networkNodeInfo.id,
        networkNodeInfo.name,
        networkNodeInfo.latitude,
        networkNodeInfo.longitude,
        networkNodeInfo.roleConnection
      )
    }

    NetworkMapPage(
      networkInfo.summary,
      nodes,
      networkInfo.nodes.map(_.id),
      networkInfo.routes.map(_.id),
      bounds
    )
  }
}
