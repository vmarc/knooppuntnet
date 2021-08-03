package kpn.server.api.analysis.pages.network

import kpn.api.common.Bounds
import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkMapNode
import kpn.api.common.network.NetworkMapPage
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.MongoNetworkRepository
import kpn.server.repository.NetworkRepository
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

  private def mongoBuildPageContents(networkInfo: NetworkInfo): NetworkMapPage = {

    val changeCount = mongoNetworkRepository.networkChangeCount(networkInfo.attributes.id)
    val networkNodeInfos = networkInfo.detail.toSeq.flatMap(_.nodes)
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
      NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
      nodes,
      networkInfo.nodeRefs,
      networkInfo.routeRefs,
      bounds
    )
  }
}
