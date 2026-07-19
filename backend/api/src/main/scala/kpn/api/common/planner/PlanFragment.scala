package kpn.api.common.planner

import kpn.api.common.LatLonImpl

case class PlanFragment(
  meters: Long,
  coordinate: PlanCoordinate,
  latLon: LatLonImpl
)
