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

    trackPath.trackPoints should matchTo(
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
    trackPath.trackPoints shouldBe empty
  }

  test("reversed") {

    val trackPath = TrackPath(
      pathId = 123L,
      startNodeId = 1001L,
      endNodeId = 1005L,
      meters = 123L,
      oneWay = false,
      segments = Seq(
        TrackSegment(
          "surface",
          source = TrackPoint("1", "1"),
          fragments = Seq(
            TrackSegmentFragment(trackPoint = TrackPoint("2", "2"), 0L, 2L, None),
            TrackSegmentFragment(trackPoint = TrackPoint("3", "3"), 0L, 3L, None),
            TrackSegmentFragment(trackPoint = TrackPoint("4", "4"), 0L, 4L, None),
            TrackSegmentFragment(trackPoint = TrackPoint("5", "5"), 0L, 5L, None)
          )
        )
      )
    )

    trackPath.reverse should matchTo(
      TrackPath(
        pathId = 123L,
        startNodeId = 1005L,
        endNodeId = 1001L,
        meters = 123L,
        oneWay = false,
        segments = Seq(
          TrackSegment(
            "surface",
            source = TrackPoint("5", "5"),
            fragments = Seq(
              TrackSegmentFragment(trackPoint = TrackPoint("4", "4"), 0L, 185L, None),
              TrackSegmentFragment(trackPoint = TrackPoint("3", "3"), 0L, 184L, None),
              TrackSegmentFragment(trackPoint = TrackPoint("2", "2"), 0L, 183L, None),
              TrackSegmentFragment(trackPoint = TrackPoint("1", "1"), 0L, 182L, None)
            )
          )
        )
      )
    )
  }
}
