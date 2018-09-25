package kpn.client.components.network.map

import kpn.client.common.map.vector.MapState
import kpn.shared.network.NetworkNodeInfo2

class NetworkMapState {
  val state: MapState = new MapState()
  var nodes: Seq[NetworkNodeInfo2] = Seq.empty
  var nodeIds: Seq[String] = Seq.empty
  var routeIds: Seq[String] = Seq.empty
}
