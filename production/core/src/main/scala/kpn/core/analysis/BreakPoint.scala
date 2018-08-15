package kpn.core.analysis

import kpn.shared.data.Node
import kpn.shared.data.Way

case class BreakPoint(way: Way, node: Node) {
  override def toString: String = s"BreakPoint(way=${way.id},node=${node.id})"
}
