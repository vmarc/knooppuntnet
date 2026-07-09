package kpn.core.history

import kpn.api.common.LatLonImpl
import kpn.api.common.data.Node
import kpn.api.common.diff.node.NodeMoved
import kpn.core.util.Haversine

class NodeMovedAnalyzer(before: Node, after: Node) {
  def analysis: Option[NodeMoved] = {
    Option.when(before.latitude != after.latitude || before.longitude != after.longitude) {
      val latLonBefore = LatLonImpl(before.latitude, before.longitude)
      val latLonAfter = LatLonImpl(after.latitude, after.longitude)
      val distance = Haversine.meters(Seq(before, after))
      NodeMoved(latLonBefore, latLonAfter, distance)
    }
  }
}
