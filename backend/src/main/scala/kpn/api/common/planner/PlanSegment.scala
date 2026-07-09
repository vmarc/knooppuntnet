package kpn.api.common.planner

case class PlanSegment(
  meters: Long,
  surface: String,
  colour: Option[String],
  fragments: Seq[PlanFragment]
) {
}
