package kpn.core.overpass

import kpn.shared.NetworkType

case class QueryRouteIds(networkType: NetworkType) extends OverpassQuery {

  def name: String = s"route-ids-${networkType.name}"

  def string: String = {
    s"relation['type'='route']['network:type'='node_network']['network'='${networkType.networkTagValue}'];out ids;"
  }
}
