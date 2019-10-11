package kpn.server.repository

import kpn.core.analysis.Network
import kpn.shared.NodeInfo
import kpn.shared.Timestamp
import kpn.shared.network.NetworkInfo
import kpn.shared.route.RouteInfo

trait AnalysisRepository {

  def saveNetwork(network: Network): Unit

  def saveIgnoredNetwork(network: NetworkInfo): Unit

  def saveRoute(route: RouteInfo): Unit

  def saveNode(node: NodeInfo): Unit

  /*
    Returns the time of the most recent minute diff that was processed by the analyzer. This provides
    an indication of how up-to-date the information in the analysis database is.
   */
  def lastUpdated(): Option[Timestamp]

  def saveLastUpdated(timestamp: Timestamp): Unit

}
