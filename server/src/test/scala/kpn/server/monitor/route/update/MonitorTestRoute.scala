package kpn.server.monitor.route.update

import kpn.api.common.Bounds
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.doc.SuperSegment
import kpn.core.test.TestObjects.newBaseRouteDoc
import kpn.core.test.TestObjects.newBaseRouteSegment
import kpn.core.test.TestObjects.newBaseRouteSegmentElement
import kpn.core.test.TestObjects.newRouteDoc
import kpn.core.test.TestObjects.newRouteSummary

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
        SuperSegment(
          Seq.empty
        )
      )
    )
  }
}
