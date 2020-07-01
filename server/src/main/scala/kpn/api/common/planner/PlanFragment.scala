package kpn.api.common.planner

import kpn.api.common.LatLonImpl

case class PlanFragment(
  meters: Long,
  orientation: Long,
  streetIndex: Long,
  coordinate: Array[Double],
  latLon: LatLonImpl
)
