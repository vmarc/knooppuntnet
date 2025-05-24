package kpn.api.common.route

import kpn.core.util.CoordinateUtil

object GeometryDiff {
  def tmpMigrate(
    common: Seq[PointSegment] = Seq.empty,
    before: Seq[PointSegment] = Seq.empty,
    after: Seq[PointSegment] = Seq.empty
  ): GeometryDiff = {

    val commonString = tmpTo(common)
    val beforeString = tmpTo(before)
    val afterString = tmpTo(after)

    GeometryDiff(
      commonString,
      beforeString,
      afterString
    )
  }

  private def tmpTo(segments: Seq[PointSegment]): Seq[String] = {
    segments.map { segment =>
      val p1 = CoordinateUtil.toCoordinate2(segment.p1.latitude, segment.p1.longitude)
      val p2 = CoordinateUtil.toCoordinate2(segment.p2.latitude, segment.p2.longitude)
      s"[[${p1._1},${p1._2}],[${p2._1},${p2._2}]]"
    }
  }
}

case class GeometryDiff(
  common: Seq[String] = Seq.empty, // blue
  before: Seq[String] = Seq.empty, // red
  after: Seq[String] = Seq.empty // green
)
