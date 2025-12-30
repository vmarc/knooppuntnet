package kpn.api.common.route

case class WayGeometryUpdate(
  wayId: Long,
  common: Option[Seq[String]], // blue
  before: Option[Seq[String]], // red
  after: Option[Seq[String]] // green
)
