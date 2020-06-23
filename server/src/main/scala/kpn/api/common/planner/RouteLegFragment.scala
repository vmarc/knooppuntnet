package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder

case class RouteLegFragment(
  lat: String,
  lon: String,
  meters: Long,
  orientation: Long,
  streetIndex: Option[Long]
) {

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("lat", lat).
    field("lon", lon).
    field("meters", meters).
    field("orientation", orientation).
    field("streetIndex", streetIndex).
    build

}
