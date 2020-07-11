package kpn.api.common.planner

import kpn.api.common.common.ToStringBuilder

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
      val fragmentCoordinates = (
        Seq(PlanFragmentCoordinate(sourceNode.coordinate, sourceNode.latLon))
          ++ segments.flatMap(_.fragments).map(f => PlanFragmentCoordinate(f.coordinate, f.latLon))
        ).reverse.drop(1)

      val fragmentCoordinateIterator = fragmentCoordinates.iterator

      val reversedSegments = segments.reverse.map { segment =>
        val reversedFragments = segment.fragments.reverse.map { fragment =>
          val fragmentCoordinate = fragmentCoordinateIterator.next()
          fragment.copy(
            latLon = fragmentCoordinate.latLon,
            coordinate = fragmentCoordinate.coordinate
          )
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
