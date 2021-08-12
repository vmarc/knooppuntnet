package kpn.server.api.analysis.pages.network

import kpn.api.common.Bounds
import kpn.api.common.network.NetworkMapNode
import kpn.api.common.network.NetworkMapPage
import kpn.core.mongo.doc.NetworkInfoDoc
import kpn.server.repository.MongoNetworkRepository
import org.springframework.stereotype.Component

@Component
class NetworkMapPageBuilder(
  mongoNetworkRepository: MongoNetworkRepository
) {

  def build(networkId: Long): Option[NetworkMapPage] = {
    if (networkId == 1) {
      Some(NetworkMapPageExample.page)
    }
    else {
      mongoBuildPage(networkId)
    }
  }

  private def mongoBuildPage(networkId: Long): Option[NetworkMapPage] = {
    mongoNetworkRepository.networkWithId(networkId).map(mongoBuildPageContents)
  }

  private def mongoBuildPageContents(networkInfo: NetworkInfoDoc): NetworkMapPage = {

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
