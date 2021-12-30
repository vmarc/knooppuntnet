package kpn.api.common.route

import kpn.api.common.Bounds
import kpn.api.common.changes.ChangeSetInfo
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.data.MetaData
import kpn.api.common.data.raw.RawNode
import kpn.api.common.diff.WayInfo
import kpn.api.common.diff.WayUpdate
import kpn.api.common.diff.route.RouteDiff

case class RouteChangeInfo(
  rowIndex: Long,
  id: Long,
  version: Long,
  changeKey: ChangeKey,
  comment: Option[String],
  before: Option[MetaData],
  after: Option[MetaData],
  removedWays: Seq[WayInfo],
  addedWays: Seq[WayInfo],
  updatedWays: Seq[WayUpdate],
  diffs: RouteDiff,
  nodes: Seq[RawNode],
  changeSetInfo: Option[ChangeSetInfo],
  geometryDiff: Option[GeometryDiff],
  bounds: Bounds,
  happy: Boolean,
  investigate: Boolean
)
