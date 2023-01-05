package kpn.server.analyzer.engine.monitor.tryout

import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteSegmentInfo
import kpn.core.util.UnitTest
import kpn.server.api.monitor.domain.MonitorRouteSuperSegment
import kpn.server.api.monitor.domain.MonitorRouteSuperSegmentElement

class MonitorRouteSuperSegmentBuilderTest extends UnitTest with SharedTestObjects {

  test("no segments") {
    MonitorRouteSuperSegmentBuilder.build(Seq.empty) should equal(Seq.empty)
  }

  test("single segment") {

    val segments = Seq(
      MonitorRouteSegmentInfo(1, 11, 1, 1001, 1002, 100, Bounds(1, 1, 1, 1)),
    )

    MonitorRouteSuperSegmentBuilder.build(segments).shouldMatchTo(
      Seq(
        MonitorRouteSuperSegment(
          Seq(
            MonitorRouteSuperSegmentElement(11, 1, 100, Bounds(1, 1, 1, 1), reversed = false),
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

    MonitorRouteSuperSegmentBuilder.build(segments).shouldMatchTo(
      Seq(
        MonitorRouteSuperSegment(
          Seq(
            MonitorRouteSuperSegmentElement(11, 1, 100, Bounds(1, 1, 1, 1), reversed = false),
            MonitorRouteSuperSegmentElement(12, 1, 200, Bounds(2, 2, 2, 2), reversed = false),
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

    MonitorRouteSuperSegmentBuilder.build(segments).shouldMatchTo(
      Seq(
        MonitorRouteSuperSegment(
          Seq(
            MonitorRouteSuperSegmentElement(11, 1, 100, Bounds(1, 1, 1, 1), reversed = false),
            MonitorRouteSuperSegmentElement(13, 1, 300, Bounds(3, 3, 3, 3), reversed = false),
            MonitorRouteSuperSegmentElement(12, 1, 200, Bounds(2, 2, 2, 2), reversed = false),
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

    MonitorRouteSuperSegmentBuilder.build(segments).shouldMatchTo(
      Seq(
        MonitorRouteSuperSegment(
          Seq(
            MonitorRouteSuperSegmentElement(11, 1, 100, Bounds(1, 1, 1, 1), reversed = false),
            MonitorRouteSuperSegmentElement(12, 1, 300, Bounds(3, 3, 3, 3), reversed = true),
            MonitorRouteSuperSegmentElement(12, 2, 400, Bounds(4, 4, 4, 4), reversed = false)
          )
        ),
        MonitorRouteSuperSegment(
          Seq(
            MonitorRouteSuperSegmentElement(11, 2, 200, Bounds(2, 2, 2, 2), reversed = false)
          )
        )
      )
    )
  }
}
