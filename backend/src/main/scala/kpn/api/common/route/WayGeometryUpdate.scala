package kpn.api.common.route

case class WayGeometryUpdate(
  wayId: Long,
  common: Option[Seq[WayLine]], // blue
  added: Option[Seq[WayLine]], // green
  removed: Option[Seq[WayLine]], // red
)
