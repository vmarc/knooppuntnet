package kpn.api.common.common;

import kpn.api.common.common.TrackPoint;
import kpn.api.common.common.TrackSegmentFragment;

import com.google.common.collect.ImmutableList;

public record TrackSegment(
  String surface,
  TrackPoint source,
  ImmutableList<TrackSegmentFragment> fragments
) {
}

/*
package kpn.api.common.common

case class TrackSegment(
  surface: String,
  source: TrackPoint,
  fragments: Seq[TrackSegmentFragment]
) {

  def trackPoints: Seq[TrackPoint] = source +: fragments.map(_.trackPoint)

}

*/
