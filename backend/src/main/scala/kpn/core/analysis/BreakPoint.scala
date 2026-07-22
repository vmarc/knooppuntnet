package kpn.core.analysis

import kpn.api.common.data.Node
import kpn.api.common.data.Way

case class BreakPoint(way: Way, node: Node) {
  override def toString: String = s"BreakPoint(way=${way.id},node=${node.id})"
}
