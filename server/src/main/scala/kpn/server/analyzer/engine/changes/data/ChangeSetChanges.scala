package kpn.server.analyzer.engine.changes.data

import kpn.api.common.changes.details.BaseRouteChange
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange

case class ChangeSetChanges(
  baseRouteChanges: Seq[BaseRouteChange] = Seq.empty,
  networkChanges: Seq[NetworkChange] = Seq.empty,
  routeChanges: Seq[RouteChange] = Seq.empty,
  nodeChanges: Seq[NodeChange] = Seq.empty
) {

  def isEmpty: Boolean = {
    baseRouteChanges.isEmpty && networkChanges.isEmpty && routeChanges.isEmpty && nodeChanges.isEmpty
  }

  def nonEmpty: Boolean = {
    baseRouteChanges.nonEmpty || networkChanges.nonEmpty || routeChanges.nonEmpty || nodeChanges.nonEmpty
  }
}
