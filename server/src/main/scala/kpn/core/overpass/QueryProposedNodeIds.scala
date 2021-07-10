package kpn.core.overpass

import kpn.api.custom.ScopedNetworkType

case class QueryProposedNodeIds(scopedNetworkType: ScopedNetworkType) extends OverpassQuery {

  def name: String = s"proposed-node-ids-${scopedNetworkType.key}"

  def string: String = {
    s"node['network:type'='node_network']['proposed:${scopedNetworkType.key}'];out ids;"
  }
}
