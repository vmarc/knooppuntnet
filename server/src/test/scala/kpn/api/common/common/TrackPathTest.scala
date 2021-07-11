package kpn.api.common.common

import kpn.core.util.UnitTest

class TrackPathTest extends UnitTest {

  test("trackPoints") {

    val trackPath = TrackPath(
      0L,
      0L,
      0L,
      0L,
      oneWay = false,
      segments = Seq(
        TrackSegment(
          "",
          source = TrackPoint("1", "1"),
          fragments = Seq(
            TrackSegmentFragment(trackPoint = TrackPoint("2", "2"), 0L, 0L, None),
            TrackSegmentFragment(trackPoint = TrackPoint("3", "3"), 0L, 0L, None),
            TrackSegmentFragment(trackPoint = TrackPoint("4", "4"), 0L, 0L, None)
          )
        )
      )
    )

    trackPath.trackPoints should equal(
      Seq(
        TrackPoint("1", "1"),
        TrackPoint("2", "2"),
        TrackPoint("3", "3"),
        TrackPoint("4", "4")
      )
    )
  }

  test("trackPoints - path without segments") {
    val trackPath = TrackPath(0L, 0L, 0L, 0L, oneWay = false, segments = Seq.empty)
    trackPath.trackPoints should equal(Seq.empty)
  }

}
