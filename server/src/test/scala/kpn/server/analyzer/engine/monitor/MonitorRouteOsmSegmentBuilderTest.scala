package kpn.server.analyzer.engine.monitor

import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteSegmentInfo
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorRouteOsmSegment
import kpn.server.monitor.domain.MonitorRouteOsmSegmentElement

class MonitorRouteOsmSegmentBuilderTest extends UnitTest with SharedTestObjects {

  test("no segments") {
    MonitorRouteOsmSegmentBuilder.build(Seq.empty) should equal(Seq.empty)
  }

  test("single segment") {

    val segments = Seq(
      MonitorRouteSegmentInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1)),
    )

    MonitorRouteOsmSegmentBuilder.build(segments).shouldMatchTo(
      Seq(
        MonitorRouteOsmSegment(
          Seq(
            MonitorRouteOsmSegmentElement(11, 1, 100, Bounds(1, 1, 1, 1), reversed = false),
          )
        )
      )
    )
  }

  test("two directly adjecent segments") {

    val segments = Seq(
      MonitorRouteSegmentInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1)),
      MonitorRouteSegmentInfo(2, 12, 1, 1002, 1003, 200, Bounds(2, 2, 2, 2)),
    )

    MonitorRouteOsmSegmentBuilder.build(segments).shouldMatchTo(
      Seq(
        MonitorRouteOsmSegment(
          Seq(
            MonitorRouteOsmSegmentElement(11, 1, 100, Bounds(1, 1, 1, 1), reversed = false),
            MonitorRouteOsmSegmentElement(12, 1, 200, Bounds(2, 2, 2, 2), reversed = false),
          )
        )
      )
    )
  }

  test("three directly adjecent segments, but not in sorted order") {

    val segments = Seq(
      MonitorRouteSegmentInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1)),
      MonitorRouteSegmentInfo(2, 12, 1, 1003, 1004, 200, Bounds(2, 2, 2, 2)),
      MonitorRouteSegmentInfo(3, 13, 1, 1002, 1003, 300, Bounds(3, 3, 3, 3)),
    )

    MonitorRouteOsmSegmentBuilder.build(segments).shouldMatchTo(
      Seq(
        MonitorRouteOsmSegment(
          Seq(
            MonitorRouteOsmSegmentElement(11, 1, 100, Bounds(1, 1, 1, 1), reversed = false),
            MonitorRouteOsmSegmentElement(13, 1, 300, Bounds(3, 3, 3, 3), reversed = false),
            MonitorRouteOsmSegmentElement(12, 1, 200, Bounds(2, 2, 2, 2), reversed = false),
          )
        )
      )
    )
  }

  test("two super segments, one with a reversed segment in the middle") {

    val segments = Seq(
      MonitorRouteSegmentInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1)),
      MonitorRouteSegmentInfo(2, 11, 2, 1003, 1004, 200, Bounds(2, 2, 2, 2)),
      MonitorRouteSegmentInfo(3, 12, 1, 1005, 1002, 300, Bounds(3, 3, 3, 3)),
      MonitorRouteSegmentInfo(4, 12, 2, 1005, 1006, 400, Bounds(4, 4, 4, 4)),
    )

    MonitorRouteOsmSegmentBuilder.build(segments).shouldMatchTo(
      Seq(
        MonitorRouteOsmSegment(
          Seq(
            MonitorRouteOsmSegmentElement(11, 1, 100, Bounds(1, 1, 1, 1), reversed = false),
            MonitorRouteOsmSegmentElement(12, 1, 300, Bounds(3, 3, 3, 3), reversed = true),
            MonitorRouteOsmSegmentElement(12, 2, 400, Bounds(4, 4, 4, 4), reversed = false)
          )
        ),
        MonitorRouteOsmSegment(
          Seq(
            MonitorRouteOsmSegmentElement(11, 2, 200, Bounds(2, 2, 2, 2), reversed = false)
          )
        )
      )
    )
  }
}
