package kpn.api.common.changes.details

import kpn.api.base.WithStringId
import kpn.api.common.ChangeType
import kpn.api.common.diff.WayDiffs

case class BaseRouteChange(
  _id: String,
  key: ChangeKey,
  changeType: ChangeType,
  wayDiffs: WayDiffs,
) extends WithStringId {

  def routeId: Long = key.elementId
}
