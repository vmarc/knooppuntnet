package kpn.core.tools.monitor


import kpn.api.common.monitor.MonitorRouteUpdate
import kpn.api.custom.Timestamp
import kpn.core.common.Time
import kpn.core.overpass.OverpassQueryExecutorRemoteImpl
import kpn.core.util.Log
import kpn.database.base.Database
import kpn.database.base.DatabaseCollection
import kpn.database.util.Mongo
import kpn.server.analyzer.engine.monitor.MonitorRouteDeviationAnalyzerImpl
import kpn.server.analyzer.engine.monitor.MonitorRouteOsmSegmentAnalyzerImpl
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.OldMonitorRoute
import kpn.server.monitor.domain.OldMonitorRouteReference
import kpn.server.monitor.repository.MonitorGroupRepositoryImpl
import kpn.server.monitor.repository.MonitorRouteRepositoryImpl
import kpn.server.monitor.route.update.MonitorRouteGapAnalyzer
import kpn.server.monitor.route.update.MonitorRouteRelationRepository
import kpn.server.monitor.route.update.MonitorRouteStructureLoader
import kpn.server.monitor.route.update.MonitorRouteUpdateExecutor
import kpn.server.monitor.route.update.MonitorUpdateContext
import kpn.server.monitor.route.update.MonitorUpdateReporterLogger
import kpn.server.monitor.route.update.MonitorUpdateStructureImpl
import org.apache.commons.io.FileUtils
import org.locationtech.jts.geom.GeometryFactory
import org.mongodb.scala.MongoNamespace

import java.io.File
import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class MonitorExampleSuperRoute(relationId: Long, relations: Seq[MonitorExampleSuperRouteSubRelation])

case class MonitorExampleSuperRouteSubRelation(relationId: Long, referenceFilename: String)

class MonitorRouteMigrationConfiguration(val database: Database) {

  val monitorGroupRepository = new MonitorGroupRepositoryImpl(database)
  val monitorRouteRepository = new MonitorRouteRepositoryImpl(database)
  private val overpassQueryExecutor = new OverpassQueryExecutorRemoteImpl()
  private val monitorRouteRelationRepository = new MonitorRouteRelationRepository(overpassQueryExecutor)
  private val monitorRouteStructureLoader = new MonitorRouteStructureLoader(overpassQueryExecutor)
  private val monitorUpdateStructure = new MonitorUpdateStructureImpl(monitorRouteRelationRepository, monitorRouteStructureLoader)
  private val monitorRouteOsmSegmentAnalyzer = new MonitorRouteOsmSegmentAnalyzerImpl()
  private val monitorRouteDeviationAnalyzer = new MonitorRouteDeviationAnalyzerImpl()
  private val monitorRouteGapAnalyzer = new MonitorRouteGapAnalyzer()

  val monitorRouteUpdateExecutor = new MonitorRouteUpdateExecutor(
    monitorGroupRepository,
    monitorRouteRepository,
    monitorUpdateStructure,
    monitorRouteRelationRepository,
    monitorRouteOsmSegmentAnalyzer,
    monitorRouteGapAnalyzer,
    monitorRouteDeviationAnalyzer
  )
}

object MonitorRouteMigrationTool {

  private val databaseName = "kpn-monitor"

  private val exampleSuperRoute = MonitorExampleSuperRoute(
    9453563L,
    Seq(
      MonitorExampleSuperRouteSubRelation(6691445L, "/kpn/routes/578-groene-hartpad-etappe-01.gpx"),
      MonitorExampleSuperRouteSubRelation(6769053L, "/kpn/routes/579-groene-hartpad-etappe-02.gpx"),
      MonitorExampleSuperRouteSubRelation(9510415L, "/kpn/routes/580-groene-hartpad-etappe-03.gpx"),
      MonitorExampleSuperRouteSubRelation(6691488L, "/kpn/routes/581-groene-hartpad-etappe-04.gpx"),
      MonitorExampleSuperRouteSubRelation(6691494L, "/kpn/routes/582-groene-hartpad-etappe-05.gpx"),
      MonitorExampleSuperRouteSubRelation(9510414L, "/kpn/routes/583-groene-hartpad-etappe-06.gpx"),
      MonitorExampleSuperRouteSubRelation(6691523L, "/kpn/routes/584-groene-hartpad-etappe-07.gpx"),
      MonitorExampleSuperRouteSubRelation(7217854L, "/kpn/routes/585-groene-hartpad-etappe-08.gpx"),
      MonitorExampleSuperRouteSubRelation(9510413L, "/kpn/routes/586-groene-hartpad-etappe-09.gpx"),
      MonitorExampleSuperRouteSubRelation(6785059L, "/kpn/routes/587-groene-hartpad-etappe-10.gpx"),
      MonitorExampleSuperRouteSubRelation(6691446L, "/kpn/routes/588-groene-hartpad-etappe-11.gpx"),
    )
  )

  def main(args: Array[String]): Unit = {
    Mongo.executeIn(databaseName) { database =>
      val configuration = new MonitorRouteMigrationConfiguration(database)
      val tool = new MonitorRouteMigrationTool(configuration)
      // tool.renameRouteCollections()
      // tool.addExampleSuperRoute(exampleSuperRoute)
      // tool.migrateOne("fr-iwn-Camino", "Voie-Toulouse")
      // tool.migrateOne("fr-iwn-Camino", "Voie-Vezelay")
      // tool.migrateOne("SGR", "GR-129S-L3")
      tool.migrateOne("NL-LAW", "LAW-9-1")
      // tool.migrate()
    }
    println("Done")
  }
}

class MonitorRouteMigrationTool(configuration: MonitorRouteMigrationConfiguration) {

  private val log = Log(classOf[MonitorRouteMigrationTool])
  private val geometryFactory = new GeometryFactory

  def renameRouteCollections(): Unit = {
    renameRouteCollection(configuration.database.monitorRoutes)
    renameRouteCollection(configuration.database.monitorRouteReferences)
    renameRouteCollection(configuration.database.monitorRouteStates)
  }

  def addExampleSuperRoute(exampleSuperRoute: MonitorExampleSuperRoute): Unit = {

    configuration.monitorGroupRepository.groupByName("AAA") match {
      case None => log.error("group not found")
      case Some(group) =>
        configuration.monitorRouteRepository.routeByName(group._id, "example") match {
          case Some(route) => configuration.monitorRouteRepository.deleteRoute(route._id)
          case None =>
        }

        configuration.monitorRouteUpdateExecutor.execute(
          MonitorUpdateContext(
            "migration",
            new MonitorUpdateReporterLogger(),
            MonitorRouteUpdate(
              action = "add",
              groupName = group.name,
              routeName = "example",
              referenceType = "multi-gpx",
              description = Some("example route with gpx trace per sub relation"),
              comment = Some("comment"),
              relationId = Some(exampleSuperRoute.relationId),
            )
          )
        )

        exampleSuperRoute.relations.foreach { superRouteSubRelation =>
          val referenceGpx = FileUtils.readFileToString(new File(superRouteSubRelation.referenceFilename), "UTF-8")
          configuration.monitorRouteUpdateExecutor.execute(
            MonitorUpdateContext(
              "migration",
              new MonitorUpdateReporterLogger(),
              MonitorRouteUpdate(
                action = "gpx-upload",
                groupName = group.name,
                routeName = "example",
                referenceType = "multi-gpx",
                relationId = Some(exampleSuperRoute.relationId),
                referenceTimestamp = Some(Time.now),
                referenceFilename = Some(superRouteSubRelation.referenceFilename),
                referenceGpx = Some(referenceGpx),
              )
            )
          )
        }
    }
  }

  def migrateOne(groupName: String, routeName: String): Unit = {
    configuration.monitorGroupRepository.groupByName(groupName) match {
      case None => log.error("group not found")
      case Some(group) =>
        configuration.monitorRouteRepository.oldRouteByName(group._id, routeName) match {
          case None => log.error("route not found")
          case Some(route) => migrateGroupRoute(group, route)
        }
    }
  }

  def migrate(): Unit = {
    configuration.monitorGroupRepository.groups().sortBy(_.name).foreach { group =>
      configuration.monitorGroupRepository.oldGroupRoutes(group._id).sortBy(_.name).foreach { route =>
        migrateGroupRoute(group, route)
      }
    }
  }

  private def migrateGroupRoute(group: MonitorGroup, route: OldMonitorRoute): Unit = {
    Log.context(s"${group.name}:${route.name}") {
      log.infoElapsed {
        configuration.monitorRouteRepository.oldRouteReferenceRouteWithId(route._id) match {
          case None => log.error("Could not read reference")
          case Some(reference) => migrateRoute(group, route, reference)
        }
        ("migrated", ())
      }
    }
  }

  private def migrateRoute(group: MonitorGroup, oldRoute: OldMonitorRoute, oldReference: OldMonitorRouteReference): Unit = {

    configuration.monitorRouteRepository.routeByName(group._id, oldRoute.name) match {
      case Some(route) => configuration.monitorRouteRepository.deleteRoute(route._id)
      case None =>
    }

    val migrationGeojson = if (oldRoute.referenceFilename.isDefined) {
      Some(oldReference.geometry)
    }
    else {
      None
    }

    configuration.monitorRouteUpdateExecutor.execute(
      MonitorUpdateContext(
        oldReference.user,
        new MonitorUpdateReporterLogger(),
        MonitorRouteUpdate(
          action = "add",
          groupName = group.name,
          routeName = oldRoute.name,
          referenceType = oldRoute.referenceType.get,
          description = Some(oldRoute.description),
          comment = oldRoute.comment,
          relationId = oldRoute.relationId,
          referenceTimestamp = oldRoute.referenceDay.map(day => Timestamp(day)),
          referenceFilename = oldRoute.referenceFilename,
          migrationGeojson = migrationGeojson,
        )
      )
    )

    /*
        if (oldRoute.referenceType.contains("gpx")) {
          val geoJson = oldReference.geometry
          val geometry = new GeoJsonReader().read(geoJson)
          val referenceLineStrings = MonitorRouteReferenceUtil.toLineStrings(geometry)
          val simplifiedLineStrings = referenceLineStrings.map { lineString =>
            val oldCoordinates = lineString.getCoordinates.toList
            val newCoordinates = MonitorRouteAnalysisSupport.simplifyCoordinates(oldCoordinates)
            if (newCoordinates.length != oldCoordinates.length) {
              geometryFactory.createLineString(newCoordinates.toArray)
            }
            else {
              lineString
            }
          }
          val simplifiedGeometry = geometryFactory.createGeometryCollection(simplifiedLineStrings.toArray)
          val simplifiedGeoJson = MonitorRouteAnalysisSupport.toGeoJson(simplifiedGeometry)

          val distance = Math.round(toMeters(simplifiedGeometry.getLength))

          val reference = MonitorRouteReference(
            oldReference._id,
            routeId = newRoute._id,
            relationId = newRoute.relationId,
            timestamp = oldReference.created,
            user = oldReference.user,
            bounds = oldReference.bounds,
            referenceType = oldReference.referenceType,
            referenceTimestamp = Timestamp(oldReference.referenceDay.get),
            distance = distance,
            segmentCount = oldReference.segmentCount,
            filename = oldReference.filename,
            geoJson = simplifiedGeoJson
          )

          configuration.monitorRouteRepository.saveRouteReference(reference)
          context = context.copy(referenceChanged = true)

          val updatedRoute = configuration.monitorRouteRelationAnalyzer.analyzeReference(newRoute._id, reference) match {
            case None => newRoute.copy(referenceDistance = reference.distance)
            case Some(state) =>

              configuration.monitorRouteRepository.saveRouteState(state)

              newRoute.copy(
                referenceDistance = reference.distance,
                deviationDistance = state.deviations.map(_.distance).sum,
                deviationCount = state.deviations.size,
                osmWayCount = state.wayCount,
                osmDistance = state.osmDistance,
                osmSegmentCount = state.osmSegments.size,
                osmSegments = state.osmSegments.map { osmSegment =>
                  MonitorRouteOsmSegment(
                    Seq(
                      MonitorRouteOsmSegmentElement(
                        relationId = newRoute.relationId.get,
                        segmentId = osmSegment.id,
                        meters = osmSegment.meters,
                        bounds = osmSegment.bounds,
                        reversed = false
                      )
                    )
                  )
                },
                relation = newRoute.relation.map { monitorRouteRelation =>
                  monitorRouteRelation.copy(
                    deviationDistance = state.deviations.map(_.meters).sum,
                    deviationCount = state.deviations.size,
                    osmWayCount = state.wayCount,
                    osmDistance = state.osmDistance,
                    osmSegmentCount = state.osmSegments.size,
                    happy = state.happy,
                  )
                },
                happy = state.happy
              )
          }
          configuration.monitorRouteRepository.saveRoute(updatedRoute)
        }
          */
  }

  private def renameRouteCollection(collection: DatabaseCollection[_]): Unit = {
    val native = collection.native
    val databaseName = native.namespace.getDatabaseName
    val collectionName = native.namespace.getCollectionName
    val namespace = new MongoNamespace(databaseName, s"old-$collectionName")
    val future = native.renameCollection(namespace).toFuture()
    Await.result(future, Duration(5, TimeUnit.SECONDS))
  }
}
