package kpn.core.overpass

import kpn.api.common.ScopedNetworkType

case class QueryRouteIds(scopedNetworkType: ScopedNetworkType) extends OverpassQuery {

  def name: String = s"route-ids-${scopedNetworkType.key}"

  def string: String = {
    s"relation['type'='route']['network:type'='node_network']['network'='${scopedNetworkType.key}'];out ids;"
  }
}
