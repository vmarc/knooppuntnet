package kpn.core.tools.monitor

import kpn.api.common.Bounds
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.monitor.MonitorGroup
import kpn.api.custom.Relation
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.loadOld.OsmDataXmlReader
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalyzer
import kpn.server.api.monitor.domain.MonitorRouteReference
import org.locationtech.jts.geom.Geometry

object MonitorDemoTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>
      new MonitorDemoTool(database).setup(MonitorDemoRoute.routes)
    }
  }
}

class MonitorDemoTool(database: Database) {

  private val now = Timestamp(2021, 12, 1, 0, 0, 0)
  private val log = Log(classOf[MonitorDemoTool])

  def setup(demoRoutes: Seq[MonitorDemoRoute]): Unit = {

    database.monitorGroups.drop(log)
    database.monitorRoutes.drop(log)
    database.monitorRouteReferences.drop(log)
    database.monitorRouteStates.drop(log)
    database.monitorRouteChanges.drop(log)
    database.monitorRouteChangeGeometries.drop(log)

    val group = MonitorGroup("SGR", "Les Sentiers de Grande Randonnée")
    database.monitorGroups.save(group)

    demoRoutes.filter(_.routeId > 0).foreach { demoRoute =>
      Log.context(demoRoute.id) {
        log.info("route start")

        val routeRelation = readRouteRelation(demoRoute)
        val monitorRoute = MonitorRouteAnalyzer.toRoute(
          demoRoute.id,
          group.name,
          demoRoute.name,
          routeRelation
        )
        database.monitorRoutes.save(monitorRoute)

        log.info("build route reference")
        val routeReference = buildRouteReference(demoRoute)
        database.monitorRouteReferences.save(routeReference)
        log.info("saved route reference")

        if (demoRoute.routeId > 1) {
          val routeState = new MonitorDemoAnalyzer().analyze(routeReference, routeRelation, now)
          database.monitorRouteStates.save(routeState)
        }
      }
    }
  }

  private def readRelation(filename: String, routeId: Long): Relation = {
    val rawData = OsmDataXmlReader.read(filename)
    val data = new DataBuilder(rawData).data
    data.relations(routeId)
  }

  private def buildRouteReference(demoRoute: MonitorDemoRoute): MonitorRouteReference = {
    val geometry = new MonitorRouteGpxReader().read(s"/kpn/monitor-demo/${demoRoute.filename}.gpx")
    log.info("geometry loaded")
    val bounds = geometryBounds(geometry)
    log.info("geometry bounds calculated")
    val geoJson = MonitorRouteAnalyzer.toGeoJson(geometry)
    log.info(s"geojson ready, size=${geoJson.length}")
    val id = s"${demoRoute.id}:${now.key}"

    MonitorRouteReference(
      id,
      monitorRouteId = demoRoute.id,
      routeId = demoRoute.routeId,
      key = now.key,
      created = now,
      user = "vmarc",
      bounds = bounds,
      referenceType = "gpx", // "osm" | "gpx"
      referenceTimestamp = Some(now),
      segmentCount = 1, // number of tracks in gpx always 1, multiple track not supported yet
      filename = Some(demoRoute.filename),
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

  private def readRouteRelation(demoRoute: MonitorDemoRoute): Relation = {
    if (demoRoute.routeId > 1) {
      val filename = s"/kpn/monitor-demo/${demoRoute.routeId}.xml"
      readRelation(filename, demoRoute.routeId)
    }
    else {
      Relation(
        RawRelation(
          demoRoute.routeId,
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
