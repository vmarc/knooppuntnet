package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder

case class RouteLegSegment(
  meters: Long,
  surface: String,
  colour: Option[String],
  fragments: Seq[RouteLegFragment]
) {

  def reverse: RouteLegSegment = {
    RouteLegSegment(
      meters,
      surface,
      colour,
      fragments.reverse
    )
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("meters", meters).
    field("surface", surface).
    field("colour", colour).
    field("fragments", fragments).
    build

}
