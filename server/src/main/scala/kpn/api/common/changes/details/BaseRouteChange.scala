package kpn.api.common.changes.details

import kpn.api.base.WithStringId
import kpn.api.common.Bounds
import kpn.api.common.ChangeType
import kpn.api.common.diff.WayDiffsInfo
import kpn.api.common.route.GeometryDiff

case class BaseRouteChange(
  _id: String,
  key: ChangeKey,
  changeType: ChangeType,
  wayDiffs: WayDiffsInfo,
  geometryDiff: GeometryDiff,
  bounds: Option[Bounds],
) extends WithStringId {

  def routeId: Long = key.elementId
}
