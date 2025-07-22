package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.api.common.data.MemberType
import kpn.api.common.monitor.MonitorRouteRelation
import kpn.api.custom.Relation
import kpn.api.custom.Tags
import kpn.core.data.DataBuilder
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newBaseRouteSegment
import kpn.core.test.TestObjects.newBaseRouteSegmentElement
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteSummary
import kpn.core.test.TestObjects.newSuperSegment
import kpn.server.monitor.domain.MonitorReferenceTile

case class MonitorTestRoute(
  relationId: Long,
  lon1: String,
  lat1: String,
  lon2: String,
  lat2: String,
  meters: Long,
  coordinates: Array[Array[String]],
  lines: Seq[String],
  bounds: Bounds,
  gpx: String,
  referenceTiles: Seq[MonitorReferenceTile],
  stateTiles: Seq[MonitorStateTileInfo],
) {
  def coordinateString: String = {
    coordinates.map(c => c.map(d => d).mkString("[", ",", "]")).mkString("[", ",", "]")
  }

  def coordinateDoubles: Array[Array[Double]] = {
    coordinates.map(c => c.map(d => d.toDouble))
  }

  def baseRouteDoc: BaseRouteDoc = {
    newBaseRouteDoc(
      newRouteSummary(relationId),
      segments = Seq(
        newBaseRouteSegment(1)
      ),
      segmentElements = Seq(
        newBaseRouteSegmentElement(
          segmentId = 1,
          segmentElementId = 1,
          meters = meters,
          coordinates = coordinateString
        )
      ),
      bounds = Some(bounds)
    )
  }

  def routeDoc: RouteDoc = {
    newRouteDoc(
      newRouteSummary(
        relationId,
        name = "route-name"
      ),
      superDistance = meters,
      routeIds = Seq(relationId),
      superSegments = Seq(
        newSuperSegment()
      ),
      bounds = Some(bounds)
    )
  }

  def overpassStructure: MonitorRouteRelation = {
    val overpassData = OverpassData()
      .relation(
        relationId,
        tags = Tags.from(
          "name" -> "route-name"
        ),
      )
    MonitorRouteRelation.from(new DataBuilder(overpassData.rawData).data.relations(relationId), None)
  }

  def overpassTopLevel: Relation = {
    val overpassData = OverpassData()
      .node(1001, latitude = lat1, longitude = lon1)
      .node(1002, latitude = lat2, longitude = lon2)
      .way(101, 1001, 1002)
      .relation(
        relationId,
        tags = Tags.from(
          "name" -> "route-name"
        ),
        members = Seq(
          newMember(MemberType.Way, 101),
        )
      )
    new DataBuilder(overpassData.rawData).data.relations(relationId)
  }
}
