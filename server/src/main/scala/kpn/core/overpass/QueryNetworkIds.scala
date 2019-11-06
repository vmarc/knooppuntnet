package kpn.core.overpass

import kpn.shared.ScopedNetworkType

case class QueryNetworkIds(scopedNetworkType: ScopedNetworkType) extends OverpassQuery {

  def name: String = s"network-ids-${scopedNetworkType.key}"

  def string: String = {
    s"relation['type'='network']['network:type'='node_network']['network'='${scopedNetworkType.key}'];out ids;"
  }
}
