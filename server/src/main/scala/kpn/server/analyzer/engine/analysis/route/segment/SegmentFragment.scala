package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.data.Node

/**
  * A route fragment with an indication of the direction that the route travels through the fragment way.
  *
  * @param fragment the route fragment
  * @param reversed true if the route direction is the opposite of the fragment way direction
  */
case class SegmentFragment(fragment: Fragment, reversed: Boolean = false) {

  override def toString: String = {
    val name = getClass.getSimpleName
    val fragments = new SegmentFragmentFormatter(this).string
    s"$name($fragments)"
  }

  def startNode: Node = {
    if (reversed) {
      fragment.nodes.last
    }
    else {
      fragment.nodes.head
    }
  }

  def endNode: Node = {
    if (reversed) {
      fragment.nodes.head
    }
    else {
      fragment.nodes.last
    }
  }

  def nodes: Seq[Node] = if (reversed) fragment.nodes.reverse else fragment.nodes
}
