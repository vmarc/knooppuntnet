package kpn.core.engine.changes.data

import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RouteChange

case class ChangeSetChanges(
  networkChanges: Seq[NetworkChange] = Seq(),
  routeChanges: Seq[RouteChange] = Seq(),
  nodeChanges: Seq[NodeChange] = Seq()
) {

  def isEmpty: Boolean = networkChanges.isEmpty && routeChanges.isEmpty && nodeChanges.isEmpty

  def nonEmpty: Boolean = networkChanges.nonEmpty || routeChanges.nonEmpty || nodeChanges.nonEmpty

  def size: Int = networkChanges.size + routeChanges.size + nodeChanges.size
}
