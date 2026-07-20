package kpn.api.common.common;

import kpn.api.common.common.TrackSegment;

import com.google.common.collect.ImmutableList;

public record TrackPath(
  Long pathId,
  Long startNodeId,
  Long endNodeId,
  Long meters,
  Boolean oneWay,
  ImmutableList<TrackSegment> segments
) {}

/* TODO migrate
package kpn.api.common.common

case class TrackPath(
  pathId: Long,
  startNodeId: Long,
  endNodeId: Long,
  meters: Long,
  oneWay: Boolean,
  segments: Seq[TrackSegment]
) {

  def trackPoints: Seq[TrackPoint] = {
    segments.headOption match {
      case None => Seq.empty
      case Some(segment) => segment.source +: segments.flatMap(segment => segment.fragments.map(_.trackPoint))
    }
  }

  def reverse: TrackPath = {

    val reversedSegments = segments.reverse.map { segment =>
      val source = segment.trackPoints.last
      val trackPoints = segment.trackPoints.dropRight(1).reverse
      val reversedFragments = trackPoints.zip(segment.fragments.reverse).map { case (trackPoint, fragment) =>
        TrackSegmentFragment(
          trackPoint,
          fragment.meters
        )
      }

      TrackSegment(
        segment.surface,
        source,
        reversedFragments
      )
    }

    TrackPath(
      pathId = pathId,
      startNodeId = endNodeId,
      endNodeId = startNodeId,
      meters = meters,
      oneWay = oneWay,
      segments = reversedSegments
    )
  }
}

*/
