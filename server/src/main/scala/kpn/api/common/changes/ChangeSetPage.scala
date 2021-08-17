package kpn.api.common.changes

import kpn.api.common.ChangeSetSummary
import kpn.api.common.LocationChangesTree
import kpn.api.common.changes.details.NetworkChangeInfo
import kpn.api.common.common.KnownElements
import kpn.api.common.node.NodeChangeInfo
import kpn.api.common.route.RouteChangeInfo

case class ChangeSetPage(
  summary: ChangeSetSummary,
  changeSetInfo: Option[ChangeSetInfo],
  trees: Seq[LocationChangesTree],
  networkChanges: Seq[NetworkChangeInfo],
  routeChanges: Seq[RouteChangeInfo],
  nodeChanges: Seq[NodeChangeInfo],
  knownElements: KnownElements
)
