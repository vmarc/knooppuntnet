package kpn.server.api.analysis.pages.network

import kpn.api.common.Bounds
import kpn.api.common.network.NetworkMapNode
import kpn.api.common.network.NetworkMapPage
import kpn.core.doc.NetworkInfoDoc
import kpn.core.doc.NetworkInfoNodeDetail
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

    val networkNodeInfos = networkInfo.nodes.filter(node => node.definedInRelation)
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

    val networkNodeIds = networkNodeInfos.filterNot(isConnection).map(_.id)
    val connectionNodeIds = networkNodeInfos.filter(isConnection).map(_.id)
    val networkRouteIds = networkInfo.routes.filterNot(_.roleConnection).map(_.id)
    val connectionRouteIds = networkInfo.routes.filter(_.roleConnection).map(_.id)

    NetworkMapPage(
      networkInfo.summary,
      nodes,
      networkNodeIds,
      connectionNodeIds,
      networkRouteIds,
      connectionRouteIds,
      bounds
    )
  }

  private def isConnection(node: NetworkInfoNodeDetail): Boolean = {
    node.roleConnection || node.connection
  }
}
