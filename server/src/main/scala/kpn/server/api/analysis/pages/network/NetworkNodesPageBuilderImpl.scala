package kpn.server.api.analysis.pages.network

import kpn.api.common.network.NetworkInfo
import kpn.api.common.network.NetworkNodesPage
import kpn.core.util.NaturalSorting
import kpn.server.api.analysis.pages.TimeInfoBuilder
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.NetworkRepository
import org.springframework.stereotype.Component

@Component
class NetworkNodesPageBuilderImpl(
  networkRepository: NetworkRepository,
  changeSetRepository: ChangeSetRepository

) extends NetworkNodesPageBuilder {

  override def build(networkId: Long): Option[NetworkNodesPage] = {
    if (networkId == 1) {
      Some(NetworkNodesPageExample.nodesPage)
    }
    else {
      buildPage(networkId)
    }
  }

  private def buildPage(networkId: Long): Option[NetworkNodesPage] = {
    networkRepository.network(networkId).map(buildPageContents)
  }

  private def buildPageContents(networkInfo: NetworkInfo): NetworkNodesPage = {
    val changeCount = changeSetRepository.networkChangesCount(networkInfo.attributes.id)
    val nodes = networkInfo.detail match {
      case Some(detail) => NaturalSorting.sortBy(detail.nodes)(_.name)
      case None => Seq()
    }
    NetworkNodesPage(
      TimeInfoBuilder.timeInfo,
      NetworkSummaryBuilder.toSummary(networkInfo, changeCount),
      networkInfo.attributes.networkType,
      nodes,
      networkInfo.routeRefs
    )
  }

}
