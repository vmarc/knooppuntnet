package kpn.shared.planner

case class RouteLegFragment(
  lat: String,
  lon: String,
  meters: Int,
  orientation: Int,
  streetIndex: Option[Int]
)
