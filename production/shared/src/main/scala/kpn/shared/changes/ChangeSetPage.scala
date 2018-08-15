package kpn.shared.changes

import kpn.shared.ChangeSetSummary
import kpn.shared.changes.details.NetworkChangeInfo
import kpn.shared.common.KnownElements
import kpn.shared.node.NodeChangeInfo
import kpn.shared.route.RouteChangeInfo

case class ChangeSetPage(
  summary: ChangeSetSummary,
  changeSetInfo: Option[ChangeSetInfo],
  networkChanges: Seq[NetworkChangeInfo],
  routeChanges: Seq[RouteChangeInfo],
  nodeChanges: Seq[NodeChangeInfo],
  knownElements: KnownElements,
  reviews: Seq[Review]
)
