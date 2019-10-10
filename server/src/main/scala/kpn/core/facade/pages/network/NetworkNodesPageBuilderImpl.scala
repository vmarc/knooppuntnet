package kpn.core.facade.pages.network

import kpn.core.db.couch.Couch
import kpn.core.facade.pages.TimeInfoBuilder
import kpn.core.repository.ChangeSetRepository
import kpn.core.repository.NetworkRepository
import kpn.core.util.NaturalSorting
import kpn.shared.network.NetworkInfo
import kpn.shared.network.NetworkNodesPage

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
    networkRepository.network(networkId, Couch.uiTimeout).map(buildPageContents)
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
