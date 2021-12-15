package kpn.core.tools.monitor

import kpn.api.common.Bounds
import kpn.api.common.data.raw.RawRelation
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.monitor.MonitorRouteNokSegment
import kpn.api.custom.Relation
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.data.DataBuilder
import kpn.core.loadOld.OsmDataXmlReader
import kpn.core.util.Log
import kpn.core.util.Util
import kpn.database.base.Database
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalysis
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalyzer
import kpn.server.analyzer.engine.monitor.MonitorRouteAnalyzer.toMeters
import kpn.server.analyzer.engine.monitor.MonitorRouteSegmentData
import kpn.server.api.monitor.domain.MonitorRouteReference
import kpn.server.api.monitor.domain.MonitorRouteState
import org.locationtech.jts.densify.Densifier
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiLineString
import org.locationtech.jts.io.geojson.GeoJsonReader

object MonitorDemoTool {
  def main(args: Array[String]): Unit = {
    Mongo.executeIn("kpn") { database =>
      new MonitorDemoTool(database).setup(MonitorDemoRoute.routes)
    }
  }
}

class MonitorDemoTool(database: Database) {

  private val geomFactory = new GeometryFactory
  private val sampleDistanceMeters = 10
  private val toleranceMeters = 10
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
          demoRoute.filename.replaceAll(".gpx", ""),
          routeRelation
        )
        database.monitorRoutes.save(monitorRoute)

        log.info("build route reference")
        val routeReference = buildRouteReference(demoRoute)
        database.monitorRouteReferences.save(routeReference)
        log.info("saved route reference")

        if (demoRoute.routeId > 1) {
          val routeState = {
            val routeSegments = MonitorRouteAnalyzer.toRouteSegments(routeRelation)
            log.info("analyzed route segments")
            val routeAnalysis = analyzeChange(routeReference, routeRelation, routeSegments)
            log.info(s"analyzed change: osm segments=${routeAnalysis.osmSegments.size}, nok=${routeAnalysis.nokSegments.size}")

            MonitorRouteState(
              demoRoute.id,
              demoRoute.routeId,
              routeAnalysis.relation.timestamp,
              routeAnalysis.wayCount,
              routeAnalysis.osmDistance,
              routeAnalysis.gpxDistance,
              routeAnalysis.bounds,
              Some(routeReference.key),
              routeAnalysis.osmSegments,
              routeAnalysis.okGeometry,
              routeAnalysis.nokSegments
            )
          }
          database.monitorRouteStates.save(routeState)
        }
      }
    }
  }

  private def analyzeChange(reference: MonitorRouteReference, routeRelation: Relation, osmRouteSegments: Seq[MonitorRouteSegmentData]): MonitorRouteAnalysis = {

    val gpxLineString = new GeoJsonReader().read(reference.geometry)

    val (okOption: Option[MultiLineString], nokSegments: Seq[MonitorRouteNokSegment]) = {

      val distanceBetweenSamples = sampleDistanceMeters.toDouble * gpxLineString.getLength / MonitorRouteAnalyzer.toMeters(gpxLineString.getLength)
      val densifiedGpx = Densifier.densify(gpxLineString, distanceBetweenSamples)
      val sampleCoordinates = densifiedGpx.getCoordinates.toSeq

      val distances = sampleCoordinates.toList.map { coordinate =>
        val point = geomFactory.createPoint(coordinate)
        toMeters(osmRouteSegments.map(segment => segment.lineString.distance(point)).min)
      }

      val withinTolerance = distances.map(distance => distance < toleranceMeters)
      val okAndIndexes = withinTolerance.zipWithIndex.map { case (ok, index) => ok -> index }
      val splittedOkAndIndexes = MonitorRouteAnalyzer.split(okAndIndexes)

      val ok: MultiLineString = MonitorRouteAnalyzer.toMultiLineString(sampleCoordinates, splittedOkAndIndexes.filter(_.head._1))

      val noks = splittedOkAndIndexes.filterNot(_.head._1)

      val nok = noks.zipWithIndex.map { case (segment, segmentIndex) =>
        val segmentIndexes = segment.map(_._2)
        val maxDistance = distances.zipWithIndex.filter { case (distance, index) =>
          segmentIndexes.contains(index)
        }.map { case (distance, index) =>
          distance
        }.max

        val lineString = MonitorRouteAnalyzer.toLineString(sampleCoordinates, segment)
        val meters: Long = Math.round(toMeters(lineString.getLength))
        val bounds = MonitorRouteAnalyzer.toBounds(lineString.getCoordinates.toSeq)
        val geoJson = MonitorRouteAnalyzer.toGeoJson(lineString)

        MonitorRouteNokSegment(
          segmentIndex + 1,
          meters,
          maxDistance.toLong,
          bounds,
          geoJson
        )
      }

      val routeNokSegments: Seq[MonitorRouteNokSegment] = nok.sortBy(_.distance).reverse.zipWithIndex.map { case (s, index) =>
        s.copy(id = index + 1)
      }

      (Some(ok), routeNokSegments)
    }

    val gpxDistance = Math.round(toMeters(gpxLineString.getLength / 1000))
    val osmDistance = Math.round(osmRouteSegments.map(_.segment.meters).sum.toDouble / 1000)

    val gpxGeometry = MonitorRouteAnalyzer.toGeoJson(gpxLineString)
    val okGeometry = okOption.map(geometry => MonitorRouteAnalyzer.toGeoJson(geometry))

    // TODO merge gpx bounds + ok
    val bounds = Util.mergeBounds(osmRouteSegments.map(_.segment.bounds) ++ nokSegments.map(_.bounds))

    MonitorRouteAnalysis(
      routeRelation,
      routeRelation.wayMembers.size,
      osmDistance,
      gpxDistance,
      bounds,
      osmRouteSegments.map(_.segment),
      Some(gpxGeometry),
      okGeometry,
      nokSegments
    )
  }

  private def readRelation(filename: String, routeId: Long): Relation = {
    val rawData = OsmDataXmlReader.read(filename)
    val data = new DataBuilder(rawData).data
    data.relations(routeId)
  }

  private def buildRouteReference(demoRoute: MonitorDemoRoute): MonitorRouteReference = {
    val geometry = new MonitorRouteGpxReader().read(s"/kpn/monitor-demo/${demoRoute.filename}")
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
