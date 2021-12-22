package kpn.api.common.changes

import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.LocationTreeItem
import kpn.api.common.changes.details.NetworkChangeInfo
import kpn.api.common.common.KnownElements
import kpn.api.common.node.NodeChangeInfo
import kpn.api.common.route.RouteChangeInfo

case class ChangeSetPage(
  summary: ChangeSetSummary,
  changeSetInfo: Option[ChangeSetInfo],
  networkChanges: Seq[NetworkChangeInfo],
  orphanRouteChanges: Seq[ChangeSetSubsetElementRefs],
  orphanNodeChanges: Seq[ChangeSetSubsetElementRefs],
  routeChanges: Seq[RouteChangeInfo],
  nodeChanges: Seq[NodeChangeInfo],
  knownElements: KnownElements,
  treeItems: Seq[LocationTreeItem]
)
