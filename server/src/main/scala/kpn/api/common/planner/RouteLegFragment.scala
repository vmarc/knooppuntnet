package kpn.api.common.planner

case class RouteLegFragment(
  lat: String,
  lon: String,
  meters: Long,
  orientation: Long,
  streetIndex: Option[Long]
)
