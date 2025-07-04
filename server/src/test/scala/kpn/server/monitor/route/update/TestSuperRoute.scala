package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.doc.SuperSegment
import kpn.core.doc.SuperSegmentElement
import kpn.core.doc.SuperSegmentElementInfo
import kpn.core.test.SharedTestObjects

object TestSuperRoute extends SharedTestObjects {

  val MainRelationId = 1
  val SubRelationId1 = 11
  val SubRelationId2 = 12
  val SubRelationDistance1 = 181
  val SubRelationDistance2 = 93

  val gpx1: String =
    """
      |<gpx>
      |  <trk>
      |    <trkseg>
      |      <trkpt lat="51.4633666" lon="4.4553911"></trkpt>
      |      <trkpt lat="51.4618272" lon="4.4562458"></trkpt>
      |    </trkseg>
      |  </trk>
      |</gpx>
      |""".stripMargin

  val gpx2: String =
    """
      |<gpx>
      |  <trk>
      |    <trkseg>
      |      <trkpt lat="51.4618272" lon="4.4562458"></trkpt>
      |      <trkpt lat="51.4614496" lon="4.4550560"></trkpt>
      |    </trkseg>
      |  </trk>
      |</gpx>
      |""".stripMargin

  val baseRouteDoc: BaseRouteDoc = newBaseRouteDoc(
    newRouteSummary(MainRelationId),
    subRelationTree = Some(
      newRouteRelation(
        relationId = MainRelationId,
        name = "main-relation",
        relations = Seq(
          newRouteRelation(
            relationId = SubRelationId1,
            name = "sub-relation-1",
          ),
          newRouteRelation(
            relationId = SubRelationId2,
            name = "sub-relation-2",
          )
        )
      )
    )
  )

  val baseRouteDoc11: BaseRouteDoc = newBaseRouteDoc(
    newRouteSummary(SubRelationId1),
    segments = Seq(
      newBaseRouteSegment(1, meters = SubRelationDistance1)
    ),
    segmentElements = Seq(
      newBaseRouteSegmentElement(
        segmentId = 1,
        segmentElementId = 1,
        coordinates = "[[4.4553911, 51.4633666],[4.4562458,51.4618272]]"
      )
    ),
  )

  val baseRouteDoc12: BaseRouteDoc = newBaseRouteDoc(
    newRouteSummary(SubRelationId2),
    segments = Seq(
      newBaseRouteSegment(1, meters = SubRelationDistance2)
    ),
    segmentElements = Seq(
      newBaseRouteSegmentElement(
        segmentId = 1,
        segmentElementId = 1,
        coordinates = "[[4.4562458,51.4618272],[4.4550560,51.4614496]]"
      )
    ),
  )

  val routeDoc1: RouteDoc = newRouteDoc(
    newRouteSummary(MainRelationId),
    superSegments = Seq(
      SuperSegment(
        Seq(
          SuperSegmentElement(
            SuperSegmentElementInfo(
              id = 0,
              relationId = 0,
              osmSegmentId = 0,
              startNodeId = 0,
              endNodeId = 0,
              meters = SubRelationDistance1 + SubRelationDistance2,
              bounds = Bounds(),
            )
          )
        )
      )
    )
  )
}
