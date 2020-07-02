package kpn.api.common.planner

import kpn.api.common.LatLonImpl
import kpn.api.common.common.ToStringBuilder
import kpn.api.common.common.TrackPoint

case class PlanRoute(
  sourceNode: PlanNode,
  sinkNode: PlanNode,
  meters: Long,
  segments: Seq[PlanSegment],
  streets: Seq[String]
) {

  def reverse: PlanRoute = {

    if (segments.isEmpty) {
      PlanRoute(
        sinkNode,
        sourceNode,
        meters,
        Seq(),
        streets
      )
    }
    else {
      val trackPoints = (
        Seq(TrackPoint(sourceNode.latLon.latitude, sourceNode.latLon.longitude))
          ++ segments.flatMap(_.fragments).map(f => TrackPoint(f.latLon.latitude, f.latLon.longitude))
        ).reverse.drop(1)

      val trackPointIterator = trackPoints.iterator

      val reversedSegments = segments.reverse.map { segment =>
        val reversedFragments = segment.fragments.reverse.map { fragment =>
          val trackPoint = trackPointIterator.next()
          fragment.copy(latLon = LatLonImpl(trackPoint.lat, trackPoint.lon))
        }
        segment.copy(fragments = reversedFragments)
      }

      PlanRoute(
        sinkNode,
        sourceNode,
        meters,
        reversedSegments,
        streets
      )
    }
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("sourceNode", sourceNode).
    field("sinkNode", sinkNode).
    field("meters", meters).
    field("segments", segments).
    field("streets", streets).
    build

}
