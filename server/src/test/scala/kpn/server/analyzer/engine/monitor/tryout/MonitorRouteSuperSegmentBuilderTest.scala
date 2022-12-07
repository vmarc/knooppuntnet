package kpn.server.analyzer.engine.monitor.tryout

import kpn.api.common.Bounds
import kpn.api.common.SharedTestObjects
import kpn.api.common.monitor.MonitorRouteSegment
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.monitor.domain.MonitorRouteRelationSegment
import kpn.server.api.monitor.domain.MonitorRouteSuperSegment
import kpn.server.api.monitor.domain.MonitorRouteSuperSegmentElement

class MonitorRouteSuperSegmentBuilderTest extends UnitTest with SharedTestObjects {

  test("no segments") {
    MonitorRouteSuperSegmentBuilder.build(Seq.empty) should equal(Seq.empty)
  }

  test("single segment") {

    val segments = Seq(
      MonitorRouteRelationSegment(1L, segment(11L, 1001, 1002)),
    )

    MonitorRouteSuperSegmentBuilder.build(segments).shouldMatchTo(
      Seq(
        MonitorRouteSuperSegment(
          Seq(
            MonitorRouteSuperSegmentElement(1L, 11L, reversed = false),
          )
        )
      )
    )
  }

  test("two directly adjecent segments") {

    val segments = Seq(
      MonitorRouteRelationSegment(1L, segment(11L, 1001, 1002)),
      MonitorRouteRelationSegment(2L, segment(21L, 1002, 1003)),
    )

    MonitorRouteSuperSegmentBuilder.build(segments).shouldMatchTo(
      Seq(
        MonitorRouteSuperSegment(
          Seq(
            MonitorRouteSuperSegmentElement(1L, 11L, reversed = false),
            MonitorRouteSuperSegmentElement(2L, 21L, reversed = false),
          )
        )
      )
    )
  }

  test("three directly adjecent segments, but not in sorted order") {

    val segments = Seq(
      MonitorRouteRelationSegment(1L, segment(11L, 1001, 1002)),
      MonitorRouteRelationSegment(1L, segment(12L, 1003, 1004)),
      MonitorRouteRelationSegment(1L, segment(13L, 1002, 1003)),
    )

    MonitorRouteSuperSegmentBuilder.build(segments).shouldMatchTo(
      Seq(
        MonitorRouteSuperSegment(
          Seq(
            MonitorRouteSuperSegmentElement(1L, 11L, reversed = false),
            MonitorRouteSuperSegmentElement(1L, 13L, reversed = false),
            MonitorRouteSuperSegmentElement(1L, 12L, reversed = false),
          )
        )
      )
    )
  }

  test("two super segments, one with a reversed segment in the middle") {

    val segments = Seq(
      MonitorRouteRelationSegment(1L, segment(11L, 1001, 1002)),
      MonitorRouteRelationSegment(1L, segment(12L, 1003, 1004)),
      MonitorRouteRelationSegment(2L, segment(21L, 1005, 1002)),
      MonitorRouteRelationSegment(3L, segment(31L, 1005, 1006)),
    )

    MonitorRouteSuperSegmentBuilder.build(segments).shouldMatchTo(
      Seq(
        MonitorRouteSuperSegment(
          Seq(
            MonitorRouteSuperSegmentElement(1L, 11L, reversed = false),
            MonitorRouteSuperSegmentElement(2L, 21L, reversed = true),
            MonitorRouteSuperSegmentElement(3L, 31L, reversed = false)
          )
        ),
        MonitorRouteSuperSegment(
          Seq(
            MonitorRouteSuperSegmentElement(1L, 12L, reversed = false)
          )
        )
      )
    )
  }

  private def segment(id: Long, startNodeId: Long, endNodeId: Long): MonitorRouteSegment = {
    MonitorRouteSegment(
      id,
      startNodeId,
      endNodeId,
      0,
      Bounds(),
      ""
    )
  }
}
