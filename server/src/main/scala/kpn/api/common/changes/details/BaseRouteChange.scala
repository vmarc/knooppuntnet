package kpn.api.common.changes.details

import kpn.api.common.ChangeType
import kpn.api.common.diff.WayDiffsInfo
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.route.GeometryDiff
import kpn.core.doc.WithStringId

case class BaseRouteChange(
  _id: String,
  key: ChangeKey,
  changeType: ChangeType,
  routeDiff: RouteDiff,
  wayDiffs: Option[WayDiffsInfo],
  geometryDiff: Option[GeometryDiff]
) extends WithStringId {

  def routeId: Long = key.elementId
}
