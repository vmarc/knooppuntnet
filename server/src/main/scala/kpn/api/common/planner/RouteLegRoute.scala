package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder
import kpn.api.common.common.TrackPoint

case class RouteLegRoute(
  source: RouteLegNode,
  sink: RouteLegNode,
  meters: Long,
  segments: Seq[RouteLegSegment],
  streets: Seq[String]
) {

  def allNodeIds: Set[Long] = Set(source.nodeId.toLong, sink.nodeId.toLong)

  def reverse: RouteLegRoute = {

    if (segments.isEmpty) {
      RouteLegRoute(
        sink,
        source,
        meters,
        Seq(),
        streets
      )
    }
    else {
      val trackPoints = (Seq(TrackPoint(source.lat, source.lon)) ++ segments.flatMap(_.fragments).map(f => TrackPoint(f.lat, f.lon))).reverse.drop(1)
      val trackPointIterator = trackPoints.iterator

      val reversedSegments = segments.reverse.map { segment =>
        val reversedFragments = segment.fragments.reverse.map { fragment =>
          val trackPoint = trackPointIterator.next()
          fragment.copy(lat = trackPoint.lat, lon = trackPoint.lon)
        }
        segment.copy(fragments = reversedFragments)
      }

      RouteLegRoute(
        sink,
        source,
        meters,
        reversedSegments,
        streets
      )
    }
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("source", source).
    field("sink", sink).
    field("meters", meters).
    field("segments", segments).
    field("streets", streets).
    build

}
