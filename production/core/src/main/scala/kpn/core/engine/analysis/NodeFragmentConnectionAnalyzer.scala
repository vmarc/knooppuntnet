package kpn.core.engine.analysis

import kpn.core.engine.analysis.route.segment.Fragment
import kpn.core.engine.analysis.route.segment.SegmentDirection
import kpn.shared.NetworkType
import kpn.shared.data.Node
import kpn.shared.route.Backward
import kpn.shared.route.Forward

/**
  * Determines whether given node can be connected to given fragment when traveling in given direction.
  *
  * If the fragment we try to connect to can be traveled in both directions, then the node can be connected to
  * the fragment if that node is the same as either the start or the end node of the fragment.
  */
class NodeFragmentConnectionAnalyzer(networkType: NetworkType, direction: SegmentDirection.Value, node: Node, fragment: Fragment) {

  def canConnect: Boolean = {

    val startNode = fragment.nodes.head
    val endNode = fragment.nodes.last

    if (isBicycle && isOneWay) {
      node == startNode
    }
    else if (isBicycle && isOneWayReversed) {
      node == endNode
    }
    else {
      if (direction == SegmentDirection.Both) {
        node == startNode || node == endNode
      }
      else {
        fragment.role match {
          case Some("forward") =>
            node == startNode
          case Some("backward") =>
            node == endNode
          case _ =>
            node == startNode || node == endNode
        }
      }
    }
  }

  private def isBicycle: Boolean = networkType == NetworkType.bicycle

  private def isOneWay: Boolean = new OneWayAnalyzer(fragment.way).direction == Forward

  private def isOneWayReversed: Boolean = new OneWayAnalyzer(fragment.way).direction == Backward
}
