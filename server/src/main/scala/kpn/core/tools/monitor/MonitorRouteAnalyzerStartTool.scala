package kpn.core.tools.monitor

import kpn.api.common.BoundsI
import kpn.api.custom.Timestamp
import kpn.core.db.couch.Couch
import kpn.core.util.Util
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteLoader
import kpn.server.analyzer.engine.monitor.MonitorRouteLoaderFileImpl
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import kpn.server.repository.MonitorAdminRouteRepository
import kpn.server.repository.MonitorAdminRouteRepositoryImpl
import org.locationtech.jts.geom.GeometryFactory

import java.io.File

object MonitorRouteAnalyzerStartTool {

  def main(args: Array[String]): Unit = {
    Couch.executeIn("kpn-database", "monitor") { monitorDatabase =>
      val monitorRouteLoader = new MonitorRouteLoaderFileImpl()
      val monitorRouteRepository = new MonitorAdminRouteRepositoryImpl(null, monitorDatabase, false)
      new MonitorRouteAnalyzerStartTool(monitorRouteLoader, monitorRouteRepository).buildInitialRouteDocuments()
    }
  }
}

class MonitorRouteAnalyzerStartTool(
  monitorRouteLoader: MonitorRouteLoader,
  monitorAdminRouteRepository: MonitorAdminRouteRepository
) {

  private val geomFactory = new GeometryFactory

  def buildInitialRouteDocuments(): Unit = {

    routeIds.foreach { routeId =>
      monitorRouteLoader.loadInitial(null, routeId).foreach { routeRelation =>

        val route = MonitorRouteAnalyzer.toRoute("example", routeRelation)
        monitorAdminRouteRepository.saveRoute(route)

        val segments = MonitorRouteAnalyzer.toRouteSegments(routeRelation)
        val timestamp = Timestamp(2020, 8, 11)
        val osmSegments = segments.map(_.segment)
        val bounds = Util.mergeBounds(segments.map(_.segment.bounds))
        val geometry = geomFactory.createMultiLineString(segments.map(_.lineString).toArray)
        val geoJson = MonitorRouteAnalyzer.toGeoJson(geometry)

        val routeState = MonitorRouteState(
          routeId,
          timestamp,
          wayCount = 0,
          osmDistance = 0,
          gpxDistance = 0,
          bounds,
          referenceKey = Some(timestamp.key),
          osmSegments = osmSegments,
          okGeometry = Some(geoJson),
          nokSegments = Seq.empty
        )

        monitorAdminRouteRepository.saveRouteState(routeState)

        val routeReference = MonitorRouteReference(
          routeId,
          timestamp.key,
          timestamp,
          "vmarc",
          bounds: BoundsI,
          "osm", // "osm" | "gpx"
          Some(timestamp),
          segments.size,
          None,
          geometry = geoJson
        )

        monitorAdminRouteRepository.saveRouteReference(routeReference)
      }
    }
  }

  private def routeIds: Seq[Long] = {
    new File("/kpn/wrk/begin").list().toSeq.map(file => file.replace(".xml", "").toLong)
  }

}
