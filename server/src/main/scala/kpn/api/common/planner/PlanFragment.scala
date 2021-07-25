package kpn.api.common.planner

import kpn.api.common.LatLonImpl

case class PlanFragment(
  meters: Long,
  orientation: Long,
  streetIndex: Option[Long],
  coordinate: PlanCoordinate,
  latLon: LatLonImpl
) {
}
