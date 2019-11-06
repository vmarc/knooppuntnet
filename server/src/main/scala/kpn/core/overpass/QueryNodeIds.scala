package kpn.core.overpass

import kpn.shared.ScopedNetworkType

case class QueryNodeIds(scopedNetworkType: ScopedNetworkType) extends OverpassQuery {

  def name: String = s"node-ids-${scopedNetworkType.key}"

  def string: String = {
    s"node['network:type'='node_network']['${scopedNetworkType.key}'];out ids;"
  }
}
