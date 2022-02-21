package kpn.server.analyzer.engine.changes.data

import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.server.analyzer.engine.changes.network.NetworkChange

case class ChangeSetChanges(
  networkChanges: Seq[NetworkChange] = Seq.empty,
  networkInfoChanges: Seq[NetworkInfoChange] = Seq.empty,
  routeChanges: Seq[RouteChange] = Seq.empty,
  nodeChanges: Seq[NodeChange] = Seq.empty
) {

  def isEmpty: Boolean = {
    networkChanges.isEmpty &&
      networkInfoChanges.isEmpty &&
      routeChanges.isEmpty &&
      nodeChanges.isEmpty
  }

  def nonEmpty: Boolean = {
    networkChanges.nonEmpty ||
      networkInfoChanges.nonEmpty ||
      routeChanges.nonEmpty ||
      nodeChanges.nonEmpty
  }

  def size: Int = {
    networkChanges.size +
      networkInfoChanges.size +
      routeChanges.size +
      nodeChanges.size
  }

  def tiles: Seq[String] = {
    (routeChanges.flatMap(_.tiles) ++ nodeChanges.flatMap(_.tiles)).distinct.sorted
  }
}
