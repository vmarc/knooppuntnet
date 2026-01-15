package kpn.server.api.analysis.pages.network

import kpn.api.common.Bounds
import kpn.api.common.network.NetworkMapNode
import kpn.api.common.network.NetworkMapPage
import kpn.core.doc.NetworkDoc
import kpn.core.doc.NetworkInfoNodeDetail
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class NetworkMapPageBuilder(
  networkRepository: NetworkRepository
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
    networkRepository.findById(networkId).map(buildPageContents)
  }

  private def buildPageContents(network: NetworkDoc): NetworkMapPage = {

    val networkNodeInfos = network.nodes.filter(node => node.definedInRelation)
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
    val networkRouteIds = network.routes.filterNot(_.roleConnection).map(_.id)
    val connectionRouteIds = network.routes.filter(_.roleConnection).map(_.id)

    NetworkMapPage(
      null,
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
