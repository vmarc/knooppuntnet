package kpn.server.analyzer.engine.analysis.common

import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackPoint
import kpn.api.common.common.TrackSegment
import kpn.api.common.common.TrackSegmentFragment
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.tiles.domain.Point

class ConverterTest extends UnitTest {

  test("trackPathToPoints - single segment") {
    val trackPath = buildTrackPath(
      Seq(
        buildSegment(TrackPoint("1", "1"), Seq(TrackPoint("2", "2"), TrackPoint("3", "3")))
      )
    )
    Converter.trackPathToPoints(trackPath) should equal(Seq(Point(1, 1), Point(2, 2), Point(3, 3)))
  }

  test("trackPathToPoints - multiple segments") {
    val trackPath = buildTrackPath(
      Seq(
        buildSegment(TrackPoint("1", "1"), Seq(TrackPoint("2", "2"), TrackPoint("3", "3"))),
        buildSegment(TrackPoint("3", "3"), Seq(TrackPoint("4", "4"), TrackPoint("5", "5")))
      )
    )
    Converter.trackPathToPoints(trackPath) should equal(
      Seq(
        Point(1, 1),
        Point(2, 2),
        Point(3, 3),
        Point(4, 4),
        Point(5, 5)
      )
    )
  }

  private def buildTrackPath(segments: Seq[TrackSegment]): TrackPath = {
    TrackPath(
      startNodeId = 1,
      endNodeId = 2,
      meters = 0,
      segments = segments
    )
  }

  private def buildSegment(source: TrackPoint, fragmentTrackPoints: Seq[TrackPoint]): TrackSegment = {
    TrackSegment(
      surface = "",
      source = source,
      fragments = fragmentTrackPoints.map { trackPoint =>
        TrackSegmentFragment(
          trackPoint = trackPoint,
          meters = 0,
          orientation = 0,
          streetIndex = None
        )
      }
    )
  }

}
