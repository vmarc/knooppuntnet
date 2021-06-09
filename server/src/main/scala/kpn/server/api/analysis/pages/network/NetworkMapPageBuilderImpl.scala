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
class NetworkMapPageBuilderImpl(
  // old
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository,
  // new
  mongoEnabled: Boolean,
  mongoNetworkRepository: MongoNetworkRepository
) extends NetworkMapPageBuilder {

  def build(networkId: Long): Option[NetworkMapPage] = {
    if (networkId == 1) {
      Some(NetworkMapPageExample.page)
    }
    else {
      if (mongoEnabled) {
        mongoBuildPage(networkId)
      }
      else {
        oldBuildPage(networkId)
      }
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

  private def oldBuildPage(networkId: Long): Option[NetworkMapPage] = {
    networkRepository.network(networkId).map(oldBuildPageContents)
  }

  private def oldBuildPageContents(networkInfo: NetworkInfo): NetworkMapPage = {

    val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)
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
