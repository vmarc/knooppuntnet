package kpn.shared.route

import kpn.shared.Bounds
import kpn.shared.changes.ChangeSetInfo
import kpn.shared.changes.details.ChangeKey
import kpn.shared.data.MetaData
import kpn.shared.data.raw.RawNode
import kpn.shared.diff.WayInfo
import kpn.shared.diff.WayUpdate
import kpn.shared.diff.route.RouteDiff

case class RouteChangeInfo(
  id: Long,
  version: Int,
  changeKey: ChangeKey,
  comment: Option[String],
  before: Option[MetaData],
  after: Option[MetaData],

  removedWays: Seq[WayInfo],
  addedWays: Seq[WayInfo],
  updatedWays: Seq[WayUpdate],

  diffs: RouteDiff,
  nodes: Seq[RawNode],
  changeSetInfo: Option[ChangeSetInfo] = None,
  addedNodes: Seq[Long] = Seq(),
  deletedNodes: Seq[Long] = Seq(),
  commonNodes: Seq[Long] = Seq(),
  addedWayIds: Seq[Long] = Seq(),
  deletedWayIds: Seq[Long] = Seq(),
  commonWayIds: Seq[Long] = Seq(),
  geometryDiff: Option[GeometryDiff] = None,
  bounds: Bounds = Bounds(),
  happy: Boolean = false,
  investigate: Boolean = false
)
