package kpn.api.common.planner

import kpn.api.common.LatLonImpl

case class PlanFragmentCoordinate(
  coordinate: PlanCoordinate,
  latLon: LatLonImpl
)
