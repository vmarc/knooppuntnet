package kpn.shared.node

import kpn.shared.common.Reference

case class NodeReferences(
  networkReferences: Seq[Reference] = Seq(),
  routeReferences: Seq[Reference] = Seq()
) {
  def isEmpty: Boolean = networkReferences.isEmpty && routeReferences.isEmpty
}
