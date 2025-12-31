package kpn.api.common.route

case class WayGeometryUpdate(
  wayId: Long,
  common: Option[Seq[String]], // blue
  added: Option[Seq[String]], // green
  removed: Option[Seq[String]], // red
)
