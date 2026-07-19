package kpn.api.common.route

import kpn.api.common.ChangeType
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.data.MetaData
import kpn.api.common.diff.WayDiffsInfo
import kpn.api.common.diff.route.RouteDiff

case class RouteChangeInfo(
  rowIndex: Long,
  id: Long,
  version: Long,
  changeKey: ChangeKey,
  changeType: ChangeType,
  comment: Option[String],
  before: Option[MetaData],
  after: Option[MetaData],
  diffs: Option[RouteDiff],
  nodes: Seq[RouteNode],
  nodeChanges: Seq[RouteNodeChange],
  changeSetInfo: Option[ChangeSetInfo],
  wayDiffs: Option[WayDiffsInfo],
  geometryDiff: Option[GeometryDiff],
  happy: Boolean,
  investigate: Boolean
)
