package kpn.server.repository

import kpn.api.common.NodeInfo
import kpn.api.common.network.NetworkInfo
import kpn.api.common.route.RouteInfo
import kpn.api.custom.Timestamp
import kpn.core.analysis.Network

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
