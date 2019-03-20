package kpn.core.planner.plan

import kpn.core.directions.Latlon

case class PlanLegFragment(
  sink: Latlon,
  meters: Int,
  orientation: Double,
  streetName: Option[String]
)
