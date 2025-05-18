package kpn.api.common.changes.details

import kpn.api.base.WithStringId
import kpn.api.common.ChangeType
import kpn.api.common.data.raw.RawWay
import kpn.api.common.diff.WayUpdate

case class BaseRouteChange(
  _id: String,
  key: ChangeKey,
  changeType: ChangeType,
  removedWays: Seq[RawWay],
  addedWays: Seq[RawWay],
  updatedWays: Seq[WayUpdate],
) extends WithStringId {

  def id: Long = key.elementId
}
