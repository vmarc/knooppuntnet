package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.doc.SuperSubSegment
import kpn.core.doc.SuperSubSegmentInfo
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newBaseRouteSegment
import kpn.core.test.TestObjects.newBaseRouteSegmentElement
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteRelation
import kpn.core.test.TestObjects.newSuperSegment

object TestSuperRoute {

  val MainRelationId = 1

  val subRoute11: MonitorTestRoute = MonitorTestData.route1.copy(relationId = 11, stateTiles = MonitorTestData.route1.stateTiles.map(_.copy(relationId = 11)))
  val subRoute12: MonitorTestRoute = MonitorTestData.route2.copy(relationId = 12, stateTiles = MonitorTestData.route2.stateTiles.map(_.copy(relationId = 12)))

  val baseRouteDoc: BaseRouteDoc = newBaseRouteDoc(
    MainRelationId,
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
    ),
    bounds = Some(Bounds.merge(Seq(subRoute11.bounds, subRoute12.bounds))),
  )

  val baseRouteDoc11: BaseRouteDoc = newBaseRouteDoc(
    subRoute11.relationId,
    segments = Seq(
      newBaseRouteSegment(1, meters = subRoute11.meters)
    ),
    segmentElements = Seq(
      newBaseRouteSegmentElement(
        segmentId = 1,
        segmentElementId = 1,
        coordinates = subRoute11.coordinates
      )
    ),
    bounds = Some(subRoute11.bounds),
  )

  val baseRouteDoc12: BaseRouteDoc = newBaseRouteDoc(
    subRoute12.relationId,
    segments = Seq(
      newBaseRouteSegment(1, meters = subRoute12.meters)
    ),
    segmentElements = Seq(
      newBaseRouteSegmentElement(
        segmentId = 1,
        segmentElementId = 1,
        coordinates = subRoute12.coordinates
      )
    ),
    bounds = Some(subRoute12.bounds),
  )

  val routeDoc1: RouteDoc = newRouteDoc(
    MainRelationId,
    superDistance = subRoute11.meters + subRoute12.meters,
    superSegments = Seq(
      newSuperSegment(
        SuperSubSegment(
          SuperSubSegmentInfo(
            id = 0,
            relationId = 0,
            segmentId = 0,
            startNodeId = 0,
            endNodeId = 0,
            meters = subRoute11.meters + subRoute12.meters,
            bounds = Bounds(),
          )
        )
      )
    ),
    routeIds = Seq(
      TestSuperRoute.MainRelationId,
      subRoute11.relationId,
      subRoute12.relationId
    ),
    bounds = Some(
      Bounds.merge(
        Seq(
          subRoute11.bounds,
          subRoute12.bounds
        )
      )
    ),
  )
}
