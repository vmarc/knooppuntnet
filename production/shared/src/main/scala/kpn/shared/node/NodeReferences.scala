package kpn.shared.node

case class NodeReferences(
  networkReferences: Seq[NodeNetworkReference] = Seq(),
  routeReferences: Seq[NodeOrphanRouteReference] = Seq()
) {
  def isEmpty: Boolean = networkReferences.isEmpty && routeReferences.isEmpty
}
