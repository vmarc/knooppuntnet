package kpn.core.history

import kpn.api.common.LatLonImpl
import kpn.api.common.data.raw.RawNode
import kpn.api.common.diff.node.NodeMoved
import kpn.core.util.Haversine

class NodeMovedAnalyzer(before: RawNode, after: RawNode) {
  def analysis: Option[NodeMoved] = {
    if (before.latitude != after.latitude || before.longitude != after.longitude) {
      val latLonBefore = LatLonImpl(before.latitude, before.longitude)
      val latLonAfter = LatLonImpl(after.latitude, after.longitude)
      val distance = Haversine.meters(Seq(before, after))
      Some(NodeMoved(latLonBefore, latLonAfter, distance))
    }
    else {
      None
    }
  }
}
