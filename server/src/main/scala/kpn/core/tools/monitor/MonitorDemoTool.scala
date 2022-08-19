package kpn.core.tools.monitor

import kpn.api.base.ObjectId
import kpn.api.common.Bounds
import kpn.api.common.data.raw.RawRelation
import kpn.api.custom.Relation
import kpn.api.custom.Tags
import kpn.core.common.Time
import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.overpass.OverpassQueryExecutor
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.overpass.QueryRelation
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysisSupport
import kpn.server.api.monitor.domain.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.MonitorRouteReference
import org.locationtech.jts.geom.Geometry

import scala.xml.XML

object MonitorDemoTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn-experimental") { database =>
      val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
      new MonitorDemoTool(database, overpassQueryExecutor).setup()
    }
  }
}

class MonitorDemoTool(database: Database, overpassQueryExecutor: OverpassQueryExecutor) {

  private val now = Time.now
  private val log = Log(classOf[MonitorDemoTool])

  def setup(): Unit = {

    database.monitorGroups.drop(log)
    database.monitorRoutes.drop(log)
    database.monitorRouteReferences.drop(log)
    database.monitorRouteStates.drop(log)
    database.monitorRouteChanges.drop(log)
    database.monitorRouteChangeGeometries.drop(log)

    setupGroup("SGR", "Les Sentiers de Grande Randonnée", MonitorDemoRoute.routes.filter(_.relationId.isDefined).take(2))
    setupGroup("GRV", "Grote Route Vlaanderen", MonitorDemoRoute.grVlaanderenRoutes.filter(_.relationId.isDefined).take(2))
  }

  private def setupGroup(groupName: String, groupDescription: String, routes: Seq[MonitorDemoRoute]): Unit = {

    val group = MonitorGroup(ObjectId(), groupName, groupDescription)
    database.monitorGroups.save(group)

    routes.foreach { demoRoute =>
      Log.context(demoRoute.name) {
        log.info("route start")
        val route = MonitorRouteAnalysisSupport.toRoute(
          group._id,
          demoRoute.name,
          demoRoute.description,
          demoRoute.relationId
        )
        database.monitorRoutes.save(route)

        log.info("build route reference")

        val gpxFilename = s"/kpn/monitor/demo/$groupName/${demoRoute.filename}.gpx"
        val routeReference = buildRouteReference(route, gpxFilename)
        database.monitorRouteReferences.save(routeReference)
        log.info("saved route reference")

        demoRoute.relationId match {
          case None =>
          case Some(relationId) =>
            val routeRelation = readRouteRelation(relationId)
            val routeState = new MonitorDemoAnalyzer().analyze(route, routeReference, routeRelation, now)
            database.monitorRouteStates.save(routeState)
        }
      }
    }
  }

  private def buildRouteReference(route: MonitorRoute, gpxFilename: String): MonitorRouteReference = {
    val geometry = new MonitorRouteGpxReader().readFile(gpxFilename)
    log.info("geometry loaded")
    val bounds = geometryBounds(geometry)
    log.info("geometry bounds calculated")
    val geoJson = MonitorRouteAnalysisSupport.toGeoJson(geometry)
    log.info(s"geojson ready, size=${geoJson.length}")
    val id = s"${route._id}:${now.key}"

    MonitorRouteReference(
      ObjectId(),
      routeId = route._id,
      relationId = None,
      key = now.key,
      created = now,
      user = "vmarc",
      bounds = bounds,
      referenceType = "gpx", // "osm" | "gpx"
      referenceTimestamp = Some(now),
      segmentCount = 1, // number of tracks in gpx always 1, multiple track not supported yet
      filename = Some(gpxFilename),
      geometry = geoJson
    )
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

  private def readRouteRelation(routeId: Long): Relation = {
    if (routeId > 1) {
      val xmlString = overpassQueryExecutor.executeQuery(Some(now), QueryRelation(routeId))
      val xml = XML.loadString(xmlString)
      val rawData = new Parser().parse(xml.head)
      val data = new DataBuilder(rawData).data
      data.relations(routeId)
    }
    else {
      Relation(
        RawRelation(
          routeId,
          1,
          now,
          1,
          Seq.empty,
          Tags.empty
        ),
        Seq.empty
      )
    }
  }
}
