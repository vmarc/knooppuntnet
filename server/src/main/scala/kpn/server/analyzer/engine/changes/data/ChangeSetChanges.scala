package kpn.server.analyzer.engine.changes.data

import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RouteChange

case class ChangeSetChanges(
  networkChanges: Seq[NetworkChange] = Seq.empty,
  routeChanges: Seq[RouteChange] = Seq.empty,
  nodeChanges: Seq[NodeChange] = Seq.empty
) {

  def isEmpty: Boolean = networkChanges.isEmpty && routeChanges.isEmpty && nodeChanges.isEmpty

  def nonEmpty: Boolean = networkChanges.nonEmpty || routeChanges.nonEmpty || nodeChanges.nonEmpty

  def size: Int = networkChanges.size + routeChanges.size + nodeChanges.size
}
