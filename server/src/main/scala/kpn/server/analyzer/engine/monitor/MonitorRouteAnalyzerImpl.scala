package kpn.server.analyzer.engine.monitor

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Relation
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.QueryRelation
import kpn.core.tools.monitor.MonitorDemoAnalyzer
import kpn.core.tools.monitor.MonitorRouteGpxReader
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.repository.MonitorRouteRepository
import org.locationtech.jts.geom.Geometry
import org.springframework.stereotype.Component

import scala.xml.Elem
import scala.xml.XML

@Component
class MonitorRouteAnalyzerImpl(
  monitorRouteRepository: MonitorRouteRepository,
  overpassQueryExecutor: OverpassQueryExecutor
) extends MonitorRouteAnalyzer {

  override def analyze(route: MonitorRoute, reference: MonitorRouteReference): Unit = {
    val now = Time.now
    analyzeRoute(route, reference, now)
  }

  override def processGpxFileUpload(user: String, route: MonitorRoute, filename: String, xml: Elem): MonitorRouteSaveResult = {

    val now = Time.now
    val geometry = new MonitorRouteGpxReader().read(xml)
    val bounds = geometryBounds(geometry)
    val geoJson = MonitorRouteAnalysisSupport.toGeoJson(geometry)

    val reference = MonitorRouteReference(
      ObjectId(),
      routeId = route._id,
      relationId = route.relationId,
      created = now,
      user = user,
      bounds = bounds,
      referenceType = "gpx", // "osm" | "gpx"
      referenceDay = None,
      segmentCount = 1, // number of tracks in gpx always 1, multiple track not supported yet
      filename = Some(filename),
      geometry = geoJson
    )

    monitorRouteRepository.saveRouteReference(reference)
    MonitorRouteSaveResult()
  }

  private def geometryBounds(geometry: Geometry): Bounds = {
    val envelope = geometry.getEnvelopeInternal
    Bounds(
      envelope.getMinY, // minLat
      envelope.getMinX, // minLon
      envelope.getMaxY, // maxLat
      envelope.getMaxX, // maxLon
    )
  }

  private def analyzeRoute(route: MonitorRoute, reference: MonitorRouteReference, now: Timestamp): Unit = {
    route.relationId match {
      case None =>
      case Some(relationId) =>
        readRelation(now, relationId) match {
          case None =>
          case Some(routeRelation) =>
            val monitorRouteState = new MonitorDemoAnalyzer().analyze(route, reference, routeRelation, now)
            new MonitorRouteStateUpdater(monitorRouteRepository).update(route, monitorRouteState, reference)
        }
    }
  }

  private def readRelation(now: Timestamp, routeId: Long): Option[Relation] = {
    val xmlString = overpassQueryExecutor.executeQuery(Some(now), QueryRelation(routeId))
    val xml = XML.loadString(xmlString)
    val rawData = new Parser().parse(xml.head)
    val data = new DataBuilder(rawData).data
    data.relations.get(routeId)
  }
}
