package kpn.api.common.planner

case class RouteLegFragment(
  lat: String,
  lon: String,
  meters: Int,
  orientation: Int,
  streetIndex: Option[Int]
)
