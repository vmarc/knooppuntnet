package kpn.client.components.network.map

import kpn.client.common.map.vector.MapState
import kpn.shared.network.NetworkNodeInfo2

class NetworkMapState {
  val state: MapState = new MapState()
  var nodes: Seq[NetworkNodeInfo2] = Seq()
  var nodeIds: Seq[String] = Seq()
  var routeIds: Seq[String] = Seq()
}
