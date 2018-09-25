package kpn.shared.node

case class NodeReferences(
  networkReferences: Seq[NodeNetworkReference]= Seq.empty,
  routeReferences: Seq[NodeOrphanRouteReference]= Seq.empty
) {
  def isEmpty: Boolean = networkReferences.isEmpty && routeReferences.isEmpty
}
