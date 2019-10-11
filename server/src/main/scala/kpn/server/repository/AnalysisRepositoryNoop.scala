package kpn.server.repository

import kpn.core.analysis.Network
import kpn.shared.NodeInfo
import kpn.shared.Timestamp
import kpn.shared.network.NetworkInfo
import kpn.shared.route.RouteInfo

class AnalysisRepositoryNoop extends AnalysisRepository {

  override def saveNetwork(network: Network): Unit = {}

  override def saveIgnoredNetwork(network: NetworkInfo): Unit = {}

  override def saveRoute(route: RouteInfo): Unit = {}

  override def saveNode(node: NodeInfo): Unit = {}

  override def lastUpdated(): Option[Timestamp] = None

  override def saveLastUpdated(timestamp: Timestamp): Unit = {}

}
