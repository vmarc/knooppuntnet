package kpn.core.tools.monitor

import kpn.api.base.MongoId
import kpn.api.custom.Timestamp
import kpn.core.util.Util
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import kpn.server.analyzer.engine.monitor.MonitorRouteLoader
import kpn.server.analyzer.engine.monitor.MonitorRouteLoaderFileImpl
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import kpn.server.repository.MonitorRouteRepository
import kpn.server.repository.MonitorRouteRepositoryImpl
import org.locationtech.jts.geom.GeometryFactory

import java.io.File

object MonitorRouteAnalyzerStartTool {

  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-test") { database =>
      val monitorRouteLoader = new MonitorRouteLoaderFileImpl()
      val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)
      new MonitorRouteAnalyzerStartTool(monitorRouteLoader, monitorRouteRepository).buildInitialRouteDocuments()
    }
  }
}

class MonitorRouteAnalyzerStartTool(
  monitorRouteLoader: MonitorRouteLoader,
  monitorRouteRepository: MonitorRouteRepository
) {

  private val geomFactory = new GeometryFactory

  def buildInitialRouteDocuments(): Unit = {

    routeIds.foreach { routeId =>
      monitorRouteLoader.loadInitial(null, routeId).foreach { routeRelation =>

        val route = MonitorRouteAnalysisSupport.toRoute("TODO routeName", MongoId("example"), "TODO route description", routeId)
        monitorRouteRepository.saveRoute(route)

        val segments = MonitorRouteAnalysisSupport.toRouteSegments(routeRelation)
        val timestamp = Timestamp(2020, 8, 11)
        val osmSegments = segments.map(_.segment)
        val bounds = Util.mergeBounds(segments.map(_.segment.bounds))
        val geometry = geomFactory.createMultiLineString(segments.map(_.lineString).toArray)
        val geoJson = MonitorRouteAnalysisSupport.toGeoJson(geometry)
        val happy = false

        val routeState = MonitorRouteState(
          MongoId(),
          null, // routeId,
          timestamp,
          wayCount = 0,
          osmDistance = 0,
          gpxDistance = 0,
          bounds,
          referenceKey = Some(timestamp.key),
          osmSegments = osmSegments,
          okGeometry = Some(geoJson),
          nokSegments = Seq.empty,
          happy
        )

        monitorRouteRepository.saveRouteState(routeState)

        val routeReference = MonitorRouteReference(
          MongoId(),
          null, // routeId,
          routeId,
          timestamp.key,
          timestamp,
          "vmarc",
          bounds,
          "osm", // "osm" | "gpx"
          Some(timestamp),
          segments.size,
          None,
          geometry = geoJson
        )

        monitorRouteRepository.saveRouteReference(routeReference)
      }
    }
  }

  private def routeIds: Seq[Long] = {
    new File("/kpn/wrk/begin").list().toSeq.map(file => file.replace(".xml", "").toLong)
  }
}
