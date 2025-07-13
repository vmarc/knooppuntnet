package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.doc.SuperSegment
import kpn.core.doc.SuperSegmentElement
import kpn.core.doc.SuperSegmentElementInfo
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newBaseRouteSegment
import kpn.core.test.TestObjects.newBaseRouteSegmentElement
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteRelation
import kpn.core.test.TestObjects.newRouteSummary

object TestSuperRoute {

  val MainRelationId = 1

  val subRoute11: MonitorTestRoute = MonitorTestData.route1.copy(relationId = 11)
  val subRoute12: MonitorTestRoute = MonitorTestData.route2.copy(relationId = 12)

  val baseRouteDoc: BaseRouteDoc = newBaseRouteDoc(
    newRouteSummary(MainRelationId),
    subRelationTree = Some(
      newRouteRelation(
        relationId = MainRelationId,
        name = "main-relation",
        relations = Seq(
          newRouteRelation(
            relationId = subRoute11.relationId,
            name = "sub-relation-1",
          ),
          newRouteRelation(
            relationId = subRoute12.relationId,
            name = "sub-relation-2",
          )
        )
      )
    )
  )

  val baseRouteDoc11: BaseRouteDoc = newBaseRouteDoc(
    newRouteSummary(subRoute11.relationId),
    segments = Seq(
      newBaseRouteSegment(1, meters = subRoute11.meters)
    ),
    segmentElements = Seq(
      newBaseRouteSegmentElement(
        segmentId = 1,
        segmentElementId = 1,
        coordinates = subRoute11.coordinateString
      )
    ),
  )

  val baseRouteDoc12: BaseRouteDoc = newBaseRouteDoc(
    newRouteSummary(subRoute12.relationId),
    segments = Seq(
      newBaseRouteSegment(1, meters = subRoute12.meters)
    ),
    segmentElements = Seq(
      newBaseRouteSegmentElement(
        segmentId = 1,
        segmentElementId = 1,
        coordinates = subRoute12.coordinateString
      )
    ),
  )

  val routeDoc1: RouteDoc = newRouteDoc(
    newRouteSummary(MainRelationId),
    superDistance = subRoute11.meters + subRoute12.meters,
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
              meters = subRoute11.meters + subRoute12.meters,
              bounds = Bounds(),
            )
          )
        )
      )
    )
  )
}
