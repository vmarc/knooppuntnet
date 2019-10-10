package kpn.core.overpass

import kpn.shared.NetworkType

case class QueryNetworkIds(networkType: NetworkType) extends OverpassQuery {

  def name: String = s"network-ids-${networkType.name}"

  def string: String = {
    s"relation['type'='network']['network:type'='node_network']['network'='${networkType.networkTagValue}'];out ids;"
  }
}
