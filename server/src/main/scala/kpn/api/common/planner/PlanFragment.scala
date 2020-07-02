package kpn.api.common.planner

import kpn.api.common.LatLonImpl
import kpn.api.common.common.ToStringBuilder

case class PlanFragment(
  meters: Long,
  orientation: Long,
  streetIndex: Option[Long],
  coordinate: PlanCoordinate,
  latLon: LatLonImpl
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("meters", meters).
    field("orientation", orientation).
    field("streetIndex", streetIndex).
    field("coordinate", coordinate).
    field("latLon", latLon).
    build

}
